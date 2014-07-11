package tutor.cesh.profile.classes;

import android.content.Context;
import android.widget.EditText;

import tutor.cesh.Tutor;
import tutor.cesh.User;

/**
 * Created by michaelk18 on 7/10/14.
 */
public class TutorClassesUtility extends ClassesUtility
{
    public TutorClassesUtility(User user, EditText textBox, Context context)
    {
        super(user, textBox, context);
    }

    public TutorClassesUtility(User user, EditText textBox)
    {
        super(user, textBox);
    }

    @Override
    public void setClasses(String[] classesFromServer)
    {
        Tutor tutor;
        tutor = super.user.getTutor();

        tutor.setClasses(classesFromServer);
    }

    @Override
    public String[] getClasses()
    {
        Tutor tutor;
        tutor = super.user.getTutor();

        return tutor.getClasses();

    }
}
