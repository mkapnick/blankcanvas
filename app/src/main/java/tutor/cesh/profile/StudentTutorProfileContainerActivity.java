package tutor.cesh.profile;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import tutor.cesh.R;
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
    protected void onCreate(Bundle savedInstanceState) {

        GlobalDatabaseHelper    globalDatabaseHelper;

        globalDatabaseHelper        = new GlobalDatabaseHelper(this);
        this.studentProfileFragment = new StudentProfileFragment();
        this.tutorProfileFragment   = new TutorProfileFragment();

        globalDatabaseHelper.downloadStudentDataFromServer(studentProfileFragment);
        globalDatabaseHelper.downloadStudentCoverImageFromServer(getResources(),
                studentProfileFragment.coverImageView);

        globalDatabaseHelper.downloadTutorDataFromServer(tutorProfileFragment);
        globalDatabaseHelper.downloadTutorCoverImageFromServer(getResources(),
                tutorProfileFragment.coverImageView);

        super.onCreate(savedInstanceState);
        setUpActionBar(savedInstanceState);
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

    /**
     *
     */
    private void setUpActionBar(Bundle savedInstanceState)
    {
        ActionBar.Tab studentTab, tutorTab;


        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // create new tabs and and set up the titles of the tabs
        studentTab  = actionBar.newTab().setText("Student Profile");
        tutorTab    = actionBar.newTab().setText("Tutor Profile");

        // bind the fragments to the tabs - set up tabListeners for each tab
        studentTab.setTabListener(new TabListener(studentProfileFragment, getApplicationContext(),
                "Student Profile"));
        tutorTab.setTabListener(new TabListener(tutorProfileFragment, getApplicationContext(),
                "Tutor Profile"));

        actionBar.addTab(studentTab);
        actionBar.addTab(tutorTab);

        // restore to navigation
        /*if (savedInstanceState != null) {
            Toast.makeText(getApplicationContext(),
                    "tab is " + savedInstanceState.getInt(TAB_KEY_INDEX, 0),
                    Toast.LENGTH_SHORT).show();

            actionBar.setSelectedNavigationItem(savedInstanceState.getInt(
                    TAB_KEY_INDEX, 0));
        }*/

        //actionBar.addTab(tutorTab);
    }
}
