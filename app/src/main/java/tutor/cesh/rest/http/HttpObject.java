package tutor.cesh.rest.http;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;

/**
 * Created by michaelk18 on 7/7/14.
 */
public interface HttpObject
{
    public HttpPost post() throws Exception;
    public HttpPut  put() throws Exception;
    public HttpGet  get() throws Exception;
}
