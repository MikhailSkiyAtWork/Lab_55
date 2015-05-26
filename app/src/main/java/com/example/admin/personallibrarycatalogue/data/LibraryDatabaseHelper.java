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
    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "library.db";

    public LibraryDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Creates table to hold book data
     *
     * @param sqLiteDatabase
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
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.BooksTable.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void addBook(Book book) {
        if (book != null) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(BooksTable.TITLE, book.getTitle());
            values.put(BooksTable.AUTHOR, book.getAuthor());
            values.put(BooksTable.DESCRIPTION, book.getDescription());
            values.put(BooksTable.COVER, book.getCover());

            db.insert(BooksTable.TABLE_NAME, null, values);
            db.close();
        } else {
            throw new NullPointerException("Passed book object is null");
        }
    }

    public List<Book> getAllBooks(){
        SQLiteDatabase db = this.getWritableDatabase();
        List<Book> bookList = new ArrayList<Book>();

        String selectQuery = "SELECT * FROM " + BooksTable.TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery,null);

        if (cursor.moveToFirst()) {
            do {
                Book book = new Book();
                book.setTitle(cursor.getString(cursor.getColumnIndex(BooksTable.TITLE)));
                book.setAuthor(cursor.getString(cursor.getColumnIndex(BooksTable.AUTHOR)));

                if ((cursor.getString(cursor.getColumnIndex(BooksTable.DESCRIPTION))) != null) {
                    book.setDescription(cursor.getString(cursor.getColumnIndex(BooksTable.DESCRIPTION)));
                }

                if (cursor.getBlob(cursor.getColumnIndex(BooksTable.COVER)) != null) {
                    book.setCover(cursor.getBlob(cursor.getColumnIndex(BooksTable.COVER)));
                }
                bookList.add(book);
            } while (cursor.moveToNext());
        }
        db.close();
        return bookList;
    }


}
