package tutor.cesh;

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
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

import database.DatabaseFacility;
import database.RESTClientFactory;
import tutor.profile.ProfileActivity;

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
        //Validate with database query

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
     * by querying the database as necessary
     */
    private void validate()
    {
        /*databaseFacility    = new DatabaseFacility(getApplicationContext());
        database            = databaseFacility.getReadableDatabase();
        databaseFacility.setDatabase(database);


        /* Writing to the Database
        //database            = databaseFacility.getWritableDatabase();
        //databaseFacility.insertUserRecord(this.email, this.password, "TEST RUN", "TEST RUN", "TEST RUN");
        //databaseFacility.setDatabase(database);

        if(!databaseFacility.validateUser(this.email, this.password))
        {
            Toast.makeText(this, "Could not verify login", Toast.LENGTH_LONG).show();
            System.out.println("YOUR CREDENTIALS DO NOT MATCH UP WITH OUR DATABASE!");
        }
        else
        {
            Toast.makeText(this, "Login verified", Toast.LENGTH_LONG).show();
            System.out.println("GOOD JOB, YOU HAVE BEEN VALIDATED IN THE DB!");
        }*/

        Log.d("", "In validate in LoginActivity");

        HttpGet                                 get;
        RESTClientGet                           rcg;
        AsyncTask<HttpGet, Integer, JSONArray>  async;
        JSONArray                               json;
        Intent                                  intent;
        JSONObject                              obj;

        rcg = null;
        try
        {
            get     = RESTClientFactory.get(this.email, this.password);
            async   = new RESTClientGet().execute(get);
            json    = async.get();

            if(checkAndValidateResponse(json))
            {
                //user has access to the application !
                intent  = new Intent(this, ProfileActivity.class);
                obj     = json.getJSONObject(0);
                intent.putExtra("firstName", obj.getString("first_name"));
                intent.putExtra("lastName", obj.getString("last_name"));
                intent.putExtra("about", obj.getString("about"));
                intent.putExtra("profPic", obj.getString("prof_pic"));
                intent.putExtra("coverPic", obj.getString("cover_pic"));
                intent.putExtra("email", obj.getString("email"));

                startActivity(intent);
            }

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
    }

    private boolean checkAndValidateResponse(JSONArray json) throws JSONException
    {
        boolean         validated;
        validated       = false;

        if(json.length() > 0)
        {
            try
            {
                System.out.println("length > 0");

                JSONObject      obj;
                String          confirm;
                String          serverHashedPassword;
                String          androidHashedPassword;
                String          salt;
                int             id;
                MessageDigest   digest;


                serverHashedPassword        = "";
                androidHashedPassword       = "";
                salt                        = "";
                confirm                     = "";

                for (int i = 0; i < json.length(); ++i)
                {
                    obj                     = json.getJSONObject(i);
                    id                      = obj.getInt("id");
                    confirm                 = obj.getString("confirm");
                    serverHashedPassword    = obj.getString("hashed_password");
                    salt                    = obj.getString("salt");
                }

                System.out.println("hashed password: " + serverHashedPassword);

                digest = MessageDigest.getInstance("SHA-256");
                digest.reset();

                androidHashedPassword = bin2hex(digest.digest((this.password + "wibble" + salt).getBytes()));

                if(serverHashedPassword.equals(androidHashedPassword) && confirm.equalsIgnoreCase("true"))
                {
                    System.out.println("holy wow they're the same!");
                    validated   = true;
                }
            }

            catch(Exception e)
            {
                System.out.println("Exception thrown");
            }
        }
        else
        {
            System.out.println("Empty JSON response!");
        }

        return validated;
    }

    static String bin2hex(byte[] data)
    {
        return String.format("%0" + (data.length * 2) + 'x', new BigInteger(1, data));
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

/*


<LinearLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:layout_marginTop="350dp"
        android:id="@+id/setUpNewAccountText"
        android:layout_gravity="center"
        android:layout_centerHorizontal="true">


        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:textAppearance="?android:attr/textAppearanceSmall"
            android:text="@string/newAccountText"
            android:textColor="@color/white"
            android:id="@+id/dontHaveAccountText"
            />

        <TextView
            android:id="@+id/signUpText"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:textAppearance="?android:attr/textAppearanceSmall"
            android:text="@string/SignUpText"
            android:layout_marginLeft="5dp"
            android:clickable="true"
            android:onClick="signUp"
            android:textColor="@color/white"/>

        </LinearLayout>
 */