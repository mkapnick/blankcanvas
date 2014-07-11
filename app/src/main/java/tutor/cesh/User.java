package tutor.cesh;

/**
 * Created by michaelk18 on 7/2/14.
 */
public class User
{
    private Student student;
    private Tutor tutor;

    private static User user;

    private User()
    {
        this.student = new Student();
        this.tutor  = new Tutor();
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

    public static User getInstance()
    {
        if(user == null)
            user = new User();

        return user;
    }
}
