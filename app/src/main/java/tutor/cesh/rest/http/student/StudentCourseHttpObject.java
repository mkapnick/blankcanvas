package tutor.cesh.rest.http.student;

import java.util.List;

import tutor.cesh.profile.User;
import tutor.cesh.rest.apisecurity.APIEndpoints;
import tutor.cesh.rest.http.CourseHttpObject;

/**
 * Created by michaelk18 on 7/10/14.
 */
public class StudentCourseHttpObject extends CourseHttpObject
{
    public StudentCourseHttpObject(User user, List<String> courses)
    {
        super(user, courses);
    }

    @Override
    public String getEndPoint()
    {
        return APIEndpoints.getSTUDENTS_COURSES_ENDPOINT() + "/" + super.user.getStudent().getId() + "/" + "current";
    }
}
