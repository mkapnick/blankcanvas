package tutor.cesh.auth;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class UserSecurity
{
    private static final int    key = 1156185;

    /**
     *
     */
    public UserSecurity()
    {
        //do nothing
    }

    /**
     *
     * @param str
     * @return
     */
    public String decrypt(String str)
    {
        String  decrypted;
        int     character;

        decrypted = "";

        for(int i = 0; i < str.length(); i++)
        {
            character = str.charAt(i);
            if (Character.isUpperCase(character))
            {
                character = character - ((key + fancyMath(i)) % 26);
                if (character < 'A')
                    character = character + 26;
            }
            else if (Character.isLowerCase(character))
            {
                character = character - ((key + fancyMath(i)) % 26);
                if (character < 'a')
                    character = character + 26;
            }
            else if (character == '!')
            {
                character = '@';
            }

            else if (character == ';')
            {
                character = '.';
            }
            decrypted += (char) character;
        }
        return decrypted;
    }

    /**
     *
     * @param str
     * @return
     */
    public String encrypt(String str)
    {
        String  encrypted;
        int     character;

        encrypted = "";

        for(int i = 0; i < str.length(); i++)
        {
            character = str.charAt(i);

            if (Character.isUpperCase(character))
            {
                //26 letters of the alphabet so mod by 26
                character = character + ((key + fancyMath(i)) % 26);
                if (character > 'Z')
                    character = character - 26;
            }
            else if (Character.isLowerCase(character))
            {
                character = character + ((key + fancyMath(i)) % 26);
                if (character > 'z')
                    character = character - 26;
            }
            else if (character == '@')
            {
                character = '!';
            }

            else if (character == '.')
            {
                character = ';';
            }
            encrypted += (char) character;
        }
        return encrypted;
    }

    /**
     *
     * @param i
     * @return
     */
    public int fancyMath(int i)
    {
        return (int) (Math.PI + ((i * Math.E) + (78360)));
    }
}

