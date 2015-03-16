package tutor.cesh.profile;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import tutor.cesh.R;

public class AccountActivity extends Activity implements View.OnClickListener
{
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initializeUI()
    {
        this.relativeLayout = (RelativeLayout) findViewById(R.id.viewProfileRelativeLayout);
        this.relativeLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        Intent intent;

        switch(v.getId())
        {
            case R.id.viewProfileRelativeLayout:

                //call the correct class
                intent = new Intent(getApplicationContext(),
                        StudentTutorProfileContainerActivity.class);
                startActivity(intent);
                break;
        }

    }
}
