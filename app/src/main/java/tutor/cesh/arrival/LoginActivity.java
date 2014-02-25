package tutor.cesh.arrival;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.NetworkOnMainThreadException;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

import tutor.cesh.database.DatabaseFacility;
import tutor.cesh.database.RestClientFactory;
import tutor.cesh.rest.AsyncAuthenticate;
import tutor.cesh.R;
import tutor.cesh.profile.ProfileActivity;

public class LoginActivity extends ActionBarActivity implements Arrival {

    private String              email;
    private String              password;
    private DatabaseFacility databaseFacility;
    private SQLiteDatabase      database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
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

    /**
     * User needs to set up an account, pass responsibility
     * over to NewAccountActivity class
     * @param view The view from the UI
     */
    public void signUp(View view)
    {
        System.out.println("IN SIGNUP!");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------\n" +
                "-----------------------------------------------------------------------------------------------------------------------------------------\n" +
                "-----------------------------------------------------------------------------------------------------------------------------------------\n" +
                "-----------------------------------------------------------------------------------------------------------------------------------------\n" +
                "-----------------------------------------------------------------------------------------------------------------------------------------\n" +
                "-----------------------------------------------------------------------------------------------------------------------------------------\n" +
                "-----------------------------------------------------------------------------------------------------------------------------------------\n" +
                "-----------------------------------------------------------------------------------------------------------------------------------------\n" +
                "-----------------------------------------------------------------------------------------------------------------------------------------\n" +
                "-----------------------------------------------------------------------------------------------------------------------------------------\n" +
                "-----------------------------------------------------------------------------------------------------------------------------------------\n" +
                "-----------------------------------------------------------------------------------------------------------------------------------------\n" +
                "-----------------------------------------------------------------------------------------------------------------------------------------\n" +
                "-----------------------------------------------------------------------------------------------------------------------------------------\n");


        Intent intent;
        intent = new Intent(this, NewAccountActivity.class);
        startActivity(intent);

    }

    /**
     * Validate the email and password of user
     * @param view The view from the UI
     */
    @Override
    public void validateArrival(View view)
    {
        //Validate with tutor.app.database query

        System.out.println("-- In validateArrival() --");

        this.email      = ((EditText) findViewById(R.id.emailTextView)).getText().toString();
        this.password   = ((EditText) findViewById(R.id.passwordTextView)).getText().toString();

        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------\n" +
                "-----------------------------------------------------------------------------------------------------------------------------------------\n" +
                "-----------------------------------------------------------------------------------------------------------------------------------------\n" +
                "-----------------------------------------------------------------------------------------------------------------------------------------\n" +
                "-----------------------------------------------------------------------------------------------------------------------------------------\n" +
                "-----------------------------------------------------------------------------------------------------------------------------------------\n" +
                "-----------------------------------------------------------------------------------------------------------------------------------------\n" +
                "-----------------------------------------------------------------------------------------------------------------------------------------\n" +
                "-----------------------------------------------------------------------------------------------------------------------------------------\n" +
                "-----------------------------------------------------------------------------------------------------------------------------------------\n" +
                "-----------------------------------------------------------------------------------------------------------------------------------------\n" +
                "-----------------------------------------------------------------------------------------------------------------------------------------\n" +
                "-----------------------------------------------------------------------------------------------------------------------------------------\n" +
                "-----------------------------------------------------------------------------------------------------------------------------------------\n");

        System.out.println("Email entered is: " + this.email);
        System.out.println("Password entered is: " + this.password);

        validate();

    }

    /**
     * Validates the user email and password
     * by querying the tutor.app.database as necessary
     */
    private void validate()
    {
        Log.d("", "In validate in LoginActivity");

        HttpGet                                             get;
        AsyncTask<HttpGet, Integer, JSONArray>              async;
        JSONArray                                           jsonArray;
        Intent                                              intent;
        JSONObject                                          object;

        try
        {

            System.out.println("authenticating...");
            get         = RestClientFactory.authenticateUser(email, password);
            async       = new AsyncAuthenticate().execute(get);
            jsonArray   = async.get();

            if(jsonArray != null)
            {
                object      = jsonArray.getJSONObject(0);
                if(object.getString("confirm").equalsIgnoreCase("true"))
                {
                    //user has access to the application !
                    System.out.println("user has access to app!");
                    intent  = new Intent(this, ProfileActivity.class);
                    intent.putExtra("id", object.getString("id"));
                    intent.putExtra("enrollId", object.getString("enroll_id"));
                    intent.putExtra("email", object.getString("email"));
                    intent.putExtra("firstName", object.getString("first_name"));
                    intent.putExtra("lastName", object.getString("last_name"));
                    intent.putExtra("about", object.getString("about"));
                    intent.putExtra("profileImage", object.getString("profile_image"));
                    intent.putExtra("coverImage", object.getString("cover_image"));

                    System.out.println("before starting intent");
                    startActivity(intent);
                }
            }
            else
                finish();

        }
        catch(NetworkOnMainThreadException e)
        {
            System.out.println("Network on main thread exception");
        }
        catch (InterruptedException e)
        {
            System.out.println("Interrupted exception");
        }
        catch (ExecutionException e)
        {
            System.out.println("Execution exception");
        }
        catch (JSONException e)
        {
            System.out.println("JSON exception");
        }
        catch(IOException ioe)
        {
            System.out.println("IOEXc");
        }
        catch(NoSuchAlgorithmException nse)
        {
            System.out.println("NSALExc");
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
            View rootView = inflater.inflate(R.layout.fragment_login, container, false);
            return rootView;
        }
    }

}
