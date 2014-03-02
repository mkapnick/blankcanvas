package tutor.cesh.sampled.statik;

import android.graphics.Bitmap;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by michaelk18 on 3/1/14.
 */
public class BitmapFactoryA
{
    public static Bitmap loadBitmap(String link)
    {
        Bitmap bmp;
        URL url;
        HttpURLConnection connection;
        InputStream input;

        bmp = null;
        try
        {
            url         = new URL(link);
            connection  = (HttpURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            input       = connection.getInputStream();
            bmp         = android.graphics.BitmapFactory.decodeStream(input);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            e.printStackTrace();
        }


        return bmp;
    }
}
