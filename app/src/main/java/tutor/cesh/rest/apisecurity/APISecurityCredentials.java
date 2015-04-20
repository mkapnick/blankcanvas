package tutor.cesh.rest.apisecurity;

import java.security.SecureRandom;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Created by michaelkapnick on 4/16/15.
 */
public class APISecurityCredentials
{
    private static final String encryptedTokenOne = "";
    private static final String encryptedTokenTwo = "";

    public static String getEncryptedTokenOne() {
        return encryptedTokenOne;
    }

    public static String getEncryptedTokenTwo() {
        return encryptedTokenTwo;
    }


    /**
     *
     *
     */


    /**
     *
     */
    public void test()
    {
        byte[] key = new byte[64];
        new SecureRandom().nextBytes(key);

        String compact = Jwts.builder().setSubject("Michael").signWith(SignatureAlgorithm.HS256, key).compact();

        System.out.print(compact);
    }
}
