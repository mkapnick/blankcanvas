package tutor.cesh;

/**
 * Created by michaelk18 on 1/26/14.
 */
public enum Query
{

    VALIDATE_LOGIN("", "");


    private String email;
    private String password;

    Query(String email, String password)
    {
        this.email      = email;
        this.password   = password;


    }

}
