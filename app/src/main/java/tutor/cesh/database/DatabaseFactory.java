package tutor.cesh.database;

import android.os.Bundle;

import java.util.ArrayList;

import tutor.cesh.Student;
import tutor.cesh.Tutor;
import tutor.cesh.User;

/**
 * Created by michaelk18 on 6/26/14.
 */
public class DatabaseFactory
{
    public static ArrayList<Student> getAllUsersWithSchoolId(int schoolId)
    {
        return null;
    }

    public static ArrayList<Student> getAllUsersWithSimilarMajor(String major)
    {
        return null;
    }


    public static void updateObjects(Bundle info)
    {
        User user;
        Student student;
        Tutor tutor;

        user = User.getInstance();
        student = user.getStudent();
        tutor = user.getTutor();

        if(info.containsKey("firstName"))
            student.setName(info.getString("firstName"));
        if(info.containsKey("major"))
            student.setMajor(info.getString("major"));
        if(info.containsKey("year"))
            student.setYear(info.getString("year"));
        if(info.containsKey("about"))
            student.setAbout(info.getString("about"));
        if(info.containsKey("tutorabout"))
            tutor.setAbout(info.getString("tutorabout"));
        if(info.containsKey("rate"))
            tutor.setRate(info.getString("rate"));
        if(info.containsKey("profileImage"))
            student.setProfileImagePath(info.getString("profileImage"));
        if(info.containsKey("coverImage"))
            student.setCoverImagePath(info.getString("coverImage"));

    }
}
