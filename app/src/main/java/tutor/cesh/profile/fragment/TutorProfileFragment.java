package tutor.cesh.profile.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONObject;

import tutor.cesh.R;
import tutor.cesh.Student;
import tutor.cesh.Tutor;
import tutor.cesh.User;
import tutor.cesh.profile.util.classes.ClassesUtility;
import tutor.cesh.profile.util.classes.TutorClassesUtility;
import tutor.cesh.rest.AsyncDownloader;
import tutor.cesh.rest.ImageHandler;
import tutor.cesh.rest.TaskDelegate;

public class TutorProfileFragment extends Fragment implements TaskDelegate {

    public static ImageView         profileImageView, coverImageView;
    private EditText                name, major, year, about, classes, rate;

    /**
     *
     */
    private void initializeUI(View inflatedView)
    {
        name                = (EditText)    inflatedView.findViewById(R.id.name);
        major               = (EditText)    inflatedView.findViewById(R.id.major);
        year                = (EditText)    inflatedView.findViewById(R.id.year);
        about               = (EditText)    inflatedView.findViewById(R.id.about);
        classes             = (EditText)    inflatedView.findViewById(R.id.classes);
        rate                = (EditText)    inflatedView.findViewById(R.id.rate);
        profileImageView    = (ImageView)   inflatedView.findViewById(R.id.profileImage);
        coverImageView      = (ImageView)   inflatedView.findViewById(R.id.profileBackgroundImage);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        //view containing the fragments UI
        View v;

        v = inflater.inflate(R.layout.fragment_tutor_profile, container, false);
        initializeUI(v);
        setUpUserInfo();

        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int position, id;

        id          = item.getItemId();
        position    = -1;

        if (id == R.id.action_settings)
            return true;
        else if(id == R.id.action_edit_profile)
            position = -100;
        else if(id == R.id.action_switch_profile)
            position = 1;

        //onOptionsItemSelected(position);
        return super.onOptionsItemSelected(item);
    }

    /**
     * Responding to clicks from the Action Bar and from
     * the drawer layout
     *
     * @param position The position in the listViewTitles array
     */
    private void onOptionsItemSelected(int position)
    {
        Intent intent;

        intent = null;
        if(position == -100)
        {
            //intent = new Intent(this, EditStudentProfileActivity.class);
            startActivityForResult(intent, 1);
        }

        else if(position == 1)
        {
            //drawerLayout.closeDrawer(listView);
            //intent = new Intent(this, TutorProfileActivity.class);
            startActivity(intent);
            //finish(); //TODO don't finish on switch
        }
        else if (position == 2)
        {

        }
    }


    @Override
    public void onResume()
    {
        super.onResume();
        setUpUserInfo();
    }

    /**
     *
     */
    private void setUpUserInfo()
    {
        ClassesUtility                              cUtility;
        User                                        user;
        Student                                     student;
        Tutor                                       tutor;

        user            = User.getInstance(getActivity());
        student         = user.getStudent();
        tutor           = user.getTutor();

        name.setText(student.getName());
        major.setText(student.getMajor());
        year.setText(student.getYear());
        about.setText(tutor.getAbout());
        rate.setText(tutor.getRate());

        coverImageView.setBackground(new BitmapDrawable(getResources(), tutor.getCoverImage()));

        //set courses front end
        cUtility = new TutorClassesUtility(user, this.classes, this.getActivity().getApplicationContext());
        cUtility.setClassesRegularMode();
    }

    @Override
    public void taskCompletionResult(Object resp) {

        //set fields based on JSON response from the server
        ClassesUtility  cUtility;
        User            user;
        Tutor           tutor;
        String          tmp;
        ImageHandler    handler;
        AsyncDownloader asyncDownloader;
        JSONObject      response;

        user    = User.getInstance(getActivity());
        tutor   = user.getTutor();

        try{
            response = (JSONObject) resp;

            if(response.has("tutor_profile_image_url"))
            {
                //nothing for now
            }
            if(response.has("tutor_courses"))
            {
                cUtility    = new TutorClassesUtility(user, null, this.getActivity().getApplicationContext());
                cUtility.formatClassesFrontEnd(response.getString("tutor_courses"));
            }

            if(response.has("rate")) {
                tmp = response.getString("rate");
                tutor.setRate(tmp);
            }

            if(response.has("about")) {
                tmp = response.getString("about");
                tutor.setAbout(tmp);
            }

            if(response.has("courses_tutored")){
                //TODO implement
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void setProgressDialog(ProgressDialog pd) {
        //nothing

    }
}

