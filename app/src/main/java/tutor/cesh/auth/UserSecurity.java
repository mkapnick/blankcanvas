package tutor.cesh.auth;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Handles AES/CBC/NoPadding encryption/decryption
 *
 * @version v1.0
 * @author  Michael Kapnick
 */
public class UserSecurity
{
    private static final String IV              = "3272b77db250a6df";
    private static final String encryptionKey   = "45c6b0753e83b48f";

    /**
     * Encrypts a string using AES encryption with no padding
     *
     * @param plainText The text to be encrypted with AES encryption
     * @return          The encrypted text
     *
     * @throws Exception
     */
    public String encrypt(String plainText) throws Exception
    {
        Cipher          cipher;
        SecretKeySpec   key;
        byte []         encryptedText;

        plainText   = formatPlainText(plainText);
        cipher      = Cipher.getInstance("AES/CBC/NoPadding");
        key         = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");

        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(IV.getBytes("UTF-8")));

        encryptedText = cipher.doFinal(plainText.getBytes("UTF-8"));

        return Base64.encodeToString(encryptedText, 0);
    }

    /**
     * Decrypts a string
     *
     * @param cipherText    The text to be deecrypted
     * @return              The decrypted text
     *
     * @throws Exception
     */
    public String decrypt(String cipherText) throws Exception
    {
        Cipher          cipher;
        SecretKeySpec   key;
        String          decryptedText;

        cipher  = Cipher.getInstance("AES/CBC/NoPadding");
        key     = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");

        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(IV.getBytes("UTF-8")));

        decryptedText = new String(cipher.doFinal(Base64.decode(cipherText, 0)),"UTF-8");

        return decryptedText;
    }
    /**
     * Pads a plain text, the text must be a multiple of 16 for the encryption to work
     *
     * @param plainText The text to be padded
     * @return          The appropriately padded text
     */
    private String formatPlainText(String plainText)
    {
        int length, maxNum;

        length = plainText.length();

        if(length < 16)
        {
            maxNum = 16 - length;
            for(int i =0; i < maxNum; i++)
            {
                plainText += "\0";
            }

        }
        else if(length > 16 && length < 32)
        {
            maxNum = 32 - length;
            for(int i =0; i < maxNum; i++)
            {
                plainText += "\0";
            }
        }
        else if (length > 32)
        {
            maxNum = 48 - length;
            for(int i =0; i < maxNum; i++)
            {
                plainText += "\0";
            }
        }

        return plainText;
    }
}

