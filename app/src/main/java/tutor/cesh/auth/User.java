package tutor.cesh.auth;

/**
 * Created by michaelkapnick on 1/27/15.
 * */
public class User
{
    public static byte [] encrypt(String plainText) throws Exception
    {
        UserSecurity userSecurity;
        userSecurity = new UserSecurity();

        return userSecurity.encrypt(plainText);
    }
    /**
     *
     * @param
     * @return
     * @throws Exception
     */
    public static String decrypt(byte [] encryptedBytes) throws Exception
    {
        UserSecurity userSecurity;
        userSecurity = new UserSecurity();

        return userSecurity.decrypt(encryptedBytes);
    }
}
