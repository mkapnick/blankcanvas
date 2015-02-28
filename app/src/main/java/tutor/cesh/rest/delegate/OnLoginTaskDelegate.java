package tutor.cesh.rest.delegate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.NetworkOnMainThreadException;
import android.view.WindowManager;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import tutor.cesh.Student;
import tutor.cesh.Tutor;
import tutor.cesh.User;
import tutor.cesh.profile.StudentTutorProfileContainerActivity;

/**
 * Created by michaelk18 on 4/7/14.
 */
public class OnLoginTaskDelegate implements TaskDelegate
{

    private Context         context;

    public OnLoginTaskDelegate(Context context)
    {
        this.context    = context;
    }

    @Override
    public void taskCompletionResult(Object obj)
    {
        Intent      intent;
        User        user;
        Student     student;
        Tutor       tutor;
        JSONObject  object;
        JSONArray   jsonArray, studentCoursesArray, pastStudentCoursesArray,
                    tutorCoursesArray, pastTutorCoursesArray;

        ArrayList<String> studentCoursesArrayList, tutorCoursesArrayList;

        user                    = User.getInstance(this.context);
        student                 = user.getStudent();
        tutor                   = user.getTutor();
        studentCoursesArrayList = new ArrayList<String>();
        tutorCoursesArrayList   = new ArrayList<String>();

        try
        {
            jsonArray   = (JSONArray) obj;
            object      = jsonArray.getJSONObject(0);

            if (object.has("confirmed"))
            {
                if (object.getString("confirmed").equalsIgnoreCase("true"))
                {
                    intent = new Intent(context, StudentTutorProfileContainerActivity.class);

                    intent.putExtra("id", object.getString("id"));
                    intent.putExtra("enrollId", object.getString("enrollId"));
                    intent.putExtra("schoolId", object.getString("schoolId"));
                    intent.putExtra("email", object.getString("email"));
                    intent.putExtra("firstName", object.getString("firstName"));
                    intent.putExtra("lastName", object.getString("lastName"));
                    intent.putExtra("about", object.getString("studentAbout"));
                    intent.putExtra("profileImage", object.getString("studentProfileImageUrl"));
                    intent.putExtra("coverImage", object.getString("studentCoverImageUrl"));

                    //student
                    student.setId(object.getString("id"));
                    student.setEnrollId(object.getString("enrollId"));
                    student.setName(object.getString("firstName"));
                    student.setAbout(object.getString("studentAbout"));
                    student.setSchoolId(object.getString("schoolId"));
                    student.setCoverImageUrl(object.getString("studentCoverImageUrl"));
                    student.setMajor(object.getString("major"));
                    student.setYear(object.getString("graduationYear"));

                    //student courses
                    studentCoursesArray     = object.getJSONArray("studentCourses");
                    pastStudentCoursesArray = object.getJSONArray("pastStudentCourses");

                    if(studentCoursesArray.length() > 0)
                    {
                        for(int i =0; i < studentCoursesArray.length(); i++)
                            studentCoursesArrayList.add(studentCoursesArray.getString(i));

                        student.setCurrentClasses(studentCoursesArrayList);
                    }
                    else
                        student.setCurrentClasses(new ArrayList<String>());

                    //pastStudent courses

                    //tutor
                    tutor.setCoverImageUrl(object.getString("tutorCoverImageUrl"));
                    tutor.setId(object.getString("tutorId"));
                    tutor.setAbout(object.getString("tutorAbout"));
                    tutor.setRate(object.getString("tutorRate"));
                    tutor.setRating(object.getString("tutorRating"));

                    //tutor courses
                    tutorCoursesArray       = object.getJSONArray("tutorCourses");
                    pastTutorCoursesArray   = object.getJSONArray("pastTutorCourses");

                    if(tutorCoursesArray.length() > 0)
                    {
                        for(int i =0; i < tutorCoursesArray.length(); i++)
                            tutorCoursesArrayList.add(tutorCoursesArray.getString(i));

                        tutor.setCurrentClasses(tutorCoursesArrayList);
                    }
                    else
                        tutor.setCurrentClasses(new ArrayList<String>());

                    context.startActivity(intent);
                }
                else
                    Toast.makeText(context, "Please confirm your email", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(context, "Email or password incorrect", Toast.LENGTH_SHORT).show();
        }
        catch(NetworkOnMainThreadException e)
        {
            Toast.makeText(context, "Check network connection", Toast.LENGTH_SHORT).show();
        }
        catch (JSONException e)
        {
            Toast.makeText(context, "Email or password incorrect", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setProgressDialog(ProgressDialog pd) {
        //nothing
    }
}
