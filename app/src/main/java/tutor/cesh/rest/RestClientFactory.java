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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import tutor.cesh.database.DatabaseTable;


/**
 * Created by michaelk18 on 2/2/14.
 */
public class RestClientFactory
{
    private static final String     POST_NEW_USER       = "http://blankcanvas.pw/users/";
    private static final String     DOMAIN              = "http://blankcanvas.pw/";
    private static final String     PUT_USER            = "http://blankcanvas.pw/users/";
    private static final String     PUT_ENROLL_DATA     = "http://blankcanvas.pw/enrolls/";
    private static final String     PUT_TUTOR_DATA      = "http://blankcanvas.pw/tutors/";


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
            email = email.replaceAll("\\.", "_");
            httpGet = new HttpGet(new URI(DOMAIN +"/auth"));
            httpGet.setHeader("Authorization",  getB6Auth(email, password));
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("Content-Type", "application/json");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return httpGet;
    }

    /**
     * Get info from server
     *
     * @param id The user's id
     * @return HttpGet for the correct user
     */
    public static HttpGet get(DatabaseTable table, String id)
    {
        HttpGet httpGet;
        httpGet = null;

        try {
            switch (table) {
                case USERS:
                    httpGet = new HttpGet(new URI(DOMAIN + "users" + "/" + id));

                    break;
                case TUTORS:
                    httpGet = new HttpGet(new URI(DOMAIN + "tutors" + "/" + id));
                    break;

                case ENROLLS:
                    httpGet = new HttpGet(new URI(DOMAIN + "enrolls" + "/" + id));

                    break;
            }
        }
        catch (URISyntaxException e)
        {
            e.printStackTrace();
        }

        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Content-Type", "application/json");

        return httpGet;

    }

    /**
     * Get info from server
     *
     * @param id The user's id
     * @param attribute A specific attribute from the user
     * @return HttpGet for the correct user
     */
    public static HttpGet get(DatabaseTable table, String id, String attribute)
    {
        HttpGet httpGet;
        httpGet = null;
        switch(table)
        {
            case USERS:
                httpGet = new HttpGet(DOMAIN + "users" + "/" + id + "/" + attribute);
                break;
            case TUTORS:
                httpGet = new HttpGet(DOMAIN + "tutors" + "/" + id + attribute);
                break;

            case ENROLLS:
                httpGet = new HttpGet(DOMAIN + "enrolls" + "/" + id + attribute);

                break;
        }

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
        JSONObject              params;
        HttpPost                httpPost;
        StringEntity            entity;
        httpPost    = new HttpPost(POST_NEW_USER);
        params      = new JSONObject();

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
     * @param name              user's name
     * @param major             user's major
     * @param year              year in school
     * @param about             short info about the user
     * @param classes           Classes user is taking
     * @return
     * @throws Exception
     */
    public static ArrayList<HttpPut> put( String id, String enrollId,
                                          String coverImagePath, String profileImagePath,
                                          String name, String major, String year,
                                          String about, JSONArray classes) throws Exception
    {
        return put (id, enrollId, coverImagePath, profileImagePath, name,
                    major, year, about, classes, null);
    }

    /**
     * Update information about a user
     *
     * @param name              user's name
     * @param major             user's major
     * @param year              year in school
     * @param about             short info about the user
     * @param classes           Classes user is taking
     * @return
     * @throws Exception
     */
    public static ArrayList<HttpPut> put( String id, String enrollId,
                                          String coverImagePath, String profileImagePath,
                                          String name, String major, String year,
                                          String about, JSONArray classes, String rate) throws Exception
    {
        JSONObject              params1, params2;
        ArrayList<HttpPut>      putsList;
        HttpPut                 puts1, puts2, puts3;
        MultipartEntity         entity;
        File                    coverImageFile, profileImageFile;
        FileBody                cBody, pBody;

        putsList    = new ArrayList<HttpPut>();

        /******************************** First put ********************************/
        puts1       = new HttpPut(PUT_USER + id);
        entity      = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

        entity.addPart("user[first_name]", new StringBody(name));
        entity.addPart("user[last_name]", new StringBody(" "));
        entity.addPart("user[about]", new StringBody(about));

        if(classes != null)
            entity.addPart("user[classes]", new StringBody(classes.toString()));

        if(coverImagePath != null)
        {
            coverImageFile  = new File(coverImagePath);
            cBody           = new FileBody(coverImageFile, "image/jpg");
            entity.addPart("user[cover_image]", cBody);
        }

        if(profileImagePath != null)
        {
            profileImageFile = new File(profileImagePath);
            pBody            = new FileBody(profileImageFile, "image/jpg");
            entity.addPart("user[profile_image]", pBody);
        }

        puts1.setEntity(entity);
        /******************************** Second put ********************************/
        puts2   = new HttpPut(PUT_ENROLL_DATA + enrollId);
        params1 = new JSONObject();

        params1.put("major", major);
        params1.put("year", year);

        puts2.setEntity(new StringEntity(params1.toString()));
        puts2.setHeader("Accept", "application/json");
        puts2.setHeader("Content-Type", "application/json");

        putsList.add(puts1);
        putsList.add(puts2);
        /******************************** Third put ********************************/
        if (rate != null)
        {
            puts3       = new HttpPut(PUT_TUTOR_DATA + id);
            params2     = new JSONObject();
            params2.put("rate", rate);

            puts3.setEntity(new StringEntity(params1.toString()));
            puts3.setHeader("Accept", "application/json");
            puts3.setHeader("Content-Type", "application/json");

            putsList.add(puts3);

        }

        return putsList;
    }
}
