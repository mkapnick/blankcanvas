package tutor.cesh.arrival;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import tutor.cesh.R;
import tutor.cesh.rest.AsyncGet;
import tutor.cesh.rest.OnLoginTaskDelegate;
import tutor.cesh.rest.RestClientFactory;
import tutor.cesh.rest.TaskDelegate;

public class LoginActivity extends ActionBarActivity implements Arrival
{

    private String              email;
    private String              password;
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

        String extension;

        this.email      = ((EditText) findViewById(R.id.emailTextView)).getText().toString();
        this.password   = ((EditText) findViewById(R.id.passwordTextView)).getText().toString();
        extension       = this.email.substring(this.email.lastIndexOf('.') + 1);

        if (this.email.length() > 0 )
        {
            if(this.password.length() > 0)
            {
                if(extension.equals("edu"))
                    validate();
                else
                    Toast.makeText(this, "Email must be a .edu address", Toast.LENGTH_LONG).show();
            }
            else
                Toast.makeText(this, "Enter your password", Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(this, "Enter your .edu email", Toast.LENGTH_LONG).show();

    }

    /**
     * Validates the user email and password
     * by querying the tutor.app.database as necessary
     */
    private void validate()
    {
        HttpGet                                     get;
        TaskDelegate                                delegate;
        ProgressDialog                              pd;

        pd = new ProgressDialog(this);
        pd.setTitle("Processing...");
        pd.setMessage("Please wait");
        pd.setCancelable(false);
        pd.setIndeterminate(true);

        try
        {
            delegate    = new OnLoginTaskDelegate(this);
            get         = RestClientFactory.authenticate(email, password);
            new AsyncGet(this, delegate, pd).execute(get);
        }
        catch(NetworkOnMainThreadException e)
        {
            e.printStackTrace();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
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
