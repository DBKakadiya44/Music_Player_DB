package com.db.musicplayerdb.ForAll;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ThubnailGet
{
    public static Bitmap drawableToBitmap(Context context,int drawableResourceId) {
        try {
            return BitmapFactory.decodeResource(context.getResources(), drawableResourceId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap getAudioThumbnail(String audioFilePath) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();

        try {
            retriever.setDataSource(audioFilePath);

            byte[] artwork = retriever.getEmbeddedPicture();

            if (artwork != null) {
                // If the audio file contains an embedded image (thumbnail), convert it to a Bitmap
                return BitmapFactory.decodeByteArray(artwork, 0, artwork.length);
            } else {
                // If no embedded image is available, you can use a default image or handle it accordingly
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions, e.g., file not found, unsupported format, etc.
            return null;
        } finally {
            try {
                retriever.release();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static String formatDuration(long durationMillis) {

        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss", Locale.getDefault());


        long minutes = TimeUnit.MILLISECONDS.toMinutes(durationMillis) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(durationMillis) % 60;

        Date date = new Date(0);
        date.setMinutes((int) minutes);
        date.setSeconds((int) seconds);

        return sdf.format(date);
    }
}
