package tutor.cesh.arrival;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import org.apache.http.client.methods.HttpPost;
import java.util.HashMap;
import tutor.cesh.R;
import tutor.cesh.rest.asynchronous.AsyncPost;
import tutor.cesh.rest.delegate.OnLoginTaskDelegate;
import tutor.cesh.rest.delegate.TaskDelegate;
import tutor.cesh.rest.factory.RestClientFactory;
import tutor.cesh.session.SessionManager;

public class SessionStartActivity extends Activity
{
    private SessionManager sessionManager;

    /**
     *
     */
    private void branchToFirstActivity()
    {
        HttpPost                post;
        TaskDelegate            delegate;
        HashMap<String, String> userInfo;
        String                  email, password;

        /** Session Manager **/
        this.sessionManager = new SessionManager(this);

        if(this.sessionManager.isLoggedIn())
        {
            userInfo    = this.sessionManager.getSessionDetails();
            email       = userInfo.get("email");
            password    = userInfo.get("password");

            delegate    = new OnLoginTaskDelegate(this, email, password);

            try
            {
                post        = RestClientFactory.authenticateViaPost(email, password);
                new AsyncPost(this, delegate, null).execute(post);
            }
            catch (Exception e)
            {

            }
        }
        else
        {
            this.sessionManager.branchToActivity(); //if no session, branch
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        branchToFirstActivity();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_start);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_session_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
}
