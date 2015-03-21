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
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.client.methods.HttpPost;

import java.util.ArrayList;
import java.util.List;

import tutor.cesh.Profile;
import tutor.cesh.R;
import tutor.cesh.Student;
import tutor.cesh.Tutor;
import tutor.cesh.User;
import tutor.cesh.google.SlidingTabLayout;
import tutor.cesh.image.BitmapHandlerFactory;
import tutor.cesh.list.TutorListActivity;
import tutor.cesh.profile.EditStudentAndTutorProfileActivity;
import tutor.cesh.profile.fragment.observer.TabObserver;
import tutor.cesh.profile.fragment.subject.TabSubject;
import tutor.cesh.rest.asynchronous.RestClientExecute;
import tutor.cesh.rest.http.student.StudentHttpObject;
import tutor.cesh.rest.http.tutor.TutorHttpObject;

/**
 * Created by michaelk18 on 12/4/14.
 */
public class FragmentTabController extends Fragment implements View.OnClickListener, TabSubject
{
    private String                  profileImagePath;
    private SlidingTabLayout        slidingTabLayout;
    private ViewPager               viewPager;
    private static final int        COVER_IMAGE_REQUEST_CODE    = 1;
    private static final int        EDIT_INFO                   = 2;
    private SamplePagerAdapter      samplePagerAdapter;
    protected static Activity       activity;
    private TextView                arrowBackTextView;
    private TextView                editButton;
    private DrawerLayout            drawerLayout;
    private ListView                drawerLayoutListView;
    private ArrayList<TabObserver>  tabObservers = new ArrayList<TabObserver>();
    private static TextView         tutorStatusCircle;

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
        HttpPost            post;
        int                 position;
        String              coverImagePath; //marks the current tab

        position = this.viewPager.getCurrentItem();

        //if(resultCode != 0)
        //{
            if(requestCode == COVER_IMAGE_REQUEST_CODE)
            {
                user        = User.getInstance(activity);

                //determine which tab we are in
                if(this.samplePagerAdapter.getTabs().get(position).getTabName().equalsIgnoreCase("Student"))
                {
                    //update the background image immediately
                    coverImagePath = updateBackgroundImage(data, this.samplePagerAdapter.getTabs().
                                    get(position).getCoverImageView(),
                            user.getStudent());

                    //make a call to the server and update the image, we need to update the image on
                    //the server because there is no save button, so like the web this is like our ajax call
                    studentHttp = new StudentHttpObject(user);
                    post         = studentHttp.postStudentCoverImage(coverImagePath);
                    new RestClientExecute(post).start();
                }
                else
                {
                    //update the background image immediately
                    coverImagePath = updateBackgroundImage(data, this.samplePagerAdapter.getTabs().
                                    get(position).getCoverImageView(),
                            user.getTutor());

                    //make a call to the server and update the image, we need to update the image on
                    //the server because there is no save button, so like the web this is like our ajax call
                    tutorHttp   = new TutorHttpObject(user);
                    post        = tutorHttp.postTutorCoverImage(coverImagePath);
                    new RestClientExecute(post).start();
                }
            }

