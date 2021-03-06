package tutor.cesh.profile.activity;

import android.content.Context;
import android.graphics.Typeface;
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

import org.apache.http.client.methods.HttpPut;

import java.util.ArrayList;

import tutor.cesh.R;
import tutor.cesh.profile.ProfileInfo;
import tutor.cesh.profile.ProfileInfoBehavior;
import tutor.cesh.metadata.ServerDataBank;
import tutor.cesh.profile.Student;
import tutor.cesh.profile.Tutor;
import tutor.cesh.profile.User;
import tutor.cesh.dialog.DialogSetterAndPopulator;
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
    private TextView        saveButton, arrowBackImage, switchText, tutorStatusCircle;
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

        name                = (EditText) findViewById(R.id.name);
        major               = (EditText) findViewById(R.id.major);
        major.setOnClickListener(this);
        major.setKeyListener(null);
        year                = (EditText) findViewById(R.id.year);
        year.setOnClickListener(this);
        year.setKeyListener(null);
        minor               = (EditText) findViewById(R.id.minor);
        minor.setOnClickListener(this);
        minor.setKeyListener(null);
        rate                = (EditText) findViewById(R.id.rate);
        rate.setOnClickListener(this);
        rate.setKeyListener(null);
        switchText          = (TextView) findViewById(R.id.switchText);
        tutorStatusCircle   = (TextView) findViewById(R.id.tutorStatusCircle);
        logoutButton        = (Button)   findViewById(R.id.logoutButton);
        tutorSwitch         = (Switch)   findViewById(R.id.tutorSwitch);
        tutorSwitch.setTypeface((Typeface.create("sans-serif-light", Typeface.NORMAL)));


        studentAbout    = (EditText) findViewById(R.id.student_about);
        tutorAbout      = (EditText) findViewById(R.id.tutor_about);

        studentCurrentClasses   = (EditText) findViewById(R.id.classes_taking);
        tutorCurrentClasses     = (EditText) findViewById(R.id.classes_tutoring);

        actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_background));

        inflator    = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v           = inflator.inflate(R.layout.action_bar_edit_student_tutor_profile, null);
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
        setUpEditables();
    }


    @Override
    public void onClick(View v)
    {
        String              majorTitle, rateTitle, yearTitle, minorTitle;
        ArrayList<String>   allData, allMajors, allRates, allYears, allMinors;
        String              thisData;

        majorTitle  = "Filter by major(s)";
        rateTitle   = "Filter by rate(s)";
        yearTitle   = "Filter by year(s)";
        minorTitle  = "Filter by minor(s)";

        allMajors   = ServerDataBank.getMajors();
        allRates    = ServerDataBank.getRates();
        allYears    = ServerDataBank.getYears();
        allMinors   = ServerDataBank.getMinors();

        allData     = new ArrayList<String>();
        thisData    = "";

        switch(v.getId())
        {
            case R.id.saveButton:
                saveUserProfile(v);
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.logoutButton:
                finish();
                this.sessionManager.logOut();
                break;
            case R.id.arrow_back_image:
                finish();
                break;
            case R.id.major:

                shallowCopy(allMajors, allData);

                thisData = ProfileInfoBehavior.getEditableMajor();

                DialogSetterAndPopulator.setMultiChoiceDialogAndShow(this, this.major, majorTitle,
                                                                     ProfileInfo.MAJOR,
                                                                     ProfileInfoBehavior.EDITABLE,
                                                                     allData,
                                                                     thisData);
                break;

            case R.id.minor:

                shallowCopy(allMinors, allData);

                thisData = ProfileInfoBehavior.getEditableMinor();

                DialogSetterAndPopulator.setMultiChoiceDialogAndShow(this, this.minor, minorTitle,
                        ProfileInfo.MINOR,
                        ProfileInfoBehavior.EDITABLE,
                        allData,
                        thisData);
                break;

            case R.id.rate:

                shallowCopy(allRates, allData);

                thisData = ProfileInfoBehavior.getEditableRate();
                DialogSetterAndPopulator.setSingleChoiceDialogAndShow(this, this.rate, rateTitle,
                        ProfileInfo.RATE,
                        ProfileInfoBehavior.EDITABLE,
                        allData,
                        thisData);
                break;
            case R.id.year:

                shallowCopy(allYears, allData);

                thisData = ProfileInfoBehavior.getEditableYear();
                DialogSetterAndPopulator.setSingleChoiceDialogAndShow(this, this.year, yearTitle,
                        ProfileInfo.YEAR,
                        ProfileInfoBehavior.EDITABLE,
                        allData,
                        thisData);
        }
    }

    /**
     *
     * @param originalData
     * @param newData
     */
    private void shallowCopy(ArrayList<String> originalData, ArrayList<String> newData)
    {
        if(null != originalData && originalData.size() > 0)
        {
            for(int i =0; i < originalData.size(); i++)
            {
                newData.add(originalData.get(i));
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        setTutorSwitchAndVisibility(isChecked);
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
        student.setAbout(this.studentAbout.getText().toString().trim());
        student.setYear(this.year.getText().toString().trim());
        student.setMajor(this.major.getText().toString().trim());
        student.setMinor( this.minor.getText().toString().trim());

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

    private void setTutorSwitchAndVisibility(boolean isActviated)
    {
        /*** Tutor Switch **/
        if(isActviated)
        {
            this.tutorSwitch.setChecked(true);
            this.switchText.setText("Activated");
            this.switchText.setBackgroundResource(R.drawable.oval_green_custom_edit);
            this.tutorStatusCircle.setBackgroundResource(R.drawable.circle_green);
        }
        else
        {
            this.tutorSwitch.setChecked(false);
            this.switchText.setText("Not activated");
            this.switchText.setBackgroundResource(R.drawable.oval_red_custom_edit);
            this.tutorStatusCircle.setBackgroundResource(R.drawable.circle_red);
        }
    }

    /**
     *
     */
    private void setUpEditables()
    {
        User    user;
        Student student;
        Tutor   tutor;

        user    = User.getInstance(this);
        student = user.getStudent();
        tutor   = user.getTutor();

        ProfileInfoBehavior.EDITABLE.setMajor(student.getMajor());
        ProfileInfoBehavior.EDITABLE.setRate(tutor.getRate());
        ProfileInfoBehavior.EDITABLE.setYear(student.getYear());
        ProfileInfoBehavior.EDITABLE.setMinor(student.getMinor());
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
        minor.setText(bundle.getString("minor"));
        year.setText(bundle.getString("year"));

        studentCurrentClasses.setText(bundle.getString("studentCurrentClasses"));
        studentAbout.setText(bundle.getString("studentAbout"));

        rate.setText(bundle.getString("rate"));
        tutorCurrentClasses.setText(bundle.getString("tutorCurrentClasses"));
        tutorAbout.setText(bundle.getString("tutorAbout"));

        /*** Tutor Switch **/
        setTutorSwitchAndVisibility(bundle.getBoolean("isPublic"));
    }
}