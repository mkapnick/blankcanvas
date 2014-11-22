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

import org.apache.http.client.methods.HttpPut;
import org.json.JSONObject;

import tutor.cesh.R;
import tutor.cesh.Student;
import tutor.cesh.Tutor;
import tutor.cesh.User;
import tutor.cesh.image.BitmapHandlerFactory;
import tutor.cesh.profile.util.classes.ClassesUtility;
import tutor.cesh.profile.util.classes.TutorClassesUtility;
import tutor.cesh.rest.RestClientExecute;
import tutor.cesh.rest.TaskDelegate;
import tutor.cesh.rest.http.TutorHttpObject;

public class TutorProfileFragment extends Fragment implements TaskDelegate, View.OnClickListener {

    public  ImageView               profileImageView, coverImageView;
    private EditText                name, major, year, about, classes, rate;
    private ImageButton             cameraIcon;
    private static final int        COVER_IMAGE_REQUEST_CODE = 1;
    private String                  profileImagePath, coverImagePath;
    private static Activity         activity;

    public String getAbout()
    {
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

    public String getRate() {
        return rate.getText().toString();
    }

    public String getYear() {
        return year.getText().toString();
    }


    /**
     *
     */
    private void initializeUI(View inflatedView)
    {
        User    user;
        Tutor   tutor;

        user    = User.getInstance(getActivity().getApplicationContext());
        tutor   = user.getTutor();

        name                = (EditText)    inflatedView.findViewById(R.id.name);
        major               = (EditText)    inflatedView.findViewById(R.id.major);
        year                = (EditText)    inflatedView.findViewById(R.id.year);
        about               = (EditText)    inflatedView.findViewById(R.id.about);
        classes             = (EditText)    inflatedView.findViewById(R.id.classes);
        rate                = (EditText)    inflatedView.findViewById(R.id.rate);
        profileImageView    = (ImageView)   inflatedView.findViewById(R.id.profileImage);
        coverImageView      = (ImageView)   inflatedView.findViewById(R.id.profileBackgroundImage);

        cameraIcon          = (ImageButton) inflatedView.findViewById(R.id.cameraIcon);

        cameraIcon.setOnClickListener(this);

        //immediately set the cover image view, don't need this here
        //tutor.setCoverImageView(coverImageView);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        User                user;
        TutorHttpObject     tutorHttp;
        HttpPut             put;

        if(requestCode == COVER_IMAGE_REQUEST_CODE)
        {
            updateBackgroundImage(data); //update the background image immediately

            //make a call to the server and update the image, we need to update the image on
            //the server because there is no save button, so like the web this is like our ajax call
            user        = User.getInstance(getActivity());
            tutorHttp   = new TutorHttpObject(user);
            tutorHttp.setCoverImagePath(coverImagePath);
            put         = tutorHttp.putTutorCoverImage();
            new RestClientExecute(put).start();
        }
    }

    @Override
    public void onClick(View v) {
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
        JSONObject      response;

        user    = User.getInstance(getActivity());
        tutor   = user.getTutor();

        try
        {
            response = (JSONObject) resp;

            if(response.has("tutor_profile_image_url"))
            {
                //nothing for now
            }
            if(response.has("tutor_courses"))
            {
                activity.getApplicationContext();
                cUtility    = new TutorClassesUtility(user, null, this.activity.getApplicationContext());
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

    public static void setActivity(Activity a)
    {
        activity = a;
    }

    @Override
    public void setProgressDialog(ProgressDialog pd) {
        //nothing

    }

    /**
     *
     * @param data
     */
    private void updateBackgroundImage(Intent data)
    {
        Bitmap bitmap;
        int     orientation;
        Matrix m;
        User    user;
        Tutor   tutor;

        user = User.getInstance(getActivity());
        tutor = user.getTutor();

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
                tutor.setCoverImage(bitmap);
                coverImageView.setBackground(new BitmapDrawable(getResources(), tutor.getCoverImage()));
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

