package tutor.cesh.profile.transiant;

import android.content.Context;


/**
 * Created by michaelkapnick on 3/25/15.
 */
public class UserEditMode
{
    private StudentEditMode studentEditMode;
    private TutorEditMode   tutorEditMode;

    private static UserEditMode userEditMode;

    private UserEditMode(Context context)
    {
        this.studentEditMode = new StudentEditMode(context);
        this.tutorEditMode  = new TutorEditMode(context);
    }
    private UserEditMode(StudentEditMode s, TutorEditMode t)
    {
        this.studentEditMode = s;
        this.tutorEditMode = t;
    }

    public StudentEditMode getStudentEditMode() {
        return studentEditMode;
    }

    public TutorEditMode getTutorEditMode() {
        return tutorEditMode;
    }

    public static UserEditMode getInstance(Context context)
    {
        if(userEditMode == null)
            userEditMode = new UserEditMode(context);

        return userEditMode;
    }
}
