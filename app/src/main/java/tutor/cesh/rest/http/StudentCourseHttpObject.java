package tutor.cesh.rest.http;

import tutor.cesh.User;

/**
 * Created by michaelk18 on 7/10/14.
 */
public class StudentCourseHttpObject extends CourseHttpObject
{
    private String postEndPoint = "http://blankcanvas.pw/students/";
    private String putEndPoint  = "http://blankcanvas.pw/students/";
    private String getEndPoint  = "http://blankcanvas.pw/students/";


    public StudentCourseHttpObject(User user)
    {
        super(user);
    }

    public StudentCourseHttpObject(User user, String classes)
    {
        super(user, classes);
    }

    public String getGetEndPoint()
    {
        return getEndPoint + this.user.getStudent().getId() + "/view/student_courses";
    }

    public String getPostEndpoint()
    {
        return postEndPoint + this.user.getStudent().getId() + "/view/student_courses";
    }

    public String getPutEndpoint()
    {
        return putEndPoint + this.user.getStudent().getId() + "/view/student_courses";
    }
}
