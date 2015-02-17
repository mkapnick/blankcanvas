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
import tutor.cesh.Tutor;
import tutor.cesh.User;

/**
 * Created by michaelk18 on 7/7/14.
 */
public class TutorHttpObject implements HttpObject
{
    private String  putEndPoint  = "http://blankcanvas.pw/tutors/";
    private String  getEndPoint  = "http://blankcanvas.pw/tutors/";
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
        StringEntity    stringEntity;
        JSONObject      params;

        Tutor           tutor;

        tutor           = user.getTutor();
        put             = new HttpPut(putEndPoint + tutor.getId());
        //entity          = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        params          = new JSONObject();

        //entity.addPart("tutorAbout", new StringBody(tutor.getAbout()));
        //entity.addPart("tutorRate", new StringBody(tutor.getRate()));
        params.put("tutorAbout", tutor.getAbout());
        params.put("tutorRate", tutor.getRate());

        stringEntity    = new StringEntity(params.toString());

        put.setHeader("Accept", "application/json");
        put.setHeader("Content-Type", "application/json");
        put.setEntity(stringEntity);

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

        put             = new HttpPut(putEndPoint + tutor.getId());
        entity          = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

        if(coverImagePath != null)
        {
            coverImageFile  = new File(coverImagePath);
            cBody           = new FileBody(coverImageFile, "image/jpg");
            entity.addPart("tutorCoverImage", cBody);
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

        put             = new HttpPut(putEndPoint + tutor.getId());
        entity          = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

        if(profileImagePath != null)
        {
            profileImageFile = new File(profileImagePath);
            pBody            = new FileBody(profileImageFile, "image/jpg");
            entity.addPart("tutorProfileImage", pBody);
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

        httpGet = new HttpGet(new URI(getEndPoint + user.getTutor().getId()));
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
