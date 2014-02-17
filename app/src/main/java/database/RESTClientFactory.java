package database;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.Buffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

import tutor.cesh.RESTClient;


/**
 * Created by michaelk18 on 2/2/14.
 */
public class RESTClientFactory
{
    private static final String     POST_NEW_USER       = "http://protected-earth-9689.herokuapp.com/users";
    private static final String     GET_EXISTING_USER   = "http://protected-earth-9689.herokuapp.com/users/info/";
    //private static SecretKeySpec    secretKey;
    //private static final String     KEY                 = "6a8875d3e484c1f82862ecd73a691c4b";

    /**
     *
     * @param email
     * @param password
     * @throws JSONException
     * @throws MalformedURLException
     * @throws UnsupportedEncodingException
     */
    public static HttpPost post(String email, String password)
            throws JSONException,
            IOException, NoSuchAlgorithmException

    {
        System.out.println("Inside post in RESTClientFactory");

        //RESTClientFactory.initializeSecretKey();

        JSONObject      params;
        HttpPost        httpPost;
        StringEntity    entity;

        params      = new JSONObject();
        httpPost    = new HttpPost(POST_NEW_USER);

        try
        {
            //params.put("email", RESTClientFactory.getEncryptedEmail(email));

        }
        catch(Exception e)
        {
            System.out.println("Having trouble encrypting! In exception");
        }

        params.put("email", email);
        params.put("password", password);
        params.put("first_name", "");
        params.put("last_name", "");
        params.put("about", "");
        params.put("profile_pic", JSONObject.NULL);
        params.put("cover_pic", JSONObject.NULL);
        params.put("confirm", "false");

        Log.i("postparams : ", params.toString());

        entity          = new StringEntity(params.toString());

        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setEntity(entity);

        return httpPost;

    }

    public static HttpGet get(String email, String password)
    {

        HttpClient      httpClient;
        HttpGet         request = null;
        BufferedReader  reader;
        String          encodedEmail;
        StringBuilder   builder;
        String          line;
        JSONTokener     tokener;
        JSONArray       finalResult = null;

        try
        {
            encodedEmail    = email.replace(".", "_");
            System.out.println("encoded email: " + encodedEmail);
            builder         = new StringBuilder();

            httpClient      = new DefaultHttpClient();
            request         = new HttpGet();
            request.setURI(new URI(GET_EXISTING_USER + encodedEmail));

            request.setHeader("Accept", "application/json");
            request.setHeader("Content-Type", "application/json");


        }

         catch (Exception e)
        {
            System.out.println("in URIE");
        }

        return request;
    }


    /*
    private static void initializeSecretKey() throws NoSuchAlgorithmException
    {
        if (secretKey == null)
        {
            RESTClientFactory.secretKey = new SecretKeySpec(KEY.getBytes(), "AES");
        }
    }

    private static String getEncryptedEmail(String email)
            throws NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            BadPaddingException,
            IllegalBlockSizeException, UnsupportedEncodingException
    {
        Cipher          emailCipher;
        byte []         encryptedEmail;

        emailCipher             = Cipher.getInstance("AES");
        emailCipher.init(Cipher.ENCRYPT_MODE, secretKey);
        encryptedEmail          = emailCipher.doFinal(email.getBytes("UTF-8"));

        String s = "";

        for(int i =0; i < encryptedEmail.length; i ++)
        {
            s += encryptedEmail[i] + "";
        }
        return s;

    }*/

}
