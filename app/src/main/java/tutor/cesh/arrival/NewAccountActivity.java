package tutor.cesh.arrival;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import tutor.cesh.R;
import tutor.cesh.rest.asynchronous.RestClientExecute;
import tutor.cesh.rest.factory.RestClientFactory;

public class NewAccountActivity extends Activity implements Arrival
{
    private String email;
    private String password;
    private EditText  passwordTextView;

    private void initializeUI()
    {
        this.passwordTextView = (EditText) findViewById(R.id.passwordTextView);
        this.passwordTextView.setHint("Password");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_new_account);

        initializeUI();

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
     *
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
     * Method makes sure the email address is not already in
     * the tutor.app.database system
     *
     * @param view The view sent from the UI
     */
    @Override
    public void validateArrival(View view)
    {

        String extension;

        this.email      = ((EditText) findViewById(R.id.emailTextView)).getText().toString();
        this.password   = ((EditText) findViewById(R.id.passwordTextView)).getText().toString();

        extension = this.email.substring(this.email.lastIndexOf('.') + 1);

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

        HttpPost            httpPost;
        RestClientExecute   rce;

        try
        {
            httpPost        = RestClientFactory.postNewUser(this.email, this.password);
            rce             = new RestClientExecute(httpPost);
            rce.start();

            Toast.makeText(this, "Account created", Toast.LENGTH_LONG).show();
            showUserDialog();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        catch(NetworkOnMainThreadException e)
        {
            e.printStackTrace();
        }
        catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
