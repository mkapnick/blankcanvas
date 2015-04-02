package tutor.cesh.rest.asynchronous;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;

import tutor.cesh.profile.Profile;
import tutor.cesh.list.BitmapCacheBehavior;
import tutor.cesh.rest.handler.ImageHandler;

/**
 * A simple class to download images from the internet
 */
public class AsyncDownloader extends AsyncTask<Void, Integer, Bitmap>
{

    private String                  url;
    private Bitmap                  bmp;
    private ProgressDialog          pd;
    private ImageHandler            handler;
    private Profile                 profile;
    private BitmapCacheBehavior     cacheBehavior;
    private boolean                 resize;
    private String                  identifier;



    /**
     * Create an AsyncDownloader instance from a valid
     * URL, progress bar, and context
     *
     * @param url A valid URL that points to an image on the web
     */
    public AsyncDownloader(String url, ImageHandler handler, Profile p, ProgressDialog pd)
    {
        this.url            = url;
        this.handler        = handler;
        this.profile        = p;
        this.pd             = pd;
        resize              = false;
    }

    public AsyncDownloader(String identifier, String url, BitmapCacheBehavior cacheBehavior)
    {
        this.identifier     = identifier;
        this.url            = url;
        this.cacheBehavior  = cacheBehavior;
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
        if(pd != null)
            pd.dismiss();

        if(handler != null)
            handler.handle(result, profile);

        if(cacheBehavior != null)
            cacheBehavior.cache(this.identifier, result);

        super.onPostExecute(result);
    };

    @Override
    protected void onPreExecute()
    {
        if(pd != null)
        {
            pd.setTitle("Setting up...");
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }
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
        InputStream         input;
        HttpGet             get;
        HttpResponse        response;
        HttpClient          client;
        BufferedHttpEntity  bufHttpEntity;

        try
        {
            client          = new DefaultHttpClient();
            get             = new HttpGet(this.url);
            response        = client.execute(get);
            bufHttpEntity   = new BufferedHttpEntity(response.getEntity());
            input           = bufHttpEntity.getContent();
            this.bmp        = BitmapFactory.decodeStream(input);

            input.close();
        }
        catch (IOException e)
        {
            //nothing yet, TODO fix this
        }

        return this.bmp;
    }
}