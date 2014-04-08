package tutor.cesh.rest;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ProgressBar;

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
    private ProgressDialog      pd;
    private TaskDelegate        taskDelegate;


    /**
     * Create an AsyncDownloader instance from a valid
     * URL, progress bar, and context
     *
     * @param url A valid URL that points to an image on the web
     * @param c   A context
     */
    public AsyncDownloader( String url,
                            Context c, TaskDelegate td)
    {
        this.url            = url;
        this.c              = c;
        pd                  = new ProgressDialog(c);
        this.taskDelegate   = td;

    }


    @Override
    protected Bitmap doInBackground(Void... arg0)
    {
        this.bmp = downloadBitmapFromURL(url);
        return this.bmp;
    }

    @Override
    protected void onPostExecute(Bitmap result)
    {
        if(pd!=null)
        {
            pd.dismiss();
            this.taskDelegate.taskCompletionResult(this.bmp);
        }
        super.onPostExecute(result);
    };

    @Override
    protected void onPreExecute()
    {
        pd.setTitle("Downloading...");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();
        super.onPreExecute();
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
            url         = new URL(link);
            connection  = (HttpURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            input       = connection.getInputStream();
            bmp         = BitmapFactory.decodeStream(input);

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return bmp;
    }
}