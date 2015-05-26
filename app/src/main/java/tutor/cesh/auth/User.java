package tutor.cesh.auth;

/**
 * Encapsulates encryption of data (data that is sent from client to server)
 *
 * @version v1.0
 * @author  Michael Kapnick
 * */
public class User
{
    /**
     * Encrypts data
     *
     * @param plainText The text to be encrypted
     * @return          The encrypted text
     *
     * @throws Exception
     */
    public static String encrypt(String plainText) throws Exception
    {
        UserSecurity userSecurity;
        userSecurity = new UserSecurity();

        return userSecurity.encrypt(plainText);
    }

}
