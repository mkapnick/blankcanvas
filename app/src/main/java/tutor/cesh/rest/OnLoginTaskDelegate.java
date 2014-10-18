package tutor.cesh.rest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.NetworkOnMainThreadException;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        JSONArray   jsonArray;

        user = User.getInstance(this.context);
        student = user.getStudent();
        tutor = user.getTutor();

        try
        {
            jsonArray   = (JSONArray) obj;
            object      = jsonArray.getJSONObject(0);

            if (object.has("confirm")) {
                if (object.getString("confirm").equalsIgnoreCase("true"))
                {
                    intent = new Intent(context, StudentTutorProfileContainerActivity.class);
                    intent.putExtra("id", object.getString("id"));
                    intent.putExtra("enrollId", object.getString("enroll_id"));
                    intent.putExtra("schoolId", object.getString("school_id"));
                    intent.putExtra("email", object.getString("email"));
                    intent.putExtra("firstName", object.getString("first_name"));
                    intent.putExtra("lastName", object.getString("last_name"));
                    intent.putExtra("about", object.getString("about"));
                    intent.putExtra("profileImage", object.getString("student_profile_image_url"));
                    intent.putExtra("coverImage", object.getString("student_cover_image_url"));

                    student.setId(object.getString("id"));
                    student.setEnrollId(object.getString("enroll_id"));
                    student.setName(object.getString("first_name"));
                    student.setAbout(object.getString("about"));
                    student.setSchoolId(object.getString("school_id"));
                    student.setCoverImageUrl(object.getString("student_cover_image_url"));

                    tutor.setCoverImageUrl(object.getString("tutor_cover_image_url"));

                    context.startActivity(intent);

                } else {
                    Toast.makeText(context, "Please confirm your email", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Email or password incorrect", Toast.LENGTH_SHORT).show();
            }
        }
        catch(NetworkOnMainThreadException e)
        {
            e.printStackTrace();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void setProgressDialog(ProgressDialog pd) {
        //nothing
    }
}
