#include <android/asset_manager_jni.h>
#include <android/native_window_jni.h>
#include <android/native_window.h>

#include <android/log.h>

#include <jni.h>

#include <string>
#include <vector>

#include <platform.h>
#include <benchmark.h>
#include "ndkcamera.h"
#include "phan_loai.h"
#include "sau_rieng_detection.h"
#include "ca_phe_detection.h"

#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <iostream>
#include <android/bitmap.h>
#include <opencv2/opencv.hpp>
#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>

using namespace cv;
#if __ARM_NEON
#include <arm_neon.h>
#endif // __ARM_NEON

static ncnn::Mutex lock;
static PhanLoai *gPhanLoai;
static sau_rieng_detection *sauRiengDetection;
static ca_phe_detection *caPheDetection;
static std::vector<Object> objects;
static cv::Mat srcRgb;

class MyNdkCamera : public NdkCameraWindow {
    virtual void on_image_render(cv::Mat &rgb) const;
};

void MyNdkCamera::on_image_render(cv::Mat &rgb) const {
    {
        srcRgb = rgb.clone();
        ncnn::MutexLockGuard g(lock);

    }
}

static MyNdkCamera *g_camera = 0;

extern "C" {

JNIEXPORT jint
JNI_OnLoad(JavaVM *vm, void *reserved) {
    __android_log_print(ANDROID_LOG_DEBUG, "ncnn", "JNI_OnLoad");

    g_camera = new MyNdkCamera;

    return JNI_VERSION_1_4;
}

JNIEXPORT void JNI_OnUnload(JavaVM *vm, void *reserved) {
    __android_log_print(ANDROID_LOG_DEBUG, "ncnn", "JNI_OnUnload");

    {
        ncnn::MutexLockGuard g(lock);
        delete gPhanLoai;
        gPhanLoai = 0;
        delete sauRiengDetection;
        sauRiengDetection = 0;
        delete caPheDetection;
        caPheDetection = 0;
    }

    delete g_camera;
    g_camera = 0;
}

extern "C" jboolean
Java_com_tondz_nhandienbenhcaytrong_CayTrongSDK_loadModel(JNIEnv *env, jobject thiz,
                                                          jobject assetManager,
                                                          jint modelid) {


    AAssetManager *mgr = AAssetManager_fromJava(env, assetManager);

    __android_log_print(ANDROID_LOG_DEBUG, "ncnn", "loadModel %p", mgr);
    delete gPhanLoai;
    gPhanLoai = 0;
    delete sauRiengDetection;
    sauRiengDetection = 0;
    delete caPheDetection;
    caPheDetection = 0;
    {
        ncnn::MutexLockGuard g(lock);

        if (modelid == 0) {
            if (!gPhanLoai)
                gPhanLoai = new PhanLoai;
            gPhanLoai->load(mgr);
        } else if (modelid == 1) {
            if (!caPheDetection)
                caPheDetection = new ca_phe_detection;
            caPheDetection->load(mgr);
        } else if (modelid == 2) {
            if (!sauRiengDetection)
                sauRiengDetection = new sau_rieng_detection;
            sauRiengDetection->load(mgr);
        }


    }

    return JNI_TRUE;
}
extern "C" jboolean
Java_com_tondz_nhandienbenhcaytrong_CayTrongSDK_openCamera(JNIEnv *env, jobject thiz, jint facing) {
    if (facing < 0 || facing > 1)
        return JNI_FALSE;

    __android_log_print(ANDROID_LOG_DEBUG, "ncnn", "openCamera %d", facing);

    g_camera->open((int) facing);

    return JNI_TRUE;
}

extern "C" jboolean
Java_com_tondz_nhandienbenhcaytrong_CayTrongSDK_closeCamera(JNIEnv *env, jobject thiz) {
    __android_log_print(ANDROID_LOG_DEBUG, "ncnn", "closeCamera");

    g_camera->close();

    return JNI_TRUE;
}

extern "C" jboolean
Java_com_tondz_nhandienbenhcaytrong_CayTrongSDK_setOutputWindow(JNIEnv *env, jobject thiz,
                                                                jobject surface) {
    ANativeWindow *win = ANativeWindow_fromSurface(env, surface);

    __android_log_print(ANDROID_LOG_DEBUG, "ncnn", "setOutputWindow %p", win);

    g_camera->set_window(win);

    return JNI_TRUE;
}

}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_tondz_nhandienbenhcaytrong_CayTrongSDK_predictCapture(JNIEnv *env, jobject thiz) {
    std::vector<float> result;
    if (gPhanLoai) {
        gPhanLoai->predict(srcRgb, result);
    }

    std::ostringstream oss;
    for (size_t i = 0; i < result.size(); ++i) {
        if (i != 0) {
            oss << ",";  // Add a separator between elements
        }
        oss << result[i];
    }

    // Convert the stream to a string
    std::string predictStr = oss.str();
    result.clear();
    return env->NewStringUTF(predictStr.c_str());

}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_tondz_nhandienbenhcaytrong_CayTrongSDK_predictImagePath(JNIEnv *env, jobject thiz,
                                                                 jstring file_path) {
    std::vector<float> result;
    jboolean isCopy;
    const char *convertedValue = (env)->GetStringUTFChars(file_path, &isCopy);
    std::string strPath = convertedValue;
    if (gPhanLoai) {
        gPhanLoai->predictPath(strPath, result);
    }

    std::ostringstream oss;
    for (size_t i = 0; i < result.size(); ++i) {
        if (i != 0) {
            oss << ",";  // Add a separator between elements
        }
        oss << result[i];
    }
    std::string predictStr = oss.str();
    result.clear();
    return env->NewStringUTF(predictStr.c_str());
}


