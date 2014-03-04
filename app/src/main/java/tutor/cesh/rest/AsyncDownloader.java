package tutor.cesh.rest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A simple class to download images from the internet
 */
public class AsyncDownloader extends AsyncTask<Void, Integer, Bitmap>
{

    private ProgressBar         pb;
    private String              url;
    private Context             c;
    private Bitmap              bmp;

    /**
     * Create an AsyncDownloader instance from a valid
     * URL, progress bar, and context
     *
     * @param url A valid URL that points to an image on the web
     * @param pb  A progress bar
     * @param c   A context
     */
    public AsyncDownloader( String url, ProgressBar pb,
                            Context c)
    {
        this.url        = url;
        this.pb         = pb;
        this.c          = c;
    }


    @Override
    protected Bitmap doInBackground(Void... arg0)
    {
        System.out.println("inside do in background");
        bmp = downloadBitmapFromURL(url);
        return bmp;
    }

    /**
     * Downloads an image from a String URL
     *
     * @param link The url where the image can be found
     *
     * @return a downloaded image in the form of a BitMap object
     */
    private Bitmap downloadBitmapFromURL(String link)
    {

        URL                 url;
        HttpURLConnection   connection;
        InputStream         input;

        try
        {
            System.out.println("start in try");
            url         = new URL(link);
            connection  = (HttpURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            input       = connection.getInputStream();
            bmp         = BitmapFactory.decodeStream(input);
            System.out.println("end in try");

        }
        catch (IOException e)
        {
            System.out.println("IOException in AsyncDownloader!");
            e.printStackTrace();
            e.printStackTrace();
        }

        return bmp;
    }
}