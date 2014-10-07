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
    protected User user;
    protected String classesFormattedForServer;

    public CourseHttpObject(User user)
    {
        this.user = user;
    }

    public CourseHttpObject(User user, String classes)
    {
        this.user = user;
        this.classesFormattedForServer = classes;
    }

    @Override
    public HttpPost post() throws Exception
    {
        HttpPost                httpPost;
        JSONObject params;
        StringEntity entity;
        Student student;

        httpPost    = new HttpPost(getPostEndpoint());
        params      = new JSONObject();
        student     = user.getStudent();

        params.put("classes", classesFormattedForServer);
        params.put("id", student.getId());
        params.put("school_id", student.getSchoolId());

        entity      = new StringEntity(params.toString());

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

        httpGet = new HttpGet(new URI(getGetEndPoint()));
        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Content-Type", "application/json");

        return httpGet;
    }

    /**
     *
     * @return
     */
    public abstract String getGetEndPoint();

    /**
     *
     * @return
     */
    public abstract String getPostEndpoint();

    /**
     *
     * @return
     */
    public abstract String getPutEndpoint();

}
