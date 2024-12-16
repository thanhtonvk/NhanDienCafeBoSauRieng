package com.tondz.nhandienbenhcaytrong;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.view.Surface;

import java.util.List;

public class CayTrongSDK {
    public native boolean loadModel(AssetManager mgr, int modelid);
    public native boolean openCamera(int facing);
    public native boolean closeCamera();
    public native boolean setOutputWindow(Surface surface);
    public native String predictCapture();
    public native String predictImagePath(String filePath);
    public native Bitmap getImage();
    public native List<String> predictCaPhePath(String filePath);
    public native List<String> predictSauRiengPath(String filePath);
    public native List<String> predictCaPhe();
    public native List<String> predictSauRieng();

    static {
        System.loadLibrary("caytrongsdk");
    }
}
