package com.tondz.nhandienbenhcaytrong;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class KetQuaActivity extends AppCompatActivity {

    TextView txtKetQua;
    ImageView imgKetQua;
    TextView txtCachChua;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ket_qua);
        txtKetQua = findViewById(R.id.txtKetQua);
        imgKetQua = findViewById(R.id.imgKetQua);
        txtCachChua = findViewById(R.id.txtCachChua);
        if (Common.result.isEmpty()) {
            txtKetQua.setText("Khoẻ mạnh");
        } else {
            String[] ketqua = Common.result.split("#");
            if (ketqua.length != 2) return;
            txtKetQua.setText(ketqua[0]);
            txtCachChua.setText(ketqua[1]);
        }

        imgKetQua.setImageBitmap(Common.bitmap);

    }
}