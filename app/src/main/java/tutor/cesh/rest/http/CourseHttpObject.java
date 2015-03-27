package tutor.cesh.rest.http;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import java.net.URI;
import java.util.List;

import tutor.cesh.profile.persistant.Student;
import tutor.cesh.profile.persistant.User;
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
        String          jsonArray;

        httpPost    = new HttpPost(getEndPoint());
        params      = new JSONObject();
        student     = user.getStudent();
        jsonArray   = ClassesUtility.formatClassesBackend(this.courses.iterator());

        params.put("courses", jsonArray);
        params.put("schoolId", student.getSchoolId());

        entity      = new StringEntity(params.toString());

        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json");
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
        String          jsonArray;

        httpPut     = new HttpPut(getEndPoint());
        params      = new JSONObject();
        student     = user.getStudent();
        jsonArray   = ClassesUtility.formatClassesBackend(this.courses.iterator());

        params.put("courses", jsonArray);
        params.put("schoolId", student.getSchoolId());

        entity      = new StringEntity(params.toString());

        httpPut.setHeader("Accept", "application/json");
        httpPut.setHeader("Content-Type", "application/json");
        httpPut.setEntity(entity);

        return httpPut;
    }

    @Override
    public HttpGet get() throws Exception
    {
        HttpGet httpGet;

        httpGet = new HttpGet(new URI(getEndPoint()));
        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Content-Type", "application/json");

        return httpGet;
    }

    public abstract String getEndPoint();
}
