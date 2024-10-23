package com.example.login_project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.login_project.JavaMailAPI;
import com.example.login_project.R;

import java.util.Random;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailInput;
    private Button sendOtpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailInput = findViewById(R.id.email_input);
        sendOtpButton = findViewById(R.id.btn_send_otp);

        sendOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString().trim();
                if (email.isEmpty()) {
                    Toast.makeText(ForgotPasswordActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                } else {
                    // Gửi OTP đến email
                    String otp = generateOtp(); // Lưu OTP dưới dạng String
                    sendOtpEmail(email, otp);
                }
            }
        });
    }

    private String generateOtp() {
        Random random = new Random();
        return String.valueOf(100000 + random.nextInt(900000)); // Tạo mã OTP 6 chữ số dưới dạng chuỗi
    }

    private void sendOtpEmail(String email, String otp) { // OTP dưới dạng String
        String subject = "Your OTP Code";
        String message = otp; // Truyền OTP dưới dạng chuỗi
        JavaMailAPI javaMailAPI = new JavaMailAPI(email, subject, message);
        javaMailAPI.execute();

        // Chuyển đến màn hình nhập OTP
        Intent otpIntent = new Intent(ForgotPasswordActivity.this, OtpActivity.class);
        otpIntent.putExtra("generatedOTP", otp); // Truyền OTP dưới dạng chuỗi
        otpIntent.putExtra("email", email);
        otpIntent.putExtra("isForgotPassword", true);  // Đây là quên mật khẩu
        startActivity(otpIntent);
    }
}

