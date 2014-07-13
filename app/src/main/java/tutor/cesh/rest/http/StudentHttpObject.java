package tutor.cesh.rest.http;

import android.graphics.BitmapFactory;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.File;
import java.net.URI;

import tutor.cesh.Student;
import tutor.cesh.User;

/**
 * Created by michaelk18 on 7/7/14.
 */
public class StudentHttpObject implements HttpObject {

    private static final String PUT     = "http://blankcanvas.pw/students/";
    private static final String GET     = "http://blankcanvas.pw/students/";

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
        File            coverImageFile, profileImageFile;
        FileBody        cBody, pBody;
        Student         student;

        student = user.getStudent();

        put             = new HttpPut(PUT + student.getId());
        entity          = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

        entity.addPart("student[first_name]", new StringBody(student.getName()));
        entity.addPart("student[last_name]", new StringBody(" "));
        entity.addPart("student[about]", new StringBody(student.getAbout()));

        if(coverImagePath != null)
        {
            coverImageFile  = new File(coverImagePath);
            cBody           = new FileBody(coverImageFile, "image/jpg");
            entity.addPart("student[cover_image]", cBody);
            student.setCoverImageUrl(coverImagePath);
            student.setCoverImage(BitmapFactory.decodeFile(coverImagePath));
        }

        if(profileImagePath != null)
        {
            profileImageFile = new File(profileImagePath);
            pBody            = new FileBody(profileImageFile, "image/jpg");
            entity.addPart("student[profile_image]", pBody);
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

        httpGet = new HttpGet(new URI(GET + student.getId()));
        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Content-Type", "application/json");

        return httpGet;
    }
}
