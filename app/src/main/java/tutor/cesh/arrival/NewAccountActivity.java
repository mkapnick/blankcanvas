package tutor.cesh.arrival;

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

import org.apache.http.client.methods.HttpPost;
import org.json.JSONException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import tutor.cesh.database.RestClientFactory;
import tutor.cesh.R;
import tutor.cesh.rest.RestClientPost;

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

    /**
     * Method makes sure the email address is not already in
     * the tutor.app.database system
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

        Log.d("","In validate in NewAccountActivity");

        HttpPost    httpPost;
        RestClientPost restClientPost;
        Thread      thread;

        try
        {
            httpPost        = RestClientFactory.postNewAccount(this.email, this.password);
            restClientPost = new RestClientPost(httpPost);
            thread          = new Thread(restClientPost);

            thread.setPriority(0x0000000a); //set this thread to a lower priority than the main UI thread
            thread.start();

            Toast.makeText(this, "Account created", Toast.LENGTH_LONG).show();
            //Dialog box here! Let user know that an email has been sent
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
