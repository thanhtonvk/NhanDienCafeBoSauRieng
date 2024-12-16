package com.tondz.nhandienbenhcaytrong;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tondz.nhandienbenhcaytrong.CayBo.LaBoActivity;
import com.tondz.nhandienbenhcaytrong.CayCaPhe.CaPheActivity;
import com.tondz.nhandienbenhcaytrong.CaySauRieng.SauRiengActivity;

public class MainActivity extends AppCompatActivity {
    boolean isPass = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String PASSWORD = "12345";
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference();

//        reference.child("pass").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.getValue().toString().equals(PASSWORD)) {
//                    isPass = true;
//                } else {
//                    Toast.makeText(getApplicationContext(), "Đã hết thời gian dùng thử", Toast.LENGTH_SHORT).show();
//                    finish();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                finish();
//            }
//        });
        findViewById(R.id.btnCayNgo).setOnClickListener(v -> {
            if (isPass) {
                startActivity(new Intent(this, CaPheActivity.class));
            }

        });
        findViewById(R.id.btnCayDau).setOnClickListener(v -> {
            startActivity(new Intent(this, SauRiengActivity.class));
        });
        findViewById(R.id.btnQuaCam).setOnClickListener(v -> {
            startActivity(new Intent(this, LaBoActivity.class));
        });

        checkPermissions();
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_MEDIA_IMAGES}, 100);
        }
    }
}