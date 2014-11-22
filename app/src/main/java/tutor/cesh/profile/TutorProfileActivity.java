package tutor.cesh.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
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

import tutor.cesh.R;
import tutor.cesh.Student;
import tutor.cesh.Tutor;
import tutor.cesh.User;
import tutor.cesh.profile.util.classes.ClassesUtility;
import tutor.cesh.profile.util.classes.TutorClassesUtility;

public class TutorProfileActivity extends ActionBarActivity implements View.OnClickListener {

    private Bundle                  info;
    public static ImageView         profileImageView, coverImageView;

    private EditText                name, major, year, about, classes, rate;
    private DrawerLayout            drawerLayout;
    private ListView                listView;
    private String []               listViewTitles;
    private ActionBar               actionBar;

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

        listViewTitles      = getResources().getStringArray(R.array.drawable_list_items);
        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, listViewTitles));
        listView.setOnItemClickListener(new DrawerItemClickListener());

        info = getIntent().getExtras();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        setUpUserInfo();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
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
        setContentView(R.layout.activity_tutor_profile);

        initializeUI();
        setUpActionBar();
        setUpUserInfo();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tutor_profile, menu);
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
            intent = new Intent(this, EditTutorProfileActivity.class);
            intent.putExtras(info);
            startActivityForResult(intent, 1);
        }

        else if(position == 1)
        {
            drawerLayout.closeDrawer(listView);
            intent = new Intent(this, StudentProfileActivity.class);
            info.putString("ok", "true");
            intent.putExtras(info);
            startActivity(intent);
            //finish(); //TODO -- don't finish on switch
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
        actionBarTextView.setText("TUTOR PROFILE");
        actionBarTextView.setTextColor(Color.WHITE);

        actionBarMenuButton = (ImageButton) actionBarView.findViewById(R.id.menu_button);
        actionBarMenuButton.setOnClickListener(this);
    }

    /**
     *
     */
    private void setUpUserInfo()
    {
        BitmapDrawable                              drawable;
        Bitmap                                      tmp;
        ClassesUtility                              cUtility;
        User                                        user;
        Student                                     student;
        Tutor                                       tutor;

        user            = User.getInstance(this);
        student         = user.getStudent();
        tutor           = user.getTutor();

        name.setText(student.getName());
        major.setText(student.getMajor());
        year.setText(student.getYear());
        about.setText(tutor.getAbout());
        rate.setText(tutor.getRate());

        //classes already formatted in StudentProfileActivity
        cUtility = new TutorClassesUtility(user, this.classes, this);
        cUtility.setClassesRegularMode();

        tmp         = tutor.getCoverImage();
        drawable    = new BitmapDrawable(getResources(), tmp);
        coverImageView.setBackground(drawable);
    }
}
