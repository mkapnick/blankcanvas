package tutor.cesh.rest;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by michaelk18 on 2/2/14.
 */
public class RestClientPost implements Runnable
{
    HttpPost    httpPost;
    HttpClient  httpClient  = new DefaultHttpClient();

    HttpPut     httpPut;

    public RestClientPost(HttpPost post)
    {
        this.httpPost   = post;
    }

    @Override
    public void run()
    {
        try
        {
            if(this.httpPost != null)
                httpClient.execute(httpPost);

        }
        catch(Exception e)
        {
            System.out.println("Exception again");
        }
    }

}
