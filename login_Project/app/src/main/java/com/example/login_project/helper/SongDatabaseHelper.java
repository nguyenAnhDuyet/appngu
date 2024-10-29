package com.example.login_project.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.login_project.model.Song;

import java.util.ArrayList;

public class SongDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "songs.db";
    private static final int DATABASE_VERSION = 3; // Updated version to add album cover support
    private static final String TABLE_SONGS = "songs";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";      // Song title
    private static final String COLUMN_ARTIST = "artist";    // Artist
    private static final String COLUMN_FILE_PATH = "file";   // File path
    private static final String COLUMN_COVER_PATH = "cover"; // Album cover path

    public SongDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_SONGS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_ARTIST + " TEXT, " +
                COLUMN_FILE_PATH + " TEXT, " +
                COLUMN_COVER_PATH + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 3) {
            db.execSQL("ALTER TABLE " + TABLE_SONGS + " ADD COLUMN " + COLUMN_COVER_PATH + " TEXT");
        }
    }

    // Add a new song to the database
    public void addSong(Song song) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, song.getTitle());
        values.put(COLUMN_ARTIST, song.getArtist());
        values.put(COLUMN_FILE_PATH, song.getFilePath());
        values.put(COLUMN_COVER_PATH, song.getCoverPath());

        // Insert the new record into the table
        db.insert(TABLE_SONGS, null, values);
        db.close();
    }

    // Retrieve all songs from the database
    public ArrayList<Song> getAllSongs() {
        ArrayList<Song> songList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query all records from the SONGS table
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SONGS, null);

        // Check if there are records returned
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
                String artist = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ARTIST));
                String filePath = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FILE_PATH));
                String coverPath = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COVER_PATH));

                // Add the song to the list
                songList.add(new Song(id, title, artist, filePath, coverPath));
            } while (cursor.moveToNext());
        }

        // Close the cursor and the database
        cursor.close();
        db.close();
        return songList;
    }
}
