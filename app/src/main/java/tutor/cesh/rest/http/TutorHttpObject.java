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
import tutor.cesh.Tutor;
import tutor.cesh.User;

/**
 * Created by michaelk18 on 7/7/14.
 */
public class TutorHttpObject implements HttpObject {

    private static final String PUT = "http://blankcanvas.pw/tutors/";
    private static final String GET = "http://blankcanvas.pw/tutors/";

    private User user;
    private String coverImagePath, profileImagePath;


    public TutorHttpObject(User user)
    {
        this.user = user;
    }

    @Override
    public HttpPost post() throws Exception
    {
        return null;
    }

    @Override
    public HttpPut put() throws Exception
    {
        HttpPut         put;
        MultipartEntity entity;
        File coverImageFile, profileImageFile;
        FileBody cBody, pBody;
        Tutor           tutor;

        tutor           = user.getTutor();
        put             = new HttpPut(PUT + tutor.getId());
        entity          = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

        entity.addPart("tutor[about]", new StringBody(tutor.getAbout()));

        if(coverImagePath != null)
        {
            coverImageFile  = new File(coverImagePath);
            cBody           = new FileBody(coverImageFile, "image/jpg");
            entity.addPart("tutor[cover_image]", cBody);
            tutor.setCoverImageUrl(coverImagePath);
            tutor.setCoverImage(BitmapFactory.decodeFile(coverImagePath));
        }

        if(profileImagePath != null)
        {
            profileImageFile = new File(profileImagePath);
            pBody            = new FileBody(profileImageFile, "image/jpg");
            entity.addPart("tutor[profile_image]", pBody);
            tutor.setProfileImageUrl(profileImagePath);
            tutor.setProfileImage(BitmapFactory.decodeFile(profileImagePath));
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


        httpGet = new HttpGet(new URI(GET + student.getTutorId()));
        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Content-Type", "application/json");

        return httpGet;

    }

    public String getCoverImagePath() {
        return coverImagePath;
    }

    public void setCoverImagePath(String coverImagePath) {
        this.coverImagePath = coverImagePath;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }
}
