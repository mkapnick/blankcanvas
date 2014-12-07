package tutor.cesh.profile.fragment;

import android.app.ProgressDialog;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import tutor.cesh.R;
import tutor.cesh.Student;
import tutor.cesh.User;
import tutor.cesh.database.GlobalDatabaseHelper;
import tutor.cesh.profile.util.classes.ClassesUtility;
import tutor.cesh.profile.util.classes.StudentClassesUtility;
import tutor.cesh.rest.TaskDelegate;


public class StudentProfileFragment extends FragmentTabController implements TaskDelegate, FragmentTabBehavior
{
    public ImageView                profileImageView, coverImageView;
    public EditText                 name, major, year, about, classes;
    private ImageButton             cameraIcon;
    static final String             LOG_TAG = "SlidingTabsBasicFragment";
    private boolean                 downloadedCoverImageFromServer  = false;
    private boolean                 downloadedDataFromServer        = false;




    @Override
    public void downloadCoverImageFromServer()
    {
        GlobalDatabaseHelper globalDatabaseHelper;
        globalDatabaseHelper        = new GlobalDatabaseHelper(super.activity);

        if(!downloadedCoverImageFromServer)
        {
            globalDatabaseHelper.downloadStudentCoverImageFromServer(super.getGeneralResources(),
                    this.coverImageView);
            downloadedCoverImageFromServer = true;
        }

    }

    @Override
    public void downloadDataFromServer()
    {
        GlobalDatabaseHelper globalDatabaseHelper;
        globalDatabaseHelper        = new GlobalDatabaseHelper(super.activity);

        if(!downloadedDataFromServer)
        {
            globalDatabaseHelper.downloadStudentDataFromServer(this);
            downloadedDataFromServer = true;
        }
    }

    @Override
    public String getAbout() {
        return about.getText().toString();
    }

    @Override
    public String getClasses() {
        return classes.getText().toString();
    }



    @Override
    public ImageView getCoverImageView() {
        return this.coverImageView;
    }

    @Override
    public int getLayout() {
        return R.layout.pager_student_fragment;
    }

    @Override
    public String getMajor() {
        return major.getText().toString();
    }

    @Override
    public String getName() {
        return name.getText().toString();
    }

    @Override
    public String getTabName(){
        return "Student";
    }
    @Override
    public CharSequence getTitle()
    {
        Drawable image = super.getGeneralResources().getDrawable(R.drawable.student_dark);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        SpannableString sb = new SpannableString(" ");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return sb;
    }

    @Override
    public String getYear() {
        return year.getText().toString();
    }

    @Override
    public void initializeUI(View inflatedView)
    {
        User    user;
        Student student;

        user    = User.getInstance(super.activity.getApplicationContext());
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
    public void setProgressDialog(ProgressDialog pd) {
        //nothing
    }

    @Override
    public void setUpUserInfo()
    {
        ClassesUtility  cUtility;
        User            user;
        Student         student;

        user            = User.getInstance(super.activity);
        student         = user.getStudent();

        //set fields based on data from the updated bundle
        name.setText(student.getName(), TextView.BufferType.EDITABLE);
        major.setText(student.getMajor(), TextView.BufferType.EDITABLE);
        year.setText(student.getYear(), TextView.BufferType.EDITABLE);
        about.setText(student.getAbout(), TextView.BufferType.EDITABLE);
        coverImageView.setBackground(new BitmapDrawable(super.getGeneralResources(), student.getCoverImage()));

        //set courses front end
        cUtility = new StudentClassesUtility(user, classes, super.activity);
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

        user    = User.getInstance(super.activity);
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
                cUtility    = new StudentClassesUtility(user, this.classes, super.activity.getApplicationContext());
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
