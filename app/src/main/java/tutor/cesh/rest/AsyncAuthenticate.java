package tutor.cesh.rest;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by michaelk18 on 2/17/14.
 */
public class AsyncAuthenticate extends AsyncTask<HttpGet, Integer, JSONArray>
{
    HttpGet     httpGet;
    HttpClient  httpClient;
    @Override
    protected JSONArray doInBackground(HttpGet ... gets)
    {
        HttpResponse    response;
        BufferedReader  reader;
        String          json;
        JSONArray       jarray;

        httpClient      = new DefaultHttpClient();
        jarray          = null;

        this.httpGet    = gets[0];
        try
        {
            response = httpClient.execute(this.httpGet);
            System.out.println("after response (in AsyncAuthenticate!");


            reader          = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            System.out.println("after reader");
            json            = reader.readLine();

            if (json.length() > 0) //if no JSON response, no such user!
            {
                System.out.println("after builder.append");
                System.out.println(response.getEntity());
                System.out.println(json);

                jarray = new JSONArray(json);
                System.out.println(jarray);
                System.out.println("After JSON array");
            }
        }
        catch(JSONException e)
        {
            System.out.println("Caught exception");
            System.out.println(e.getMessage());
            System.out.println(e.toString());
        }
        catch(Exception e)
        {

        }

        return jarray;
    }


}
