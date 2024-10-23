package com.example.login_project.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login_project.R;


public class HomeActivity extends AppCompatActivity {

    private Button logoutButton;

    private SharedPreferences sharedPreferences;
    private static final String API_URL = "https://api.jamendo.com/v3.0/albums?client_id=ca8a7d9e&limit=10&format=json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize buttons and RecyclerView
        logoutButton = findViewById(R.id.btn_logout);

        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);



        // Set onClickListener cho nút Logout
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }



    // Xử lý đăng xuất
    private void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
