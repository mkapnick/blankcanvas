package tutor.cesh.profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tutor.cesh.MasterStudent;
import tutor.cesh.R;
import tutor.cesh.database.DatabaseFactory;
import tutor.cesh.profile.classes.ClassesUtility;
import tutor.cesh.rest.AsyncDownloader;
import tutor.cesh.rest.AsyncGet;
import tutor.cesh.rest.BackgroundImageTaskDelegate;
import tutor.cesh.rest.TaskDelegate;
import tutor.cesh.rest.http.CourseHttpObject;
import tutor.cesh.rest.http.EnrollHttpObject;
import tutor.cesh.rest.http.TutorHttpObject;


public class StudentProfileActivity extends ActionBarActivity implements View.OnClickListener, TaskDelegate
{
    private Bundle                  info;
    public static ImageView         profileImageView, coverImageView;
    private EditText                name, major, year, about, classes;
    private DrawerLayout            drawerLayout;
    private ListView                listView;
    private static String           DOMAIN = "http://blankcanvas.pw/";
    private String []               listViewTitles;
    private ActionBar               actionBar;
    public static Subject           profileImageSubject, coverImageSubject;
    private ImageController         imageController;

    /**
     * A private class responsible for handling click events on the
     * DrawableLayout
     *
     * @author Michael Kapnick
     * @version v1
     */
    private class DrawerItemClickListener implements ListView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id)
        {
            listView.setItemChecked(position, true);
            onOptionsItemSelected(position);
        }
    }

    /**
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
        drawerLayout        = (DrawerLayout)findViewById(R.id.drawer_layout);
        listView            = (ListView)    findViewById(R.id.left_drawer);

        listViewTitles      = getResources().getStringArray(R.array.drawable_list_items);
        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, listViewTitles));
        listView.setOnItemClickListener(new DrawerItemClickListener());

        /* Set up boolean value in bundle */
        info                = getIntent().getExtras();
        info.putString("ok", "false");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Bundle          savedInstanceState;
        if (requestCode == 1)
        {
            if (resultCode == RESULT_OK)
            {
                savedInstanceState = data.getExtras();
                info.putString("firstName", savedInstanceState.getString("firstName"));
                info.putString("about",     savedInstanceState.getString("about"));
                info.putString("year",      savedInstanceState.getString("year"));
                info.putString("major",     savedInstanceState.getString("major"));
                info.putString("ok", "true");

                DatabaseFactory.updateMasterStudent(info);
            }
        }
        setUpUserInfo();
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.menu_button:
                if(drawerLayout.isDrawerOpen(listView))
                    drawerLayout.closeDrawer(listView);
                else
                    drawerLayout.openDrawer(listView);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        initializeUI();
        setUpRelationships();
        setUpActionBar();
        setUpUserInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int position, id;

        id          = item.getItemId();
        position    = -1;

        if (id == R.id.action_settings)
            return true;
        else if(id == R.id.action_edit_profile)
        {
            position = -100;
        }
        else if(id == R.id.action_switch_profile)
            position = 1;


        onOptionsItemSelected(position);
        return super.onOptionsItemSelected(item);

    }

    /**
     * Responding to clicks from the Action Bar and from
     * the drawer layout
     *
     * @param position The position in the listViewTitles array
     */
    private void onOptionsItemSelected(int position)
    {
        Intent  intent;

        if(position == -100)
        {
            intent = new Intent(this, EditStudentProfileActivity.class);
            intent.putExtras(info);
            startActivityForResult(intent, 1);
        }

        else if(position == 1)
        {
            drawerLayout.closeDrawer(listView);
            intent = new Intent(this, TutorProfileActivity.class);
            intent.putExtras(info);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        info.putString("ok", "true");
        setUpUserInfo();
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        info.putString("ok", "true");
        setUpUserInfo();
    }

    @Override
    public void setProgressDialog(ProgressDialog pd) {
        //nothing
    }

    /**
     *
     */
    private void setUpActionBar()
    {
        TextView    actionBarTextView;
        View        actionBarView;
        ImageButton actionBarMenuButton;

        actionBar           = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.action_bar);

        //displaying custom ActionBar
        actionBarView       = getSupportActionBar().getCustomView();
        actionBarTextView   = (TextView) actionBarView.findViewById(R.id.textViewActionBar);
        actionBarTextView.setText("STUDENT");
        actionBarTextView.setTextColor(Color.WHITE);

        actionBarMenuButton = (ImageButton) actionBarView.findViewById(R.id.menu_button);
        actionBarMenuButton.setOnClickListener(this);
    }

    /**
     *
     */
    private void setUpRelationships()
    {
        /* Set up Subject-observer relationship */

        imageController = ImageController.getInstance();

        profileImageSubject         = new ImageSubject();
        coverImageSubject           = new ImageSubject();

        new ImageObserver(profileImageView, profileImageSubject);
        new ImageDrawableObserver(coverImageView, coverImageSubject, getResources());
    }

    private void setUpQueriedUserInfo()
    {
        MasterStudent   ms;
        TaskDelegate    taskDelegate;
        AsyncDownloader asyncDownloader;

        ms = DatabaseFactory.getMasterStudent();

        /* Data that has already been retrieved from the server */
        name.setText(ms.getName(), TextView.BufferType.EDITABLE);
        info.putString("firstName", ms.getName());
        about.setText(ms.getAbout(), TextView.BufferType.EDITABLE);
        info.putString("about", ms.getAbout());

        //get profile image from server and set profile image
        //taskDelegate        = new ProfileImageTaskDelegate(profileImageSubject);
        /*asyncDownloader     = new AsyncDownloader(  info.getString("profile_image_url"),
                                                    this, taskDelegate, profileImageView.getWidth(),
                                                    profileImageView.getHeight());*/
        //asyncDownloader.execute();

        //get background image from server and set the background
        taskDelegate        = new BackgroundImageTaskDelegate(  getResources(),
                                                                coverImageSubject);
        asyncDownloader     = new AsyncDownloader(  info.getString("coverImage"),
                                                    this, taskDelegate, coverImageView.getWidth(),
                                                    coverImageView.getHeight());
        asyncDownloader.execute();

    }

    /**
     *
     */
    private void setUpUserInfo()
    {
        JSONArray                                   jsonArray;
        TextFieldHelper                             classesTextFieldHelper;
        HttpGet                                     get;
        String                                      formatted;
        Drawable                                    drawable;
        MasterStudent                               ms;
        ClassesUtility                              cUtility;

        classesTextFieldHelper  = new ClassesTextFieldHelper(this);
        ms                      = DatabaseFactory.getMasterStudent();

        try
        {
            if(info.getString("ok").equalsIgnoreCase("false"))
            {
                setUpQueriedUserInfo();
                /* Data that needs to be queried from the server */
                get = new EnrollHttpObject(ms).get();
                new AsyncGet(this, this).execute(get);

                get = new TutorHttpObject(ms).get();
                new AsyncGet(this, this).execute(get);

                get = new CourseHttpObject(ms).get();
                new AsyncGet(this, this).execute(get);
            }
            else
            {
                //set fields based on data from the updated bundle
                name.setText(ms.getName(), TextView.BufferType.EDITABLE);
                major.setText(ms.getMajor(), TextView.BufferType.EDITABLE);
                year.setText(ms.getYear(), TextView.BufferType.EDITABLE);
                about.setText(ms.getAbout(), TextView.BufferType.EDITABLE);

                drawable = new BitmapDrawable(getResources(), imageController.peek(ImageLocation.BACKGROUND));
                //profileImageView.setImageBitmap(imageController.peek(ImageLocation.PROFILE));
                coverImageView.setBackground(drawable);

                cUtility = new ClassesUtility(ms, this.classes, this);
                cUtility.setClassesRegularMode();

            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public void taskCompletionResult(Bitmap b, boolean check)
    {
        //nothing
    }

    @Override
    public void taskCompletionResult(JSONObject response)
    {
        //set fields based on JSON response from the server
        ClassesUtility  cUtility;
        String  []      classes;
        MasterStudent   ms;

        ms = DatabaseFactory.getMasterStudent();
        cUtility = new ClassesUtility(ms, this.classes, this);

        try
        {
            if(response.has("major")) {
                major.setText(response.getString("major"), TextView.BufferType.EDITABLE);
                info.putString("major", major.getText().toString());
            }

            if(response.has("year")) {
                year.setText(response.getString("year"), TextView.BufferType.EDITABLE);
                info.putString("year", year.getText().toString());
            }

            if(response.has("cname"))
            {
                System.out.println(response.getString("cname"));
                cUtility.formatClassesFrontEnd(response.getString("cname"));
                cUtility.setClassesRegularMode();
            }

        }
        catch (JSONException e){
            e.printStackTrace();
        }

        DatabaseFactory.updateMasterStudent(info);
    }
}
