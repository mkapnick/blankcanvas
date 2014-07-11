package tutor.cesh.rest.http;

import tutor.cesh.Student;
import tutor.cesh.User;

/**
 * Created by michaelk18 on 7/10/14.
 */
public class StudentCourseHttpObject extends CourseHttpObject
{
    private static final String POST = "http://blankcanvas.pw/studentcourses/";
    private static final String PUT = "blankcanvas.pw/studentcourses/";
    private static final String GET = "http://blankcanvas.pw/join/studentcourses/";

    public StudentCourseHttpObject(User student)
    {
        super(student,POST, PUT, GET);
    }

    public StudentCourseHttpObject(User student, String classes)
    {
        super(student,POST, PUT, GET, classes);
    }


    @Override
    public String getId()
    {
        Student student;
        student = this.user.getStudent();

        return student.getId();

    }
}