jobject mat_to_bitmap(JNIEnv *env, cv::Mat &src, bool needPremultiplyAlpha) {
    jclass java_bitmap_class = env->FindClass("android/graphics/Bitmap");
    jclass bmpCfgCls = env->FindClass("android/graphics/Bitmap$Config");
    jmethodID bmpClsValueOfMid = env->GetStaticMethodID(bmpCfgCls, "valueOf",
                                                        "(Ljava/lang/String;)Landroid/graphics/Bitmap$Config;");
    jobject jBmpCfg = env->CallStaticObjectMethod(bmpCfgCls, bmpClsValueOfMid,
                                                  env->NewStringUTF("ARGB_8888"));

    jmethodID mid = env->GetStaticMethodID(java_bitmap_class,
                                           "createBitmap",
                                           "(IILandroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;");

    jobject bitmap = env->CallStaticObjectMethod(java_bitmap_class,
                                                 mid, src.cols, src.rows,
                                                 jBmpCfg);

    AndroidBitmapInfo info;
    void *pixels = nullptr;


    // Validate
    if (AndroidBitmap_getInfo(env, bitmap, &info) < 0) {
        std::runtime_error("Failed to get Bitmap info.");
    }
    if (src.type() != CV_8UC1 && src.type() != CV_8UC3 && src.type() != CV_8UC4) {
        std::runtime_error("Unsupported cv::Mat type.");
    }
    if (AndroidBitmap_lockPixels(env, bitmap, &pixels) < 0) {
        std::runtime_error("Failed to lock Bitmap pixels.");
    }
    if (!pixels) {
        std::runtime_error("Bitmap pixels are null.");
    }

    // Convert cv::Mat to the Bitmap format
    if (info.format == ANDROID_BITMAP_FORMAT_RGBA_8888) {
        Mat tmp(info.height, info.width, CV_8UC4, pixels);
        if (src.type() == CV_8UC1) {
            cvtColor(src, tmp, COLOR_GRAY2RGBA);
        } else if (src.type() == CV_8UC3) {
            cvtColor(src, tmp, COLOR_RGB2RGBA);
        } else if (src.type() == CV_8UC4) {
            if (needPremultiplyAlpha) {
                cvtColor(src, tmp, COLOR_RGBA2mRGBA);
            } else {
                src.copyTo(tmp);
            }
        }
    } else if (info.format == ANDROID_BITMAP_FORMAT_RGB_565) {
        Mat tmp(info.height, info.width, CV_8UC2, pixels);
        if (src.type() == CV_8UC1) {
            cvtColor(src, tmp, COLOR_GRAY2BGR565);
        } else if (src.type() == CV_8UC3) {
            cvtColor(src, tmp, COLOR_RGB2BGR565);
        } else if (src.type() == CV_8UC4) {
            cvtColor(src, tmp, COLOR_RGBA2BGR565);
        }
    }

    AndroidBitmap_unlockPixels(env, bitmap);
    return bitmap;
}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_tondz_nhandienbenhcaytrong_CayTrongSDK_getImage(JNIEnv *env, jobject thiz) {
    jobject bitmap = mat_to_bitmap(env, srcRgb, false);
    return bitmap;
}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_tondz_nhandienbenhcaytrong_CayTrongSDK_predictCaPhePath(JNIEnv *env, jobject thiz,
                                                                 jstring file_path) {
    jboolean isCopy;
    const char *convertedValue = (env)->GetStringUTFChars(file_path, &isCopy);
    std::string strPath = convertedValue;
    objects.clear();
    if (caPheDetection) {
        caPheDetection->predictPath(strPath, objects, 0.3, 0.3);
        __android_log_print(ANDROID_LOG_DEBUG, "ncnn", "ca phe");
    }


    jclass arrayListClass = env->FindClass("java/util/ArrayList");
    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
    jobject arrayList = env->NewObject(arrayListClass, arrayListConstructor);

    // Get the add method of ArrayList
    jmethodID arrayListAdd = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");
    for (const Object &obj: objects) {
        std::ostringstream oss;
        oss << obj.label << " " << obj.rect.x << " " << obj.rect.y << " "
            << obj.rect.width << " " << obj.rect.height << " " << obj.prob;
        std::string objName = oss.str();
        jstring javaString = env->NewStringUTF(objName.c_str());  // Convert to jstring
        env->CallBooleanMethod(arrayList, arrayListAdd, javaString);
        env->DeleteLocalRef(javaString);  // Clean up local reference
    }
    return arrayList;
}
extern "C"
JNIEXPORT jobject JNICALL
Java_com_tondz_nhandienbenhcaytrong_CayTrongSDK_predictSauRiengPath(JNIEnv *env, jobject thiz,
                                                                    jstring file_path) {
    jboolean isCopy;
    const char *convertedValue = (env)->GetStringUTFChars(file_path, &isCopy);
    std::string strPath = convertedValue;
    objects.clear();
    if (sauRiengDetection) {
        sauRiengDetection->predictPath(strPath, objects, 0.3, 0.3);
        __android_log_print(ANDROID_LOG_DEBUG, "ncnn", "sau rieng");
    }


    jclass arrayListClass = env->FindClass("java/util/ArrayList");
    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
    jobject arrayList = env->NewObject(arrayListClass, arrayListConstructor);

    // Get the add method of ArrayList
    jmethodID arrayListAdd = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");
    for (const Object &obj: objects) {
        std::ostringstream oss;
        oss << obj.label << " " << obj.rect.x << " " << obj.rect.y << " "
            << obj.rect.width << " " << obj.rect.height << " " << obj.prob;
        std::string objName = oss.str();
        jstring javaString = env->NewStringUTF(objName.c_str());  // Convert to jstring
        env->CallBooleanMethod(arrayList, arrayListAdd, javaString);
        env->DeleteLocalRef(javaString);  // Clean up local reference
    }
    return arrayList;
}
extern "C"
JNIEXPORT jobject JNICALL
Java_com_tondz_nhandienbenhcaytrong_CayTrongSDK_predictCaPhe(JNIEnv *env, jobject thiz) {

    objects.clear();
    __android_log_print(ANDROID_LOG_DEBUG, "ncnn", "objects.clear()");

    if (caPheDetection) {
        caPheDetection->detect(srcRgb, objects, 0.3, 0.3);
    }


    jclass arrayListClass = env->FindClass("java/util/ArrayList");
    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
    jobject arrayList = env->NewObject(arrayListClass, arrayListConstructor);

    // Get the add method of ArrayList
    jmethodID arrayListAdd = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");
    for (const Object &obj: objects) {
        std::ostringstream oss;
        oss << obj.label << " " << obj.rect.x << " " << obj.rect.y << " "
            << obj.rect.width << " " << obj.rect.height << " " << obj.prob;
        std::string objName = oss.str();
        jstring javaString = env->NewStringUTF(objName.c_str());  // Convert to jstring
        env->CallBooleanMethod(arrayList, arrayListAdd, javaString);
        env->DeleteLocalRef(javaString);  // Clean up local reference
    }
    return arrayList;
}
extern "C"
JNIEXPORT jobject JNICALL
Java_com_tondz_nhandienbenhcaytrong_CayTrongSDK_predictSauRieng(JNIEnv *env, jobject thiz) {
    objects.clear();
    if (sauRiengDetection) {
        sauRiengDetection->detect(srcRgb, objects, 0.3, 0.3);
        __android_log_print(ANDROID_LOG_DEBUG, "ncnn", "sau rieng");
    }


    jclass arrayListClass = env->FindClass("java/util/ArrayList");
    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
    jobject arrayList = env->NewObject(arrayListClass, arrayListConstructor);

    // Get the add method of ArrayList
    jmethodID arrayListAdd = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");
    for (const Object &obj: objects) {
        std::ostringstream oss;
        oss << obj.label << " " << obj.rect.x << " " << obj.rect.y << " "
            << obj.rect.width << " " << obj.rect.height << " " << obj.prob;
        std::string objName = oss.str();
        jstring javaString = env->NewStringUTF(objName.c_str());  // Convert to jstring
        env->CallBooleanMethod(arrayList, arrayListAdd, javaString);
        env->DeleteLocalRef(javaString);  // Clean up local reference
    }
    return arrayList;
}