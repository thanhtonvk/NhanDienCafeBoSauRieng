package com.tondz.nhandienbenhcaytrong.CayBo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.tondz.nhandienbenhcaytrong.CayTrongSDK;
import com.tondz.nhandienbenhcaytrong.Common;
import com.tondz.nhandienbenhcaytrong.FileUtils;
import com.tondz.nhandienbenhcaytrong.KetQuaActivity;
import com.tondz.nhandienbenhcaytrong.R;

import java.io.FileNotFoundException;
import java.util.List;

public class LaBoActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    CayTrongSDK cayTrongSDK = new CayTrongSDK();
    private SurfaceView cameraView;
    private static final int REQUEST_CAMERA = 510;
    private static final int SELECT_IMAGE = 456;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ca_phe);
        init();
        reload();
        onClick();
    }

    public void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                Bitmap bitmap = Common.decodeUri(selectedImageUri, getApplicationContext());
                String imagePath = FileUtils.getPath(getApplicationContext(), selectedImageUri);
                Toast.makeText(getApplicationContext(), imagePath, Toast.LENGTH_SHORT).show();
                String result = cayTrongSDK.predictImagePath(imagePath);
                Common.result = predict(result);
                Common.bitmap = bitmap;
                startActivity(new Intent(getApplicationContext(), KetQuaActivity.class));
                Log.e("IMAGE_PATH", imagePath);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void onClick() {
        findViewById(R.id.btnCapture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.bitmap = cayTrongSDK.getImage();
                String result = cayTrongSDK.predictCapture();
                Common.result = predict(result);
                Log.e("MainActivity", "result: " + Common.result);
                startActivity(new Intent(getApplicationContext(), KetQuaActivity.class));

            }
        });
        findViewById(R.id.btnImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
    }

    private String predict(String result) {
        if (result.isEmpty()) return "Bình thường# ";
        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        String[] arr = result.split(",");
        if (Float.parseFloat(arr[0]) > 0.5) {
            return Common.bos[0] + "# " + Common.chuaBo[0];
        } else {
            return Common.bos[1] + "# " + Common.chuaBo[1];
        }

    }

    private void init() {
        cameraView = findViewById(R.id.cameraView);

        cameraView.getHolder().addCallback(this);

    }


    private void reload() {
        boolean ret_init = cayTrongSDK.loadModel(getAssets(), 0);
        if (!ret_init) {
            Log.e("MainActivity", "model loadModel failed");
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        cayTrongSDK.setOutputWindow(holder.getSurface());


    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    public void onResume() {
        super.onResume();

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA);
        }
        cayTrongSDK.openCamera(1);
    }

    @Override
    public void onPause() {
        super.onPause();

        cayTrongSDK.closeCamera();
    }
}