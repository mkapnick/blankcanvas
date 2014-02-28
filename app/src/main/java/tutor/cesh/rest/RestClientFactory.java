package tutor.cesh.rest;

import android.util.Base64;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


/**
 * Created by michaelk18 on 2/2/14.
 */
public class RestClientFactory
{
    private static final String     POST_NEW_USER       = "http://protected-earth-9689.herokuapp.com/users";
    private static final String     DOMAIN              = "http://protected-earth-9689.herokuapp.com/";
    private static final String     PUT_USER            = "http://protected-earth-9689.herokuapp.com/users/";
    private static final String     PUT_ENROLL_DATA    = "http://protected-earth-9689.herokuapp.com/enrolls/";
    private static       JSONObject userParams;
    private static       JSONObject enrollParams;

    /**
     * Authorize that a user's email and password is
     * stored on the server
     *
     * @param email User's email
     * @param password User's password
     *
     * @return an HttpGet ready to authenticate a user's email and password
     * @throws JSONException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static HttpGet authenticate(String email, String password) throws JSONException,
                                                                    IOException,
                                                                    NoSuchAlgorithmException
    {
        HttpGet      httpGet;
        httpGet     = null;
        try
        {
            System.out.println("inside authenticate user");
            email = email.replaceAll("\\.", "_");
            System.out.println(email);
            httpGet = new HttpGet(new URI(DOMAIN));
            System.out.println("after httpPost");
            httpGet.setHeader("Authorization",  getB6Auth(email, password));
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("Content-Type", "application/json");
        }
        catch(Exception e)
        {
            System.out.println("in Exception !!");
        }
        return httpGet;
    }

    /**
     * Get user info
     *
     * @param id The user's id
     * @return HttpGet for the correct user
     */
    public static HttpGet get(String table, String id)
    {
        HttpGet httpGet;
        httpGet = new HttpGet(DOMAIN + table + "/" + id);
        System.out.println("Getting: " + DOMAIN + table + "/" + id);
        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Content-Type", "application/json");

        return httpGet;

    }


    /**
     *
     * @param email User's email
     * @param password User's password
     * @return
     */
    private static String getB6Auth(String email, String password)
    {
        String source=email+":"+password;
        String ret="Basic "+ Base64.encodeToString(source.getBytes(),Base64.URL_SAFE | Base64.NO_WRAP);
        return ret;
    }

    /**
     *
     * @return
     */
    private static JSONObject getEnrollParams() throws JSONException
    {
        if(enrollParams == null)
        {
            enrollParams = new JSONObject();
            enrollParams.put("major", "major");
            enrollParams.put("year", "year");
        }
        return enrollParams;
    }

    /**
     *
     * @return
     * @throws JSONException
     */
    private static JSONObject getUserParams() throws JSONException
    {
        if (userParams == null)
        {
            userParams = new JSONObject();
            userParams.put("first_name", "Name");
            userParams.put("last_name", "Name");
            userParams.put("about", "About");
            userParams.put("profile_image", "");
            userParams.put("cover_image", "");
        }

        return userParams;
    }


    /**
     * Create a new user with the given email and password
     *
     * @param email The user's email
     * @param password The user's password
     * @throws JSONException
     * @throws MalformedURLException
     * @throws UnsupportedEncodingException
     */
    public static HttpPost post(String email, String password) throws   JSONException,
                                                                        IOException,
                                                                        NoSuchAlgorithmException

    {
        System.out.println("Inside post in RestClientFactory");
        System.out.println("posting new account)");

        JSONObject              params;
        HttpPost                httpPost;
        StringEntity            entity;
        httpPost    = new HttpPost(POST_NEW_USER);
        params      = getUserParams();

        params.put("email", email);
        params.put("password", password);

        entity          = new StringEntity(params.toString());

        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setEntity(entity);

        return httpPost;

    }

    /**
     * Update information about a user
     *
     * @param backGroundPath    File path to the user's background image
     * @param profilePath       File path to the user's profile picture
     * @param name              user's name
     * @param major             user's major
     * @param year              year in school
     * @param about             short info about the user
     * @param subjects          Subject's user teaches
     * @return
     * @throws Exception
     */
    public static ArrayList<HttpPut> put( String id, String enrollId,
                                          String backGroundPath, String profilePath,
                                          String name, String major, String year,
                                          String about, String subjects) throws Exception
    {
        System.out.println("updating user data !");

        JSONObject              params1, params2;
        ArrayList<HttpPut>      putsList;
        HttpPut                 puts1, puts2;
        MultipartEntity         entity;
        File                    coverImageFile, profileImageFile;
        FileBody                cBody, pBody;

        params1     = getUserParams();
        putsList    = new ArrayList<HttpPut>();
        puts1       = new HttpPut(PUT_USER + id);
        entity      = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

        entity.addPart("user[first_name]", new StringBody(name));
        entity.addPart("user[last_name]", new StringBody("testing"));
        entity.addPart("user[about]", new StringBody(about));

        // Add Images to the multipart entity
        if(backGroundPath != null)
        {
            coverImageFile  = new File(backGroundPath);
            cBody           = new FileBody(coverImageFile, "image/jpeg");
            entity.addPart("user[cover_image]", cBody);
        }
        else
            entity.addPart("user[cover_image]", new StringBody(params1.getString("cover_image")));

        if(profilePath != null)
        {
            profileImageFile    = new File(profilePath);
            pBody               = new FileBody(profileImageFile, "image/jpeg");
            entity.addPart("user[profile_image]", pBody);
        }
        else
            entity.addPart("user[profile_image]", new StringBody(params1.getString("profile_image")));

        puts1.setEntity(entity);
        putsList.add(puts1);

        System.out.println("puts1 entity complete");

        /** Second put **/
        puts2   = new HttpPut(PUT_ENROLL_DATA + enrollId);
        params2 = getEnrollParams();

        params2.put("major", major);
        params2.put("year", year);

        puts2.setEntity(new StringEntity(params2.toString()));
        puts2.setHeader("Accept", "application/json");
        puts2.setHeader("Content-Type", "application/json");

        putsList.add(puts2);

        System.out.println("Done with posting user data, returning arraylist");

        return putsList;
    }
}
