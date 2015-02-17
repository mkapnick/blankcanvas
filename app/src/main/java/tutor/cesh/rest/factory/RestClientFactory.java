package tutor.cesh.rest.factory;

import android.util.Base64;
import android.util.Log;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.security.NoSuchAlgorithmException;

import tutor.cesh.auth.User;


/**
 * Created by michaelk18 on 2/2/14.
 */
public class RestClientFactory
{
    private static final String     POST_NEW_STUDENT       = "http://blankcanvas.pw/students/";
    private static final String     DOMAIN              = "http://blankcanvas.pw";
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

    /*public static HttpGet authenticate(String email, String password) throws JSONException,
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

        private static String getB6Auth(String email, String password)
        {
            String source=email+":"+password;
            String ret="Basic "+ Base64.encodeToString(source.getBytes(),Base64.URL_SAFE | Base64.NO_WRAP);
            return ret;
        }
    }*/

    /**
     * Create a new user with the given email and password
     *
     * @param email The user's email
     * @param password The user's password
     * @throws JSONException
     * @throws MalformedURLException
     * @throws UnsupportedEncodingException
     */
    public static HttpPost postNewUser(String email, String password) throws   JSONException,
                                                                        IOException,
                                                                        NoSuchAlgorithmException,
                                                                        Exception
    {
        JSONObject              params;
        HttpPost                httpPost;
        StringEntity            entity;
        //byte []                 encryptedEmail, encryptedPassword;
        String                  encryptedEmail, encryptedPassword;
        httpPost            = new HttpPost(DOMAIN + "/users/new");
        params              = new JSONObject();
        encryptedEmail      = email;//User.encrypt(email);
        encryptedPassword   = password;//User.encrypt(password);

        params.put("email", email);//Base64.encode(encryptedEmail, 0));
        params.put("password", password);

        Log.d("encrypted email base 64 toString is: ", encryptedEmail);
        Log.d("encrypted pswd base 64 toString is: ", encryptedPassword);

        entity          = new StringEntity(params.toString());

        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setEntity(entity);

        return httpPost;
    }

    /**
     *
     * @param email
     * @param password
     * @return
     * @throws JSONException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static HttpPost authenticateViaPost(String email, String password) throws JSONException,
                                                                            IOException,
                                                                            NoSuchAlgorithmException
    {
        //byte []                 encryptedEmail, encryptedPassword;
        String                  encryptedEmail, encryptedPassword;
        JSONObject              params;
        HttpPost                httpPost;
        StringEntity            entity;

        encryptedEmail      = null;
        encryptedPassword   = null;

        try{
            encryptedEmail      = email; //User.encrypt(email);
            encryptedPassword   = password; //User.encrypt(password);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        httpPost    = new HttpPost(DOMAIN + "/auth");
        params      = new JSONObject();

        params.put("email", encryptedEmail);
        params.put("password", encryptedPassword);

        entity      = new StringEntity(params.toString());

        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setEntity(entity);

        return httpPost;

    }
}
