package tutor.cesh.rest;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by michaelk18 on 2/2/14.
 */
public class AsyncGet extends AsyncTask<HttpGet, Integer, JSONObject>
{
    HttpGet     httpGet;
    HttpClient  httpClient;

    @Override
    protected JSONObject doInBackground(HttpGet... httpGets)
    {

        HttpResponse    response;
        BufferedReader  reader;
        String          json;
        JSONObject      obj;

        this.httpGet    = httpGets[0];
        this.httpClient = new DefaultHttpClient();
        obj             = null;
        try
        {
            System.out.println("httpGet: " + httpGet);
            response        = httpClient.execute(httpGet);
            System.out.println("after response!");
            reader          = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            json            = reader.readLine();
            System.out.println(json);
            json = json.replaceAll("\\[", "").replaceAll("\\]","");
            System.out.println("json is: " + json);

            obj = new JSONObject(json);

        }
        catch(Exception e)
        {
            System.out.println(" Inside AsyncGet Exception!");
            e.printStackTrace();
        }

        return obj;
    }
}