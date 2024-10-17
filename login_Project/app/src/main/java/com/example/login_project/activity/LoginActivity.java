package com.example.login_project.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.login_project.R;
import com.example.login_project.helper.UserDatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Kiểm tra trạng thái đăng nhập trong SharedPreferences
        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        int userRole = sharedPreferences.getInt("userRole", -1);

        // Nếu người dùng đã đăng nhập trước đó và không phải là admin, bỏ qua màn hình đăng nhập
        if (isLoggedIn && userRole == 1) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        } else if (isLoggedIn && userRole == 2) {
            startActivity(new Intent(LoginActivity.this, AdminActivity.class));
            finish();
        }

        // Nếu chưa đăng nhập, tiếp tục hiển thị màn hình đăng nhập
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        Button loginButton = findViewById(R.id.btn_login);

        // Xử lý sự kiện khi bấm nút đăng nhập
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                if (checkLogin(email, password)) {
                    UserDatabaseHelper dbHelper = new UserDatabaseHelper(LoginActivity.this);
                    int role = dbHelper.getUserRole(email, password);
                    Intent intent;

                    // Lưu trạng thái đăng nhập vào SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isLoggedIn", true);  // Ghi nhớ trạng thái đăng nhập
                    editor.putInt("userRole", role);  // Lưu vai trò người dùng

                    if (role == 1) {
                        // Người dùng thông thường
                        intent = new Intent(LoginActivity.this, HomeActivity.class);
                    } else if (role == 2) {
                        // Admin
                        intent = new Intent(LoginActivity.this, AdminActivity.class);
                    } else {
                        Toast.makeText(LoginActivity.this, "Role not defined", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    editor.apply(); // Lưu các thay đổi vào SharedPreferences
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Link đến trang đăng ký và quên mật khẩu
        findViewById(R.id.login_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        findViewById(R.id.forgot_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });
    }

    // Hàm kiểm tra thông tin đăng nhập
    private boolean checkLogin(String email, String password) {
        UserDatabaseHelper dbHelper = new UserDatabaseHelper(this);
        Cursor cursor = dbHelper.getUser(email, password);

        if (cursor != null && cursor.getCount() > 0) {
            if (dbHelper.isUserBanned(email)) {
                Toast.makeText(this, "This account is banned.", Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        }
        return false;
    }
}
