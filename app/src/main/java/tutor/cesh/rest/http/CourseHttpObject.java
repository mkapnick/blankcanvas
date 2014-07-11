package tutor.cesh.rest.http;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import java.net.URI;

import tutor.cesh.Student;
import tutor.cesh.User;

/**
 * Created by michaelk18 on 7/7/14.
 */
public abstract class CourseHttpObject implements HttpObject
{
    protected String post;
    protected String get;
    protected String put;

    protected User user;
    protected String              classesFormattedForServer;

    public CourseHttpObject(User user, String post, String put, String get)
    {
        this.user = user;
        this.post = post;
        this.put = put;
        this.get = get;
    }

    public CourseHttpObject(User user, String post, String put, String get, String classes)
    {
        this(user, post, put, get);
        this.classesFormattedForServer = classes;
    }

    public abstract String getId();

    @Override
    public HttpPost post() throws Exception
    {
        HttpPost                httpPost;
        JSONObject params;
        StringEntity entity;
        Student student;


        System.out.println("Post url is: .... " + post);
        httpPost    = new HttpPost(post);
        params      = new JSONObject();
        student     = user.getStudent();

        params.put("classes", classesFormattedForServer);
        params.put("id", getId());
        params.put("school_id", student.getSchoolId());

        entity          = new StringEntity(params.toString());

        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setEntity(entity);

        return httpPost;
    }

    @Override
    public HttpPut put() throws Exception
    {
        return null;

    }

    @Override
    public HttpGet get() throws Exception
    {
        HttpGet httpGet;

        httpGet = new HttpGet(new URI(get + getId()));
        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Content-Type", "application/json");

        return httpGet;
    }
}
