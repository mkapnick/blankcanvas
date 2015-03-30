package tutor.cesh.list;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import tutor.cesh.R;
import tutor.cesh.dialog.ProfileInfo;
import tutor.cesh.dialog.ProfileInfoBehavior;
import tutor.cesh.filter.TutorFilterActivity;
import tutor.cesh.format.TextFormatter;
import tutor.cesh.list.view.adapter.TutorListAdapter;
import tutor.cesh.list.view.adapter.TutorListViewItem;
import tutor.cesh.profile.activity.ReadOnlyTutorProfileActivity;
import tutor.cesh.profile.activity.StudentTutorProfileContainerActivity;
import tutor.cesh.rest.asynchronous.AsyncDownloader;
import tutor.cesh.rest.asynchronous.AsyncGet;
import tutor.cesh.rest.delegate.TaskDelegate;

public class TutorListActivity extends Activity implements  TaskDelegate,
                                                            SearchView.OnQueryTextListener,
                                                            BitmapCacheBehavior,
                                                            View.OnClickListener
{
    private ListView                        listView, drawerLayoutListView;
    private MenuItem                        searchItem;
    private SearchView                      searchView;
    protected DrawerLayout                  drawerLayout;
    private ActionBarDrawerToggle           actionBarDrawerToggle;
    private TextView                        actionBarProfileButton, resetTextView;
    private RelativeLayout                  relativeLayout, userProfileRelativeLayout;
    private ArrayList<TutorListViewItem>    tutorListViewItems;
    private Button                          filterButton;


    private TutorListAdapter                    tutorListAdapter;
    private TextView                            emptyTextView;
    private ArrayList<HashMap<String, String>>  data;
    private HashMap<String, Bitmap>             mapIDToBitmap;
    private String []                           listViewTitles;


    private static final String ALL_TUTORS    = "http://blankcanvas.pw/bc/tutors";
    public static final String ID             = "id"; // parent node
    public static final String FIRST_NAME     = "firstName";
    public static final String COVER_IMAGE    = "coverImage";
    public static final String RATE           = "rate";
    public static final String RATING         = "rating";
    public static final String MAJOR          = "major";
    public static final String MINOR          = "minor";
    public static final String YEAR           = "year";
    public static final String TUTOR_COURSES  = "tutorCourses";
    public static final String ABOUT          = "about";
    public static final String EMAIL          = "email";
    private static final int   REQUEST_CODE_FILTER   = 1;
    public Context context                    = this;

    @Override
    public void cache(String identifier, Bitmap bitmap)
    {
        mapIDToBitmap.put(identifier, bitmap);

        if(tutorListAdapter != null) //HACK RIGHT HERE IF I EVER SAW ONE!!! NEED THIS LINE OF CODE
                            //TO INITIALLY POPULATE THE LISTVIEW, BECAUSE THE MAPURLTOBITMPA
                            //DATA STRUCTURE WILL NOT BE FULLY SET IN TIME WHEN POPULATING
                            //THE LIST VIEW THE FIRST TIME, THIS GET FILTER TELLS THE LISTVIEW TO
                            //REPOPULATE ITSELF AFTER EVERY NEW ADDITION TO THE MAPURLTOBITMAP
                            //DATA STRUCTURE
        {
            tutorListAdapter.getFilter().filter("");
        }
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
            drawerLayoutListView.setItemChecked(position, true);

            switch(position)
            {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
            }
        }
    }

    /**
     *
     */
    private void initializeUI()
    {
        this.listView                   = (ListView) findViewById(R.id.tutor_list_activity_main_list_view);
        this.emptyTextView              = (TextView) findViewById(R.id.emptyTextView);
        this.resetTextView              = (TextView) findViewById(R.id.resetTextView);
        this.resetTextView.setOnClickListener(this);
        this.filterButton               = (Button)   findViewById(R.id.filterButton);
        this.filterButton.setOnClickListener(this);
        this.searchView                 = (SearchView) findViewById(R.id.action_search_icon);
        this.searchView.setOnQueryTextListener(this);

        this.actionBarProfileButton     = (TextView) findViewById(R.id.action_bar_profile_button);
        this.actionBarProfileButton.setOnClickListener(this);

        this.tutorListViewItems         = new ArrayList<TutorListViewItem>();
        this.data                       = new ArrayList<HashMap<String, String>>();
        this.mapIDToBitmap              = new HashMap<String, Bitmap>();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_CODE_FILTER && resultCode == RESULT_OK)
        {
            this.tutorListAdapter.applySpecificFilters(ProfileInfoBehavior.getFilterableMajor(),
                                                       ProfileInfoBehavior.getFilterableRate(),
                                                       ProfileInfoBehavior.getFilterableYear());

            Toast.makeText(this, "Filters applied", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onClick(View v)
    {
        Intent intent;

        switch(v.getId())
        {

            case R.id.action_bar_profile_button:

                if(drawerLayout.isDrawerOpen(drawerLayoutListView))
                {
                    drawerLayout.closeDrawer(drawerLayoutListView);

                    //call the correct class
                    intent = new Intent(getApplicationContext(),
                                        StudentTutorProfileContainerActivity.class);

                    startActivity(intent);
                }
                else
                {
                    drawerLayout.openDrawer(drawerLayoutListView);
                }
                break;

            case R.id.tutor_list_activity_relative_layout:
                intent = new Intent(getApplicationContext(), StudentTutorProfileContainerActivity.class);
                startActivity(intent);
                break;

            case R.id.filterButton:
                intent = new Intent(getApplicationContext(), TutorFilterActivity.class);
                startActivityForResult(intent,REQUEST_CODE_FILTER);
                break;

            case R.id.resetTextView:
                this.tutorListAdapter.resetFilters();
                Toast.makeText(this, "Filters reset", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_list);

        initializeUI();
        populateDataFromServer();

        setUpListViewItems();
        setUpDrawerLayout();
    }

    @Override
    public boolean onQueryTextChange(String s)
    {
        if(null != this.tutorListAdapter && this.tutorListAdapter.getData().size() > 0)
            tutorListAdapter.getFilter().filter(s);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s)
    {
        return true;
    }

    public void populateDataFromServer()
    {
        AsyncGet        asyncGet;
        HttpGet         httpGet;
        ProgressDialog  pd;

        pd = new ProgressDialog(this);
        pd.setTitle("Fetching available tutors...");
        pd.setMessage("Please wait");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();

        asyncGet    = new AsyncGet(this, this, pd);
        httpGet     = new HttpGet(ALL_TUTORS);

        asyncGet.execute(httpGet);
    }

    private void setUpListViewItems()
    {
        TutorListViewItem   item;
        String              text;
        Drawable            drawable;

        text        = "Settings";
        drawable    = getResources().getDrawable(android.R.drawable.ic_menu_preferences);
        item        = new TutorListViewItem(text, drawable);
        this.tutorListViewItems.add(item);

        text        = "Contact Us";
        drawable    = getResources().getDrawable(R.drawable.ic_action_send_now);
        item        = new TutorListViewItem(text, drawable);
        this.tutorListViewItems.add(item);

        text        = "Careers";
        item        = new TutorListViewItem(text, null);
        this.tutorListViewItems.add(item);

        text        = "Help";
        drawable    = getResources().getDrawable(R.drawable.ic_action_help);
        item        = new TutorListViewItem(text, drawable);
        this.tutorListViewItems.add(item);
    }

    /**
     * All of the tutor data is right here in one place
     *
     * @param response
     */
    @Override
    public void taskCompletionResult(Object response)
    {
        JSONArray               jsonArray, tutorCoursesJSONArray;
        JSONObject              jsonObject;
        HashMap<String, String> map;
        String                  courseNames;
        AsyncDownloader         asyncDownloader;

        jsonArray  = (JSONArray) response;

        if(jsonArray.length() == 0)
        {
            this.emptyTextView.setVisibility(View.VISIBLE); //set the textview to visible
            //getSupportActionBar().hide(); //hide the action bar
        }
        else
        {
            try
            {
                for(int i =0; i < jsonArray.length(); i++)
                {
                    map         = new HashMap<String, String>();
                    jsonObject  = jsonArray.getJSONObject(i);
                    courseNames = "";

                    /* info from the server api */
                    map.put(ID, jsonObject.getString("id"));
                    map.put(FIRST_NAME, TextFormatter.capitalizeAllFirstLetters(jsonObject.getString("firstName")));
                    map.put(RATE, jsonObject.getString("rate"));
                    map.put(RATING, jsonObject.getString("rating"));
                    map.put(MAJOR, jsonObject.getString("major"));
                    map.put(MINOR, jsonObject.getString("minor"));
                    map.put(YEAR, jsonObject.getString("year"));
                    map.put(ABOUT, jsonObject.getString("about"));
                    map.put(EMAIL, jsonObject.getString("email"));
                    map.put(COVER_IMAGE, jsonObject.getString("tutorCoverImageUrl"));

                    //get the courses associated with this tutor
                    tutorCoursesJSONArray   = jsonObject.getJSONArray("tutorCourses");
                    for(int j =0; j < tutorCoursesJSONArray.length(); j++)
                    {
                        if(j != tutorCoursesJSONArray.length() -1)
                            courseNames += tutorCoursesJSONArray.getString(j) + ", ";
                        else
                            courseNames += tutorCoursesJSONArray.getString(j);
                    }

                    //add the courses to the map
                    map.put(TUTOR_COURSES, courseNames);

                    //add the map to the list
                    this.data.add(map);

                    // download the bitmap and cache the results
                    asyncDownloader = new AsyncDownloader(map.get(ID), map.get(COVER_IMAGE), this);
                    asyncDownloader.execute();
                }
            }
            catch(JSONException e)
            {
                e.printStackTrace();
            }

            tutorListAdapter = new TutorListAdapter(this, this.getResources(), this.data, this.listView);
            tutorListAdapter.setCachedMap(this.mapIDToBitmap); //need to associate the cached map
                                                      // with this tutorListAdapter!!!
            this.listView.setAdapter(tutorListAdapter);
            this.listView.setOnItemClickListener(new ListViewOnClickListener()); //Click event for
                                                                                 //single list row
        }
    }

    @Override
    public void setProgressDialog(ProgressDialog pd)
    {
        //do nothing
    }

    private void setUpDrawerLayout()
    {
        ViewGroup header, footer;

        this.drawerLayout           = (DrawerLayout) findViewById(R.id.tutor_list_drawer_layout);
        drawerLayoutListView        = (ListView) findViewById(R.id.tutor_list_activity_right_drawer_list_view);
        header                      = (ViewGroup) getLayoutInflater().
                                        inflate(R.layout.header_drawerlayout_tutor_list_activity,
                                                this.drawerLayoutListView,
                                                false);
        footer                      = (ViewGroup) getLayoutInflater().
                                        inflate(R.layout.z_not_used_footer_drawerlayout_tutor_list_activity,
                                                this.drawerLayoutListView,
                                                false);

        //v                           = View.inflate(this, R.layout.header_drawerlayout_tutor_list_activity, null);
        userProfileRelativeLayout   = (RelativeLayout) header.findViewById(R.id.tutor_list_activity_relative_layout);
        userProfileRelativeLayout.setOnClickListener(this);
        drawerLayoutListView.addHeaderView(header, null, false);
        //drawerLayoutListView.addFooterView(footer, null, false);
        drawerLayoutListView.setAdapter(new TutorListDrawerLayoutAdapter(this, this.tutorListViewItems));
        drawerLayoutListView.setOnItemClickListener(new DrawerItemClickListener());

        //Set up animation for on slide of the drawer layout
        relativeLayout          = (RelativeLayout) findViewById(R.id.tutor_list_main_layout);
        actionBarDrawerToggle   = new ActionBarDrawerToggle
                                    (this, drawerLayout, null, R.string.app_name, R.string.app_name)
        {
            public void onDrawerClosed(View view)
            {
                //supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView)
            {
                //supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset)
            {
                super.onDrawerSlide(drawerView, slideOffset);
                relativeLayout.setTranslationX((slideOffset * drawerView.getWidth() * -1));
                drawerLayout.bringChildToFront(drawerView);
                drawerLayout.requestLayout();
            }
        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
    }


    /**
     *
     *
     *
     *
     *
     */
    private class ListViewOnClickListener implements AdapterView.OnItemClickListener
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            // Get the tutorListAdapter, get the data, get the hashmap associated with the position the user clicked,
            //start a new read only tutor list activity
            // @version by Michael Kapnick

            TutorListAdapter tutorListAdapter;
            ArrayList<HashMap<String, String>>  data;
            HashMap<String, String>             map;
            Intent                              intent;
            Bundle                              bundle;
            int                                 bytes;
            ByteBuffer                          byteBuffer;
            Bitmap                              coverImageBitmap;
            byte []                             byteArray;

            tutorListAdapter = (TutorListAdapter) parent.getAdapter();
            data                = tutorListAdapter.getData();
            map                 = data.get(position);
            bundle              = new Bundle();
            coverImageBitmap    = mapIDToBitmap.get(data.get(position).get(ID));

            /** Include info related to specific tutor clicked on */
            bundle.putString(ID, map.get(ID));
            bundle.putString(FIRST_NAME, map.get(FIRST_NAME));
            bundle.putString(RATE, map.get(RATE));
            bundle.putString(RATING, map.get(RATING));
            bundle.putString(MAJOR, map.get(MAJOR));
            bundle.putString(MINOR, map.get(MINOR));
            bundle.putString(YEAR, map.get(YEAR));
            bundle.putString(ABOUT, map.get(ABOUT));
            bundle.putString(EMAIL, map.get(EMAIL));
            bundle.putString(TUTOR_COURSES, map.get(TUTOR_COURSES));

            intent = new Intent(context, ReadOnlyTutorProfileActivity.class);
            intent.putExtras(bundle);

            StaticCurrentBitmapReadOnlyView.currentCoverImageBitmap = coverImageBitmap;

            startActivity(intent);
        }
    }
}
