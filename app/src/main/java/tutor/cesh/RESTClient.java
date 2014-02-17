package tutor.cesh;

import android.os.AsyncTask;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.net.URL;

/**
 * Created by michaelk18 on 2/2/14.
 */
public class RESTClient implements Runnable
{
    HttpPost    httpPost;
    HttpClient  httpClient;

    public RESTClient(HttpPost post)
    {
        this.httpPost   = post;
        this.httpClient = new DefaultHttpClient();
    }

    @Override
    public void run()
    {
        try
        {
            httpClient.execute(httpPost);
        }
        catch(Exception e)
        {
            System.out.println("Exception again");
        }
    }

}
