package tutor.cesh.rest;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by michaelk18 on 2/2/14.
 */
public class AsyncGet extends AsyncTask<HttpGet, Integer, JSONArray>
{
    HttpGet     httpGet;
    HttpClient  httpClient;

    @Override
    protected JSONArray doInBackground(HttpGet... httpGets)
    {

        HttpResponse    response;
        BufferedReader  reader;
        StringBuilder   builder;
        JSONTokener     tokener;
        JSONArray       finalResult;

        this.httpGet    = httpGets[0];
        this.httpClient = new DefaultHttpClient();
        finalResult     = null;
        builder         = null;

        try
        {
            System.out.println("httpGet: " + httpGet);
            response        = httpClient.execute(httpGet);
            System.out.println("after response!");
            reader          = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            builder         = new StringBuilder();
            builder.append(reader.readLine());


            tokener         = new JSONTokener(builder.toString());
            finalResult     = new JSONArray(tokener);
            System.out.println("After JSON array");

        }
        catch(Exception e)
        {
            System.out.println("Exception again");
        }

        return finalResult;
    }
}
