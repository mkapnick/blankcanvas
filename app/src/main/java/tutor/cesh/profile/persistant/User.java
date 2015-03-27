package tutor.cesh.profile.persistant;

import android.content.Context;

/**
 * Created by michaelk18 on 7/2/14.
 */
public class User
{
    private Student student;
    private Tutor tutor;

    private static User user;

    private User(Context context)
    {
        this.student = new Student(context);
        this.tutor  = new Tutor(context);
    }
    private User(Student s, Tutor t)
    {
        this.student = s;
        this.tutor = t;
    }

    public Student getStudent() {
        return student;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public static User getInstance(Context context)
    {
        if(user == null)
            user = new User(context);

        return user;
    }
}
