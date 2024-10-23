package com.example.login_project.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.login_project.R;
import com.example.login_project.activity.UserDetailActivity;
import com.example.login_project.model.User;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;
    private Context context;

    // Constructor nhận vào danh sách User và Context
    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    // Tạo ViewHolder mới cho mỗi item của RecyclerView
    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    // Bind dữ liệu vào ViewHolder tương ứng với vị trí hiện tại
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.textViewName.setText(user.getUsername()); // Hiển thị tên người dùng

        // Set sự kiện khi nhấn vào item để mở UserDetailActivity
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, UserDetailActivity.class);
            intent.putExtra("userId", user.getId()); // Truyền userId qua Intent
            context.startActivity(intent);
        });
    }

    // Trả về số lượng item trong danh sách User
    @Override
    public int getItemCount() {
        return userList.size();
    }

    // ViewHolder class lưu trữ các thành phần giao diện của từng item
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        ImageView userIcon;

        // Khởi tạo ViewHolder với các thành phần giao diện
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            userIcon = itemView.findViewById(R.id.userIcon);
        }
    }
}
