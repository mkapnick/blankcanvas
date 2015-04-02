package tutor.cesh.rest.http.tutor;

import java.util.List;

import tutor.cesh.profile.User;
import tutor.cesh.rest.http.CourseHttpObject;

/**
 * Created by michaelk18 on 7/10/14.
 */
public class TutorCourseHttpObject extends CourseHttpObject
{
    private final String TUTOR_CURRENT_COURSES_ENDPOINT = "http://blankcanvas.pw/bc/courses/tutors/";

    public TutorCourseHttpObject(User user, List<String> courses)
    {
        super(user, courses);
    }

    @Override
    public String getEndPoint()
    {
        return TUTOR_CURRENT_COURSES_ENDPOINT + super.user.getTutor().getId() + "/current";
    }


}
