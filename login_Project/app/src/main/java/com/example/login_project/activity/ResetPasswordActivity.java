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

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText newPasswordInput, confirmPasswordInput;
    private Button resetPasswordButton;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        newPasswordInput = findViewById(R.id.new_password_input);
        confirmPasswordInput = findViewById(R.id.confirm_password_input);
        resetPasswordButton = findViewById(R.id.btn_reset_password);

        // Lấy email từ Intent
        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = newPasswordInput.getText().toString().trim();
                String confirmPassword = confirmPasswordInput.getText().toString().trim();

                if (newPassword.isEmpty()) {
                    newPasswordInput.setError("Password is required");
                } else if (!newPassword.equals(confirmPassword)) {
                    confirmPasswordInput.setError("Passwords do not match");
                } else {
                    // Cập nhật mật khẩu trong cơ sở dữ liệu
                    updatePasswordInDatabase(email, newPassword);
                    Toast.makeText(ResetPasswordActivity.this, "Password reset successful", Toast.LENGTH_SHORT).show();

                    // Chuyển đến màn hình đăng nhập
                    Intent loginIntent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                    finish();
                }
            }
        });
    }

    // Hàm cập nhật mật khẩu trong SQLite
    private void updatePasswordInDatabase(String email, String newPassword) {
        UserDatabaseHelper dbHelper = new UserDatabaseHelper(this);
        dbHelper.updatePassword(email, newPassword);
    }
}
