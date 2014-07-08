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
public class CourseHttpObject implements HttpObject
{
    private static final String POST = "http://blankcanvas.pw/courses/";
    private static final String PUT = "blankcanvas.pw/courses/";
    private static final String GET = "http://blankcanvas.pw/join/courses/";
    private static final String DOMAIN  = "http://blankcanvas.pw/";

    private AbstractStudent     student;
    private String              classesFormattedForServer;

    public CourseHttpObject(AbstractStudent student)
    {
        this.student = student;
    }

    public CourseHttpObject(AbstractStudent student, String classes)
    {
        this(student);
        this.classesFormattedForServer = classes;
    }

    @Override
    public HttpPost post() throws Exception
    {
        HttpPost                httpPost;
        JSONObject              params;
        StringEntity            entity;

        httpPost    = new HttpPost(POST);
        params      = new JSONObject();

        params.put("classes", this.classesFormattedForServer);
        params.put("id", student.getId());
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

        httpGet = new HttpGet(new URI(GET + student.getId()));
        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Content-Type", "application/json");

        return httpGet;


    }
}
