package tutor.cesh.rest.http;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import java.net.URI;

import tutor.cesh.apisecurity.APIAuthorization;
import tutor.cesh.profile.Student;
import tutor.cesh.profile.User;
import tutor.cesh.apisecurity.APIEndpoints;

/**
 * Created by michaelk18 on 7/7/14.
 */
public class EnrollHttpObject implements HttpObject
{
    private User user;

    public EnrollHttpObject(User user)
    {
        this.user = user;
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
        Student         student;
        String          path, method, jwt;

        student = user.getStudent();

        path            = APIEndpoints.getENROLLS_ENDPOINT() + "/" + student.getEnrollId();
        method          = "PUT";
        put             = new HttpPut(path);
        params          = new JSONObject();

        params.put("major", student.getMajor());
        params.put("year", student.getYear());
        params.put("minor", student.getMinor());

        put.setEntity(new StringEntity(params.toString()));

        jwt             = APIAuthorization.getAuthorizationHeader(params, path, method);

        put.setHeader("Accept", "application/json");
        put.setHeader("Content-Type", "application/json");
        put.setHeader("Authorization", jwt);

        return put;
    }

    @Override
    public HttpGet get() throws Exception
    {
        HttpGet httpGet;
        Student student;
        String  path, method, jwt;

        student = user.getStudent();
        path    = APIEndpoints.getENROLLS_ENDPOINT() + "/" + student.getEnrollId();
        method  = "GET";

        jwt     = APIAuthorization.getAuthorizationHeader(null, path, method);

        //get student enroll data
        httpGet = new HttpGet(new URI(APIEndpoints.getENROLLS_ENDPOINT() + "/" + student.getEnrollId()));

        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Content-Type", "application/json");
        httpGet.setHeader("Authorization", jwt);

        return httpGet;
    }
}
