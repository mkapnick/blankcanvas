package tutor.cesh.profile;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONObject;

import tutor.cesh.R;
import tutor.cesh.database.DatabaseTable;
import tutor.cesh.rest.AsyncDownloader;
import tutor.cesh.rest.RestClientFactory;
import tutor.cesh.rest.AsyncGet;


import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.TextView;



public class StudentProfileActivity extends Activity
{
    private Bundle          info;
    private ImageView       profileImageView, coverImageView;
    private EditText        name, major, year, about, classes;
    private DrawerLayout    drawerLayout;
    private ListView        listView;
    private static String   DOMAIN = "http://protected-earth-9689.herokuapp.com";
    private String []       listViewTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        name                = (EditText)    findViewById(R.id.name);
        major               = (EditText)    findViewById(R.id.major);
        year                = (EditText)    findViewById(R.id.year);
        about               = (EditText)    findViewById(R.id.about);
        classes             = (EditText)    findViewById(R.id.classes);
        profileImageView    = (ImageView)   findViewById(R.id.profileImage);
        coverImageView      = (ImageView)   findViewById(R.id.profileBackgroundImage);
        drawerLayout        = (DrawerLayout)findViewById(R.id.drawer_layout);
        listView            = (ListView)    findViewById(R.id.left_drawer);

        listViewTitles = getResources().getStringArray(R.array.drawable_list_items);
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
    protected void onRestart()
    {
        setUpUserInfo();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Bundle      savedInstanceState;
        JSONObject  json;

        if (requestCode == 1)
        {
            if (resultCode == RESULT_OK)
            {
                savedInstanceState = data.getExtras();
                info.putString("firstName", savedInstanceState.getString("firstName"));
                info.putString("about", savedInstanceState.getString("about"));
                info.putString("year", savedInstanceState.getString("year"));
                info.putString("major", savedInstanceState.getString("major"));
                info.putString("isRateSet", "true");

                try
                {
                    json       = setUpGet(DatabaseTable.USERS, info.getString("userId"));
                    info.putString("profileImage", DOMAIN + json.getString("profile_image_url"));
                    info.putString("coverImage", DOMAIN + json.getString("cover_image_url"));
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        setUpUserInfo();
    }


    /**
     * Helper method to set up an HttpGet request
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
        AsyncTask<Void, Integer, Bitmap>            asyncDownloader;
        JSONObject                                  json1, json2;
        Drawable                                    drawable;

        try
        {
            if(info.getString("isRateSet").equalsIgnoreCase("false"))
            {
                json1 = setUpGet(DatabaseTable.USERS, info.getString("id"));
                json2 = setUpGet(DatabaseTable.ENROLLS, info.getString("enrollId"));

                name.setText(json1.getString("first_name"), TextView.BufferType.EDITABLE);
                major.setText(json2.getString("major"), TextView.BufferType.EDITABLE);
                year.setText(json2.getString("year"), TextView.BufferType.EDITABLE);
                about.setText(json1.getString("about"), TextView.BufferType.EDITABLE);

                asyncDownloader = new AsyncDownloader(DOMAIN + json1.getString("profile_image_url"), null, null).execute();
                profileImageView.setImageBitmap(asyncDownloader.get());

                asyncDownloader = new AsyncDownloader(DOMAIN + json1.getString("cover_image_url"), null, null).execute();
                drawable = new BitmapDrawable(getResources(), asyncDownloader.get());
                coverImageView.setBackground(drawable);

                info.putString("firstName", name.getText().toString());
                info.putString("about", about.getText().toString());
                info.putString("year", year.getText().toString());
                info.putString("major", major.getText().toString());
                info.putString("profileImage", DOMAIN + json1.getString("profile_image_url"));
                info.putString("coverImage", DOMAIN + json1.getString("cover_image_url"));
            }
            else
            {
                name.setText(info.getString("firstName"), TextView.BufferType.EDITABLE);
                major.setText(info.getString("major"), TextView.BufferType.EDITABLE);
                year.setText(info.getString("year"), TextView.BufferType.EDITABLE);
                about.setText(info.getString("about"), TextView.BufferType.EDITABLE);

                asyncDownloader = new AsyncDownloader(info.getString("profileImage"), null, null).execute();
                profileImageView.setImageBitmap(asyncDownloader.get());

                asyncDownloader = new AsyncDownloader(info.getString("coverImage"), null, null).execute();
                drawable = new BitmapDrawable(getResources(), asyncDownloader.get());
                coverImageView.setBackground(drawable);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
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
            intent = new Intent(this, TutorProfileActivity.class);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_student_profile, container, false);

            return rootView;
        }
    }

}
