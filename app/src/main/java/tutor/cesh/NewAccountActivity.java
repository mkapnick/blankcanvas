package tutor.cesh;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.EditText;
import android.widget.Toast;

import database.DatabaseFacility;

public class NewAccountActivity extends ActionBarActivity implements Arrival {

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
     * Validates the user email and password
     * by querying the database as necessary
     */
    private void validate()
    {
        Log.d("","In validate in NewAccountActivity");
        DatabaseFacility    databaseFacility;
        SQLiteDatabase      database;


        databaseFacility    = new DatabaseFacility(getApplicationContext());
        database            = databaseFacility.getReadableDatabase();
        databaseFacility.setDatabase(database);


        /* Writing to the Database */
        //database            = databaseFacility.getWritableDatabase();
        //databaseFacility.insertUserRecord(this.email, this.password, "TEST RUN", "TEST RUN", "TEST RUN");
        //databaseFacility.setDatabase(database);

        if(!databaseFacility.validateUser(this.email, this.password))
        {
            database        = databaseFacility.getWritableDatabase();
            databaseFacility.setDatabase(database);
            databaseFacility.insertUserRecord(this.email, this.password, "", "", "");
            Toast.makeText(this, "Account created", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(this, "Email account already active", Toast.LENGTH_LONG).show();
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
