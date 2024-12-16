package com.tondz.nhandienbenhcaytrong.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tondz.nhandienbenhcaytrong.MainActivity;
import com.tondz.nhandienbenhcaytrong.databinding.ActivityLoginBinding;
import com.tondz.nhandienbenhcaytrong.register.RegisterActivity;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        onClick();
        loadAccount();
    }


    private void onClick() {
        binding.btnRegister.setOnClickListener(view -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
        binding.btnLogin.setOnClickListener(v -> {
            login();
        });
        binding.btnForgotPassword.setOnClickListener(v -> {
            forgotPassword();
        });
    }

    private void forgotPassword() {
        String email = binding.edtEmail.getText().toString();
        if (email.isEmpty()) {
            Toast.makeText(getApplicationContext(), " Thông tin Email không được bỏ trong ", Toast.LENGTH_SHORT).show();
        } else {
            auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Thông báo");
                    builder.setMessage("Yêu cầu quên mật khẩu đã được gửi về mail của bạn");
                    builder.setPositiveButton("OK", (dialog, which) -> {
                        dialog.dismiss();
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Thông báo");
                    builder.setMessage("Có lỗi xảy ra, hãy thử lại");
                    builder.setPositiveButton("OK", (dialog, which) -> {
                        dialog.dismiss();
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }).addOnFailureListener(command ->
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Thông báo");
                builder.setMessage("Có lỗi xảy ra, hãy thử lại");
                builder.setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            });
        }

    }

    private void login() {
        String email = binding.edtEmail.getText().toString();
        String password = binding.edtPassword.getText().toString();
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getApplicationContext(), " Thông tin không được bỏ trong ", Toast.LENGTH_SHORT).show();
        } else {
            ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
            dialog.setTitle("Đang đăng nhập");
            dialog.show();
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    saveAccount(email, password);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Tài khoản hoặc mật khẩu không chinh xác", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }).addOnFailureListener(command -> {
                Toast.makeText(getApplicationContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });
        }
    }

    private void saveAccount(String email, String password) {
        SharedPreferences prefs = getSharedPreferences("Account", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.apply();
    }

    private void loadAccount() {
        SharedPreferences prefs = getSharedPreferences("Account", Context.MODE_PRIVATE);
        String email = prefs.getString("email", "");
        String password = prefs.getString("password", "");
        binding.edtEmail.setText(email);
        binding.edtPassword.setText(password);
        login();
    }


}