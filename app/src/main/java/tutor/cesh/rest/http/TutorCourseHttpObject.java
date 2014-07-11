package tutor.cesh.rest.http;

import tutor.cesh.Student;
import tutor.cesh.User;

/**
 * Created by michaelk18 on 7/10/14.
 */
public class TutorCourseHttpObject extends CourseHttpObject
{
    private static final String POST = "http://blankcanvas.pw/tutorcourses/";
    private static final String PUT = "http://blankcanvas.pw/tutorcourses/";
    private static final String GET = "http://blankcanvas.pw/join/tutorcourses/";

    public TutorCourseHttpObject(User user)
    {
        super(user,POST, PUT, GET);
    }

    public TutorCourseHttpObject(User user, String classes)
    {
        super(user,POST, PUT, GET, classes);
    }


    @Override
    public String getId()
    {
        Student student;
        student = this.user.getStudent();

        return student.getTutorId();
    }
}
