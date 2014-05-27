package tutor.cesh.rest;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ProgressBar;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

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
    private int                 width, height;


    /**
     * Create an AsyncDownloader instance from a valid
     * URL, progress bar, and context
     *
     * @param url A valid URL that points to an image on the web
     * @param c   A context
     */
    public AsyncDownloader( String url,
                            Context c, TaskDelegate td, int widthBounds, int heightBounds)
    {
        this.url            = url;
        this.c              = c;
        pd                  = new ProgressDialog(c);
        this.taskDelegate   = td;
        this.width          = widthBounds;
        this.height         = heightBounds;

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
            this.taskDelegate.taskCompletionResult(this.bmp, false);
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
        BitmapFactory.Options       options;

        HttpGet get;
        HttpResponse response;
        HttpClient client;
        BufferedHttpEntity bufHttpEntity;

        try
        {
            client  = new DefaultHttpClient();
            get     = new HttpGet(this.url);
            response= client.execute(get);
            bufHttpEntity = new BufferedHttpEntity(response.getEntity());
            input   = bufHttpEntity.getContent();

            //options = new BitmapFactory.Options();
            //options.inSampleSize = 20;
            bmp = BitmapFactory.decodeStream(input);

            input.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return bmp;
    }
}