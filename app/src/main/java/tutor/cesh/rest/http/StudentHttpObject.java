package tutor.cesh.rest.http;

import android.graphics.BitmapFactory;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONObject;

import java.io.File;
import java.net.URI;

import tutor.cesh.Student;
import tutor.cesh.User;

/**
 * Created by michaelk18 on 7/7/14.
 */
public class StudentHttpObject implements HttpObject {

    private String putEndPoint  = "http://blankcanvas.pw/students/";
    private String getEndPoint  = "http://blankcanvas.pw/students/";

    private String              coverImagePath, profileImagePath;
    private User user;

    public StudentHttpObject(User user)
    {
        this.user = user;
    }
    public String getCoverImagePath() {
        return coverImagePath;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setCoverImagePath(String coverImagePath) {
        this.coverImagePath = coverImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    @Override
    public HttpPost post() throws Exception {

        return null;
    }

    @Override
    public HttpPut put() throws Exception
    {
        HttpPut         put;
        MultipartEntity entity;
        Student         student;
        JSONObject      params;
        StringEntity    stringEntity;

        student = user.getStudent();
        params  = new JSONObject();
        put     = new HttpPut(putEndPoint + student.getId());

        params.put("firstName", student.getName());
        params.put("studentAbout", student.getAbout());

        stringEntity = new StringEntity(params.toString());
        //entity.addPart("firstName", new StringBody(student.getName()));
        //entity.addPart("studentAbout", new StringBody(student.getAbout()));

        put.setHeader("Accept", "application/json");
        put.setHeader("Content-Type", "application/json");
        put.setEntity(stringEntity);

        return put;
    }

    /**
     *
     * @return
     */
    public HttpPut putStudentCoverImage()
    {
        HttpPut         put;
        MultipartEntity entity;
        File            coverImageFile;
        FileBody        cBody;
        Student         student;

        student = user.getStudent();

        put             = new HttpPut(putEndPoint + student.getId());
        entity          = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

        if(coverImagePath != null)
        {
            coverImageFile  = new File(coverImagePath);
            cBody           = new FileBody(coverImageFile, "image/jpg");
            entity.addPart("studentCoverImage", cBody);
            student.setCoverImageUrl(coverImagePath);
            student.setCoverImage(BitmapFactory.decodeFile(coverImagePath));
        }

        put.setEntity(entity);

        return put;
    }

    /**
     *
     * @return
     */
    public HttpPut putStudentProfileImage()
    {
        HttpPut         put;
        MultipartEntity entity;
        File            profileImageFile;
        FileBody        pBody;
        Student         student;

        student = user.getStudent();

        put             = new HttpPut(putEndPoint + student.getId());
        entity          = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

        if(profileImagePath != null)
        {
            profileImageFile = new File(profileImagePath);
            pBody            = new FileBody(profileImageFile, "image/jpg");
            entity.addPart("studentProfileImage", pBody);
            student.setCoverImageUrl(profileImagePath);
            student.setCoverImage(BitmapFactory.decodeFile(profileImagePath));
        }

        put.setEntity(entity);

        return put;
    }

    @Override
    public HttpGet get() throws Exception
    {

        HttpGet httpGet;
        Student student;

        student = user.getStudent();

        httpGet = new HttpGet(new URI(getEndPoint + student.getId()));
        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Content-Type", "application/json");

        return httpGet;
    }
}
