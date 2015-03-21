package tutor.cesh.profile;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import tutor.cesh.R;
import tutor.cesh.list.TutorListActivity;
import tutor.cesh.profile.fragment.FragmentTabController;
import tutor.cesh.profile.fragment.StudentProfileFragment;
import tutor.cesh.profile.fragment.TutorProfileFragment;
import tutor.cesh.profile.fragment.observer.TabObserver;
import tutor.cesh.session.SessionManager;

public class StudentTutorProfileContainerActivity extends FragmentActivity
{
    protected DrawerLayout          drawerLayout;
    private   TabObserver           studentProfileFragment;
    private   TabObserver           tutorProfileFragment;
    private ListView                listView;
    private String []               listViewTitles;
    private FragmentTabController   tabController;
    private ActionBarDrawerToggle   actionBarDrawerToggle;
    private LinearLayout            mainLayout;
    private SessionManager          sessionManager;

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
                    {
                        drawerLayout.closeDrawer(listView);

                        //call the correct class
                        intent = new Intent(getApplicationContext(), TutorListActivity.class);
                        startActivity(intent);
                        //finish();
                    }
                    else
                    {
                        drawerLayout.openDrawer(listView);
                    }

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
        this.tabController.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setUpTabs(savedInstanceState);
        //setUpDrawerLayout();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.student_tutor_profile_container, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
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


    private void setUpDrawerLayout()
    {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setStatusBarBackgroundColor(Color.GREEN);
        listView     = (ListView) findViewById(R.id.left_drawer);

        listViewTitles = getResources().getStringArray(R.array.drawable_list_items);
        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.z_not_used_drawer_list_item, listViewTitles));
        listView.setOnItemClickListener(new DrawerItemClickListener());

        //Set up animation for on slide of the drawer layout
        mainLayout = (LinearLayout) findViewById(R.id.main_layout);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, null, R.string.app_name, R.string.app_name) {
            public void onDrawerClosed(View view) {
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                mainLayout.setTranslationX((slideOffset * drawerView.getWidth()));
                drawerLayout.bringChildToFront(drawerView);
                drawerLayout.requestLayout();
            }
        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //set the drawer layout/listview in the fragment tab controller
        //this.tabController.setDrawerLayout(this.drawerLayout);
        //this.tabController.setDrawerLayoutListView(this.listView);
    }

    private void setUpTabs(Bundle savedInstanceState)
    {
        List<TabObserver>   tabs;

        //instantiate all tab observers, and the tab subject
        this.studentProfileFragment = new StudentProfileFragment();
        this.tutorProfileFragment   = new TutorProfileFragment();
        this.tabController          = new FragmentTabController();

        //assign observer's to their subject
        this.studentProfileFragment.setTabSubject(tabController);
        this.tutorProfileFragment.setTabSubject(tabController);

        tabs                        = new ArrayList<TabObserver>();

        //set the list of tabs for the fragment controller
        tabs.add(this.studentProfileFragment);
        tabs.add(this.tutorProfileFragment);

        //add data to the fragment controller -- mutable object
        this.tabController.setStaticActivity(this); //must set the activity before anything!!!
        this.tabController.setSamplePagerAdapter(tabs); //look in pager adapter instantiate item,
        //to see how data gets populated in fragments

        setContentView(R.layout.activity_student_tutor_profile_container);

        //NEED THIS CODE FOR ADDING THE TABS
        if (savedInstanceState == null)
        {
            FragmentTransaction transaction;
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.sample_content_fragment, this.tabController);
            transaction.commit();
        }
    }
}
