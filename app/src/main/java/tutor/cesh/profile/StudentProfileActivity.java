package tutor.cesh.profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
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

import tutor.cesh.R;
import tutor.cesh.database.DatabaseTable;
import tutor.cesh.rest.AsyncDownloader;
import tutor.cesh.rest.AsyncGet;
import tutor.cesh.rest.BackgroundImageTaskDelegate;
import tutor.cesh.rest.ProfileImageTaskDelegate;
import tutor.cesh.rest.RestClientFactory;
import tutor.cesh.rest.TaskDelegate;


public class StudentProfileActivity extends ActionBarActivity implements View.OnClickListener, TaskDelegate
{
    private Bundle                  info;
    public static ImageView         profileImageView, coverImageView;
    private EditText                name, major, year, about, classes;
    private DrawerLayout            drawerLayout;
    private ListView                listView;
    private static String           DOMAIN = "http://protected-earth-9689.herokuapp.com";
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
        BitmapDrawable  drawable;

        if (requestCode == 1)
        {
            if (resultCode == RESULT_OK)
            {
                savedInstanceState = data.getExtras();
                info.putString("firstName", savedInstanceState.getString("firstName"));
                info.putString("about",     savedInstanceState.getString("about"));
                info.putString("year",      savedInstanceState.getString("year"));
                info.putString("major",     savedInstanceState.getString("major"));
                info.putString("classes",   savedInstanceState.getString("classes"));
                info.putString("ok", "true");

                drawable = new BitmapDrawable(getResources(), imageController.peek(ImageLocation.BACKGROUND));
                profileImageView.setImageBitmap(imageController.peek(ImageLocation.PROFILE));
                coverImageView.setBackground(drawable);

                //profileImageSubject.notifyObservers();
                //coverImageSubject.notifyObservers();

                /*try
                {
                    setUpAndExecuteGet(DatabaseTable.USERS, info.getString("userId"));
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }*/
            }
        }
        setUpUserInfo();
    }

    @Override
    public void onClick(View v)
    {
        Intent intent;
        switch(v.getId())
        {
            case R.id.editCurrentProfile:
                intent = new Intent(this, EditStudentProfileActivity.class);
                intent.putExtras(info);
                startActivityForResult(intent, 1);
                break;

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
        else if(id == R.id.editCurrentProfile)
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
        setUpUserInfo();
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
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
        TextView    actionBarTextView, actionBarEdit;
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

        actionBarEdit       = (TextView)actionBarView.findViewById(R.id.editCurrentProfile);
        actionBarEdit.setOnClickListener(this);

        actionBarMenuButton = (ImageButton) actionBarView.findViewById(R.id.menu_button);
        actionBarMenuButton.setOnClickListener(this);
    }

    /**
     * Helper method to set up an HttpGet request
     * @param table
     * @param id
     * @return
     */
    private void setUpAndExecuteGet(DatabaseTable table, String id)
    {
        HttpGet get1;

        try
        {
            get1        = RestClientFactory.get(table, id);
            new AsyncGet(this, this).execute(get1);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
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

    /**
     *
     */
    private void setUpUserInfo()
    {
        AsyncTask<Void, Integer, Bitmap>            asyncDownloader;
        JSONArray                                   jsonArray;
        TextFieldHelper                             classesTextFieldHelper;
        String                                      formatted;
        TaskDelegate                                taskDelegate;

        classesTextFieldHelper  = new ClassesTextFieldHelper(this);

        try
        {
            //get user info from the server
            if(info.getString("ok").equalsIgnoreCase("false"))
            {
                setUpAndExecuteGet(DatabaseTable.USERS, info.getString("id"));
                setUpAndExecuteGet(DatabaseTable.ENROLLS, info.getString("enrollId"));
            }
            else
            {
                //set fields based on data from the updated bundle
                name.setText(info.getString("firstName"), TextView.BufferType.EDITABLE);
                major.setText(info.getString("major"), TextView.BufferType.EDITABLE);
                year.setText(info.getString("year"), TextView.BufferType.EDITABLE);
                about.setText(info.getString("about"), TextView.BufferType.EDITABLE);

                //get profile image from bundle and set profile image
                taskDelegate        = new ProfileImageTaskDelegate(profileImageSubject);
                asyncDownloader     = new AsyncDownloader(info.getString("profileImage"),
                                                          this,taskDelegate);
                asyncDownloader.execute();

                //get background image from bundle and set the background
                taskDelegate        = new BackgroundImageTaskDelegate(  getResources(),
                                                                        coverImageSubject);

                asyncDownloader     = new AsyncDownloader(info.getString("coverImage"),
                                                          this, taskDelegate);
                asyncDownloader.execute();

                if (!info.getString("classes").equalsIgnoreCase("null"))
                {
                    jsonArray = new JSONArray(info.getString("classes"));
                    formatted = classesTextFieldHelper.help(this.classes, null, jsonArray);
                    info.putString("classes", formatted);
                }
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
        AsyncTask<Void, Integer, Bitmap>            asyncDownloader;
        String []                                   classesFromServer;
        TextFieldHelper                             classesTextFieldHelper;
        TaskDelegate                                taskDelegate;

        classesTextFieldHelper  = new ClassesTextFieldHelper(this);

        try
        {
            if(response.has("first_name")) {
                name.setText(response.getString("first_name"), TextView.BufferType.EDITABLE);
                info.putString("firstName", name.getText().toString());
            }

            if(response.has("major")) {
                major.setText(response.getString("major"), TextView.BufferType.EDITABLE);
                info.putString("major", major.getText().toString());
            }

            if(response.has("year")) {
                year.setText(response.getString("year"), TextView.BufferType.EDITABLE);
                info.putString("year", year.getText().toString());
            }

            if(response.has("about")) {
                about.setText(response.getString("about"), TextView.BufferType.EDITABLE);
                info.putString("about", about.getText().toString());
            }

            //get profile image from server and set profile image
            if(response.has("profile_image_url")) {
                taskDelegate        = new ProfileImageTaskDelegate(profileImageSubject);
                asyncDownloader     = new AsyncDownloader(DOMAIN + response.getString("profile_image_url"),
                                                          this, taskDelegate);
                asyncDownloader.execute();
                info.putString("profileImage", DOMAIN + response.getString("profile_image_url"));

            }
            //get background image from server and set the background
            if(response.has("cover_image_url")) {
                taskDelegate        = new BackgroundImageTaskDelegate(getResources(),
                                                                      coverImageSubject);
                asyncDownloader     = new AsyncDownloader(DOMAIN + response.getString("cover_image_url"),
                                                          this, taskDelegate);
                asyncDownloader.execute();

                info.putString("coverImage", DOMAIN + response.getString("cover_image_url"));
            }
            if(response.has("classes")) {
                classesFromServer = response.getString("classes").split(",");
                classesTextFieldHelper.help(this.classes, null, classesFromServer, false);
                info.putString("classes", response.getString("classes"));
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }
}
