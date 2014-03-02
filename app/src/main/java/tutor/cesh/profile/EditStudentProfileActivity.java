package tutor.cesh.profile;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.client.methods.HttpPut;
import org.json.JSONException;

import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import tutor.cesh.rest.RestClientExecute;
import tutor.cesh.rest.RestClientFactory;

import tutor.cesh.R;
import tutor.cesh.sampled.statik.BitMapOp;

public class EditStudentProfileActivity extends Activity {


    private String  profileImagePath;
    private String  coverImagePath;
    private Bundle  info;
    private EditText        name, major, year, about, subjects, classes;
    private ImageView       profileImageView, coverImageView;
    private Bitmap          profileImage, coverImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student_profile);

        /*if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }*/
        this.info = getIntent().getExtras();
        System.out.println("inside edit profile activity!");

        name                = (EditText)    findViewById(R.id.name);
        major               = (EditText)    findViewById(R.id.major);
        year                = (EditText)    findViewById(R.id.year);
        about               = (EditText)    findViewById(R.id.about);
        subjects            = (EditText)    findViewById(R.id.subjects);
        classes             = (EditText)    findViewById(R.id.classes);
        profileImageView    = (ImageView)   findViewById(R.id.profileImage);
        coverImageView      = (ImageView)   findViewById(R.id.profileBackgroundImage);

        try
        {
            setUpTextAreas();
            //setUpImages();
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

        System.out.println("on activity for result in edit profile");
        super.onActivityResult(requestCode, resultCode, data);

        try
        {
            //targetUri = Uri.parse(getRealPathFromURI(this, data.getData()));
            if (requestCode == 1)
            {
                System.out.println("request code is 1***********************************************************************");
                profileImagePath    = data.getData().getPath();
                if(profileImagePath != null)
                {
                    profileImagePath = getRealPathFromURI(this, data.getData());
                    System.out.println(profileImagePath);
                    bitMap              = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData()));
                    bitMap              = BitMapOp.getResizedBitmap(bitMap, 75,75);
                    profileImageView.setImageBitmap(bitMap);
                    profileImage = bitMap;
                }

            }
            else
            {
                System.out.println("request code is: " + requestCode);
                //coverImagePath      = targetUri.getPath();
                if(coverImagePath != null)
                {
                    bitMap = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData()));
                    drawable = new BitmapDrawable(getResources(), bitMap);
                    coverImageView.setBackground(drawable);
                    coverImage = bitMap;
                }

            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
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
                startActivityForResult(intent, 1);
                break;

            case R.id.profileBackgroundImage:
                System.out.println("in profile background image button");
                intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
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
        ArrayList<HttpPut>  puts;
        RestClientExecute   rce;
        String              id, enrollId;

        System.out.println("before bundle");

        id              = info.getString("id");
        System.out.println("id is.." + id);
        enrollId        = info.getString("enrollId");
        System.out.println("enroll id is.." + enrollId);

        System.out.println("after bundle");

        try
        {
            System.out.println("calling rest client factory!");
            puts        = RestClientFactory.put(id, enrollId, coverImage, profileImagePath,
                    name.getText().toString(), major.getText().toString(),
                    year.getText().toString(), about.getText().toString(),
                    subjects.getText().toString());

            for (int i =0; i < puts.size(); i++)
            {
                rce = new RestClientExecute(puts.get(i));
                rce.start();
            }

            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
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
     *
     * @param context
     * @param contentUri
     * @return
     */
    private String getRealPathFromURI(Context context, Uri contentUri)
    {
        Cursor cursor;
        int columnIndex;
        cursor = null;
        try
        {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri,proj, null, null, null);
            columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(columnIndex);
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
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
            View rootView = inflater.inflate(R.layout.fragment_edit_student_profile, container, false);
            return rootView;
        }
    }

}
