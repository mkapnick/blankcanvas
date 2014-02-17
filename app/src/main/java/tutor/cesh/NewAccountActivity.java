package tutor.cesh;

import android.database.sqlite.SQLiteDatabase;
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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import database.DatabaseFacility;
import database.RESTClientFactory;

public class NewAccountActivity extends ActionBarActivity implements Arrival
{

    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_account, menu);
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

    private void sendOutConfirmationEmail()
    {
        /*Intent              intent;
        String                 url;

        intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");

        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{this.email});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Welcome to Tutorcesh");

        try
        {
            System.out.println("INSIDE TRY CATCH IN SENDOUTCONFIRMATIONEMAIL!!");

            url = "localhost:8080/ConfirmationLinkAndroid/verify-email?token=testingtesting123herewego";

            intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml("Click the link below to activate account\n" +
                "<a href=" + url + "/>"+ url + "</a>"));

            System.out.println("before start activity");
            startActivity(Intent.createChooser(intent, "Send mail..."));
            System.out.println("after start activity");
        }

        catch(Exception e)
        {
            System.out.println("Exception thrown");
        }*/

    }

    /**
     * Method makes sure the email address is not already in
     * the database system
     *
     * @param view The view sent from the UI
     */
    @Override
    public void validateArrival(View view)
    {
        System.out.println("-- In validateArrival() --");

        this.email      = ((EditText) findViewById(R.id.emailTextView)).getText().toString();
        this.password   = ((EditText) findViewById(R.id.passwordTextView)).getText().toString();

        System.out.println("Email entered is: " + this.email);
        System.out.println("Password entered is: " + this.password);

        validate();

    }

    /**
     *
     * Creates a new User account by sending the Web server
     * a POST request with the necessary JSON data
     */
    private void validate()
    {
        //DatabaseFacility    databaseFacility;
        //SQLiteDatabase      database;

        //databaseFacility        = new DatabaseFacility(getApplicationContext());
        //database                = databaseFacility.getReadableDatabase();
        //databaseFacility.setDatabase(database);

        //if(!databaseFacility.validateUser(this.email, this.password))
        //{
        //database        = databaseFacility.getWritableDatabase();
        //databaseFacility.setDatabase(database);
        //databaseFacility.insertUserRecord(this.email, this.password, "", "", "", "false");
        //Send out email
        //sendOutConfirmationEmail();
        //}
        //else
        //{
        //Toast.makeText(this, "Email account already active", Toast.LENGTH_LONG).show();
        //}

        Log.d("","In validate in NewAccountActivity");

        HttpPost    httpPost;
        RESTClient  restClient;
        Thread      thread;

        try
        {
            httpPost        = RESTClientFactory.post(this.email, this.password);
            restClient      = new RESTClient(httpPost);
            thread          = new Thread(restClient);

            thread.setPriority(0x0000000a); //set this thread to a lower priority than the main UI thread
            thread.start();

            Toast.makeText(this, "Account created", Toast.LENGTH_LONG).show();
            finish();
        }
        catch(IOException e)
        {
            System.out.println("Inside IOException exception!.. not good!");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        catch(JSONException e)
        {
            System.out.println("Inside JSONException!.. not good!");
        }
        catch(NetworkOnMainThreadException e)
        {
            System.out.println("Network on main thread exception");
        }
        catch(NoSuchAlgorithmException e)
        {
            System.out.println("No such algorithm exception");
        }

        //Log out and make sure the user confirm his/her email before
        //anything else
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
            View rootView = inflater.inflate(R.layout.fragment_new_account, container, false);
            return rootView;
        }
    }

}
