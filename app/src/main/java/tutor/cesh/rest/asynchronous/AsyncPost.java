package tutor.cesh.rest.asynchronous;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import tutor.cesh.rest.delegate.TaskDelegate;

/**
 * Created by michaelk18 on 2/2/14.
 */
public class AsyncPost extends AsyncTask<HttpPost, Integer, Object>
{
    private HttpPost                httpPost;
    private HttpClient              httpClient;
    private Context                 context;
    private ProgressDialog          pd;
    private Object                  response;
    private TaskDelegate taskDelegate;


    public AsyncPost(Context c, TaskDelegate td, ProgressDialog pd)
    {
        this.context        = c;
        this.taskDelegate   = td;
        this.pd             = pd;
    }

    @Override
    protected Object doInBackground(HttpPost... httpPosts)
    {

        HttpResponse    response;
        BufferedReader  reader;
        String          json;

        this.httpPost   = httpPosts[0];
        this.httpClient = new DefaultHttpClient();
        try
        {
            response        = httpClient.execute(httpPost);
            reader          = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            json            = reader.readLine();

            if(!json.equalsIgnoreCase("null"))
            {
                try
                {
                    this.response   = new JSONObject(json);
                }
                catch(Exception e)
                {
                    //System.out.println(json);
                    this.response   = new JSONArray(json);
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return this.response;
    }

    @Override
    protected void onPostExecute(Object result)
    {
        //super.onPostExecute(result);
        if(pd!=null)
            this.pd.dismiss();

        if(taskDelegate != null && this.response != null)
            taskDelegate.taskCompletionResult(this.response);


        super.onPostExecute(result);
    };

    @Override
    protected void onPreExecute() {
        //super.onPreExecute();
        if(pd != null)
        {
            pd.show();
        }
        super.onPreExecute();
    }

}