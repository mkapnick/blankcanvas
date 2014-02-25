package tutor.cesh.database;

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
    private static final String     GET_EXISTING_USER   = "http://protected-earth-9689.herokuapp.com/users/info/";
    private static final String     DOMAIN              = "http://protected-earth-9689.herokuapp.com/";
    private static final String     PUT_USER            = "http://protected-earth-9689.herokuapp.com/users/";
    private static final String     PUT_ENROLL_DATA    = "http://protected-earth-9689.herokuapp.com/enrolls/";
    private static       JSONObject userParams;
    private static       JSONObject enrollParams;
    /**
     *
     * @param email
     * @param password
     * @return
     * @throws JSONException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static HttpGet authenticateUser(String email, String password)
            throws JSONException,
            IOException, NoSuchAlgorithmException
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
     *
     * @param email
     * @param password
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
            userParams.put("first_name", "");
            userParams.put("last_name", "");
            userParams.put("about", "");
            userParams.put("profile_image", "");
            userParams.put("cover_image", "");
        }

        return userParams;
    }


    /**
     *
     * @param email
     * @param password
     * @throws JSONException
     * @throws MalformedURLException
     * @throws UnsupportedEncodingException
     */
    public static HttpPost postNewAccount(String email, String password)
            throws JSONException,
            IOException, NoSuchAlgorithmException

    {
        System.out.println("Inside post in RestClientFactory");

        //RestClientFactory.initializeSecretKey();

        System.out.println("posting new account)");
        JSONObject              params;
        HttpPost                httpPost;
        StringEntity            entity;
        httpPost    = new HttpPost(POST_NEW_USER);
        params      = getUserParams();


        params.put("email", email);
        params.put("password", password);
        //params.put("s", new FileBody(new File("edwfd", "")));

        entity          = new StringEntity(params.toString());

        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setEntity(entity);

        return httpPost;

    }

    /**
     *
     * @param backGroundPath
     * @param profilePath
     * @param name
     * @param major
     * @param year
     * @param about
     * @param subjects
     * @return
     * @throws Exception
     */
    public static ArrayList<HttpPut> postUserData( String id, String enrollId,
                                                    String backGroundPath, String profilePath,
                                                    String name, String major, String year,
                                                    String about, String subjects)

                            throws Exception
    {

        System.out.println("posting user data !");
        JSONObject              params1, params2;
        ArrayList<HttpPut>      puts;
        HttpPut                 puts1, puts2, post3;
        MultipartEntity         entity;
        File                    coverImageFile, profileImageFile;
        FileBody                cBody, pBody;

        params1  = getUserParams();
        puts   = new ArrayList<HttpPut>();
        puts1   = new HttpPut(PUT_USER + id);

        entity  = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);


        entity.addPart("user[first_name]", new StringBody(name));
        entity.addPart("user[last_name]", new StringBody("testing"));
        entity.addPart("user[about]", new StringBody(about));


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
        puts.add(puts1);

        System.out.println("puts1 entity complete");

        /** Second post **/
        puts2   = new HttpPut(PUT_ENROLL_DATA + enrollId);
        params2 = getEnrollParams();

        params2.put("major", major);
        params2.put("year", year);

        puts2.setEntity(new StringEntity(params2.toString()));
        puts2.setHeader("Accept", "application/json");
        puts2.setHeader("Content-Type", "application/json");


        puts.add(puts2);

        System.out.println("Done with posting user data, returning arraylist");
        return puts;
    }
}




    /*backGround.compress(Bitmap.CompressFormat.JPEG, 75, bos);
    profile.compress(Bitmap.CompressFormat.JPEG, 75, bos2);

    data    = bos.toByteArray();
    data2   = bos2.toByteArray();

    bab     = new ByteArrayBody(data, "cover_pick");
    bab2    = new ByteArrayBody(data2, "profile_pic");


    entity.addPart("cover_image", bab);
    entity.addPart("profile_image", bab2);
    entity.addPart("first_name", new StringBody(name));
    entity.addPart("major", new StringBody(major));
    entity.addPart("year", new StringBody(year));
    entity.addPart("about", new StringBody(about));
    entity.addPart("classes", new StringBody(subjects));
    post.setEntity(entity);*/


    /*public static HttpGet getUserInfo(String email, String password)
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

            /**
     *
     * @return

    private static List<NameValuePair> getUserParams()
    {
        System.out.println("get user params");
        if (params == null)
        {
            params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", "-"));
            params.add(new BasicNameValuePair("password", "-"));
            params.add(new BasicNameValuePair("first_name", "-"));
            params.add(new BasicNameValuePair("last_name", "-"));
            params.add(new BasicNameValuePair("about", "-"));
            params.add(new BasicNameValuePair("profile_image", "-"));
            params.add(new BasicNameValuePair("cover_image", "-"));
            params.add(new BasicNameValuePair("confirm", "false"));
        }

        return params;
    }

}*/
