package tutor.cesh;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import database.DatabaseFacility;

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

    private void validate()
    {
        databaseFacility    = new DatabaseFacility(getApplicationContext());
        database            = databaseFacility.getReadableDatabase();
        databaseFacility.setDatabase(database);


        /* Writing to the Database */
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
