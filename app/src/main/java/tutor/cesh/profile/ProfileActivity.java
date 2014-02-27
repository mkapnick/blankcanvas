package tutor.cesh.profile;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import tutor.cesh.R;


public class ProfileActivity extends Activity
{
    private Bundle      info;
    public  ImageButton  ib;
    public  ImageView    iv;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        System.out.println("inside oncreate Profile activity");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);

        info = getIntent().getExtras();

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();

        }

        setUpTextAreas();

    }

    private void setUpTextAreas()
    {
        EditText name, major, year, about, subjects, classes;

        name        = (EditText)    findViewById(R.id.name);
        major       = (EditText)    findViewById(R.id.major);
        year        = (EditText)    findViewById(R.id.year);
        about       = (EditText)    findViewById(R.id.about);
        subjects    = (EditText)    findViewById(R.id.subjects);
        classes     = (EditText)    findViewById(R.id.classes);

       // Query web server and get user's information

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int     id;
        Intent  intent;

        id  = item.getItemId();


        if (id == R.id.action_settings)
        {
            return true;
        }

        if(id == R.id.action_edit_profile)
        {
            //start new intent and call editprofile
            System.out.println("before starting intent");
            intent = new Intent(this, EditProfileActivity.class);
            intent.putExtras(info);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
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
            View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

            return rootView;
        }
    }

}
