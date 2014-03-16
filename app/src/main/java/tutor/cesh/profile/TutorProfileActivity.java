package tutor.cesh.profile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;
import org.json.JSONObject;

import tutor.cesh.R;
import tutor.cesh.database.DatabaseTable;
import tutor.cesh.rest.AsyncDownloader;
import tutor.cesh.rest.AsyncGet;
import tutor.cesh.rest.RestClientFactory;

public class TutorProfileActivity extends Activity {

    private Bundle          info;
    private ImageView       profileImageView, coverImageView;
    private EditText        name, major, year, about, classes, rate;
    private static String   DOMAIN = "http://protected-earth-9689.herokuapp.com";
    private DrawerLayout    drawerLayout;
    private ListView        listView;
    private String []       listViewTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_profile);

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


        listViewTitles = getResources().getStringArray(R.array.drawable_list_items_tutor);
        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, listViewTitles));
        listView.setOnItemClickListener(new DrawerItemClickListener());

        info = getIntent().getExtras();
        info.putString("isRateSet", "false");
        /*if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }*/

        setUpUserInfo();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Bundle      savedInstanceState;
        JSONObject  json;

        System.out.println("In on activity for result");
        if (requestCode == 1)
        {
            if (resultCode == RESULT_OK)
            {
                savedInstanceState = data.getExtras();
                info.putString("firstName", savedInstanceState.getString("firstName"));
                info.putString("about", savedInstanceState.getString("about"));
                info.putString("year", savedInstanceState.getString("year"));
                info.putString("major", savedInstanceState.getString("major"));
                info.putString("rate", savedInstanceState.getString("rate"));
                info.putString("isRateSet", "true");

                try
                {
                    json       = setUpGet(DatabaseTable.USERS, info.getString("userId"));
                    info.putString("profileImage", DOMAIN + json.getString("profile_image_url"));
                    info.putString("coverImage", DOMAIN + json.getString("cover_image_url"));
                }
                catch(Exception e)
                {

                }
            }
        }
        setUpUserInfo();
    }

    @Override
    protected void onRestart()
    {
        setUpUserInfo();
    }

    /**
     * Set up an HttpGet request
     * @param table
     * @param id
     * @return
     */
    private JSONObject setUpGet(DatabaseTable table, String id)
    {
        JSONObject                                  obj1;
        HttpGet                                     get1;
        AsyncTask<HttpGet, Integer, JSONObject>     asyncGet1;

        obj1 = null;
        try
        {
            get1        = RestClientFactory.get(table, id);
            asyncGet1   = new AsyncGet().execute(get1);
            obj1        = asyncGet1.get();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return obj1;
    }
    /**
     *
     */
    private void setUpUserInfo()
    {
        AsyncTask<Void, Integer, Bitmap>    asyncDownloader;
        Drawable                            drawable;
        JSONObject                          json;

        try
        {
            System.out.println("here 1");
            name.setText(info.getString("firstName"), TextView.BufferType.EDITABLE);
            major.setText(info.getString("major"), TextView.BufferType.EDITABLE);
            year.setText(info.getString("year"), TextView.BufferType.EDITABLE);
            about.setText(info.getString("about"), TextView.BufferType.EDITABLE);

            if(info.getString("isRateSet").equalsIgnoreCase("false"))
            {
                json = setUpGet(DatabaseTable.TUTORS, info.getString("tutorId"));
                rate.setText(json.getString("rate"), TextView.BufferType.EDITABLE);
            }
            else
            {
                rate.setText(info.getString("rate"), TextView.BufferType.EDITABLE);
            }

            asyncDownloader = new AsyncDownloader(info.getString("profileImage"), null, null).execute();
            profileImageView.setImageBitmap(asyncDownloader.get());

            asyncDownloader = new AsyncDownloader(info.getString("coverImage"), null, null).execute();
            drawable = new BitmapDrawable(getResources(), asyncDownloader.get());
            coverImageView.setBackground(drawable);
        }
        catch(Exception e)
        {
            System.out.println("Exception!");
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tutor_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int position, id;

        id          = item.getItemId();
        position    = -1;

        if (id == R.id.action_settings)
            return true;
        else if(id == R.id.action_edit_student_profile)
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
            intent = new Intent(this, StudentProfileActivity.class);
            intent.putExtras(info);
            startActivity(intent);
            finish();
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
            System.out.println("on item click!");
            listView.setItemChecked(position, true);
            onOptionsItemSelected(position);
        }
    }

}
