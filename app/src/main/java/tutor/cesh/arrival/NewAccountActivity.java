package tutor.cesh.arrival;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import tutor.cesh.rest.RestClientExecute;
import tutor.cesh.rest.RestClientFactory;
import tutor.cesh.R;

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

        String extension;

        this.email      = ((EditText) findViewById(R.id.emailTextView)).getText().toString();
        this.password   = ((EditText) findViewById(R.id.passwordTextView)).getText().toString();

        System.out.println("Email entered is: " + this.email);
        System.out.println("Password entered is: " + this.password);
        extension = this.email.substring(this.email.lastIndexOf('.') + 1);
        System.out.println(extension);

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
               Toast.makeText(this, "Enter a password!", Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(this, "Enter your .edu email!", Toast.LENGTH_LONG).show();

    }

    /**
     *
     * Creates a new User account by sending the Web server
     * a POST request with the necessary JSON data
     */
    private void validate()
    {

        Log.d("", "In validate in NewAccountActivity");

        HttpPost            httpPost;
        RestClientExecute   rce;

        try
        {
            httpPost        = RestClientFactory.post(this.email, this.password);
            rce             = new RestClientExecute(httpPost);
            rce.start();

            Toast.makeText(this, "Account created", Toast.LENGTH_LONG).show();
            showUserDialog();
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

     */
    private void showUserDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.on_create_new_account)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });

        builder.create().show();
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
