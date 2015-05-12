package tutor.cesh.list;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import tutor.cesh.R;
import tutor.cesh.administrative.SettingsActivity;
import tutor.cesh.dialog.ProfileInfoBehavior;
import tutor.cesh.filter.TutorFilterActivity;
import tutor.cesh.format.TextFormatter;
import tutor.cesh.list.view.adapter.EmptyTextViewAdapter;
import tutor.cesh.list.view.adapter.TutorListAdapter;
import tutor.cesh.list.view.adapter.TutorListViewItem;
import tutor.cesh.profile.Student;
import tutor.cesh.profile.User;
import tutor.cesh.profile.activity.ReadOnlyTutorProfileActivity;
import tutor.cesh.profile.activity.StudentTutorProfileContainerActivity;
import tutor.cesh.apisecurity.APIEndpoints;
import tutor.cesh.rest.asynchronous.AsyncDownloader;
import tutor.cesh.rest.asynchronous.AsyncGet;
import tutor.cesh.rest.delegate.TaskDelegate;

public class TutorListActivity extends Activity implements  TaskDelegate,
                                                            SearchView.OnQueryTextListener,
                                                            BitmapCacheBehavior,
                                                            View.OnClickListener,
                                                            SwipeRefreshLayout.OnRefreshListener
{
    private ListView                        listView, drawerLayoutListView;
    private SearchView                      searchView;
    protected DrawerLayout                  drawerLayout;
    private ActionBarDrawerToggle           actionBarDrawerToggle;
    private TextView                        actionBarProfileButton;
    private RelativeLayout                  relativeLayout, userProfileRelativeLayout;
    private ArrayList<TutorListViewItem>    tutorListViewItems;
    private TextView                        filterButton;
    private SwipeRefreshLayout              swipeRefreshLayout;


    private TutorListAdapter                    tutorListAdapter;
    //private TextView                            emptyTextView;
    private ArrayList<HashMap<String, String>>  data;
    private HashMap<String, Bitmap>             mapIDToBitmap;


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
                    intent = new Intent(getApplicationContext(), SettingsActivity.class);
                    startActivity(intent);
                    break;
                case 2:
                    setUpEmail();
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
        //this.emptyTextView              = (TextView) findViewById(R.id.emptyTextView);
        this.filterButton               = (TextView)   findViewById(R.id.filterButton);
        this.filterButton.setOnClickListener(this);
        this.swipeRefreshLayout         = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        this.swipeRefreshLayout.setOnRefreshListener(this);
        this.swipeRefreshLayout.setColorScheme(R.color.app_light_light_grey,
                R.color.app_light_light_grey,
                R.color.app_light_light_grey,
                R.color.app_light_light_grey);
        this.searchView                 = (SearchView) findViewById(R.id.action_search_icon);
        this.searchView.setOnQueryTextListener(this);
        this.searchView.setQueryHint("Search by keywords...");

        this.actionBarProfileButton     = (TextView) findViewById(R.id.action_bar_profile_button);
        this.actionBarProfileButton.setOnClickListener(this);

        this.tutorListViewItems         = new ArrayList<TutorListViewItem>();
        this.mapIDToBitmap              = new HashMap<String, Bitmap>();

        //Set on scroll listener for listview, to determine when to invoke the refresh of the
        //list
        this.listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                boolean enable, firstItemVisible, topOfFirstItemVisible;

                enable = false;

                /**
                 * This enables us to force the layout to refresh only when the first item
                 * of the list is visible.
                 **/
                if(listView != null && listView.getChildCount() > 0)
                {
                    // check if the first item of the list is visible
                    firstItemVisible = listView.getFirstVisiblePosition() == 0;
                    // check if the top of the first item is visible
                    topOfFirstItemVisible = listView.getChildAt(0).getTop() == 0;
                    // enabling or disabling the refresh layout
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                swipeRefreshLayout.setEnabled(enable);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_CODE_FILTER && resultCode == RESULT_OK)
        {
            if(this.data.size() > 0)
            {
                this.tutorListAdapter.applyAdvancedFilters(ProfileInfoBehavior.getFilterableMajor(),
                                                           ProfileInfoBehavior.getFilterableRate(),
                                                           ProfileInfoBehavior.getFilterableYear());
            }

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
                drawerLayout.closeDrawer(drawerLayoutListView);
                intent = new Intent(getApplicationContext(), StudentTutorProfileContainerActivity.class);
                startActivity(intent);
                break;

            case R.id.filterButton:
                intent = new Intent(getApplicationContext(), TutorFilterActivity.class);
                startActivityForResult(intent, REQUEST_CODE_FILTER);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_list);

        initializeUI();
        populateDataFromServer(true);

        setUpListViewItems();
        setUpDrawerLayout();
    }

    @Override
    public boolean onQueryTextChange(String s)
    {
        if(null != this.tutorListAdapter)
        {
            if(s.length() > 0)
                tutorListAdapter.getFilter().filter(s);
            else
                tutorListAdapter.resetFilters();
        }

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s)
    {
        return true;
    }

    @Override
    public void onRefresh()
    {
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run() {
                populateDataFromServer(false);
            }
        }, 3000);
    }

    public void populateDataFromServer(boolean showProgressDialog)
    {
        AsyncGet        asyncGet;
        HttpGet         httpGet;
        ProgressDialog  pd;
        User            user;
        Student         student;
        String          mySchoolId;

        pd          = null;
        user        = User.getInstance(this);
        student     = user.getStudent();
        mySchoolId  = student.getSchoolId();

        if(showProgressDialog)
        {
            pd = new ProgressDialog(this);
            pd.setTitle("Fetching available tutors...");
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }

        asyncGet    = new AsyncGet(this, this, pd);
        httpGet     = new HttpGet(APIEndpoints.getTUTORS_SCHOOL_ENDPOINT() + "/" + mySchoolId + "/tutors");

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

        /*text        = "Careers";
        item        = new TutorListViewItem(text, null);
        this.tutorListViewItems.add(item);

        text        = "Help";
        drawable    = getResources().getDrawable(R.drawable.ic_action_help);
        item        = new TutorListViewItem(text, drawable);
        this.tutorListViewItems.add(item);*/
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
        this.data  = new ArrayList<HashMap<String, String>>(); //need to re initialize the data here

        if(jsonArray.length() == 0)
        {
            //this.emptyTextView.setVisibility(View.VISIBLE); //set the textview to visible
            this.listView.setAdapter(new EmptyTextViewAdapter(this));
        }
        else
        {
            //this.emptyTextView.setVisibility(View.GONE); //set the textview to gone

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

            this.tutorListAdapter = new TutorListAdapter(this, this.getResources(),
                                                         this.data, this.listView);


            this.tutorListAdapter.setCachedMap(this.mapIDToBitmap); //need to associate the cached map
                                                                    // with this tutorListAdapter!!!

            //reset filters at this point (in case of refresh and filters are on, we
            //want to reset the filters
            ProfileInfoBehavior.FILTERABLE.setMajor("");
            ProfileInfoBehavior.FILTERABLE.setRate("");
            ProfileInfoBehavior.FILTERABLE.setYear("");


            this.listView.setAdapter(tutorListAdapter);
            this.listView.setOnItemClickListener(new ListViewOnClickListener()); //Click event for
                                                                                 //single list row
        }

        if(swipeRefreshLayout.isRefreshing())
        {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void setProgressDialog(ProgressDialog pd)
    {
        //do nothing
    }

    private void setUpDrawerLayout()
    {
        ViewGroup header;

        this.drawerLayout           = (DrawerLayout) findViewById(R.id.tutor_list_drawer_layout);
        drawerLayoutListView        = (ListView) findViewById(R.id.tutor_list_activity_right_drawer_list_view);
        header                      = (ViewGroup) getLayoutInflater().
                                        inflate(R.layout.header_drawerlayout_tutor_list_activity,
                                                this.drawerLayoutListView,
                                                false);


        userProfileRelativeLayout   = (RelativeLayout) header.findViewById(R.id.tutor_list_activity_relative_layout);
        userProfileRelativeLayout.setOnClickListener(this);
        drawerLayoutListView.addHeaderView(header, null, false);
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
     */
    private void setUpEmail()
    {
        Intent emailIntent;

        emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"blankcanvasteam@gmail.com"});
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
        emailIntent.setType("message/rfc822");

        startActivity(Intent.createChooser(emailIntent, "Please select an email application"));
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
            Bitmap                              coverImageBitmap;

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
