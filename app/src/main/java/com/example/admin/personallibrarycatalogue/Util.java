/**
 * Created by Mikhail Valuyskiy on 28.05.2015.
 */
package com.example.admin.personallibrarycatalogue;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.example.admin.personallibrarycatalogue.data.Book;
import com.example.admin.personallibrarycatalogue.data.LibraryDatabaseHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Class which helps convert image from byte[] to Bitmap and vice-versa
 */
public class Util {

    /**
     * Converts byte[] to Bitmap
     */
    static public Bitmap getBitmapFromBytes(byte[] image) {
        ByteArrayInputStream imageStream = new ByteArrayInputStream(image);
        Bitmap decodedImage = BitmapFactory.decodeStream(imageStream);
        return decodedImage;
    }

    static public byte[] getBytesFromBitmap(Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte imageInByte[] = stream.toByteArray();
        return imageInByte;
    }

    static public byte[] getBytesFromDrawable(Drawable image){
        Bitmap bitmap = ((BitmapDrawable)image).getBitmap();
        byte imageInByte[] = getBytesFromBitmap(bitmap);
        return  imageInByte;
    }

    /**
     * Cheks that the database is empty or not
     * @return True in case the database contain smth, otherwise false
     */
    static public boolean isDatabaseContainsItems(LibraryDatabaseHelper helper){
        List<Book> booksList = new ArrayList<Book>();
        booksList = helper.getAllBooks();

        if (booksList.size() > 0) {
            return true;
        } else {
            return false;
        }
    }


}
