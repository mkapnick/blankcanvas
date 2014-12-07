package tutor.cesh.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;

import tutor.cesh.R;
import tutor.cesh.Student;
import tutor.cesh.Tutor;
import tutor.cesh.User;
import tutor.cesh.profile.util.classes.ClassesUtility;
import tutor.cesh.profile.util.classes.StudentClassesUtility;
import tutor.cesh.profile.util.classes.TutorClassesUtility;
import tutor.cesh.rest.RestClientExecute;
import tutor.cesh.rest.http.EnrollHttpObject;
import tutor.cesh.rest.http.HttpObject;
import tutor.cesh.rest.http.StudentCourseHttpObject;
import tutor.cesh.rest.http.StudentHttpObject;
import tutor.cesh.rest.http.TutorCourseHttpObject;
import tutor.cesh.rest.http.TutorHttpObject;

public class EditStudentAndTutorProfileActivity extends Activity implements View.OnClickListener
{
    private static String profileImagePath, coverImagePath;
    private Bundle bundle;
    private EditText name, major, minor, year, tutorAbout, studentAbout,
                     classesTaking, classesTutoring, rate;
    private android.support.v7.app.ActionBar actionBar;
    private TextView saveButton;


    /**
     * Initialize main parts of the UI
     *
     */
    private void initializeUI()
    {
        name        = (EditText) findViewById(R.id.name);
        major       = (EditText) findViewById(R.id.major);
        year        = (EditText) findViewById(R.id.year);
        minor       = (EditText) findViewById(R.id.minor);
        rate        = (EditText) findViewById(R.id.rate);
        saveButton  = (TextView) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);

        //format the save button
        saveButton.setBackgroundResource(R.drawable.oval_save);
        saveButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.oval_save, 0);


        studentAbout = (EditText) findViewById(R.id.student_about);
        tutorAbout = (EditText) findViewById(R.id.tutor_about);

        classesTaking = (EditText) findViewById(R.id.classes_taking);
        classesTutoring= (EditText) findViewById(R.id.classes_tutoring);

        /*actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);

        LayoutInflater inflator = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.edit_info_custom_action_bar, null);
        saveButton = (TextView) v.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);
        actionBar.setCustomView(v);*/
    }
    /**
     *
     * @param
     * @param
     * @param

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2)
        {
            if (data != null)
                updateBackgroundImage(data);
        }
    }*/

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student_and_tutor_profile);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); //don't show the keyboard right away
        initializeUI();
        setUpUserData();
    }


    @Override
    public void onClick(View v) {

        Intent intent;

        switch(v.getId())
        {
            case R.id.saveButton:
                saveUserProfile(v);
                setResult(RESULT_OK);
                finish();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.edit_profile_student, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id;
        id = item.getItemId();
        if (id == R.id.action_settings)
            return true;

        return super.onOptionsItemSelected(item);
    }



    /**
     * Save user data to the server
     * @param view
     */
    private void saveUserProfile(View view)
    {
        HttpPost                post;
        HttpPut                 put;
        String                  jsonArray;
        HttpObject              enroll, course;
        StudentHttpObject       studentHttp;
        TutorHttpObject         tutorHttp;
        Student                 student;
        Tutor                   tutor;
        ClassesUtility          cUtility;
        User                    user;
        StudentCourseHttpObject studentCourses;
        TutorCourseHttpObject   tutorCourses;

        user            = User.getInstance(this);
        student         = user.getStudent();
        tutor           = user.getTutor();


        //update student attributes
        student.setName(name.getText().toString());
        student.setAbout(studentAbout.getText().toString());
        student.setYear(year.getText().toString());
        student.setMajor(major.getText().toString());

        //classes taking
        cUtility        = new StudentClassesUtility(user, this.classesTaking);
        jsonArray       = cUtility.formatClassesBackend();
        studentCourses  = new StudentCourseHttpObject(user, jsonArray);

        //update tutor attributes
        tutor.setRate(rate.getText().toString());
        tutor.setAbout(tutorAbout.getText().toString());

        //classes tutoring
        cUtility        = new TutorClassesUtility(user, this.classesTutoring);
        jsonArray       = cUtility.formatClassesBackend();
        tutorCourses    = new TutorCourseHttpObject(user, jsonArray);

        //Send a put request with the user's data up to the server
        try
        {
            studentHttp     = new StudentHttpObject(user);
            tutorHttp       = new TutorHttpObject(user);
            enroll          = new EnrollHttpObject(user);

            //Make calls to the server and save on the backend
            post            = studentCourses.post();
            new RestClientExecute(post).start();

            post            = tutorCourses.post();
            new RestClientExecute(post).start();

            put             = studentHttp.put();
            new RestClientExecute(put).start();

            put             = tutorHttp.put();
            new RestClientExecute(put).start();

            put             = enroll.put();
            new RestClientExecute(put).start();

            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    private void setUpUserData()
    {
        this.bundle = getIntent().getExtras();
        name.setText(bundle.getString("name"));
        major.setText(bundle.getString("major"));
        year.setText(bundle.getString("year"));

        classesTaking.setText(bundle.getString("classesTaking"));
        studentAbout.setText(bundle.getString("studentAbout"));

        rate.setText(bundle.getString("rate"));
        classesTutoring.setText(bundle.getString("classesTutoring"));
        tutorAbout.setText(bundle.getString("tutorAbout"));

    }
}