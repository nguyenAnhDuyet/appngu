package com.example.login_project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.login_project.R;
import com.example.login_project.helper.UserDatabaseHelper;

public class OtpActivity extends AppCompatActivity {

    private EditText otpInput;
    private String generatedOTP, email, password, username, gender;
    private boolean isForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        otpInput = findViewById(R.id.otp_input);
        Button verifyOtpButton = findViewById(R.id.btn_verify_otp);

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        generatedOTP = intent.getStringExtra("generatedOTP");
        email = intent.getStringExtra("email");
        isForgotPassword = intent.getBooleanExtra("isForgotPassword", false);  // Xác định trạng thái
        if (!isForgotPassword) {
            password = intent.getStringExtra("password");
            username = intent.getStringExtra("username");
            gender = intent.getStringExtra("gender");
        }

        verifyOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredOTP = otpInput.getText().toString().trim();

                if (enteredOTP.isEmpty()) {
                    Toast.makeText(OtpActivity.this, "Please enter the OTP", Toast.LENGTH_SHORT).show();
                } else if (enteredOTP.equals(generatedOTP)) {  // So sánh OTP dưới dạng chuỗi
                    // OTP hợp lệ
                    if (isForgotPassword) {
                        // Nếu là quên mật khẩu, chuyển đến màn hình đặt lại mật khẩu
                        Intent resetPasswordIntent = new Intent(OtpActivity.this, ResetPasswordActivity.class);
                        resetPasswordIntent.putExtra("email", email);
                        startActivity(resetPasswordIntent);
                        finish();
                    } else {
                        // Nếu là đăng ký, lưu thông tin người dùng vào database
                        saveUserToDatabase(email, password, username, gender);
                        Toast.makeText(OtpActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();

                        // Chuyển đến màn hình đăng nhập hoặc trang chủ
                        Intent loginIntent = new Intent(OtpActivity.this, LoginActivity.class);
                        startActivity(loginIntent);
                        finish();
                    }
                } else {
                    Toast.makeText(OtpActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Hàm lưu người dùng vào SQLite (cho đăng ký)
    private void saveUserToDatabase(String email, String password, String username, String gender) {
        UserDatabaseHelper dbHelper = new UserDatabaseHelper(this);
        dbHelper.addUser(email, password, username, gender);
    }
}
