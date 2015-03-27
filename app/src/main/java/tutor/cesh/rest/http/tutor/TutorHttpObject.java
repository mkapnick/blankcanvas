package tutor.cesh.rest.http.tutor;

import android.graphics.BitmapFactory;
import android.util.Log;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.json.JSONObject;

import java.io.File;
import java.net.URI;

import tutor.cesh.profile.persistant.Tutor;
import tutor.cesh.profile.persistant.User;
import tutor.cesh.rest.http.HttpObject;

/**
 * Created by michaelk18 on 7/7/14.
 */
public class TutorHttpObject implements HttpObject
{
    private String  putEndPoint         = "http://blankcanvas.pw/bc/tutors/";
    private String  getEndPoint         = "http://blankcanvas.pw/bc/tutors/";
    private String  coverImageEndPoint  = "http://blankcanvas.pw/bc/images/tutors/";

    private User    user;

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
        StringEntity    stringEntity;
        JSONObject      params;
        Tutor           tutor;

        tutor           = user.getTutor();
        put             = new HttpPut(putEndPoint + tutor.getId());
        params          = new JSONObject();

        params.put("tutorAbout", tutor.getAbout());
        params.put("tutorRate", tutor.getRate());
        params.put("isPublic", tutor.isPublic() == true ? "yes" : "no");

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
    public HttpPost postTutorCoverImage(String coverImagePath)
    {
        HttpPost        post;
        MultipartEntity entity;
        File            coverImageFile;
        FileBody        cBody;
        Tutor           tutor;

        tutor = user.getTutor();

        Log.d("TEST", coverImageEndPoint + tutor.getId() + "/cover");

        post    = new HttpPost(coverImageEndPoint + tutor.getId() + "/cover");
        entity  = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

        if(coverImagePath != null)
        {
            coverImageFile  = new File(coverImagePath);
            cBody           = new FileBody(coverImageFile, "image/jpg");
            entity.addPart("tutorCoverImage", cBody);
            tutor.setCoverImageUrl(coverImagePath);
            tutor.setCoverImage(BitmapFactory.decodeFile(coverImagePath));
        }

        post.setEntity(entity);

        return post;
    }

    /**
     *
     * @return
     */
    public HttpPut putTutorProfileImage(String profileImagePath)
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

}
