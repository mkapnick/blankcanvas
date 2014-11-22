package tutor.cesh.image;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;

/**
 * Created by michaelk18 on 11/19/14.
 */
public class BitmapHandlerFactory {

    /**
     *
     * @param inContext
     * @param inImage
     * @return
     */
    public static Uri getImageUri(Context inContext, Bitmap inImage)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    /**
     *
     * @param context
     * @param photoUri
     * @return
     */
    public static int getOrientation(Context context, Uri photoUri)
    {
        Cursor cursor;
        cursor = context.getContentResolver().query(photoUri,
                new String[] { MediaStore.Images.ImageColumns.ORIENTATION }, null, null, null);
        if (cursor.getCount() != 1)
            return -1;
        cursor.moveToFirst();
        return cursor.getInt(0);
    }


    public static String getRealPathFromURI(Uri uri, Activity activity)
    {
        Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

}
