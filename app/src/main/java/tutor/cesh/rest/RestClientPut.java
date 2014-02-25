package tutor.cesh.rest;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by michaelk18 on 2/24/14.
 */
public class RestClientPut implements Runnable
{
    HttpClient httpClient  = new DefaultHttpClient();
    HttpPut httpPut;



    public RestClientPut(HttpPut put)
    {
        this.httpPut = put;

    }

    @Override
    public void run()
    {
        try
        {
            if(this.httpPut != null)
                httpClient.execute(httpPut);

        }
        catch(Exception e)
        {
            System.out.println("Exception again");
        }
    }

}

