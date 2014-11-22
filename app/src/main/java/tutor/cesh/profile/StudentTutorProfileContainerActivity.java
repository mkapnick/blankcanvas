package tutor.cesh.profile;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import tutor.cesh.R;
import tutor.cesh.Tutor;
import tutor.cesh.User;
import tutor.cesh.database.GlobalDatabaseHelper;
import tutor.cesh.profile.fragment.StudentProfileFragment;
import tutor.cesh.profile.fragment.TutorProfileFragment;

public class StudentTutorProfileContainerActivity extends ActionBarActivity implements View.OnClickListener {

    protected DrawerLayout          drawerLayout;
    private ListView                listView;
    private String []               listViewTitles;
    private android.support.v7.app.ActionBar actionBar;
    private StudentProfileFragment  studentProfileFragment;
    private TutorProfileFragment    tutorProfileFragment;
    private ImageButton             drawerLayoutButton, editActionBarButton;

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
            Intent intent;
            listView.setItemChecked(position, true);
            switch(position)
            {
                case 0:
                    //update drawer layout
                    if(drawerLayout.isDrawerOpen(listView))
                        drawerLayout.closeDrawer(listView);
                    else
                        drawerLayout.openDrawer(listView);
                    //call the correct class
                    //intent = new Intent(getApplicationContext(), TutorListActivity.class);
                    //startActivity(intent);
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
            }
            //onOptionsItemSelected(position);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
        {
            if (resultCode == RESULT_OK){
                //setUpActionBar();
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        GlobalDatabaseHelper    globalDatabaseHelper;

        globalDatabaseHelper        = new GlobalDatabaseHelper(this);
        this.studentProfileFragment = new StudentProfileFragment();
        this.tutorProfileFragment   = new TutorProfileFragment();
        TutorProfileFragment.setActivity(this);

        globalDatabaseHelper.downloadStudentDataFromServer(studentProfileFragment);
        globalDatabaseHelper.downloadStudentCoverImageFromServer(getResources(),
                studentProfileFragment.coverImageView);

        globalDatabaseHelper.downloadTutorDataFromServer(tutorProfileFragment);
        globalDatabaseHelper.downloadTutorCoverImageFromServer(getResources(),
                tutorProfileFragment.coverImageView);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_tutor_profile_container);

        setUpActionBar();
        setUpDrawerLayout();
        setUpOtherVariables();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.student_tutor_profile_container, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if(id == R.id.left_action_bar_image){
            if(drawerLayout.isDrawerOpen(listView))
                drawerLayout.closeDrawer(listView);
            else
                drawerLayout.openDrawer(listView);
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View v)
    {
        Intent intent;
        Bundle bundle;

        switch(v.getId())
        {
            case R.id.left_action_bar_image:
                if(drawerLayout.isDrawerOpen(listView))
                    drawerLayout.closeDrawer(listView);
                else
                    drawerLayout.openDrawer(listView);
                break;
            case R.id.edit_action_bar_icon:
                intent = setIntentOnEdit();
                startActivityForResult(intent, RESULT_OK);
                break;
        }
    }

    /**
     * Main entry point is when user clicks the edit button field
     */
    private Intent setIntentOnEdit()
    {
        Bundle  bundle;
        Intent  intent;
        User    user;
        Tutor   tutor;
        String  name, major, minor, year, studentAbout,
                tutorAbout, classesTaking, classesTutoring, rate;

        String [] classesTutoringArray;

        intent  = null;
        user    = User.getInstance(this);
        tutor   = user.getTutor();

        try
        {
            intent              = new Intent(this, EditStudentAndTutorProfileActivity.class);
            bundle              = new Bundle();
            name                = this.studentProfileFragment.getName();
            major               = this.studentProfileFragment.getMajor();
            //minor             = this.studentProfileFragment.getMinor();
            year                = this.studentProfileFragment.getYear();
            studentAbout        = this.studentProfileFragment.getAbout();
            tutorAbout          = tutor.getAbout();
            classesTaking       = this.studentProfileFragment.getClasses();
            classesTutoringArray= tutor.getClasses();
            classesTutoring     = "";

            for (String tutorClass: classesTutoringArray)
                classesTutoring += tutorClass + " ";

            rate                = tutor.getRate();

            bundle.putString("name", name);
            bundle.putString("major", major);
            bundle.putString("year", year);
            bundle.putString("studentAbout", studentAbout);
            bundle.putString("tutorAbout", tutorAbout);
            bundle.putString("classesTaking", classesTaking);
            bundle.putString("classesTutoring", classesTutoring);
            bundle.putString("rate", rate);

            intent.putExtras(bundle);
        }
        catch(Exception e){
            e.printStackTrace();

        }

        return intent;
    }

    /**
     *
     */
    private void setUpActionBar()
    {
        ActionBar.Tab   studentTab, tutorTab;
        LayoutInflater  inflator;
        View            v;

        /* Initialize this first */
        inflator    = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v           = inflator.inflate(R.layout.custom_action_bar, null);

        actionBar = getSupportActionBar();

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        actionBar.setDisplayShowHomeEnabled(false);
        //actionBar.setDisplayShowTitleEnabled(false);

        // create new tabs and and set up the titles of the tabs
        studentTab  = actionBar.newTab();//setText("Student Profile");
        tutorTab    = actionBar.newTab();//setText("Tutor Profile");

        studentTab.setIcon(R.drawable.student_dark);
        tutorTab.setIcon(R.drawable.tutor_light);

        // bind the fragments to the tabs - set up tabListeners for each tab
        studentTab.setTabListener(new TabListener(studentProfileFragment, getApplicationContext(),
                "student", v));
        tutorTab.setTabListener(new TabListener(tutorProfileFragment, getApplicationContext(),
                "tutor", v));

        actionBar.addTab(studentTab);
        actionBar.addTab(tutorTab);

        //tutorTab.select();
        //studentTab.select();

        //actionBar.addTab(studentTab);

        actionBar.setDisplayShowCustomEnabled(true);


        actionBar.setCustomView(v);

        drawerLayoutButton = (ImageButton) actionBar.getCustomView().findViewById(R.id.left_action_bar_image);
        editActionBarButton = (ImageButton)actionBar.getCustomView().findViewById(R.id.edit_action_bar_icon);

        drawerLayoutButton.setOnClickListener(this);
        editActionBarButton.setOnClickListener(this);

    }

    private void setUpDrawerLayout()
    {
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        listView     = (ListView) findViewById(R.id.left_drawer);

        listViewTitles = getResources().getStringArray(R.array.drawable_list_items);
        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, listViewTitles));
        listView.setOnItemClickListener(new DrawerItemClickListener());
    }

    private void setUpOtherVariables()
    {
    }
}
