package tutor.cesh.profile.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import tutor.cesh.R;
import tutor.cesh.profile.Student;
import tutor.cesh.profile.User;
import tutor.cesh.database.DownloadImageHelper;
import tutor.cesh.profile.fragment.observer.TabObserver;
import tutor.cesh.profile.fragment.subject.TabSubject;
import tutor.cesh.profile.util.classes.ClassesUtility;


public class StudentProfileFragment extends FragmentTabController implements TabObserver
{
    public ImageView                profileImageView, coverImageView;
    public EditText                 name, major, year, about, classes, minor;
    private ImageButton             cameraIcon;


    @Override
    public void downloadCoverImageFromServer()
    {
        DownloadImageHelper     downloadImageHelper;
        User                    user;
        Student                 student;

        user                    = User.getInstance(super.activity);
        student                 = user.getStudent();

        if(null == student.getCoverImage())
        {
            downloadImageHelper = new DownloadImageHelper(super.activity);
            downloadImageHelper.downloadStudentCoverImageFromServer(super.getGeneralResources(),
                                                                     this.coverImageView);
        }
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
    public String getTabName(){
        return "Student";
    }
    @Override
    public CharSequence getTitle()
    {
        /*Drawable image = super.getGeneralResources().getDrawable(R.drawable.student_dark);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        SpannableString sb = new SpannableString(" ");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                return sb;
        */

        return "Student";
    }

    @Override
    public void initializeUI(View inflatedView)
    {
        User    user;
        Student student;

        user    = User.getInstance(super.activity.getApplicationContext());
        student = user.getStudent();

        name                = (EditText) inflatedView.findViewById(R.id.name);
        major               = (EditText) inflatedView.findViewById(R.id.major);
        minor               = (EditText) inflatedView.findViewById(R.id.minor);
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
    public void setUpUserInfo()
    {
        ArrayList<String>   currentClasses;
        User                user;
        Student             student;

        user            = User.getInstance(super.activity);
        student         = user.getStudent();
        currentClasses  = student.getCurrentClasses();

        //set fields based on data from the updated bundle
        name.setText(student.getName(), TextView.BufferType.EDITABLE);
        major.setText(student.getMajor(), TextView.BufferType.EDITABLE);
        minor.setText(student.getMinor(), TextView.BufferType.EDITABLE);
        year.setText(student.getYear(), TextView.BufferType.EDITABLE);
        about.setText(student.getAbout(), TextView.BufferType.EDITABLE);
        coverImageView.setBackground(new BitmapDrawable(super.getGeneralResources(),
                                                        student.getCoverImage()));

        if(currentClasses.size() > 0)
            ClassesUtility.formatClassesFrontEnd(currentClasses.iterator(),
                                                 super.activity, this.classes);
        else
            this.classes.setText("");
    }

    @Override
    public void updateFrontEnd()
    {
        setUpUserInfo();
    }

    @Override
    public void setTabSubject(TabSubject subject)
    {
        subject.addObserver(this);
    }
}
