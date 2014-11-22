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

    private String  putEndPoint  = "http://blankcanvas.pw/students/";
    private String  getEndPoint  = "http://blankcanvas.pw/students/";

    private User    user;
    private String  coverImagePath, profileImagePath;


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
        Student         student;
        Tutor           tutor;

        student         = user.getStudent();
        tutor           = user.getTutor();
        put             = new HttpPut(putEndPoint + student.getId() + "/view/tutors");
        entity          = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

        entity.addPart("tutor[about]", new StringBody(tutor.getAbout()));
        entity.addPart("tutor[rate]", new StringBody(tutor.getRate()));

        put.setEntity(entity);

        return put;
    }

    /**
     *
     * @return
     */
    public HttpPut putTutorCoverImage()
    {
        HttpPut         put;
        MultipartEntity entity;
        File            coverImageFile;
        FileBody        cBody;
        Tutor           tutor;

        tutor = user.getTutor();

        put             = new HttpPut(putEndPoint + user.getStudent().getId() + "/view/tutors");
        entity          = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

        if(coverImagePath != null)
        {
            coverImageFile  = new File(coverImagePath);
            cBody           = new FileBody(coverImageFile, "image/jpg");
            entity.addPart("tutor[cover_image]", cBody);
            tutor.setCoverImageUrl(coverImagePath);
            tutor.setCoverImage(BitmapFactory.decodeFile(coverImagePath));
        }
        put.setEntity(entity);

        return put;
    }

    /**
     *
     * @return
     */
    public HttpPut putTutorProfileImage()
    {
        HttpPut         put;
        MultipartEntity entity;
        File            profileImageFile;
        FileBody        pBody;
        Tutor           tutor;

        tutor = user.getTutor();

        put             = new HttpPut(putEndPoint + user.getStudent().getId() + "/view/tutors");
        entity          = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

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


        httpGet = new HttpGet(new URI(getEndPoint + student.getId() + "/view/tutors"));
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
