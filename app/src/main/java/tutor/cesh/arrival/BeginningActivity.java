package tutor.cesh.arrival;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import tutor.cesh.R;
import tutor.cesh.session.SessionManager;

public class BeginningActivity extends Activity implements Arrival
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_beginning);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.beginning, menu);
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
     * Not much validation here, just handing off the validation to
     * another activity depending on what the user clicks
     * @param view The view that was clicked
     */
    @Override
    public void validateArrival(View view)
    {
        Intent intent;

        switch(view.getId())
        {
            case R.id.loginButton:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;

            case R.id.createAccountButton:
                intent = new Intent(this, NewAccountActivity.class);
                startActivity(intent);
                break;
        }

    }
}
