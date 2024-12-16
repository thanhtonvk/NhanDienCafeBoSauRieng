package com.tondz.nhandienbenhcaytrong.register;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.tondz.nhandienbenhcaytrong.R;
import com.tondz.nhandienbenhcaytrong.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    FirebaseAuth auth;
    TextInputEditText edtEmail, edtFullname, edtPassword, edtConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        onClick();
        auth = FirebaseAuth.getInstance();

    }

    private void init() {
        edtEmail = findViewById(R.id.edtEmail);
        edtFullname = findViewById(R.id.edtFullName);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
    }

    private void onClick() {
        findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register() {
        String email = edtEmail.getText().toString();
        String fullName = edtFullname.getText().toString();
        String password = edtPassword.getText().toString();
        String confirmPassword = edtConfirmPassword.getText().toString();
        if (email.isEmpty() || fullName.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        } else {
            if (password.equals(confirmPassword)) {
                if (password.length() >= 6) {
                    ProgressDialog dialog = new ProgressDialog(RegisterActivity.this);
                    dialog.setMessage("Đang đăng ký...");
                    dialog.show();
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                        Log.e("TAG", "register: "+task.toString() );
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getApplicationContext(), "Đăng ký thất bại, tài khoản đã tồn tại ", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }).addOnFailureListener(command -> {
                        Toast.makeText(getApplicationContext(), "Đăng ký thất bại do lỗi kết nối ", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    Toast.makeText(getApplicationContext(), " Mật khẩu phải >6 kí tự", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(getApplicationContext(), " Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            }

        }
    }
}