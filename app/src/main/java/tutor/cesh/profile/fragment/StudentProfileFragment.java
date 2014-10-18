package tutor.cesh.profile.fragment;

import android.app.Activity;
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
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import tutor.cesh.R;
import tutor.cesh.Student;
import tutor.cesh.User;
import tutor.cesh.profile.util.classes.ClassesUtility;
import tutor.cesh.profile.util.classes.StudentClassesUtility;
import tutor.cesh.rest.TaskDelegate;


public class StudentProfileFragment extends Fragment implements TaskDelegate
{
    public static ImageView         profileImageView, coverImageView;
    public EditText                name, major, year, about, classes;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        setUpUserInfo();
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        //view containing the fragments UI
        View v;

        v = inflater.inflate(R.layout.fragment_student_profile, container, false);
        initializeUI(v);
        setUpUserInfo();

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onResume(){
        super.onResume();
        setUpUserInfo();
    }
    /**
     *
     */
    private void initializeUI(View inflatedView)
    {
        name = (EditText) inflatedView.findViewById(R.id.name); //TODO-- fix this!
        major = (EditText) inflatedView.findViewById(R.id.major);
        year = (EditText) inflatedView.findViewById(R.id.year);
        about = (EditText) inflatedView.findViewById(R.id.about);
        classes = (EditText)inflatedView.findViewById(R.id.classes);
        profileImageView = (ImageView) inflatedView.findViewById(R.id.profileImage);
        coverImageView = (ImageView) inflatedView.findViewById(R.id.profileBackgroundImage);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int position, id;

        id          = item.getItemId();
        position    = -1;

        if (id == R.id.action_settings)
            position = 2;
        else if(id == R.id.action_edit_profile)
            position = -100;
        else if(id == R.id.action_switch_profile)
            position = 1;

        onOptionsItemSelected(position);
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
        Intent  intent;

        intent = null;
        if(position == -100)
        {
            //intent = new Intent(this, EditStudentProfileActivity.class);
            //intent.putExtras(info);
            startActivityForResult(intent, 1);
        }

        else if(position == 1)
        {
            //drawerLayout.closeDrawer(listView);
            //intent = new Intent(this, TutorProfileActivity.class);
            //intent.putExtras(info);
            startActivity(intent);
            //finish(); //TODO don't finish on switch
        }
        else if (position == 2)
        {

        }
    }

    @Override
    public void setProgressDialog(ProgressDialog pd) {
        //nothing
    }

    /**
     *
     */
    private void setUpUserInfo()
    {
        ClassesUtility  cUtility;
        User            user;
        Student         student;

        user            = User.getInstance(getActivity());
        student         = user.getStudent();

        //set fields based on data from the updated bundle
        name.setText(student.getName(), TextView.BufferType.EDITABLE);
        major.setText(student.getMajor(), TextView.BufferType.EDITABLE);
        year.setText(student.getYear(), TextView.BufferType.EDITABLE);
        about.setText(student.getAbout(), TextView.BufferType.EDITABLE);
        coverImageView.setBackground(new BitmapDrawable(getResources(), student.getCoverImage()));

        //set courses front end
        cUtility = new StudentClassesUtility(user, this.classes, this.getActivity().getApplicationContext());
        cUtility.setClassesRegularMode();
    }

    @Override
    public void taskCompletionResult(Object resp)
    {
        //set fields based on JSON response from the server
        ClassesUtility  cUtility;
        User            user;
        Student         student;
        String          tmp;
        JSONObject      response;

        user    = User.getInstance(getActivity());
        student = user.getStudent();

        try
        {
            response = (JSONObject) resp;

            if(response.has("major")) {
                tmp = response.getString("major");
                major.setText(tmp, TextView.BufferType.EDITABLE);
                student.setMajor(tmp);
            }

            if(response.has("year")) {
                tmp = response.getString("year");
                year.setText(tmp, TextView.BufferType.EDITABLE);
                student.setYear(tmp);
            }

            if(response.has("student_courses"))
            {
                cUtility    = new StudentClassesUtility(user, this.classes, this.getActivity().getApplicationContext());
                cUtility.formatClassesFrontEnd(response.getString("student_courses"));
                cUtility.setClassesRegularMode();
            }

            if(response.has("courses_taken")){
                //TODO implement
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
