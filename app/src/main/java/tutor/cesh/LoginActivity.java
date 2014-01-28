package tutor.cesh;

import android.content.Intent;
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