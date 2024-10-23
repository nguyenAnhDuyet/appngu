package com.example.login_project.helper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.login_project.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "user_db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_USERNAME = "username"; // Thêm cột username
    private static final String COLUMN_GENDER = "gender"; // Thêm cột gender
    private static final String COLUMN_ROLE = "role"; // Thêm cột role

    public UserDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_GENDER + " TEXT,"
                + COLUMN_ROLE + " INTEGER DEFAULT 1,"
                + "isBanned INTEGER DEFAULT 0" // Thêm cột isBanned
                + ")";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN isBanned INTEGER DEFAULT 0");
        }
    }

    public void addUser(String email, String password, String username, String gender) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_GENDER, gender);
        values.put(COLUMN_ROLE, 1); // Gán giá trị role mặc định là 1
        db.insert(TABLE_USERS, null, values);
        db.close();
    }


    public Cursor getUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?", new String[]{email, password});
    }

    public boolean updatePassword(String email, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Cập nhật mật khẩu cho email
        db.execSQL("UPDATE users SET password = ? WHERE email = ?", new Object[]{newPassword, email});
        return true; // Trả về true nếu thành công
    }
    // Thêm một hàm để lấy vai trò
    public int getUserRole(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_ROLE + " FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?", new String[]{email, password});
        if (cursor != null && cursor.moveToFirst()) {
            int role = cursor.getInt(0); // Giả sử role là cột đầu tiên
            cursor.close();
            return role;
        }
        return -1; // Trả về -1 nếu không tìm thấy người dùng
    }
    @SuppressLint("Range")
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_USERS;
        Cursor cursor = db.rawQuery(selectQuery, null); // Loại bỏ selectionArgs

        if (cursor.moveToFirst()) { // Sửa lại điều kiện
            do {
                User user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID))); // Đảm bảo COLUMN_ID đã được định nghĩa
                user.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME))); // Đảm bảo COLUMN_USERNAME đã được định nghĩa
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL))); // Đảm bảo COLUMN_EMAIL đã được định nghĩa
                user.setGender(cursor.getString(cursor.getColumnIndex(COLUMN_GENDER)));

                userList.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return userList;
    }
    @SuppressLint("Range")
    public User getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_ID + "=?", new String[]{String.valueOf(userId)});

        if (cursor != null && cursor.moveToFirst()) {
            User user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
            user.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME)));
            user.setGender(cursor.getString(cursor.getColumnIndex(COLUMN_GENDER)));
            user.setRole(cursor.getInt(cursor.getColumnIndex(COLUMN_ROLE)));
            cursor.close();
            return user;
        }
        cursor.close();
        return null;
    }

    public boolean banUser(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("isBanned", 1); // Giả sử bạn có cột isBanned trong bảng users
        int rowsAffected = db.update(TABLE_USERS, values, COLUMN_ID + "=?", new String[]{String.valueOf(userId)});
        db.close();
        return rowsAffected > 0; // Trả về true nếu cập nhật thành công
    }

    public boolean isUserBanned(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{"isBanned"}, COLUMN_EMAIL + "=?", new String[]{email}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int isBanned = cursor.getInt(cursor.getColumnIndexOrThrow("isBanned"));
            cursor.close();
            return isBanned == 1; // Trả về true nếu bị ban
        }
        return false;
    }

    public boolean updateBanStatus(int userId, boolean banStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("isBanned", banStatus ? 1 : 0);
        int rowsAffected = db.update(TABLE_USERS, values, COLUMN_ID + "=?", new String[]{String.valueOf(userId)});
        db.close();
        return rowsAffected > 0; // Trả về true nếu cập nhật thành công
    }

    @SuppressLint("Range")
    public List<User> getUsersWithRole1() {
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Lấy người dùng với role = 1
        String selectQuery = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_ROLE + " = 1";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                user.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
                user.setGender(cursor.getString(cursor.getColumnIndex(COLUMN_GENDER)));
                user.setRole(cursor.getInt(cursor.getColumnIndex(COLUMN_ROLE)));

                userList.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return userList;
    }






}
