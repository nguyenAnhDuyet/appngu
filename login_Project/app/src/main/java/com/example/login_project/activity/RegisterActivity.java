package com.example.login_project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login_project.JavaMailAPI;
import com.example.login_project.R;

import java.util.Random;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput, confirmPasswordInput, usernameInput;
    private Spinner genderSpinner;
    private String generatedOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        confirmPasswordInput = findViewById(R.id.confirm_password_input);
        usernameInput = findViewById(R.id.username_input);
        genderSpinner = findViewById(R.id.gender_spinner);

        // Khởi tạo adapter cho gender spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);

        Button registerButton = findViewById(R.id.btn_register);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();
                String confirmPassword = confirmPasswordInput.getText().toString().trim();
                String username = usernameInput.getText().toString().trim();
                String gender = genderSpinner.getSelectedItem().toString();

                if (username.isEmpty()) {
                    usernameInput.setError("Username is required");
                } else if (email.isEmpty()) {
                    emailInput.setError("Email is required");
                } else if (!password.equals(confirmPassword)) {
                    confirmPasswordInput.setError("Passwords do not match");
                } else {
                    // Tạo OTP ngẫu nhiên
                    generatedOTP = generateOTP();
                    String subject = "Your OTP Code";
                    String message = "Your OTP code is: " + generatedOTP;

                    // Gửi OTP qua email
                    JavaMailAPI javaMailAPI = new JavaMailAPI(email, subject, message);
                    javaMailAPI.execute();

                    // Chuyển sang màn hình nhập OTP
                    Intent otpIntent = new Intent(RegisterActivity.this, OtpActivity.class);
                    otpIntent.putExtra("generatedOTP", generatedOTP);  // Đúng biến là generatedOTP
                    otpIntent.putExtra("email", email);
                    otpIntent.putExtra("password", password);
                    otpIntent.putExtra("username", username);
                    otpIntent.putExtra("gender", gender);
                    otpIntent.putExtra("isForgotPassword", false);  // Đây là đăng ký
                    startActivity(otpIntent);

                }
            }
        });
    }

    // Hàm tạo OTP ngẫu nhiên
    private String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Tạo mã OTP 6 chữ số
        return String.valueOf(otp);
    }
}
