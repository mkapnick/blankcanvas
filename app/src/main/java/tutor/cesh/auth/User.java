package tutor.cesh.auth;

/**
 * Created by michaelkapnick on 1/27/15.
 * */
public class User
{
    public static String encrypt(String plainText) throws Exception
    {
        UserSecurity userSecurity;
        userSecurity = new UserSecurity();

        return userSecurity.encrypt(plainText);
    }

}