            else if(requestCode == EDIT_INFO)
            {
                notifyObservers();
            }
        //}
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
                intent = new Intent(Intent.ACTION_GET_CONTENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activity.startActivityForResult(intent, COVER_IMAGE_REQUEST_CODE);
                break;

            case R.id.edit_action_bar_icon:
                intent = setIntentOnEdit();
                activity.startActivityForResult(intent, EDIT_INFO);
                break;

            case R.id.arrow_back_image:
                intent = new Intent(activity, TutorListActivity.class);
                activity.startActivity(intent);
                activity.finish();
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        //view containing the fragments UI
        View v;

        v = inflater.inflate(R.layout.fragment_student_tutor_profile_container, container, false);

        //set up the image buttons found in the action bar
        this.editButton             = (TextView)v.findViewById(R.id.edit_action_bar_icon);
        this.arrowBackTextView      = (TextView)v.findViewById(R.id.arrow_back_image);
        this.tutorStatusCircle      = (TextView)v.findViewById(R.id.tutorStatusCircle);

        this.editButton.setOnClickListener(this);
        this.arrowBackTextView.setOnClickListener(this);

        //set up view pager and sliding tab layout
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        viewPager.setAdapter(this.samplePagerAdapter);

        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // it's PagerAdapter set.
        slidingTabLayout = (SlidingTabLayout) v.findViewById(R.id.sliding_tabs);
        slidingTabLayout.setCustomTabView(R.layout.tab_custom_layout, 0);
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
        Bundle  bundle;
        Intent  intent;
        User    user;
        Tutor   tutor;
        Student student;
        String  name, major, minor, year, studentAbout,
                tutorAbout, studentCurrentClasses, tutorCurrentClasses, rate;
        boolean isPublic;

        ArrayList<String> studentCurrentClassesList, tutorCurrentClassesList;

        intent                  = null;
        user                    = User.getInstance(activity);
        student                 = user.getStudent();
        tutor                   = user.getTutor();
        studentCurrentClasses   = "";
        tutorCurrentClasses     = "";

        try
        {
            intent                      = new Intent(activity, EditStudentAndTutorProfileActivity.class);
            bundle                      = new Bundle();
            name                        = student.getName();
            major                       = student.getMajor();
            rate                        = tutor.getRate();
            isPublic                    = tutor.isPublic();
            //minor                     = this.studentProfileFragment.getMinor();
            year                        = student.getYear();
            studentAbout                = student.getAbout();
            tutorAbout                  = tutor.getAbout();
            studentCurrentClassesList   = student.getCurrentClasses();
            tutorCurrentClassesList     = tutor.getCurrentClasses();

            if(studentCurrentClassesList.size() > 0)
            {
                for(String c: studentCurrentClassesList)
                    studentCurrentClasses += c + " ";
            }

            if(tutorCurrentClassesList.size() > 0)
            {
                for(String c: tutorCurrentClassesList)
                    tutorCurrentClasses += c + " ";
            }

            bundle.putString("name", name);
            bundle.putString("major", major);
            bundle.putString("year", year);
            bundle.putString("studentAbout", studentAbout);
            bundle.putString("tutorAbout", tutorAbout);
            bundle.putString("studentCurrentClasses", studentCurrentClasses);
            bundle.putString("tutorCurrentClasses", tutorCurrentClasses);
            bundle.putString("rate", rate);
            bundle.putBoolean("isPublic", isPublic);

            intent.putExtras(bundle);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return intent;
    }

    public void setSamplePagerAdapter(List<TabObserver> list)
    {
        this.samplePagerAdapter = new SamplePagerAdapter(activity, activity, list);
    }


    public static void setStaticActivity(Activity a)
    {
        activity = a;
    }

    /**
     *
     * @param data
     */
    private String updateBackgroundImage(Intent data, ImageView coverImageView, Profile profile)
    {
        Bitmap  bitmap;
        int     orientation;
        Matrix  m;
        Uri     uri;
        String  coverImagePath;

        coverImagePath = "";
        try
        {
            coverImagePath = data.getData().getPath();

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
                uri = BitmapHandlerFactory.getImageUri(activity, bitmap);
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

        return coverImagePath;
    }

    @Override
    public void addObserver(TabObserver observer)
    {
        this.tabObservers.add(observer);
    }

    @Override
    public void notifyObservers()
    {
        for(TabObserver observer: this.tabObservers)
            observer.updateFrontEnd();
    }

    @Override
    public void removeObserver(TabObserver observer)
    {
        if(this.tabObservers.contains(observer))
            this.tabObservers.remove(observer);
    }

    /**
     *
     * @param activate
     */
    public static void setTutorActivated(boolean activate)
    {
        if(activate)
        {
            tutorStatusCircle.setBackgroundResource(R.drawable.circle_green);
        }
        else
        {
            tutorStatusCircle.setBackgroundResource(R.drawable.circle_red);
        }
    }
}
