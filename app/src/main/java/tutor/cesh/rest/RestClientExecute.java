package tutor.cesh.rest;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
            System.out.println("2");
        }
        catch (ClientProtocolException e)
        {
            e.printStackTrace();
            System.out.println("3");
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("4");
        }
    }

    public void start()
    {
        thread      = new Thread(this);
        thread.setPriority(0x0000000a); //set this thread to a lower priority than the main UI thread
        thread.start();
    }

}
