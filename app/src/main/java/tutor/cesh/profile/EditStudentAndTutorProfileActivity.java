package tutor.cesh.profile;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;

import java.util.ArrayList;

import tutor.cesh.R;
import tutor.cesh.Student;
import tutor.cesh.Tutor;
import tutor.cesh.User;
import tutor.cesh.profile.util.classes.ClassesUtility;
import tutor.cesh.rest.asynchronous.RestClientExecute;
import tutor.cesh.rest.http.CourseHttpObject;
import tutor.cesh.rest.http.EnrollHttpObject;
import tutor.cesh.rest.http.HttpObject;
import tutor.cesh.rest.http.student.StudentCourseHttpObject;
import tutor.cesh.rest.http.student.StudentHttpObject;
import tutor.cesh.rest.http.tutor.TutorCourseHttpObject;
import tutor.cesh.rest.http.tutor.TutorHttpObject;
import tutor.cesh.session.SessionManager;

public class EditStudentAndTutorProfileActivity extends ActionBarActivity implements
                                                              View.OnClickListener,
                                                              CompoundButton.OnCheckedChangeListener
{
    private Bundle          bundle;
    private EditText        name, major, minor, year, tutorAbout, studentAbout,
                            studentCurrentClasses, tutorCurrentClasses, rate;
    private TextView        saveButton, arrowBackImage;
    private Button          logoutButton;
    private Switch          tutorSwitch;
    private android.support.v7.app.ActionBar actionBar;
    private SessionManager  sessionManager;

    /**
     * Initialize main parts of the UI
     *
     */
    private void initializeUI()
    {
        LayoutInflater  inflator;
        View            v;

        name        = (EditText) findViewById(R.id.name);
        major       = (EditText) findViewById(R.id.major);
        year        = (EditText) findViewById(R.id.year);
        minor       = (EditText) findViewById(R.id.minor);
        rate        = (EditText) findViewById(R.id.rate);
        logoutButton= (Button)   findViewById(R.id.logoutButton);
        tutorSwitch = (Switch)   findViewById(R.id.tutorSwitch);

        studentAbout    = (EditText) findViewById(R.id.student_about);
        tutorAbout      = (EditText) findViewById(R.id.tutor_about);

        studentCurrentClasses   = (EditText) findViewById(R.id.classes_taking);
        tutorCurrentClasses     = (EditText) findViewById(R.id.classes_tutoring);

        actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_background));

        inflator    = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v           = inflator.inflate(R.layout.edit_info_custom_action_bar, null);
        saveButton  = (TextView) v.findViewById(R.id.saveButton);
        arrowBackImage = (TextView) v.findViewById(R.id.arrow_back_image);


        //set on click listeners
        saveButton.setOnClickListener(this);
        logoutButton.setOnClickListener(this);
        tutorSwitch.setOnCheckedChangeListener(this);
        arrowBackImage.setOnClickListener(this);

        actionBar.setCustomView(v);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student_and_tutor_profile);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); //don't
                                                                                          // show
                                                                                          // the
                                                                                          // keyboard
                                                                                          // right
                                                                                          // away
        this.sessionManager = new SessionManager(this); //set the session manager

        initializeUI();
        setUpUserData();
    }


    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.saveButton:
                saveUserProfile(v);
                finish();
                break;
            case R.id.logoutButton:
                this.sessionManager.logOut();
                finish();
                break;
            case R.id.arrow_back_image:
                finish();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
       //nothing as of now...
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
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
        HttpPut                 put;
        String                  studentCurrentClasses, tutorCurrentClasses;
        String []               studentCurrentClassesArray, tutorCurrentClassesArray;
        ArrayList<String>       studentCurrentClassesList, tutorCurrentClassesList;
        HttpObject              enroll;
        StudentHttpObject       studentHttp;
        TutorHttpObject         tutorHttp;
        Student                 student;
        Tutor                   tutor;
        User                    user;
        CourseHttpObject        studentCourseHttpObject, tutorCourseHttpObject;

        user                        = User.getInstance(this);
        student                     = user.getStudent();
        tutor                       = user.getTutor();
        studentCurrentClassesList   = new ArrayList<String>();
        tutorCurrentClassesList     = new ArrayList<String>();

        //update student attributes
        student.setName(  this.name.getText().toString().trim());
        student.setAbout( this.studentAbout.getText().toString().trim());
        student.setYear(  this.year.getText().toString().trim());
        student.setMajor( this.major.getText().toString().trim());

        //update tutor attributes
        tutor.setRate(  this.rate.getText().toString().trim());
        tutor.setAbout( this.tutorAbout.getText().toString().trim());
        tutor.setPublic(this.tutorSwitch.isChecked());

        //update both student and tutor classes
        studentCurrentClasses   = this.studentCurrentClasses.getText().toString();
        tutorCurrentClasses     = this.tutorCurrentClasses.getText().toString();

        //move student classes into an arraylist if they exist
        if(studentCurrentClasses.length() > 0)
        {
            studentCurrentClassesArray = studentCurrentClasses.split(" ");
            for(int i =0; i < studentCurrentClassesArray.length; i++)
                studentCurrentClassesList.add(studentCurrentClassesArray[i]);
        }

        //move tutor classes into an arraylist if they exist
        if(tutorCurrentClasses.length() > 0)
        {
            tutorCurrentClassesArray    = tutorCurrentClasses.split(" ");
            for(int i =0; i < tutorCurrentClassesArray.length; i++)
                tutorCurrentClassesList.add(tutorCurrentClassesArray[i]);
        }

        //set the classes for the student and tutor objects, so changes get reflected on front end
        student.setCurrentClasses(studentCurrentClassesList);
        tutor.setCurrentClasses(tutorCurrentClassesList);

        //Send all the data to the server, so data gets saved in Database
        try
        {
            //student updates
            studentCourseHttpObject = new StudentCourseHttpObject(user, studentCurrentClassesList);
            studentHttp             = new StudentHttpObject(user);

            //tutor updates
            tutorCourseHttpObject   = new TutorCourseHttpObject(user, tutorCurrentClassesList);
            tutorHttp               = new TutorHttpObject(user);

            //enroll updates
            enroll                  = new EnrollHttpObject(user);

            //Make calls to the server and save on the backend

            put             = studentHttp.put();
            new RestClientExecute(put).start();

            put             = tutorHttp.put();
            new RestClientExecute(put).start();

            put            = studentCourseHttpObject.put();
            new RestClientExecute(put).start();

            put            = tutorCourseHttpObject.put();
            new RestClientExecute(put).start();

            put             = enroll.put();
            new RestClientExecute(put).start();

            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e)
        {
            Log.e("Error", "Caught exception!!");
        }
    }

    /**
     *
     */
    private void setUpUserData()
    {
        //TODO DO WE EVEN NEED THE BUNDLE FOR THIS??
        this.bundle = getIntent().getExtras();
        name.setText(bundle.getString("name"));
        major.setText(bundle.getString("major"));
        year.setText(bundle.getString("year"));

        studentCurrentClasses.setText(bundle.getString("studentCurrentClasses"));
        studentAbout.setText(bundle.getString("studentAbout"));

        rate.setText(bundle.getString("rate"));
        tutorCurrentClasses.setText(bundle.getString("tutorCurrentClasses"));
        tutorAbout.setText(bundle.getString("tutorAbout"));

        this.tutorSwitch.setChecked(bundle.getBoolean("isPublic"));
    }


}