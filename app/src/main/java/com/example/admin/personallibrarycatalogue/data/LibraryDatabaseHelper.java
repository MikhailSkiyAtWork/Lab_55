package com.example.admin.personallibrarycatalogue.data;

/**
 * Created by Mikhail Valuyskiy on 25.05.2015.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.admin.personallibrarycatalogue.data.*;

/**
 * Utility class which helps work with database
 */
public class LibraryDatabaseHelper extends SQLiteOpenHelper {

    // When the database schema was changed, you must increment the database version
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "library.db";

    public LibraryDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Creates table to hold book data
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_BOOKS_TABLE = "CREATE TABLE " + DatabaseContract.BooksTable.TABLE_NAME + " (" +
                DatabaseContract.BooksTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DatabaseContract.BooksTable.AUTHOR + " TEXT NOT NULL, " +
                DatabaseContract.BooksTable.TITLE + "TEXT NOT NULL, " +
                DatabaseContract.BooksTable.DESCRIPTION + "TEXT, " +
                DatabaseContract.BooksTable.COVER + "BLOB );";

        sqLiteDatabase.execSQL(SQL_CREATE_BOOKS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.BooksTable.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void addBook(Book book){

    }

}
