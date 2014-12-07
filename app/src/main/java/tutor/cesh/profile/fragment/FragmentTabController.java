package tutor.cesh.profile.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import org.apache.http.client.methods.HttpPut;

import java.util.List;

import tutor.cesh.Profile;
import tutor.cesh.R;
import tutor.cesh.Student;
import tutor.cesh.Tutor;
import tutor.cesh.User;
import tutor.cesh.google.SlidingTabLayout;
import tutor.cesh.image.BitmapHandlerFactory;
import tutor.cesh.profile.EditStudentAndTutorProfileActivity;
import tutor.cesh.rest.RestClientExecute;
import tutor.cesh.rest.http.StudentHttpObject;
import tutor.cesh.rest.http.TutorHttpObject;

/**
 * Created by michaelk18 on 12/4/14.
 */
public class FragmentTabController extends Fragment implements  View.OnClickListener
{
    private String                  profileImagePath, coverImagePath;
    private SlidingTabLayout        slidingTabLayout;
    private ViewPager               viewPager;
    private static final int        COVER_IMAGE_REQUEST_CODE    = 1;
    private static final int        EDIT_INFO                   = 2;
    private SamplePagerAdapter      samplePagerAdapter;
    protected static Activity                activity;
    private ImageButton             editButton, drawerLayoutButton;


    public Resources getGeneralResources()
    {
        return activity.getResources();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        User                user;
        StudentHttpObject   studentHttp;
        TutorHttpObject     tutorHttp;
        HttpPut             put;
        int                 position; //marks the current tab

        position = this.viewPager.getCurrentItem();


        if(requestCode == COVER_IMAGE_REQUEST_CODE)
        {
            user        = User.getInstance(activity);

            //determine which tab we are in
            if(this.samplePagerAdapter.getTabs().get(position).getTabName().equalsIgnoreCase("Student"))
            {
                updateBackgroundImage(data, this.samplePagerAdapter.getTabs().
                                                                get(position).getCoverImageView(),
                                                                user.getStudent());
                                                                //update the background image immediately

                //make a call to the server and update the image, we need to update the image on
                //the server because there is no save button, so like the web this is like our ajax call
                studentHttp = new StudentHttpObject(user);
                studentHttp.setCoverImagePath(coverImagePath);
                put         = studentHttp.putStudentCoverImage();
                new RestClientExecute(put).start();
            }
            else
            {
                updateBackgroundImage(data, this.samplePagerAdapter.getTabs().
                                            get(position).getCoverImageView(),
                                            user.getTutor());

                //make a call to the server and update the image, we need to update the image on
                //the server because there is no save button, so like the web this is like our ajax call
                tutorHttp   = new TutorHttpObject(user);
                tutorHttp.setCoverImagePath(coverImagePath);
                put         = tutorHttp.putTutorCoverImage();
                new RestClientExecute(put).start();
            }
        }
        else if(requestCode == EDIT_INFO)
        {
            //DO NOTHING...
        }

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
                activity.startActivityForResult(intent, COVER_IMAGE_REQUEST_CODE);
                break;

            case R.id.edit_action_bar_icon:
                intent = setIntentOnEdit();
                startActivityForResult(intent, EDIT_INFO);
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

        //set up the image buttons found in the action bar
        this.editButton             = (ImageButton)v.findViewById(R.id.edit_action_bar_icon);
        this.drawerLayoutButton     = (ImageButton)v.findViewById(R.id.left_action_bar_image);
        this.editButton.setOnClickListener(this);
        this.drawerLayoutButton.setOnClickListener(this);

        //set up view pager and sliding tab layout
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        viewPager.setAdapter(this.samplePagerAdapter);

        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // it's PagerAdapter set.
        slidingTabLayout = (SlidingTabLayout) v.findViewById(R.id.sliding_tabs);
        slidingTabLayout.setCustomTabView(R.layout.custom_tab, 0);
        slidingTabLayout.setViewPager(viewPager);

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
    }

    /**
     * Main entry point is when user clicks the edit button field
     */
    private Intent setIntentOnEdit()
    {
        Bundle bundle;
        Intent intent;
        User user;
        Tutor tutor;
        Student student;
        String name, major, minor, year, studentAbout,
                tutorAbout, classesTaking, classesTutoring, rate;

        String [] classesTutoringArray, classesTakingArray;
        intent = null;
        user = User.getInstance(activity);
        tutor = user.getTutor();
        student = user.getStudent();

        try
        {
            intent              = new Intent(activity, EditStudentAndTutorProfileActivity.class);
            bundle              = new Bundle();
            name                = student.getName();
            major               = student.getMajor();
            //minor             = this.studentProfileFragment.getMinor();
            year                = student.getYear();
            studentAbout        = student.getAbout();
            tutorAbout          = tutor.getAbout();
            classesTakingArray  = student.getClasses();

            classesTutoringArray= tutor.getClasses();
            classesTutoring     = "";
            classesTaking       = "";

            for (String studentClass: classesTakingArray)
                classesTaking   += studentClass + " ";

            for (String tutorClass: classesTutoringArray)
                classesTutoring += tutorClass + " ";

            rate                = tutor.getRate();
            bundle.putString("name", name);
            bundle.putString("major", major);
            bundle.putString("year", year);
            bundle.putString("studentAbout", studentAbout);
            bundle.putString("tutorAbout", tutorAbout);
            bundle.putString("classesTaking", classesTaking);
            bundle.putString("classesTutoring", classesTutoring);
            bundle.putString("rate", rate);
            intent.putExtras(bundle);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return intent;
    }

    public void setSamplePagerAdapter(List<FragmentTabBehavior> list)
    {
        this.samplePagerAdapter = new SamplePagerAdapter(activity, activity, list);
    }


    public static void setStaticActivity(Activity a){
        activity = a;
    }

    /**
     *
     * @param data
     */
    private void updateBackgroundImage(Intent data, ImageView coverImageView, Profile profile)
    {
        Bitmap  bitmap;
        int     orientation;
        Matrix  m;

        try
        {
            coverImagePath = data.getData().getPath();
            System.out.println(coverImagePath);
            if (coverImagePath != null)
            {
                orientation = BitmapHandlerFactory.getOrientation(activity, data.getData());
                bitmap = BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(data.getData()));
                if (orientation > 0)
                {
                    m = new Matrix();
                    m.postRotate(orientation);
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                            bitmap.getHeight(), m, true);
                }
                Uri uri = BitmapHandlerFactory.getImageUri(activity, bitmap);
                coverImagePath = BitmapHandlerFactory.getRealPathFromURI(uri, activity);

                //NEED THIS STEP, DO NOT DELETE!
                profile.setCoverImage(bitmap);
                coverImageView.setBackground(new BitmapDrawable(getGeneralResources(), profile.getCoverImage()));
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
