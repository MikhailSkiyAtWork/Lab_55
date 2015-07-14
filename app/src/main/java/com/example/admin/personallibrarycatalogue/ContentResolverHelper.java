package com.example.admin.personallibrarycatalogue;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.admin.personallibrarycatalogue.data.Book;
import com.example.admin.personallibrarycatalogue.data.DatabaseContract;
import com.example.admin.personallibrarycatalogue.data.LibraryDatabaseHelper;

/**
 * Created by Mikhail Valuyskiy on 13.07.2015.
 */
public class ContentResolverHelper {

    private static final String bookSelectionQuery_ = DatabaseContract.BooksTable.TABLE_NAME + "." + DatabaseContract.BooksTable._ID + " = ?";

    public static Book getBook (Context context,int id){
        Uri bookWithIdUri = DatabaseContract.BooksTable.buildBookUri(id);
        Cursor cursor = context.getContentResolver().query(bookWithIdUri,
                DatabaseContract.BOOK_COLUMNS,
                bookSelectionQuery_,
                new String[]{Integer.toString(id)},
                null);

        Book book = LibraryDatabaseHelper.getBook(cursor);
        return book;
    }
}
