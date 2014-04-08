package tutor.cesh.rest;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by michaelk18 on 2/2/14.
 */
public class RestClientExecute implements Runnable
{
    HttpPost                        httpPost;
    HttpPut                         httpPut;
    HttpClient                      httpClient;
    Thread                          thread;


    public RestClientExecute(HttpPost post)
    {
        this.httpPost   = post;
        httpClient      = new DefaultHttpClient();
    }

    public RestClientExecute(HttpPut put)
    {
        this.httpPut    = put;
        httpClient      = new DefaultHttpClient();
    }

    @Override
    public void run()
    {
        try
        {

            if (this.httpPost != null)
                httpClient.execute(httpPost);
            else
                httpClient.execute(httpPut);
       }

        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        catch (ClientProtocolException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void start()
    {
        thread      = new Thread(this);
        thread.setPriority(0x0000000a); //set this thread to a lower priority than the main UI thread
        thread.start();
    }

}
