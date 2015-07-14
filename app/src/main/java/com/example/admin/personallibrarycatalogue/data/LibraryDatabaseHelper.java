package com.example.admin.personallibrarycatalogue.data;

/**
 * Created by Mikhail Valuyskiy on 25.05.2015.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.example.admin.personallibrarycatalogue.data.DatabaseContract.BooksTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class which helps work with database
 */
public class LibraryDatabaseHelper extends SQLiteOpenHelper {

    // When the database schema was changed, you must increment the database version
    private static final int DATABASE_VERSION = 6;
    static final String DATABASE_NAME = "library.db";

    public LibraryDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Creates table to hold book data
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_BOOKS_TABLE = "CREATE TABLE " + BooksTable.TABLE_NAME + " (" +
                BooksTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                BooksTable.AUTHOR + " TEXT NOT NULL, " +
                BooksTable.TITLE + " TEXT NOT NULL, " +
                BooksTable.DESCRIPTION + " TEXT, " +
                BooksTable.COVER + " BLOB," +
                BooksTable.YEAR + " INTEGER, " +
                BooksTable.ISBN + " TEXT );";

        sqLiteDatabase.execSQL(SQL_CREATE_BOOKS_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //onCreate(sqLiteDatabase);
        if (oldVersion <= DATABASE_VERSION) {
            sqLiteDatabase.execSQL("ALTER TABLE " + BooksTable.TABLE_NAME + " ADD " + BooksTable.YEAR + " INTEGER ");
            sqLiteDatabase.execSQL("ALTER TABLE " + BooksTable.TABLE_NAME + " ADD " + BooksTable.ISBN + " TEXT ");
        }
    }

    public static List<Book> getBooksList(Cursor cursor) {
        List<Book> bookList = new ArrayList<Book>();

        if (cursor.moveToFirst()) {
            do {
                Book book = getBook(cursor);
                bookList.add(book);
            } while (cursor.moveToNext());
        }
        return bookList;
    }

    public static Book getBook(Cursor cursor) {
        int position = cursor.getPosition();
        if (position > 0) {
            Book book = setBookValues(cursor);
            return book;
        } else if (cursor.moveToFirst()) {
            Book book = setBookValues(cursor);
            return book;
        }
        return null;
    }

    public static Book setBookValues(Cursor cursor) {
        Book book = getBookFromCursor(cursor);
        return book;
    }

    public static ContentValues createBooksValues(Book book) {
        ContentValues booksValues = new ContentValues();
        booksValues.put(DatabaseContract.BooksTable.TITLE, book.getTitle());
        booksValues.put(DatabaseContract.BooksTable.AUTHOR, book.getAuthor());
        booksValues.put(DatabaseContract.BooksTable.DESCRIPTION, book.getDescription());
        booksValues.put(DatabaseContract.BooksTable.COVER, book.getCover());
        booksValues.put(BooksTable.YEAR, book.getYear());
        booksValues.put(BooksTable.ISBN, book.getIsbn());
        return booksValues;
    }

    public static Book getBookFromCursor(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(DatabaseContract.BooksTable._ID));
        String title = cursor.getString(cursor.getColumnIndex(BooksTable.TITLE));
        String author = cursor.getString(cursor.getColumnIndex(BooksTable.AUTHOR));
        String description = cursor.getString(cursor.getColumnIndex(BooksTable.DESCRIPTION));
        int year = cursor.getInt(cursor.getColumnIndex(BooksTable.YEAR));
        String isbn = cursor.getString(cursor.getColumnIndex(BooksTable.ISBN));
        byte[] cover = cursor.getBlob(cursor.getColumnIndex(BooksTable.COVER));

        Book book = new Book(id, title, author, description, cover, year, isbn);
        return book;
    }
}
