package tutor.cesh.rest.apisecurity;

/**
 * Created by michaelkapnick on 4/18/15.
 */
public class APIEndpoints
{
    private static final String DOMAIN                              = "http://blankcanvas.pw";
    private static final String tokenOne                            = "6wy9d32nqtsy2c74h96q390ahjnl2646d74488709114g8n39y19507m3163aee7";
    private static final String tokenTwo                            = "tw87c04k7tw5ae26986s874cdkk2160y8b4l6pp0x08417685x73d95nn8u158fj";

    private static final String METADATA_GET_ENDPOINT               = DOMAIN + "/api/v1/" + tokenOne + "/bc/" + tokenTwo + "/bc/metadata/profile";
    private static final String AUTH_POST_ENDPOINT                  = DOMAIN + "/api/v1/" + tokenOne + "/bc/" + tokenTwo + "/bc/auth";
    private static final String USER_NEW_POST_ENDPOINT              = DOMAIN + "/api/v1/" + tokenOne + "/bc/" + tokenTwo + "/bc/users/new";
    private static final String ENROLLS_ENDPOINT                    = DOMAIN + "/api/v1/" + tokenOne + "/bc/" + tokenTwo + "/bc/enrolls";
    private static final String TUTORS_SCHOOL_ENDPOINT              = DOMAIN + "/api/v1/" + tokenOne + "/bc/" + tokenTwo + "/bc/schools"; //TODO

    /* STUDENTS */
    private static final String STUDENTS_ENDPOINT                   = DOMAIN + "/api/v1/" + tokenOne + "/bc/" + tokenTwo + "/bc/students";
    private static final String STUDENTS_COURSES_ENDPOINT           = DOMAIN + "/api/v1/" + tokenOne + "/bc/" + tokenTwo + "/bc/courses/students";
    private static final String STUDENTS_IMAGE_ENDPOINT             = DOMAIN + "/api/v1/" + tokenOne + "/bc/" + tokenTwo + "/bc/images/students";

    /* TUTORS */
    private static final String TUTORS_ENDPOINT                     = DOMAIN + "/api/v1/" + tokenOne + "/bc/" + tokenTwo + "/bc/tutors";
    private static final String TUTORS_COURSES_ENDPOINT             = DOMAIN + "/api/v1/" + tokenOne + "/bc/" + tokenTwo + "/bc/courses/tutors";
    private static final String TUTORS_IMAGE_ENDPOINT               = DOMAIN + "/api/v1/" + tokenOne + "/bc/" + tokenTwo + "/bc/images/tutors";

    /*EMAIL SUBSCRIBERS */
    private static final String EMAIL_SUBSCRIBERS_ENDPOINT          = DOMAIN + "/api/v1/" + tokenOne + "/bc/" + tokenTwo + "/bc/notifications";


    public static String getAUTH_POST_ENDPOINT() {
        return AUTH_POST_ENDPOINT;
    }

    public static String getMETADATA_GET_ENDPOINT() {
        return METADATA_GET_ENDPOINT;
    }

    public static String getUSER_NEW_POST_ENDPOINT() {
        return USER_NEW_POST_ENDPOINT;
    }

    public static String getSTUDENTS_COURSES_ENDPOINT() {
        return STUDENTS_COURSES_ENDPOINT;
    }

    public static String getSTUDENTS_ENDPOINT() {
        return STUDENTS_ENDPOINT;
    }

    public static String getSTUDENTS_IMAGE_ENDPOINT() {
        return STUDENTS_IMAGE_ENDPOINT;
    }

    public static String getTUTORS_ENDPOINT() {
        return TUTORS_ENDPOINT;
    }

    public static String getTUTORS_COURSES_ENDPOINT() {
        return TUTORS_COURSES_ENDPOINT;
    }

    public static String getTUTORS_IMAGE_ENDPOINT() {
        return TUTORS_IMAGE_ENDPOINT;
    }

    public static String getENROLLS_ENDPOINT() {
        return ENROLLS_ENDPOINT;
    }

    public static String getTUTORS_SCHOOL_ENDPOINT() {
        return TUTORS_SCHOOL_ENDPOINT;
    }

    public static String getEMAIL_SUBSCRIBERS_ENDPOINT() {
        return EMAIL_SUBSCRIBERS_ENDPOINT;
    }

}
