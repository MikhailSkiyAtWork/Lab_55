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
    public static Bitmap getBitmapFromBytes(byte[] image) {
        Bitmap decodedImage = null;
        if (image != null) {
            ByteArrayInputStream imageStream = new ByteArrayInputStream(image);
            decodedImage = BitmapFactory.decodeStream(imageStream);
        }
        return decodedImage;
    }

    public static byte[] getBytesFromBitmap(Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte imageInByte[] = stream.toByteArray();
        return imageInByte;
    }

    public static byte[] getBytesFromDrawable(Drawable image) {
        Bitmap bitmap = ((BitmapDrawable) image).getBitmap();
        byte imageInByte[] = getBytesFromBitmap(bitmap);
        return imageInByte;
    }
}
