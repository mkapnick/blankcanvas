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
import tutor.cesh.rest.AsyncGet;
import tutor.cesh.rest.RestClientFactory;
import tutor.cesh.rest.TaskDelegate;

public class TutorProfileActivity extends ActionBarActivity implements View.OnClickListener, TaskDelegate {

    private Bundle                  info;
    public static ImageView        profileImageView, coverImageView;

    private EditText                name, major, year, about, classes, rate;
    private static String           DOMAIN = "http://protected-earth-9689.herokuapp.com";
    private DrawerLayout            drawerLayout;
    private ListView                listView;
    private String []               listViewTitles;
    private ActionBar               actionBar;
    private ImageController         controller;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Bundle      savedInstanceState;
        BitmapDrawable  drawable;


        if (requestCode == 1)
        {
            if (resultCode == RESULT_OK)
            {
                savedInstanceState = data.getExtras();
                info.putString("firstName", savedInstanceState.getString("firstName"));
                info.putString("about", savedInstanceState.getString("about"));
                info.putString("year", savedInstanceState.getString("year"));
                info.putString("major", savedInstanceState.getString("major"));
                info.putString("rate", savedInstanceState.getString("rate"));
                info.putString("isRateSet", "true");

                drawable = new BitmapDrawable(getResources(), controller.peek(ImageLocation.BACKGROUND));
                profileImageView.setImageBitmap(controller.peek(ImageLocation.PROFILE));
                coverImageView.setBackground(drawable);
            }
        }
        setUpUserInfo();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_profile);

        initializeUI();
        setUpRelationships();
        setUpActionBar();
        setUpUserInfo();
    }

    /**
     *
     */
    private void initializeUI()
    {
        name                = (EditText)    this.findViewById(R.id.name);
        major               = (EditText)    this.findViewById(R.id.major);
        year                = (EditText)    this.findViewById(R.id.year);
        about               = (EditText)    this.findViewById(R.id.about);
        classes             = (EditText)    this.findViewById(R.id.classes);
        rate                = (EditText)    this.findViewById(R.id.rate);
        profileImageView    = (ImageView)   findViewById(R.id.profileImage);
        coverImageView      = (ImageView)   findViewById(R.id.profileBackgroundImage);
        drawerLayout        = (DrawerLayout)findViewById(R.id.drawer_layout_tutor);
        listView            = (ListView)    findViewById(R.id.left_drawer_tutor);


        listViewTitles = getResources().getStringArray(R.array.drawable_list_items_tutor);
        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, listViewTitles));
        listView.setOnItemClickListener(new DrawerItemClickListener());

        info = getIntent().getExtras();
        info.putString("isRateSet", "false");
    }

    @Override
    public void onClick(View v)
    {
        Intent intent;
        switch(v.getId())
        {
            case R.id.editCurrentProfile:
                intent = new Intent(this, EditTutorProfileActivity.class);
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
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tutor_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int position, id;

        id          = item.getItemId();
        position    = -1;

        if (id == R.id.action_settings)
            return true;
        else if(id == R.id.action_edit_tutor_profile)
            position = -100;
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
            intent = new Intent(this, EditTutorProfileActivity.class);
            intent.putExtras(info);
            startActivityForResult(intent, 1);
        }

        else if(position == 1)
        {
            drawerLayout.closeDrawer(listView);
            intent = new Intent(this, StudentProfileActivity.class);
            intent.putExtras(info);
            startActivity(intent);
            finish();
        }
    }


    @Override
    protected void onRestart()
    {
        super.onRestart();
        setUpUserInfo();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        setUpUserInfo();
    }
    @Override
    public void setProgressDialog(ProgressDialog pd) {

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
        actionBarTextView.setText("TUTOR");
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
        HttpGet                                     get1;

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

    private void setUpRelationships()
    {
        controller = ImageController.getInstance();
        //may need to only do this in TutorProfileActivity
        new ImageObserver(profileImageView, StudentProfileActivity.profileImageSubject);
        new ImageDrawableObserver(coverImageView, StudentProfileActivity.coverImageSubject, getResources());
    }
    /**
     *
     */
    private void setUpUserInfo()
    {
        JSONArray                                   jsonArray;
        TextFieldHelper                             classesTextFieldHelper;
        String                                      formatted;
        BitmapDrawable                              drawable;
        Bitmap                                      tmp;

        classesTextFieldHelper  = new ClassesTextFieldHelper(this);

        try
        {
            //Stuff unique to TutorProfileActivity
            if(info.getString("isRateSet").equalsIgnoreCase("false")){
                setUpAndExecuteGet(DatabaseTable.TUTORS, info.getString("tutorId"));
                //rate.setText(json.getString("rate"), TextView.BufferType.EDITABLE);
            }
            else{
                rate.setText(info.getString("rate"), TextView.BufferType.EDITABLE);
            }

            //This info is already set from the StudentProfileActivity. Do not need
            //to query the server for this
            name.setText(info.getString("firstName"), TextView.BufferType.EDITABLE);
            major.setText(info.getString("major"), TextView.BufferType.EDITABLE);
            year.setText(info.getString("year"), TextView.BufferType.EDITABLE);
            about.setText(info.getString("about"), TextView.BufferType.EDITABLE);

            //No need to download profile image and cover image.
            //These were set from StudentProfileActivity

            tmp         = controller.peek(ImageLocation.PROFILE);
            profileImageView.setImageBitmap(tmp);
            tmp         = controller.peek(ImageLocation.BACKGROUND);
            drawable    = new BitmapDrawable(getResources(), tmp);
            coverImageView.setBackground(drawable);

            /****** ADD CODE FOR USER ENTERING IN CLASSES HE/SHE IS TUTORING***/
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
            if(response.has("rate")) {
                rate.setText(response.getString("rate"));
                info.putString("rate", rate.getText().toString());
            }
            //Also have to set up tutor classes here
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }
}
