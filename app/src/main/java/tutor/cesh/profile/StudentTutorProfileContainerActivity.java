package tutor.cesh.profile;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import tutor.cesh.R;
import tutor.cesh.Tutor;
import tutor.cesh.User;
import tutor.cesh.profile.fragment.FragmentTabBehavior;
import tutor.cesh.profile.fragment.FragmentTabController;
import tutor.cesh.profile.fragment.StudentProfileFragment;
import tutor.cesh.profile.fragment.TutorProfileFragment;

public class StudentTutorProfileContainerActivity extends FragmentActivity implements View.OnClickListener {

    protected DrawerLayout          drawerLayout;
    private FragmentTabBehavior     studentProfileFragment;
    private FragmentTabBehavior     tutorProfileFragment;
    private ImageButton             editActionBarButton;
    private ListView                listView;
    private String []               listViewTitles;
    private Toolbar                 actionBar;
    private FragmentTabController   fragmentController;
    //private ViewPager               viewPager;
    //private SlidingTabLayout        slidingTabLayout;

    public StudentTutorProfileContainerActivity() {
    }


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
            System.out.println ("inside here");
            listView.setItemChecked(position, true);
            switch(position)
            {
                case 0:
                    //update drawer layout
                    if(drawerLayout.isDrawerOpen(listView)){
                        drawerLayout.closeDrawer(listView);
                        //setUpActionBar();
                    }
                    else
                    {
                        drawerLayout.openDrawer(listView);
                        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                    }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //delegate power to the fragment controller
        this.fragmentController.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        List<FragmentTabBehavior>   tabs;

        this.studentProfileFragment = new StudentProfileFragment();
        this.tutorProfileFragment   = new TutorProfileFragment();
        this.fragmentController     = new FragmentTabController();
        tabs                        = new ArrayList<FragmentTabBehavior>();

        //set the list of tabs for the fragment controller
        tabs.add(this.studentProfileFragment);
        tabs.add(this.tutorProfileFragment);

        //add data to the fragment controller -- mutable object
        this.fragmentController.setStaticActivity(this); //must set the activity before anything!!!
        this.fragmentController.setSamplePagerAdapter(tabs);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_tutor_profile_container);

        //NEED THIS CODE FOR ADDING THE TABS
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.sample_content_fragment, this.fragmentController);
            transaction.commit();
        }

        //setToolBar();
        //setUpActionBar();
        setUpDrawerLayout();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.student_tutor_profile_container, menu);
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


        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View v)
    {
        Intent intent;
        Bundle bundle;

        switch(v.getId())
        {

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

    private void setToolBar()
    {
        //this.actionBar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        //setSupportActionBar(actionBar);

        /* Set an OnMenuItemClickListener to handle menu item clicks
        actionBar.setOnMenuItemClickListener(
                new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // Handle the menu item
                        return true;
                    }
                });

        // Inflate a menu to be displayed in the toolbar
        //actionBar.inflateMenu(R.menu.your_toolbar_menu);*/
    }
    /**
     *
     */

   /* private void setUpActionBar()
    {
        ActionBar.Tab   studentTab, tutorTab;
        LayoutInflater  inflator;
        View            v;

        inflator    = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v           = inflator.inflate(R.layout.custom_action_bar, null);

        //actionBar = getSupportActionBar(); --deprecated

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        actionBar.setDisplayShowHomeEnabled(false);
        //getActionBar().setDisplayHomeAsUpEnabled(true);

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
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(v);

        editActionBarButton = (ImageButton)actionBar.getCustomView().findViewById(R.id.edit_action_bar_icon);

        editActionBarButton.setOnClickListener(this);
    }*/

    private void setUpDrawerLayout()
    {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setStatusBarBackgroundColor(Color.BLACK);
        listView     = (ListView) findViewById(R.id.left_drawer);

        listViewTitles = getResources().getStringArray(R.array.drawable_list_items);
        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, listViewTitles));
        listView.setOnItemClickListener(new DrawerItemClickListener());
    }
}
