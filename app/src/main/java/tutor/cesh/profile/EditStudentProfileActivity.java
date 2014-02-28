package tutor.cesh.profile;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import tutor.cesh.rest.AsyncGet;
import tutor.cesh.rest.RestClientExecute;
import tutor.cesh.rest.RestClientFactory;

import tutor.cesh.R;

public class EditStudentProfileActivity extends Activity {


    private boolean isProfPic = false;
    private String  profilePicPath = null;
    private String  backgroundPicPath = null;
    private Bundle  info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        this.info = getIntent().getExtras();
        System.out.println("inside edit profile activity!");

        try
        {
            setUpTextAreas();
        }
        catch (JSONException e)
        {
            System.out.println("Inside exception student profile activity");
            e.printStackTrace();
        }
        catch(Exception ee)
        {

        }
    }

    /**
     *
     */
    private void setUpTextAreas() throws JSONException, Exception
    {
        EditText            name, major, year, about, subjects, classes;


        name        = (EditText)    findViewById(R.id.name);
        major       = (EditText)    findViewById(R.id.major);
        year        = (EditText)    findViewById(R.id.year);
        about       = (EditText)    findViewById(R.id.about);
        subjects    = (EditText)    findViewById(R.id.subjects);
        classes     = (EditText)    findViewById(R.id.classes);

        name.setText(info.getString("first_name"));
        major.setText(info.getString("major"));
        year.setText(info.getString("year"));
        about.setText(info.getString("about"));


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Uri             targetUri;
        Bitmap          bitMap;
        BitmapDrawable  drawable;
        ImageView       image;

        System.out.println("on activity for result in edit profile");
        super.onActivityResult(requestCode, resultCode, data);
        targetUri = data.getData();

        if(isProfPic)
        {
            image               = (ImageView) findViewById(R.id.profileImage);
            profilePicPath      = targetUri.getPath();
        }

        else
        {
            image               = (ImageView) findViewById(R.id.profileBackgroundImage);
            backgroundPicPath   = targetUri.getPath();
        }

        if (requestCode == 1)
        {
            if (resultCode == RESULT_OK)
            {
                try
                {
                    bitMap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                    //image.setImageBitmap(bitMap);
                    drawable = new BitmapDrawable(getResources(), bitMap);
                    image.setBackground(drawable);
                }
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }
            }
        }

        isProfPic       = false;
    }
    /**
     *
     * @param view
     */
    public void onUserClick(View view)
    {
        Intent intent;

        System.out.println("in on user click");
        switch(view.getId())
        {
            case R.id.profileImage:
                System.out.println("in profile image button");
                intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                isProfPic= true;
                startActivityForResult(intent, 1);
                break;

            case R.id.profileBackgroundImage:
                System.out.println("in profile background image button");
                intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
                break;

            case R.id.saveButton:
                saveUserProfile(view);
                break;
        }
    }

    /**
     * Save user data to the server
     * @param view
     */
    private void saveUserProfile(View view)
    {
        System.out.println("in save user profile");
        EditText            name, major, year, about, subjects;
        ArrayList<HttpPut>  puts;
        RestClientExecute   rce;
        String              id, enrollId;

        System.out.println("before bundle");

        id              = info.getString("id");
        enrollId        = "1";//info.getString("enrollId");

        System.out.println("after bundle");

        name            = (EditText)    findViewById(R.id.name);
        major           = (EditText)    findViewById(R.id.major);
        year            = (EditText)    findViewById(R.id.year);
        about           = (EditText)    findViewById(R.id.about);
        subjects        = (EditText)    findViewById(R.id.classes);

        try
        {
            System.out.println("calling rest client factory!");
            puts        = RestClientFactory.put(id, enrollId, backgroundPicPath, profilePicPath,
                    name.getText().toString(), major.getText().toString(),
                    year.getText().toString(), about.getText().toString(),
                    subjects.getText().toString());

            for (int i =0; i < puts.size(); i++)
            {
                rce = new RestClientExecute(puts.get(i));
                rce.start();
            }

            Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show();
            finish();
        }
        catch(Exception e)
        {
            System.out.println("inside exception........edit student profile activity");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
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
            View rootView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
            return rootView;
        }
    }

}
