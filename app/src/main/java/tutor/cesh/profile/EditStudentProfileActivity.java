package tutor.cesh.profile;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.methods.HttpPut;
import org.json.JSONArray;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import tutor.cesh.R;
import tutor.cesh.rest.AsyncDownloader;
import tutor.cesh.rest.RestClientExecute;
import tutor.cesh.rest.RestClientFactory;
import tutor.cesh.sampled.statik.BitMapOp;

public class EditStudentProfileActivity extends Activity
{


    private String              profileImagePath, coverImagePath;
    private Bundle              info;
    private EditText            name, major, year, about,   classes;
    private ImageView           profileImageView, coverImageView;
    private GenericTextWatcher  textWatcher;


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

        initializeUI();
        setUpUserInfo();
    }


    /**
     * Initialize main parts of the UI
     *
     */
    private void initializeUI()
    {
        name                = (EditText)    findViewById(R.id.name);
        major               = (EditText)    findViewById(R.id.major);
        year                = (EditText)    findViewById(R.id.year);
        about               = (EditText)    findViewById(R.id.about);
        classes             = (EditText)    findViewById(R.id.classes);

        profileImageView    = (ImageView)   findViewById(R.id.profileImage);
        coverImageView      = (ImageView)   findViewById(R.id.profileBackgroundImage);

        textWatcher         = new GenericTextWatcher(getApplicationContext(), classes);
        classes.addTextChangedListener(textWatcher);
    }

    /**
     *
     */
    private void setUpUserInfo()
    {
        AsyncTask<Void, Integer, Bitmap>    asyncDownloader;
        Drawable                            drawable;
        String []                           classesFromBundle;
        BubbleTextView                      bubbleTextView;
        String                              courseName;
        SpannableStringBuilder              sb;
        int                                 start, end;
        TextView                            tv;

        start           = 0;
        end             = 0;

        //set fields based on data from the bundle
        name.setText(info.getString("firstName"));
        major.setText(info.getString("major"));
        year.setText(info.getString("year"));
        about.setText(info.getString("about"));

        try
        {
            //get profile image from bundle and set as profile image
            asyncDownloader = new AsyncDownloader(info.getString("profileImage"), null,null).execute();
            profileImageView.setImageBitmap(asyncDownloader.get());

            //get background image from bundle and set as background image
            asyncDownloader = new AsyncDownloader(info.getString("coverImage"), null,null).execute();
            drawable = new BitmapDrawable(getResources(), asyncDownloader.get());
            coverImageView.setBackground(drawable);

            /************************* Handle classes field from server *******************/
            classes.setText("");
            classesFromBundle = info.getString("classes").split(",");
            System.out.println("In edit student!! -- " + info.getString("classes"));
            for (int i =0; i < classesFromBundle.length; i++)
            {
                courseName      = classesFromBundle[i];
                System.out.println("courseName " + courseName);

                courseName      = courseName.replaceAll("\\]", "");
                courseName      = courseName.replaceAll("\\[", "");
                courseName      = courseName.replaceAll("\"", "");
                System.out.println(courseName);
                tv              = new TextView(this);
                this.textWatcher.addTextView(tv);

                bubbleTextView  = new BubbleTextView(getApplicationContext(), tv);
                sb              = bubbleTextView.createBubbleOverText(courseName, true);
                end+=sb.length();


                System.out.println("start " + start);
                System.out.println("end " + end);

                classes.append(sb);
                classes.getText().replace(start, end, sb, 0, sb.length());
                this.textWatcher.addLength(sb.length());



                this.textWatcher.setPreviousEndPosition(end);
                this.textWatcher.setPreviousStartPosition(end - sb.length());
                this.textWatcher.addStartingPosition(end - sb.length());
                //this.classes.setSelection(cursorPosition);

                start += sb.length();
                System.out.println("start " + start);


            }
            /******************************************************************************/
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Bitmap          bitMap;
        BitmapDrawable  drawable;

        super.onActivityResult(requestCode, resultCode, data);

        try
        {
            if (requestCode == 1)
            {
                profileImagePath    = data.getData().getPath();
                if(profileImagePath != null)
                {
                    profileImagePath = getRealPathFromURI(this, data.getData());
                    bitMap              = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData()));
                    bitMap              = BitMapOp.getResizedBitmap(bitMap, 75,75);
                    profileImageView.setImageBitmap(bitMap);
                }
            }
            else
            {
                coverImagePath      = data.getData().getPath();
                if(coverImagePath != null)
                {
                    coverImagePath = getRealPathFromURI(this, data.getData());
                    bitMap = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData()));
                    drawable = new BitmapDrawable(getResources(), bitMap);
                    coverImageView.setBackground(drawable);
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
                intent = new Intent();
                intent.putExtras(info);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    /**
     * Save user data to the server
     * @param view
     */
    private void saveUserProfile(View view)
    {
        ArrayList<HttpPut>  puts;
        ArrayList<TextView> tvClasses;
        RestClientExecute   rce;
        String              id, enrollId, courseName, jsonArray;
        JSONArray           classesAsStudent;

        id                  = info.getString("id");
        enrollId            = info.getString("enrollId");
        tvClasses           = textWatcher.getTextViews();
        classes             = null;
        jsonArray           = null;
        classesAsStudent    = null;

        //As long as the user entered in the classes he/she is taking
        if (tvClasses.size() > 0)
        {
            //convert to a valid JSON Array to send up to the server
            jsonArray      = "[";
            for (int i = 0; i < tvClasses.size(); i++)
            {
                courseName = tvClasses.get(i).getText().toString();
                if (i != tvClasses.size() - 1)
                    jsonArray += "{\"name\": " + "\"" + courseName + "\"" + "},";
                else
                    jsonArray += "{\"name\": " + "\"" + courseName + "\""+ "}";
            }
            jsonArray += "]";
        }

        System.out.println("Updating this to the server: " + jsonArray);
        //Send a put request with the user's data up the server
        try
        {
            if (jsonArray != null)
            {
                System.out.println("JSON array is not null");
                System.out.println(jsonArray);
                classesAsStudent = new JSONArray(jsonArray);
            }

            puts        = RestClientFactory.put(id, enrollId, coverImagePath, profileImagePath,
                    name.getText().toString(), major.getText().toString(),
                    year.getText().toString(), about.getText().toString(),
                    classesAsStudent);

            for (int i =0; i < puts.size(); i++)
            {
                rce = new RestClientExecute(puts.get(i));
                rce.start();
            }

            info.putString("firstName", name.getText().toString());
            info.putString("about", about.getText().toString());
            info.putString("year", year.getText().toString());
            info.putString("major", major.getText().toString());
            System.out.println("JSONARAAY bitch: " + jsonArray);
            info.putString("classes", jsonArray);

            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e)
        {
            System.out.println("Exception!!!---!!!");
            e.printStackTrace();
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
