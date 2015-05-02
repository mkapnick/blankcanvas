package tutor.cesh.administrative;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.methods.HttpPost;

import tutor.cesh.R;
import tutor.cesh.profile.Student;
import tutor.cesh.profile.User;
import tutor.cesh.rest.apisecurity.APIEndpoints;
import tutor.cesh.rest.asynchronous.AsyncPost;
import tutor.cesh.rest.delegate.TaskDelegate;

public class SettingsActivity extends ActionBarActivity implements View.OnClickListener,
                                                                   CompoundButton.OnCheckedChangeListener,
                                                                   TaskDelegate
{
    private android.support.v7.app.ActionBar    actionBar;
    private TextView                            arrowBackImage;
    private Switch                              switchWidget;


    /**
     *
     */
    private void initializeUI()
    {
        this.switchWidget = (Switch) findViewById(R.id.switchSettings);
        this.switchWidget.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId())
        {
            case R.id.arrow_back_image_settings:
                finish();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initializeUI();
        setUpActionBar();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        HttpPost    post;
        String      endPoint, studentId;
        User        user;
        Student     student;

        endPoint    = APIEndpoints.getEMAIL_SUBSCRIBERS_ENDPOINT();
        user        = User.getInstance(this);
        student     = user.getStudent();
        studentId   = student.getId();

        if(isChecked)
        {

            endPoint    += "/" + studentId + "/subscribe";
            post        = new HttpPost(endPoint);

            new AsyncPost(this, this, null).execute(post);

            Toast.makeText(this, "You are now registered to receive email updates from" +
                    " the blankcanvas team.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            endPoint    += "/" + studentId + "/unsubscribe";
            post        = new HttpPost(endPoint);

            new AsyncPost(this, this, null).execute(post);

            Toast.makeText(this, "You are no longer registered to receive email updates from" +
                    " the blankcanvas team.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     *
     */
    private void setUpActionBar()
    {
        LayoutInflater inflator;
        View           v;
        this.actionBar = getSupportActionBar();
        this.actionBar.setDisplayShowCustomEnabled(true);
        this.actionBar.setDisplayShowHomeEnabled(false);
        this.actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_background));


        inflator    = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v           = inflator.inflate(R.layout.action_bar_settings, null);

        this.arrowBackImage              = (TextView)    v.findViewById(R.id.arrow_back_image_settings);
        this.arrowBackImage.setOnClickListener(this);

        this.actionBar.setCustomView(v);
    }

    @Override
    public void taskCompletionResult(Object response) {
        //do nothing
    }

    @Override
    public void setProgressDialog(ProgressDialog pd) {
        //do nothing

    }
}
