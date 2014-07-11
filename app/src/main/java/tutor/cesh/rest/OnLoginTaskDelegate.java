package tutor.cesh.rest;

import android.content.Context;
import android.content.Intent;
import android.os.NetworkOnMainThreadException;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import tutor.cesh.Student;
import tutor.cesh.User;
import tutor.cesh.profile.StudentProfileActivity;
import tutor.cesh.Tutor;

/**
 * Created by michaelk18 on 4/7/14.
 */
public class OnLoginTaskDelegate extends AbstractTaskDelegate
{

    private Context         context;

    public OnLoginTaskDelegate(Context context)
    {
        this.context    = context;
    }
    @Override
    public void taskCompletionResult(JSONObject object)
    {
        Intent intent;
        User user;
        Student student;
        Tutor tutor;

        System.out.println("Object is: " + object.toString());
        user = User.getInstance();
        student = user.getStudent();
        tutor = user.getTutor();

        try {
            if (object.has("confirm")) {
                if (object.getString("confirm").equalsIgnoreCase("true")) {
                    intent = new Intent(context, StudentProfileActivity.class);
                    intent.putExtra("id", object.getString("id"));
                    intent.putExtra("enrollId", object.getString("enroll_id"));
                    intent.putExtra("schoolId", object.getString("school_id"));
                    intent.putExtra("tutorId", object.getString("tutor_id"));
                    intent.putExtra("email", object.getString("email"));
                    intent.putExtra("firstName", object.getString("first_name"));
                    intent.putExtra("lastName", object.getString("last_name"));
                    intent.putExtra("about", object.getString("about"));
                    intent.putExtra("profileImage", object.getString("profile_image_url"));
                    intent.putExtra("coverImage", object.getString("cover_image_url"));

                    student.setId(object.getString("id"));
                    student.setEnrollId(object.getString("enroll_id"));
                    student.setTutorId(object.getString("tutor_id"));
                    tutor.setId(object.getString("tutor_id"));
                    student.setName(object.getString("first_name"));
                    student.setAbout(object.getString("about"));
                    student.setSchoolId(object.getString("school_id"));

                    this.pd.dismiss();
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Confirm your Email!", Toast.LENGTH_SHORT).show();
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
}
