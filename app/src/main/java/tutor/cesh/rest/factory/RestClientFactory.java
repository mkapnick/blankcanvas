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
import tutor.cesh.rest.apisecurity.APIEndpoints;


/**
 * Created by michaelk18 on 2/2/14.
 */
public class RestClientFactory
{
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
        String                  encryptedEmail, encryptedPassword;

        httpPost            = new HttpPost(APIEndpoints.getUSER_NEW_POST_ENDPOINT());
        params              = new JSONObject();
        encryptedEmail      = User.encrypt(email);
        encryptedPassword   = User.encrypt(password);

        params.put("email", encryptedEmail);//Base64.encode(encryptedEmail, 0));
        params.put("password", encryptedPassword);

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
        String                  encryptedEmail, encryptedPassword;
        JSONObject              params;
        HttpPost                httpPost;
        StringEntity            entity;

        encryptedEmail      = null;
        encryptedPassword   = null;

        try
        {
            encryptedEmail      = User.encrypt(email);
            encryptedPassword   = User.encrypt(password);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        httpPost    = new HttpPost(APIEndpoints.getAUTH_POST_ENDPOINT());
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
