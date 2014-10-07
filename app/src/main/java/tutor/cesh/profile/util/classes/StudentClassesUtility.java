package tutor.cesh.profile.util.classes;

import android.content.Context;
import android.widget.EditText;

import tutor.cesh.Student;
import tutor.cesh.User;

/**
 * Created by michaelk18 on 7/10/14.
 */
public class StudentClassesUtility extends ClassesUtility
{
    public StudentClassesUtility(User user, EditText textBox, Context context)
    {
        super(user, textBox, context);
    }

    public StudentClassesUtility(User user, EditText textBox)
    {
        super(user, textBox);
    }

    @Override
    public void setClasses(String[] classesFromServer)
    {
        Student student;
        student = super.user.getStudent();

        student.setClasses(classesFromServer);
    }

    @Override
    public String[] getClasses()
    {
        Student student;
        student = super.user.getStudent();

        return student.getClasses();
    }
}
