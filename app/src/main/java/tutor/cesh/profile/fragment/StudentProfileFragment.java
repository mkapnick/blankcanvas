package tutor.cesh.profile.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.client.methods.HttpPut;
import org.json.JSONException;
import org.json.JSONObject;

import tutor.cesh.R;
import tutor.cesh.Student;
import tutor.cesh.User;
import tutor.cesh.image.BitmapHandlerFactory;
import tutor.cesh.profile.util.classes.ClassesUtility;
import tutor.cesh.profile.util.classes.StudentClassesUtility;
import tutor.cesh.rest.RestClientExecute;
import tutor.cesh.rest.TaskDelegate;
import tutor.cesh.rest.http.StudentHttpObject;


public class StudentProfileFragment extends Fragment implements TaskDelegate, View.OnClickListener
{
    public ImageView                profileImageView, coverImageView;
    public EditText                 name, major, year, about, classes;
    private ImageButton             cameraIcon;
    private static final int        COVER_IMAGE_REQUEST_CODE = 1;
    private String                  profileImagePath, coverImagePath;

    public String getAbout() {
        return about.getText().toString();
    }
    public String getClasses() {
        return classes.getText().toString();
    }

    public String getMajor() {
        return major.getText().toString();
    }

    public String getName() {
        return name.getText().toString();
    }

    public String getYear() {
        return year.getText().toString();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        User                user;
        StudentHttpObject   studentHttp;
        HttpPut             put;


        if(requestCode == COVER_IMAGE_REQUEST_CODE)
        {
            updateBackgroundImage(data); //update the background image immediately

            //make a call to the server and update the image, we need to update the image on
            //the server because there is no save button, so like the web this is like our ajax call
            user        = User.getInstance(getActivity());
            studentHttp = new StudentHttpObject(user);
            studentHttp.setCoverImagePath(coverImagePath);
            put         = studentHttp.putStudentCoverImage();
            new RestClientExecute(put).start();
        }

    }

    /**
     *
     */
    private void initializeUI(View inflatedView)
    {
        User    user;
        Student student;

        user    = User.getInstance(getActivity().getApplicationContext());
        student = user.getStudent();

        name                = (EditText) inflatedView.findViewById(R.id.name); //TODO-- fix this!
        major               = (EditText) inflatedView.findViewById(R.id.major);
        year                = (EditText) inflatedView.findViewById(R.id.year);
        about               = (EditText) inflatedView.findViewById(R.id.about);
        classes             = (EditText)inflatedView.findViewById(R.id.classes);
        profileImageView    = (ImageView) inflatedView.findViewById(R.id.profileImage);

        coverImageView      = (ImageView) inflatedView.findViewById(R.id.profileBackgroundImage);
        cameraIcon          = (ImageButton) inflatedView.findViewById(R.id.cameraIcon);

        cameraIcon.setOnClickListener(this);
        //immediately set the cover image view
        student.setCoverImageView(coverImageView);
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
    }

    @Override
    public void onClick(View v)
    {
        Intent intent;

        switch(v.getId())
        {
            case R.id.cameraIcon:
                intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, COVER_IMAGE_REQUEST_CODE);
                break;
        }
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
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onResume(){
        super.onResume();
        setUpUserInfo();
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


    /**
     *
     * @param data
     */
    private void updateBackgroundImage(Intent data)
    {
        Bitmap  bitmap;
        int     orientation;
        Matrix  m;
        User    user;
        Student student;

        user = User.getInstance(getActivity());
        student = user.getStudent();

        try
        {
            coverImagePath = data.getData().getPath();
            System.out.println(coverImagePath);
            if (coverImagePath != null)
            {
                orientation = BitmapHandlerFactory.getOrientation(getActivity(), data.getData());
                bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(data.getData()));
                if (orientation > 0)
                {
                    m = new Matrix();
                    m.postRotate(orientation);
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                            bitmap.getHeight(), m, true);
                }
                Uri uri = BitmapHandlerFactory.getImageUri(getActivity(), bitmap);
                coverImagePath = BitmapHandlerFactory.getRealPathFromURI(uri, getActivity());

                //NEED THIS STEP, DO NOT DELETE!
                student.setCoverImage(bitmap);
                coverImageView.setBackground(new BitmapDrawable(getResources(), student.getCoverImage()));
            }
        }
        catch (Exception e)
        {
            System.out.println("IN EXCEPTION IN STUDENT PROF FRAGMENT");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
