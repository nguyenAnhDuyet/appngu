package com.example.login_project.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.login_project.R;
import com.example.login_project.model.User;

public class ProfileActivity extends Activity {
    private ImageView userAvatar, editAvatarIcon;
    private EditText usernameEditText, genderEditText;
    private Button saveButton;
    private User currentUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userAvatar = findViewById(R.id.userAvatar);
        editAvatarIcon = findViewById(R.id.editAvatarIcon);
        usernameEditText = findViewById(R.id.usernameEditText);
        genderEditText = findViewById(R.id.genderEditText);
        saveButton = findViewById(R.id.saveButton);

        // Assume currentUser is obtained from the intent or a local storage/database
        currentUser = new User(1, "User123", "user@example.com", "Nam");

        // Set initial user information
        usernameEditText.setText(currentUser.getUsername());
        genderEditText.setText(currentUser.getGender());

        // Handle avatar editing
        editAvatarIcon.setOnClickListener(v -> {
            // Open image picker to change avatar
            Toast.makeText(this, "Chọn ảnh đại diện mới", Toast.LENGTH_SHORT).show();
        });

        // Handle saving the user profile
        saveButton.setOnClickListener(v -> {
            // Save updated user information
            currentUser.setUsername(usernameEditText.getText().toString());
            currentUser.setGender(genderEditText.getText().toString());

            Toast.makeText(this, "Thông tin đã được lưu!", Toast.LENGTH_SHORT).show();
            // Return to the previous activity
            finish();
        });
    }
}
