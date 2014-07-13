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
import org.json.JSONException;
import org.json.JSONObject;

import tutor.cesh.R;
import tutor.cesh.Student;
import tutor.cesh.Tutor;
import tutor.cesh.User;
import tutor.cesh.profile.classes.ClassesUtility;
import tutor.cesh.profile.classes.StudentClassesUtility;
import tutor.cesh.profile.classes.TutorClassesUtility;
import tutor.cesh.rest.AsyncDownloader;
import tutor.cesh.rest.AsyncGet;
import tutor.cesh.rest.CoverImageHandler;
import tutor.cesh.rest.ImageHandler;
import tutor.cesh.rest.TaskDelegate;
import tutor.cesh.rest.http.EnrollHttpObject;
import tutor.cesh.rest.http.StudentCourseHttpObject;
import tutor.cesh.rest.http.TutorCourseHttpObject;
import tutor.cesh.rest.http.TutorHttpObject;


public class StudentProfileActivity extends ActionBarActivity implements View.OnClickListener, TaskDelegate
{
    private Bundle                  info;
    public static ImageView         profileImageView, coverImageView;
    private EditText                name, major, year, about, classes;
    private DrawerLayout            drawerLayout;
    private ListView                listView;
    private String []               listViewTitles;
    private ActionBar               actionBar;
    public static Subject           profileImageSubject, coverImageSubject;

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
        if(!info.containsKey("ok"))
            info.putString("ok", "false");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        User    user;
        Student student;

        user    = User.getInstance();
        student = user.getStudent();

        if (requestCode == 1)
        {
            if (resultCode == RESULT_OK)
            {
                System.out.println("In here!!!");
                info.putString("ok", "true");
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
        setUpSubjectObservers();
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
    private void setUpSubjectObservers()
    {
        /* Set up Subject-observer relationship */

        profileImageSubject         = new ImageSubject();
        coverImageSubject           = new ImageSubject();

        new ImageObserver(profileImageView, profileImageSubject);
        new ImageDrawableObserver(coverImageView, coverImageSubject, getResources());
    }

    private void setUpQueriedUserInfo()
    {
        User            user;
        Student         student;
        ImageHandler    handler;
        AsyncDownloader asyncDownloader;

        user    = User.getInstance();
        student = user.getStudent();

        /* Data that has already been retrieved from the server */
        name.setText(student.getName(), TextView.BufferType.EDITABLE);
        about.setText(student.getAbout(), TextView.BufferType.EDITABLE);

        // Download cover image from server, belongs to student
        handler             = new CoverImageHandler(getResources(), this.coverImageView);
        asyncDownloader     = new AsyncDownloader(student.getCoverImageUrl(), handler, student, new ProgressDialog(this));
        asyncDownloader.execute();
    }

    /**
     *
     */
    private void setUpUserInfo()
    {
        HttpGet                                     get;
        Drawable                                    drawable;
        ClassesUtility                              cUtility;
        User                                        user;
        Student                                     student;
        ProgressDialog                              pd;

        user            = User.getInstance();
        student         = user.getStudent();
        pd              = new ProgressDialog(this);


        try
        {
            if(info.getString("ok").equalsIgnoreCase("false"))
            {
                setUpQueriedUserInfo();
                pd.setTitle("Downloading...");
                pd.setMessage("Please wait");
                pd.setCancelable(false);
                pd.setIndeterminate(true);

                /* Data that needs to be queried from the server */
                get = new EnrollHttpObject(user).get();
                new AsyncGet(this, this, pd).execute(get);

                get = new TutorHttpObject(user).get();
                new AsyncGet(this, this, pd).execute(get);

                get = new StudentCourseHttpObject(user).get();
                new AsyncGet(this, this, pd).execute(get);

                get = new TutorCourseHttpObject(user).get();
                new AsyncGet(this, this, pd).execute(get);
            }
            else
            {
                //set fields based on data from the updated bundle
                name.setText(student.getName(), TextView.BufferType.EDITABLE);
                major.setText(student.getMajor(), TextView.BufferType.EDITABLE);
                year.setText(student.getYear(), TextView.BufferType.EDITABLE);
                about.setText(student.getAbout(), TextView.BufferType.EDITABLE);
                coverImageView.setBackground(new BitmapDrawable(getResources(), student.getCoverImage()));

                cUtility = new StudentClassesUtility(user, this.classes, this);
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
        User            user;
        Student         student;
        Tutor           tutor;
        String          tmp;
        ImageHandler    handler;
        AsyncDownloader asyncDownloader;

        user = User.getInstance();
        student = user.getStudent();
        tutor = user.getTutor();

        try
        {
            if(response.has("major")) {
                tmp = response.getString("major");
                major.setText(tmp, TextView.BufferType.EDITABLE);
                student.setMajor(tmp);
            }

            if(response.has("year")) {
                tmp = response.getString("year");
                year.setText(tmp, TextView.BufferType.EDITABLE);
                student.setYear(tmp);
            }

            if(response.has("rate")) {
                tmp = response.getString("rate");
                tutor.setRate(tmp);
            }

            if(response.has("tutorabout")) {
                tmp = response.getString("tutorabout");
                tutor.setAbout(tmp);
            }

            if(response.has("tutor_cover_image_url"))
            {
                tmp = response.getString("tutor_cover_image_url");
                tutor.setCoverImageUrl(tmp);

                // Download cover image from server, belongs to tutor
                handler             = new CoverImageHandler(getResources(), null);
                asyncDownloader     = new AsyncDownloader(tutor.getCoverImageUrl(), handler, tutor, null);
                asyncDownloader.execute();
            }

            if(response.has("tutor_profile_image_url"))
            {
                //nothing for now
            }

            if(response.has("coursesTaking"))
            {
                cUtility    = new StudentClassesUtility(user, this.classes, this);
                cUtility.formatClassesFrontEnd(response.getString("coursesTaking"));
                cUtility.setClassesRegularMode();
            }
            if(response.has("coursesTutoring"))
            {
                cUtility    = new TutorClassesUtility(user, null, this);
                cUtility.formatClassesFrontEnd(response.getString("coursesTutoring"));
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }
}
