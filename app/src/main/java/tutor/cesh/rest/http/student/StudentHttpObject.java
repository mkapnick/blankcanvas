package tutor.cesh.rest.http.student;

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

import tutor.cesh.apisecurity.APIAuthorization;
import tutor.cesh.profile.Student;
import tutor.cesh.profile.User;
import tutor.cesh.apisecurity.APIEndpoints;
import tutor.cesh.rest.http.HttpObject;

/**
 * Created by michaelk18 on 7/7/14.
 */
public class StudentHttpObject implements HttpObject {

    private User   user;

    public StudentHttpObject(User user)
    {
        this.user = user;
    }


    @Override
    public HttpPost post() throws Exception {

        return null;
    }

    @Override
    public HttpPut put() throws Exception
    {
        HttpPut         put;
        Student         student;
        JSONObject      params;
        StringEntity    stringEntity;
        String          path, method, jwt;

        student = user.getStudent();
        params  = new JSONObject();
        path    = APIEndpoints.getSTUDENTS_ENDPOINT() + "/" + student.getId();
        method  = "PUT";
        put     = new HttpPut(path);

        params.put("firstName", student.getName());
        params.put("studentAbout", student.getAbout());

        stringEntity = new StringEntity(params.toString());

        jwt          = APIAuthorization.getAuthorizationHeader(params, path, method);

        put.setHeader("Accept", "application/json");
        put.setHeader("Content-Type", "application/json");
        put.setHeader("Authorization", jwt);
        put.setEntity(stringEntity);

        return put;
    }

    /**
     *
     * @return
     */
    public HttpPost postStudentCoverImage(String coverImagePath)
    {
        HttpPost        post;
        MultipartEntity entity;
        File            coverImageFile;
        FileBody        cBody;
        Student         student;
        String          path, method, jwt;

        student = user.getStudent();
        path    = APIEndpoints.getSTUDENTS_IMAGE_ENDPOINT() + "/" + student.getId() + "/" + "cover";
        method  = "POST";

        Log.d("TEST", path);
        post    = new HttpPost(path);
        entity  = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

        if(coverImagePath != null)
        {
            coverImageFile  = new File(coverImagePath);
            cBody           = new FileBody(coverImageFile, "image/jpg");
            entity.addPart("studentCoverImage", cBody);
            student.setCoverImageUrl(coverImagePath);
            student.setCoverImage(BitmapFactory.decodeFile(coverImagePath));
        }

        jwt     = APIAuthorization.getAuthorizationHeader(null, path, method);

        post.setHeader("Authorization", jwt);
        post.setEntity(entity);

        return post;
    }

    /**
     *
     * @return
     */
    public HttpPut putStudentProfileImage(String profileImagePath)
    {
        HttpPut         put;
        MultipartEntity entity;
        File            profileImageFile;
        FileBody        pBody;
        Student         student;

        student = user.getStudent();

        put             = new HttpPut(APIEndpoints.getSTUDENTS_ENDPOINT() + "/" + student.getId());
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
        String  path, method, jwt;

        student = user.getStudent();
        path    = APIEndpoints.getSTUDENTS_ENDPOINT() + "/" + student.getId();
        method  = "GET";
        jwt     = APIAuthorization.getAuthorizationHeader(null, path, method);

        httpGet = new HttpGet(new URI(path));
        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Content-Type", "application/json");
        httpGet.setHeader("Authorization", jwt);

        return httpGet;
    }
}
