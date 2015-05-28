package com.example.admin.personallibrarycatalogue;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;

/**
 * Created by Mikhail Valuyskiy on 28.05.2015.
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
}
