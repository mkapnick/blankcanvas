package tutor.cesh.profile;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;
import org.json.JSONObject;

import tutor.cesh.R;
import tutor.cesh.rest.AsyncDownloader;
import tutor.cesh.rest.RestClientFactory;
import tutor.cesh.rest.AsyncGet;
import tutor.cesh.sampled.statik.BitMapOp;
import tutor.cesh.sampled.statik.BitmapFactoryA;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class StudentProfileActivity extends Activity
{
    private Bundle          info;
    private ImageView       profileImageView, coverImageView;
    private EditText        name, major, year, about, subjects, classes;
    private static String   DOMAIN = "http://protected-earth-9689.herokuapp.com";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        System.out.println("inside student Profile activity");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);
        name                = (EditText)    this.findViewById(R.id.name);
        major               = (EditText)    this.findViewById(R.id.major);
        year                = (EditText)    this.findViewById(R.id.year);
        about               = (EditText)    this.findViewById(R.id.about);
        subjects            = (EditText)    this.findViewById(R.id.subjects);
        classes             = (EditText)    this.findViewById(R.id.classes);
        profileImageView    = (ImageView)   findViewById(R.id.profileImage);
        coverImageView      = (ImageView)   findViewById(R.id.profileBackgroundImage);

        info = getIntent().getExtras();

        /*if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }*/

        try
        {
            System.out.println("Setting up user info");
            setUpUserInfo();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        catch(Exception e)
        {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        System.out.println("In on activity for result");
        if (requestCode == 1)
        {
            System.out.println("request code is 1!");
            try
            {
                System.out.println("Setting up text areas");
                setUpUserInfo();
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            catch(Exception ee)
            {

            }
        }
    }

    /**
     *
     */
    private void setUpUserInfo() throws JSONException, Exception
    {
        System.out.println("Inisde setUpUserInfo");
        HttpGet                                     get1, get2;
        AsyncTask<HttpGet, Integer, JSONObject>     asyncGet1, asyncGet2;
        AsyncTask<Void, Integer, Bitmap>            asyncDownloader;
        JSONObject                                  obj1, obj2;
        Drawable                                    drawable;

       // Query web server and get user's information

        get1        = RestClientFactory.get("users", info.getString("id"));
        get2        = RestClientFactory.get("enrolls", info.getString("enrollId"));

        asyncGet1      = new AsyncGet().execute(get1);
        obj1        = asyncGet1.get();

        asyncGet2      = new AsyncGet().execute(get2);
        obj2        = asyncGet2.get();

        name.setText(obj1.getString("first_name"), TextView.BufferType.EDITABLE);
        major.setText(obj2.getString("major"), TextView.BufferType.EDITABLE);
        year.setText(obj2.getString("year"), TextView.BufferType.EDITABLE);
        about.setText(obj1.getString("about"), TextView.BufferType.EDITABLE);

        info.putString("first_name", name.getText().toString());
        info.putString("about", about.getText().toString());
        info.putString("year", year.getText().toString());
        info.putString("major", major.getText().toString());


        System.out.println("profile image...");
        asyncDownloader = new AsyncDownloader(DOMAIN + obj1.getString("profile_image_url"), null, null).execute();
        profileImageView.setImageBitmap(asyncDownloader.get());
        info.putString("profileImage", DOMAIN + obj1.getString("profile_image_url"));
        System.out.println("done profile image...");

        System.out.println("cover image...");
        asyncDownloader = new AsyncDownloader(DOMAIN + obj1.getString("cover_image_url"), null, null).execute();
        drawable = new BitmapDrawable(getResources(), asyncDownloader.get());
        coverImageView.setBackground(drawable);
        info.putString("coverImage", DOMAIN + obj1.getString("cover_image_url"));
        System.out.println("done cover image...");

        //((BitmapDrawable) profileImageView.getDrawable()).getBitmap());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int     id;
        Intent  intent;

        id  = item.getItemId();


        if (id == R.id.action_settings)
        {
            return true;
        }

        if(id == R.id.action_edit_profile)
        {
            //start new intent and call editprofile
            System.out.println("before starting intent");
            Bundle bundle;
            intent = new Intent(this, EditStudentProfileActivity.class);
            intent.putExtras(info);
            startActivityForResult(intent, 1);
        }


        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_student_profile, container, false);

            return rootView;
        }
    }

}
