package tutor.cesh.database;

import android.os.Bundle;

import java.util.ArrayList;

import tutor.cesh.MasterStudent;
import tutor.cesh.Student;

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

    public static MasterStudent getMasterStudent()
    {
        return MasterStudent.getInstance();
    }

    public static void updateMasterStudent(Bundle info)
    {
        MasterStudent ms = getMasterStudent();

        if(info.containsKey("firstName"))
            ms.setName(info.getString("firstName"));
        if(info.containsKey("major"))
            ms.setMajor(info.getString("major"));
        if(info.containsKey("year"))
            ms.setYear(info.getString("year"));
        if(info.containsKey("about"))
            ms.setAbout(info.getString("about"));
        if(info.containsKey("profileImage"))
            ms.setProfileImagePath(info.getString("profileImage"));
        if(info.containsKey("coverImage"))
            ms.setCoverImagePath(info.getString("coverImage"));
    }
}
