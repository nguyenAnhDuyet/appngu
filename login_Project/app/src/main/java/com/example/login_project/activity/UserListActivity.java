package com.example.login_project.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.login_project.R;
import com.example.login_project.adapter.UserAdapter;
import com.example.login_project.helper.UserDatabaseHelper;
import com.example.login_project.model.User;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private UserDatabaseHelper userDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        recyclerView = findViewById(R.id.recyclerViewUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userDatabaseHelper = new UserDatabaseHelper(this);

        // Lấy danh sách người dùng có role = 1 từ SQLite và thiết lập Adapter
        List<User> userList = userDatabaseHelper.getUsersWithRole1();

        // Thêm context vào khi khởi tạo UserAdapter
        userAdapter = new UserAdapter(this, userList);
        recyclerView.setAdapter(userAdapter);
    }
}
