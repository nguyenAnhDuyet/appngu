package com.example.login_project.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.login_project.R;
import com.google.android.material.navigation.NavigationView;

public class AdminActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private Toolbar toolbar;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.navigationView);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_manage_songs) {
                    Intent intent = new Intent(AdminActivity.this, ManageSongsActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_manage_users) {
                    // Xử lý quản lý người dùng

                    Intent intent = new Intent(AdminActivity.this, UserListActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_logout) {
                    // Xử lý đăng xuất

                    logout();
                }
                return true;
            }

        });
    }

    // Hàm xử lý đăng xuất
    private void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();  // Xóa trạng thái đăng nhập trong SharedPreferences
        editor.apply();

        // Quay lại màn hình đăng nhập
        Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
