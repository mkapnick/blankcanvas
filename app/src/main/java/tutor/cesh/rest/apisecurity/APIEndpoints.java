package tutor.cesh.rest.apisecurity;

/**
 * Created by michaelkapnick on 4/18/15.
 */
public class APIEndpoints
{
    private static final String DOMAIN                              = "http://blankcanvas.pw";
    private static final String tokenOne                            = "6wy9d32nqtsy2c74h96q390ahjnl2646d74488709114g8n39y19507m3163aee7";
    private static final String tokenTwo                            = "tw87c04k7tw5ae26986s874cdkk2160y8b4l6pp0x08417685x73d95nn8u158fj";

    private static final String METADATA_GET_ENDPOINT               = DOMAIN + "/api/v1/" + tokenOne + "/bc/" + tokenTwo + "/metadata/profile";
    private static final String AUTH_POST_ENDPOINT                  = DOMAIN + "/api/v1/" + tokenOne + "/bc/" + tokenTwo + "/auth";
    private static final String USER_NEW_POST_ENDPOINT              = DOMAIN + "/api/v1/" + tokenOne + "/bc/" + tokenTwo + "/users/new";
    private static final String ENROLLS_ENDPOINT                    = DOMAIN + "/api/v1/" + tokenOne + "/bc/" + tokenTwo + "/enrolls";

    /* STUDENTS */
    private static final String STUDENTS_ENDPOINT                   = DOMAIN + "/api/v1/" + tokenOne + "/bc/" + tokenTwo + "/students";
    private static final String STUDENTS_COURSES_ENDPOINT           = DOMAIN + "/api/v1/" + tokenOne + "/bc/" + tokenTwo + "/courses/students";
    private static final String STUDENTS_IMAGE_ENDPOINT             = DOMAIN + "/api/v1/" + tokenOne + "/bc/" + tokenTwo + "/images/students";

    /* TUTORS */
    private static final String TUTORS_ENDPOINT                     = DOMAIN + "/api/v1/" + tokenOne + "/bc/" + tokenTwo + "/tutors";
    private static final String TUTORS_COURSES_ENDPOINT             = DOMAIN + "/api/v1/" + tokenOne + "/bc/" + tokenTwo + "/courses/tutors";
    private static final String TUTORS_IMAGE_ENDPOINT               = DOMAIN + "/api/v1/" + tokenOne + "/bc/" + tokenTwo + "/images/tutors";

    
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


}
