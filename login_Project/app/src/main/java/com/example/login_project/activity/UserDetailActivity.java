package com.example.login_project.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.login_project.R;
import com.example.login_project.helper.UserDatabaseHelper;
import com.example.login_project.model.User;

public class UserDetailActivity extends AppCompatActivity {

    private TextView textViewEmail, textViewUsername, textViewGender, textViewRole;
    private Button btnBanUnban;
    private UserDatabaseHelper userDatabaseHelper;
    private boolean isUserBanned;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        textViewEmail = findViewById(R.id.textViewEmail);
        textViewUsername = findViewById(R.id.textViewUsername);
        textViewGender = findViewById(R.id.textViewGender);
        textViewRole = findViewById(R.id.textViewRole);
        btnBanUnban = findViewById(R.id.btnBanUnban);

        userDatabaseHelper = new UserDatabaseHelper(this);

        // Lấy userId từ Intent
        int userId = getIntent().getIntExtra("userId", -1);

        // Lấy thông tin người dùng từ SQLite
        if (userId != -1) {
            User user = userDatabaseHelper.getUserById(userId);
            if (user != null) {
                textViewEmail.setText("Email: " + user.getEmail());
                textViewUsername.setText("Username: " + user.getUsername());
                textViewGender.setText("Gender: " + user.getGender());
                textViewRole.setText("Role: " + user.getRole());

                // Kiểm tra xem người dùng có bị ban hay không
                isUserBanned = userDatabaseHelper.isUserBanned(user.getEmail());
                updateBanButton();
            }
        }

        // Xử lý sự kiện khi nhấn nút ban/unban
        btnBanUnban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleBanStatus(userId);
            }
        });
    }

    // Cập nhật trạng thái nút Ban/Unban
    private void updateBanButton() {
        if (isUserBanned) {
            btnBanUnban.setText("Unban User");
        } else {
            btnBanUnban.setText("Ban User");
        }
    }

    // Thay đổi trạng thái ban của người dùng
    private void toggleBanStatus(int userId) {
        boolean newBanStatus = !isUserBanned;
        if (userDatabaseHelper.updateBanStatus(userId, newBanStatus)) {
            isUserBanned = newBanStatus;
            updateBanButton();
            String message = isUserBanned ? "User has been banned." : "User has been unbanned.";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to update ban status.", Toast.LENGTH_SHORT).show();
        }
    }
}
