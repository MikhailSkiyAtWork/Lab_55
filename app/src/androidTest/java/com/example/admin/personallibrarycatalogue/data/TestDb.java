package com.example.admin.personallibrarycatalogue.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.AndroidTestCase;
import android.util.Log;

import com.example.admin.personallibrarycatalogue.R;
import com.example.admin.personallibrarycatalogue.data.DatabaseContract.BooksTable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by Mikhail Valuyskiy on 26.05.2015.
 */
public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    private static final String BOOK_TITLE = "Thinking in Java";
    private static final String BOOK_AUTHOR = "Bruce Eckel";
    private static final String BOOK_DESCRIPTION = "Thinking in Java is a book about the Java programming language, written by Bruce Eckel and first published in 1998";

    private LibraryDatabaseHelper helper_;
    private SQLiteDatabase database_;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        helper_ = new LibraryDatabaseHelper(mContext);
        database_ = helper_.getWritableDatabase();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        mContext.deleteDatabase(LibraryDatabaseHelper.DATABASE_NAME);
        helper_.close();
    }

    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(LibraryDatabaseHelper.DATABASE_NAME);
        SQLiteDatabase database = new LibraryDatabaseHelper(mContext).getWritableDatabase();
        assertEquals(true, database.isOpen());
        database.close();
    }

    public void testInsert() {

        ContentValues book = createBooksValues();
        long bookId = database_.insert(BooksTable.TABLE_NAME, null, book);

        assertTrue(bookId != -1);
        Log.d(LOG_TAG, "New row id: " + bookId);

        Cursor cursor = database_.query(
                BooksTable.TABLE_NAME, // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        if (!cursor.moveToFirst()) {
            fail("Now data returned");
        }
        assertEquals(cursor.getString(cursor.getColumnIndex(BooksTable.TITLE)), BOOK_TITLE);
        assertEquals(cursor.getString(cursor.getColumnIndex(BooksTable.AUTHOR)), BOOK_AUTHOR);
        assertEquals(cursor.getString(cursor.getColumnIndex(BooksTable.DESCRIPTION)), BOOK_DESCRIPTION);

        cursor.close();
    }

    private ContentValues createBooksValues() {
        ContentValues booksValues = new ContentValues();
        booksValues.put(BooksTable.TITLE, BOOK_TITLE);
        booksValues.put(BooksTable.AUTHOR, BOOK_AUTHOR);
        booksValues.put(BooksTable.DESCRIPTION, BOOK_DESCRIPTION);
        Bitmap image = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
        booksValues.put(BooksTable.COVER, getBytesFromBitmap(image));
        return booksValues;
    }

    private static byte[] getBytesFromBitmap(Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte imageInByte[] = stream.toByteArray();
        return imageInByte;
    }

    private static Bitmap getBitmapFromBytes(byte[] image) {
        ByteArrayInputStream imageStream = new ByteArrayInputStream(image);
        Bitmap decodedImage = BitmapFactory.decodeStream(imageStream);
        return decodedImage;
    }
}
