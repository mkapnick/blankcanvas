package tutor.cesh.list;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import tutor.cesh.R;
import tutor.cesh.profile.ReadOnlyTutorProfileActivity;
import tutor.cesh.rest.AsyncGet;
import tutor.cesh.rest.TaskDelegate;

public class TutorListActivity extends Activity implements TaskDelegate {

    private ListView listView;
    private JSONAdapter adapter;
    private ArrayList<HashMap<String, String>> data;
    private final String ALL_TUTORS     = "http://blankcanvas.pw/tutors/view/all";

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_list);

        listView            = (ListView)    findViewById(R.id.tutor_list_view);
        data                = new ArrayList<HashMap<String, String>>();
        populateDataFromServer();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tutor_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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

        jsonObjectTmp   = (JSONObject) response;
        courseNames     = "";

        try
        {
            jsonArray   = new JSONArray(jsonObjectTmp.getString("all_tutors"));
            for(int i =0; i < jsonArray.length(); i++)
            {
                map         = new HashMap<String, String>();
                jsonObject  = jsonArray.getJSONObject(i);

                /* info from the server api */
                map.put(FIRST_NAME, jsonObject.getString("first_name"));
                map.put(COVER_IMAGE, jsonObject.getString("tutor_cover_image_url"));
                map.put(RATE, jsonObject.getString("rate"));
                map.put(RATING, jsonObject.getString("rating"));
                map.put(MAJOR, jsonObject.getString("major"));
                map.put(MINOR, jsonObject.getString("minor"));
                map.put(YEAR, jsonObject.getString("year"));
                map.put(ABOUT, jsonObject.getString("about"));

                tutorCoursesJSONArray   = jsonObject.getJSONArray("tutor_courses");

                for(int j =0; j < tutorCoursesJSONArray.length(); j++)
                {
                    if(j != tutorCoursesJSONArray.length() -1)
                        courseNames += tutorCoursesJSONArray.getString(i) + ", ";
                    else
                        courseNames += tutorCoursesJSONArray.getString(i);
                }

                map.put(TUTOR_COURSES, courseNames);
                this.data.add(map);
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        adapter =  new JSONAdapter(this, this.getResources(), this.data);
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

                adapter = (JSONAdapter) parent.getAdapter();
                data    = adapter.getData();
                map     = data.get(position);
                bundle  = new Bundle();

                /** Include info related to specific tutor clicked on */
                bundle.putString(ID, map.get(ID));
                bundle.putString(FIRST_NAME, map.get(FIRST_NAME));
                bundle.putString(COVER_IMAGE, map.get(COVER_IMAGE));
                bundle.putString(RATE, map.get(RATE));
                bundle.putString(RATING, map.get(RATING));
                bundle.putString(MAJOR, map.get(MAJOR));
                bundle.putString(MINOR, map.get(MINOR));
                bundle.putString(YEAR, map.get(YEAR));
                bundle.putString(ABOUT, map.get(ABOUT));
                bundle.putString(TUTOR_COURSES, map.get(TUTOR_COURSES));

                intent = new Intent(context, ReadOnlyTutorProfileActivity.class);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });
    }

    @Override
    public void setProgressDialog(ProgressDialog pd) {

    }


}
