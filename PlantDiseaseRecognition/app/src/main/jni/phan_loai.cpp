//
// Created by TonSociu on 23/8/24.
//
#include "phan_loai.h"
#include <string.h>
#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/highgui/highgui.hpp>
#include "cpu.h"

using namespace cv;

PhanLoai::PhanLoai() {

}

int PhanLoai::load(AAssetManager *mgr) {
    model.clear();
    ncnn::set_cpu_powersave(2);
    ncnn::set_omp_num_threads(ncnn::get_big_cpu_count());
    model.opt = ncnn::Option();
    model.opt.num_threads = ncnn::get_big_cpu_count();
    char paramPath[255];
    char modelPath[255];
    sprintf(paramPath, "bo.param");
    sprintf(modelPath, "bo.bin");
    model.load_param(mgr, paramPath);
    model.load_model(mgr, modelPath);
    return 0;
}

int PhanLoai::predict(cv::Mat src, std::vector<float> &result) {
    ncnn::Mat in_net = ncnn::Mat::from_pixels_resize(src.clone().data,
                                                     ncnn::Mat::PIXEL_RGB, src.cols,
                                                     src.rows,
                                                     224, 224);
    float norm[3] = {1 / 127.5f, 1 / 127.5f, 1 / 127.5f};
    float mean[3] = {127.5f, 127.5f, 127.5f};
    in_net.substract_mean_normalize(0, norm);
    ncnn::Extractor extractor = model.create_extractor();
    extractor.set_light_mode(true);
    extractor.set_num_threads(4);
    extractor.input("in0", in_net);
    ncnn::Mat outBlob;
    extractor.extract("out0", outBlob);
    for (int i = 0; i < outBlob.w; i++) {
        float test = outBlob.row(0)[i];
        result.push_back(test);
    }
    return 0;
}

int PhanLoai::predictPath(const std::string &imagePath, std::vector<float> &result) {
    cv::Mat bgr = imread(imagePath, IMREAD_COLOR);
    __android_log_print(ANDROID_LOG_DEBUG, "ncnn", "setOutputWindow %s", bgr.data);
    cv::Mat rgb;
    cv::cvtColor(bgr, rgb, cv::COLOR_BGR2RGB);
    ncnn::Mat in_net = ncnn::Mat::from_pixels_resize(rgb.clone().data,
                                                     ncnn::Mat::PIXEL_BGR2RGB, rgb.cols,
                                                     rgb.rows,
                                                     224, 224);
    float norm[3] = {1 / 127.5f, 1 / 127.5f, 1 / 127.5f};
    float mean[3] = {127.5f, 127.5f, 127.5f};
    in_net.substract_mean_normalize(0, norm);
    ncnn::Extractor extractor = model.create_extractor();
    extractor.set_light_mode(true);
    extractor.set_num_threads(4);
    extractor.input("in0", in_net);
    ncnn::Mat outBlob;
    extractor.extract("out0", outBlob);
    for (int i = 0; i < outBlob.w; i++) {
        float test = outBlob.row(0)[i];

        result.push_back(test);
    }
    return 0;
}