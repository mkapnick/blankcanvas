package tutor.cesh.profile.fragment;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import tutor.cesh.R;
import tutor.cesh.Student;
import tutor.cesh.Tutor;
import tutor.cesh.User;
import tutor.cesh.database.GlobalDatabaseHelper;
import tutor.cesh.profile.fragment.observer.TabObserver;
import tutor.cesh.profile.fragment.subject.TabSubject;
import tutor.cesh.profile.util.classes.ClassesUtility;

public class TutorProfileFragment extends FragmentTabController implements TabObserver
{

    public  ImageView               profileImageView, coverImageView;
    private EditText                name, major, year, about, classes;
    private TextView                rate;
    private ImageButton             cameraIcon;
    private Switch                  tutorSwitch;
    private TextView                switchText;



    @Override
    public void downloadCoverImageFromServer()
    {
        GlobalDatabaseHelper    globalDatabaseHelper;
        User                    user;
        Tutor                   tutor;

        user                    = User.getInstance(super.activity);
        tutor                   = user.getTutor();

        if(null == tutor.getCoverImage())
        {
            globalDatabaseHelper= new GlobalDatabaseHelper(super.activity);
            globalDatabaseHelper.downloadTutorCoverImageFromServer(super.getGeneralResources(),
                    this.coverImageView);
        }
    }

    @Override
    public ImageView getCoverImageView() {
        return this.coverImageView;
    }

    @Override
    public int getLayout() {
        return R.layout.pager_tutor_fragment;
    }

    public String getRate() {
        return rate.getText().toString();
    }

    @Override
    public String getTabName(){
        return "Tutor";
    }
    @Override
    public CharSequence getTitle()
    {
        /*Drawable image = super.getGeneralResources().getDrawable(R.drawable.tutor_dark);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        SpannableString sb = new SpannableString(" ");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return sb;*/

        return "Tutor";
    }

    @Override
    public void initializeUI(View inflatedView)
    {
        name                = (EditText)    inflatedView.findViewById(R.id.name);
        major               = (EditText)    inflatedView.findViewById(R.id.major);
        year                = (EditText)    inflatedView.findViewById(R.id.year);
        about               = (EditText)    inflatedView.findViewById(R.id.about);
        classes             = (EditText)    inflatedView.findViewById(R.id.classes);
        rate                = (TextView)    inflatedView.findViewById(R.id.rate);
        profileImageView    = (ImageView)   inflatedView.findViewById(R.id.profileImage);
        coverImageView      = (ImageView)   inflatedView.findViewById(R.id.profileBackgroundImage);
        tutorSwitch         = (Switch)      inflatedView.findViewById(R.id.tutorSwitch);
        //switchText         = (ScrollView)  inflatedView.findViewById(R.id.tutorFragmentScrollView);

        cameraIcon          = (ImageButton) inflatedView.findViewById(R.id.cameraIcon);
        cameraIcon.setOnClickListener(this);

        setScrollViewBackground();
    }

    /**
     * Sets the tutor switch background heigh
     */
    private void setScrollViewBackground()
    {
        User user;
        Tutor tutor;

        user    = User.getInstance(activity);
        tutor   = user.getTutor();

        if(tutor.isPublic())
        {
            this.tutorSwitch.setChecked(true);
            //this.tutorSwitch.setVisibility(View.INVISIBLE);
        }
        else
        {
            //this.scrollView.setBackgroundColor(Color.parseColor("#000000"));
            //this.scrollView.setAlpha(.8f);
        }
    }

    @Override
    public void setUpUserInfo()
    {
        User                                        user;
        Student                                     student;
        Tutor                                       tutor;
        ArrayList<String>                           currentClasses;

        user            = User.getInstance(super.activity);
        student         = user.getStudent();
        tutor           = user.getTutor();
        currentClasses  = tutor.getCurrentClasses();

        name.setText(student.getName());
        major.setText(student.getMajor());
        year.setText(student.getYear());
        about.setText(tutor.getAbout());
        rate.setText(tutor.getRate());
        coverImageView.setBackground(new BitmapDrawable(super.getGeneralResources(),
                                                        tutor.getCoverImage()));

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

