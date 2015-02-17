package tutor.cesh.list;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import tutor.cesh.R;
import tutor.cesh.profile.ReadOnlyTutorProfileActivity;
import tutor.cesh.rest.asynchronous.AsyncDownloader;
import tutor.cesh.rest.asynchronous.AsyncGet;
import tutor.cesh.rest.delegate.TaskDelegate;

public class TutorListActivity extends ActionBarActivity implements TaskDelegate,
                                                                    SearchView.OnQueryTextListener,
                                                                    BitmapCacheBehavior

{

    private ListView    listView;
    private MenuItem    searchItem;
    private SearchView  searchView;

    private JSONAdapter                         adapter;
    private ArrayList<HashMap<String, String>>  data;
    private HashMap<String, Bitmap>             mapUrlToBitmap;

    private static final String ALL_TUTORS    = "http://blankcanvas.pw/tutors/view/all";
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
    public Context context                    = this;

    @Override
    public void cache(String identifier, Bitmap bitmap)
    {
        mapUrlToBitmap.put(identifier, bitmap);

        if(adapter != null) //HACK RIGHT HERE IF I EVER SAW ONE!!! NEED THIS LINE OF CODE
                            //TO INITIALLY POPULATE THE LISTVIEW, BECAUSE THE MAPURLTOBITMPA
                            //DATA STRUCTURE WILL NOT BE FULLY SET IN TIME WHEN POPULATING
                            //THE LIST VIEW THE FIRST TIME, THIS GET FILTER TELLS THE LISTVIEW TO
                            //REPOPULATE ITSELF AFTER EVERY NEW ADDITION TO THE MAPURLTOBITMAP
                            //DATA STRUCTURE
        {
            adapter.getFilter().filter("");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_list);

        listView            = (ListView)    findViewById(R.id.tutor_list_view);
        data                = new ArrayList<HashMap<String, String>>();
        mapUrlToBitmap      = new HashMap<String, Bitmap>();

        populateDataFromServer();
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable( R.drawable.action_bar_background));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater;

        inflater = getMenuInflater();
        inflater.inflate(R.menu.tutor_list_view_menu, menu);

        this.searchItem = menu.findItem(R.id.action_search);
        this.searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        this.searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextChange(String s)
    {
        adapter.getFilter().filter(s);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s)
    {
        return true;
    }

    public void populateDataFromServer()
    {
        AsyncGet                asyncGet;
        HttpGet                 httpGet;

        asyncGet                = new AsyncGet(this, this, new ProgressDialog(this));
        httpGet                 = new HttpGet(ALL_TUTORS);

        asyncGet.execute(httpGet);
    }

    @Override
    public void taskCompletionResult(Object response)
    {
        JSONArray               jsonArray, tutorCoursesJSONArray;
        JSONObject              jsonObjectTmp, jsonObject;
        HashMap<String, String> map;
        String                  courseNames;
        AsyncDownloader         asyncDownloader;

        jsonObjectTmp   = (JSONObject) response;

        //all of the tutor data is right here in one place
        try
        {
            jsonArray   = new JSONArray(jsonObjectTmp.getString("all_tutors"));

            for(int i =0; i < jsonArray.length(); i++)
            {
                map         = new HashMap<String, String>();
                jsonObject  = jsonArray.getJSONObject(i);
                courseNames = "";


                /* info from the server api */
                //map.put(ID, jsonObject.getString("id")); TODO -- GET ID DOWN FROM THE SERVER!!
                map.put(FIRST_NAME, jsonObject.getString("first_name"));
                map.put(COVER_IMAGE, jsonObject.getString("tutor_cover_image_url"));
                map.put(RATE, jsonObject.getString("rate"));
                map.put(RATING, jsonObject.getString("rating"));
                map.put(MAJOR, jsonObject.getString("major"));
                map.put(MINOR, jsonObject.getString("minor"));
                map.put(YEAR, jsonObject.getString("year"));
                map.put(ABOUT, jsonObject.getString("about"));

                //get the courses associated with this tutor
                tutorCoursesJSONArray   = jsonObject.getJSONArray("tutor_courses");
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
                asyncDownloader = new AsyncDownloader(map.get(COVER_IMAGE), this);
                asyncDownloader.execute();
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        adapter =  new JSONAdapter(this, this.getResources(), this.data, this.listView);
        this.listView.setAdapter(adapter);

        // Click event for single list row
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                // Get the adapter, get the data, get the hashmap associated with the position the user clicked,
                //start a new read only tutor list activity
                // @version by Michael Kapnick

                JSONAdapter                         adapter;
                ArrayList<HashMap<String, String>>  data;
                HashMap<String, String>             map;
                Intent                              intent;
                Bundle                              bundle;
                int                                 bytes;
                ByteBuffer                          byteBuffer;
                Bitmap                              coverImageBitmap;
                byte []                             byteArray;

                adapter             = (JSONAdapter) parent.getAdapter();
                data                = adapter.getData();
                map                 = data.get(position);
                bundle              = new Bundle();
                coverImageBitmap    = mapUrlToBitmap.get(data.get(position).get(COVER_IMAGE));

                /** Include info related to specific tutor clicked on */
                bundle.putString(ID, map.get(ID));
                bundle.putString(FIRST_NAME, map.get(FIRST_NAME));
                bundle.putString(RATE, map.get(RATE));
                bundle.putString(RATING, map.get(RATING));
                bundle.putString(MAJOR, map.get(MAJOR));
                bundle.putString(MINOR, map.get(MINOR));
                bundle.putString(YEAR, map.get(YEAR));
                bundle.putString(ABOUT, map.get(ABOUT));
                bundle.putString(TUTOR_COURSES, map.get(TUTOR_COURSES));

                intent = new Intent(context, ReadOnlyTutorProfileActivity.class);
                intent.putExtras(bundle);

                StaticCurrentBitmapReadOnlyView.currentCoverImageBitmap = coverImageBitmap;

                startActivity(intent);
            }
        });

        //need to associate the cached map with this adapter!!!
        adapter.setCachedMap(this.mapUrlToBitmap);
    }

    @Override
    public void setProgressDialog(ProgressDialog pd) {

    }
}
