package tutor.cesh.rest.http;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import java.net.URI;
import java.util.List;

import tutor.cesh.apisecurity.APIAuthorization;
import tutor.cesh.profile.Student;
import tutor.cesh.profile.User;
import tutor.cesh.profile.util.classes.ClassesUtility;

/**
 * Created by michaelk18 on 7/7/14.
 */
public abstract class CourseHttpObject implements HttpObject
{
    protected User              user;
    protected List<String>      courses;

    public CourseHttpObject(User user, List<String> courses)
    {
        this.user       = user;
        this.courses    = courses;
    }

    @Override
    public HttpPost post() throws Exception
    {
        HttpPost        httpPost;
        JSONObject      params;
        StringEntity    entity;
        Student         student;
        String          jsonArray, path, method, jwt;

        httpPost    = new HttpPost(getEndPoint());
        params      = new JSONObject();
        student     = user.getStudent();
        jsonArray   = ClassesUtility.formatClassesBackend(this.courses.iterator());

        path    = getEndPoint();
        method  = "POST";

        params.put("courses", jsonArray);
        params.put("schoolId", student.getSchoolId());

        entity      = new StringEntity(params.toString());

        jwt         = APIAuthorization.getAuthorizationHeader(params, path, method);

        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("Authorization", jwt);
        httpPost.setEntity(entity);

        return httpPost;
    }

    @Override
    public HttpPut put() throws Exception
    {
        HttpPut         httpPut;
        JSONObject      params;
        StringEntity    entity;
        Student         student;
        String          jsonArray, path, method, jwt;

        httpPut     = new HttpPut(getEndPoint());
        params      = new JSONObject();
        student     = user.getStudent();
        jsonArray   = ClassesUtility.formatClassesBackend(this.courses.iterator());

        path        = getEndPoint();
        method      = "PUT";

        params.put("courses", jsonArray);
        params.put("schoolId", student.getSchoolId());

        entity      = new StringEntity(params.toString());

        jwt         = APIAuthorization.getAuthorizationHeader(params, path, method);

        httpPut.setHeader("Accept", "application/json");
        httpPut.setHeader("Content-Type", "application/json");
        httpPut.setHeader("Authorization", jwt);
        httpPut.setEntity(entity);

        return httpPut;
    }

    @Override
    public HttpGet get() throws Exception
    {
        HttpGet httpGet;
        String  path, method, jwt;

        path    = getEndPoint();
        method  = "GET";
        jwt     = APIAuthorization.getAuthorizationHeader(null, path, method);

        httpGet = new HttpGet(new URI(path));
        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Content-Type", "application/json");
        httpGet.setHeader("Authorization", jwt);

        return httpGet;
    }

    public abstract String getEndPoint();
}
