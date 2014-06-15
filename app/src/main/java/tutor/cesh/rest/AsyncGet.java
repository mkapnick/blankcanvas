package tutor.cesh.rest;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by michaelk18 on 2/2/14.
 */
public class AsyncGet extends AsyncTask<HttpGet, Integer, JSONObject>
{
    private HttpGet                 httpGet;
    private HttpClient              httpClient;
    private Context                 context;
    private ProgressDialog          pd;
    private JSONObject              response;
    private TaskDelegate            taskDelegate;


    public AsyncGet(Context c, TaskDelegate td)
    {
        this.context        = c;
        this.taskDelegate   = td;
        this.pd             = new ProgressDialog(c);
        if(this.taskDelegate != null)
            this.taskDelegate.setProgressDialog(this.pd);

    }

    @Override
    protected JSONObject doInBackground(HttpGet... httpGets)
    {

        HttpResponse    response;
        BufferedReader  reader;
        String          json;

        this.httpGet    = httpGets[0];
        this.httpClient = new DefaultHttpClient();
        try
        {
            response        = httpClient.execute(httpGet);
            reader          = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            json            = reader.readLine();

            System.out.println("iNSIDE ASYNC GET, APPARENTLY THIS IS WHERE THINGS ARE BLOWING UP -------");
            System.out.println(json);
            this.response   = new JSONObject(json);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return this.response;
    }

    @Override
    protected void onPostExecute(JSONObject result)
    {
        //super.onPostExecute(result);
        if(pd!=null)
        {
            if(taskDelegate != null) {
                this.pd.dismiss();
                taskDelegate.taskCompletionResult(this.response);
            }

        }

        super.onPostExecute(result);
    };

    @Override
    protected void onPreExecute() {
        //super.onPreExecute();
        pd.setTitle("Processing...");
        pd.setMessage("Please wait");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();
        super.onPreExecute();
    }

}