package tutor.cesh.rest.delegate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.NetworkOnMainThreadException;
import android.widget.Toast;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import tutor.cesh.apisecurity.APIAuthorization;
import tutor.cesh.profile.Student;
import tutor.cesh.profile.Tutor;
import tutor.cesh.profile.User;
import tutor.cesh.list.TutorListActivity;
import tutor.cesh.apisecurity.APIEndpoints;
import tutor.cesh.rest.asynchronous.AsyncGet;
import tutor.cesh.session.SessionManager;

/**
 * Created by michaelk18 on 4/7/14.
 */
public class OnLoginTaskDelegate implements TaskDelegate
{
    private Context         context;
    private String          email, password;
    private boolean         branch;
    private SessionManager  sessionManager;

    public OnLoginTaskDelegate(Context context, String email, String password, boolean branch)
    {
        this.context    = context;
        this.email      = email;
        this.password   = password;
        this.branch     = branch;
        this.sessionManager = new SessionManager(context);

    }

    /**
     *
     */
    private void populateMetaData()
    {
        HttpGet                 httpGet;
        MetaDataTaskDelegate    metaDataTaskDelegate;
        String                  path, method, jwt;

        path                    = APIEndpoints.getMETADATA_GET_ENDPOINT();
        method                  = "GET";
        jwt                     = APIAuthorization.getAuthorizationHeader(null, path, method);

        httpGet                 = new HttpGet(path);
        httpGet.setHeader("Authorization", jwt);

        metaDataTaskDelegate    = new MetaDataTaskDelegate(this.context);

        new AsyncGet(this.context, metaDataTaskDelegate, null).execute(httpGet);
    }

    /**
     *
     * @param object
     * @return
     * @throws JSONException
     */
    private Intent setIntent(JSONObject object) throws JSONException
    {
        Intent intent;

        intent = new Intent(context, TutorListActivity.class);

        intent.putExtra("id", object.getString("id"));
        intent.putExtra("enrollId", object.getString("enrollId"));
        intent.putExtra("schoolId", object.getString("schoolId"));
        intent.putExtra("email", this.email);
        intent.putExtra("firstName", object.getString("firstName"));
        intent.putExtra("lastName", object.getString("lastName"));
        intent.putExtra("about", object.getString("studentAbout"));
        intent.putExtra("profileImage", object.getString("studentProfileImageUrl"));
        intent.putExtra("coverImage", object.getString("studentCoverImageUrl"));

        return intent;
    }

    @Override
    public void setProgressDialog(ProgressDialog pd) {
        //nothing
    }

    /**
     *
     * @param student
     * @param object
     * @throws JSONException
     */
    private void setStudentData(Student student, JSONObject object) throws JSONException
    {
        JSONArray           studentCoursesArray, pastStudentCoursesArray;
        ArrayList<String>   studentCoursesArrayList;

        studentCoursesArrayList = new ArrayList<String>();

        student.setId(object.getString("id"));
        student.setEnrollId(object.getString("enrollId"));
        student.setName(object.getString("firstName"));
        student.setAbout(object.getString("studentAbout"));
        student.setSchoolId(object.getString("schoolId"));
        student.setCoverImageUrl(object.getString("studentCoverImageUrl"));
        student.setMajor(object.getString("major"));
        student.setMinor(object.getString("minor"));
        student.setYear(object.getString("graduationYear"));
        student.setSubscribed(object.getBoolean("subscribed"));
        student.setEmail(this.email);


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
    }

    /**
     *
     * @param tutor
     * @param object
     * @throws JSONException
     */
    private void setTutorData(Tutor tutor, JSONObject object) throws JSONException
    {
        JSONArray           tutorCoursesArray, pastTutorCoursesArray;
        ArrayList<String>   tutorCoursesArrayList;

        tutorCoursesArrayList = new ArrayList<String>();

        tutor.setCoverImageUrl(object.getString("tutorCoverImageUrl"));
        tutor.setId(object.getString("tutorId"));
        tutor.setAbout(object.getString("tutorAbout"));
        tutor.setRate(object.getString("tutorRate"));
        tutor.setRating(object.getString("tutorRating"));
        tutor.setPublic(object.getString("isPublic").equalsIgnoreCase("true") ? true : false);

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
    }

    @Override
    public void taskCompletionResult(Object response)
    {
        Intent          intent;
        User            user;
        Student         student;
        Tutor           tutor;
        JSONObject      object;
        JSONArray       jsonArray;
        boolean         isAuthenticated;

        user            = User.getInstance(this.context);
        student         = user.getStudent();
        tutor           = user.getTutor();
        isAuthenticated = false;

        try
        {
            jsonArray   = (JSONArray) response;
            object      = jsonArray.getJSONObject(0);

            if (object.has("confirmed"))
            {
                if (object.getString("confirmed").equalsIgnoreCase("true"))
                {
                    isAuthenticated = true;

                    //intent
                    intent = setIntent(object);

                    //student
                    setStudentData(student, object);

                    //tutor
                    setTutorData(tutor, object);

                    //get metadata from server
                    populateMetaData();

                    //start activity
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

        if(!isAuthenticated)
        {
            if(this.branch)
            {
                this.sessionManager.logOut();
                this.sessionManager.startLoginActivity();
            }
        }
    }
}
