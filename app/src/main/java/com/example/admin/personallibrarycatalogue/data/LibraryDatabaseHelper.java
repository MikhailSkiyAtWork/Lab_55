package com.example.admin.personallibrarycatalogue.data;

/**
 * Created by Mikhail Valuyskiy on 25.05.2015.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.admin.personallibrarycatalogue.data.DatabaseContract.BooksTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class which helps work with database
 */
public class LibraryDatabaseHelper extends SQLiteOpenHelper {

    // When the database schema was changed, you must increment the database version
    private static final int DATABASE_VERSION = 4;
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
                BooksTable.COVER + " BLOB );";

        sqLiteDatabase.execSQL(SQL_CREATE_BOOKS_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        onCreate(sqLiteDatabase);
    }

    public boolean deleteBook(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(BooksTable.TABLE_NAME, BooksTable._ID + " = " + id, null);
        if (rowsDeleted > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void deleteDatabase(Context context) {
        context.deleteDatabase(LibraryDatabaseHelper.DATABASE_NAME);
    }

    /**
     * Returns Book or null if there is no such book
     */
    public Book getBookById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT * FROM " + BooksTable.TABLE_NAME +
                " WHERE " + BooksTable._ID + " = " + id;

        Cursor cursor = db.rawQuery(selectQuery, null);
        Book book;
        if (cursor.moveToFirst()) {
            book = getBookFromCursor(cursor);
        } else {
            return null;
        }

        db.close();
        return book;
    }

    public void updateBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = putBookIntoContentValues(book);
        db.update(BooksTable.TABLE_NAME, values, BooksTable._ID + "=" + book.getId(), null);
        db.close();
    }

    public void addBook(Book book) {
        if (book != null) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = putBookIntoContentValues(book);
            db.insert(BooksTable.TABLE_NAME, null, values);
            db.close();
        } else {
            throw new IllegalArgumentException("Passed book object is null");
        }
    }

    public List<Book> getAllBooks() {
        SQLiteDatabase db = this.getWritableDatabase();
        List<Book> bookList = new ArrayList<Book>();
        String selectQuery = "SELECT * FROM " + BooksTable.TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Book book = getBookFromCursor(cursor);
                bookList.add(book);
            } while (cursor.moveToNext());
        }
        db.close();
        return bookList;
    }

    /**
     * Put values from object Book into ContentValues
     */
    private ContentValues putBookIntoContentValues(Book book) {
        ContentValues values = new ContentValues();
        values.put(BooksTable.TITLE, book.getTitle());
        values.put(BooksTable.AUTHOR, book.getAuthor());
        values.put(BooksTable.DESCRIPTION, book.getDescription());
        values.put(BooksTable.COVER, book.getCover());
        return values;
    }

    private Book getBookFromCursor(Cursor cursor){
        int id = cursor.getInt(cursor.getColumnIndex(BooksTable._ID));
        String title = cursor.getString(cursor.getColumnIndex(BooksTable.TITLE));
        String author = cursor.getString(cursor.getColumnIndex(BooksTable.AUTHOR));
        String description = cursor.getString(cursor.getColumnIndex(BooksTable.DESCRIPTION));
        byte[] cover = cursor.getBlob(cursor.getColumnIndex(BooksTable.COVER));
        Book book = new Book(id,title,author,description,cover);

        return book;
    }
}
