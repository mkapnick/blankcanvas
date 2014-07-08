package tutor.cesh.rest.http;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import java.net.URI;

import tutor.cesh.AbstractStudent;

/**
 * Created by michaelk18 on 7/7/14.
 */
public class EnrollHttpObject implements HttpObject
{
    private static final String PUT     = "http://blankcanvas.pw/enrolls/";
    private static final String GET     = "http://blankcanvas.pw/join/enrolls/";

    private AbstractStudent student;

    public EnrollHttpObject(AbstractStudent student)
    {
        this.student = student;
    }
    @Override
    public HttpPost post() throws Exception {
        return null;
    }

    @Override
    public HttpPut put() throws Exception
    {

        HttpPut         put;
        JSONObject      params;

        put             = new HttpPut(PUT + student.getEnrollId());
        params          = new JSONObject();

        params.put("major", student.getMajor());
        params.put("year", student.getYear());

        put.setEntity(new StringEntity(params.toString()));
        put.setHeader("Accept", "application/json");
        put.setHeader("Content-Type", "application/json");

        return put;

    }

    @Override
    public HttpGet get() throws Exception
    {
        HttpGet httpGet;

        httpGet = new HttpGet(new URI(GET + student.getId()));
        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Content-Type", "application/json");

        return httpGet;
    }
}
