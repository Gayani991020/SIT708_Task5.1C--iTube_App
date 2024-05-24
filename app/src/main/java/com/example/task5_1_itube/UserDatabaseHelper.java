package com.example.task5_1_itube;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDatabaseHelper extends SQLiteOpenHelper {
    // Constants for database name and version
    private static final String DATABASE_NAME = "users.db"; // Name of the database
    private static final int DATABASE_VERSION = 2; // Version of the database

    // Constants for the users table and its columns
    private static final String TABLE_USERS = "users"; // Name of the users table
    private static final String COLUMN_ID = "id"; // Column for user ID (primary key)
    private static final String COLUMN_USERNAME = "username"; // Column for username
    private static final String COLUMN_PASSWORD = "password"; // Column for password

    // Constants for the playlist table and its columns
    private static final String PLAYLIST_TABLE = "playlist"; // Name of the playlist table
    private static final String COLUMN_LINK = "link"; // Column for storing playlist links

    // SQL statement to create the users table
    private static final String CREATE_TABLE_USERS =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USERNAME + " TEXT UNIQUE, " +
                    COLUMN_PASSWORD + " TEXT)"; // SQL to create the users table with ID, unique username, and password columns

    // SQL statement to create the playlist table
    private static final String CREATE_TABLE_PLAYLIST =
            "CREATE TABLE " + PLAYLIST_TABLE + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_LINK + " TEXT NOT NULL)"; // SQL to create the playlist table with ID and link columns

    /**
     * Constructor to create a new database helper instance.
     *
     * @param context The context in which the database is being created.
     */
    public UserDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION); // Calls the superclass constructor with the database name and version
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create both tables when the database is created for the first time
        db.execSQL(CREATE_TABLE_USERS); // Execute SQL to create the users table
        db.execSQL(CREATE_TABLE_PLAYLIST); // Execute SQL to create the playlist table
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) { // If upgrading from a version older than 2
            // Add the playlist table, leaving the users table unaffected
            db.execSQL(CREATE_TABLE_PLAYLIST); // Execute SQL to create the playlist table
        }
    }

    /**
     * Method to add a new user to the database.
     *
     * @param username The username of the new user.
     * @param password The password of the new user.
     * @return True if the user was added successfully, false otherwise.
     */
    public boolean addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase(); // Get a writable database instance
        ContentValues values = new ContentValues(); // Create ContentValues to hold the data
        values.put(COLUMN_USERNAME, username); // Put the username into the ContentValues
        values.put(COLUMN_PASSWORD, password); // Put the password into the ContentValues

        long result = db.insert(TABLE_USERS, null, values); // Insert the data into the users table
        return result != -1; // Return true if the insertion was successful, otherwise false
    }

    /**
     * Method to validate a user with a given username and password.
     *
     * @param username The username to validate.
     * @param password The password to validate.
     * @return True if the username and password are valid, false otherwise.
     */
    public boolean validateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase(); // Get a readable database instance
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password}); // Execute the query with provided username and password

        boolean isValid = cursor.moveToFirst(); // Move to the first record, if available, indicating a valid user
        cursor.close(); // Close the cursor to avoid memory leaks
        return isValid; // Return the validation result
    }

    /**
     * Method to add a link to the playlist.
     *
     * @param link The link to be added to the playlist.
     * @return True if the link was added successfully, false otherwise.
     */
    public boolean addLinkToPlaylist(String link) {
        SQLiteDatabase db = this.getWritableDatabase(); // Get a writable database instance
        ContentValues values = new ContentValues(); // Create ContentValues to hold the data
        values.put(COLUMN_LINK, link); // Put the link into the ContentValues

        long result = db.insert(PLAYLIST_TABLE, null, values); // Insert the link into the playlist table
        db.close(); // Close the database to release resources
        return result != -1; // Return true if the insertion was successful, otherwise false
    }

    /**
     * Method to get all playlist links from the database.
     *
     * @return A Cursor object containing all playlist links.
     */
    public Cursor getPlaylist() {
        SQLiteDatabase db = this.getReadableDatabase(); // Get a readable database instance
        return db.rawQuery("SELECT * FROM " + PLAYLIST_TABLE, null); // Execute query to retrieve all records from the playlist table
    }
}
