package tutor.cesh.auth;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class UserSecurity
{
    private SecretKey aesKey;
    private byte []             iv;

    private static final String KEY     = "a8875d3e484c1f82";
    private static final String SALT    = "wibble";
    private static final int    BIT_SIZE= 128;

    public UserSecurity()
    {
        SecretKey           tmp;
        SecretKeyFactory factory;
        KeySpec spec;
        Cipher encryptCipher;

        try
        {
            factory     = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            spec        = new PBEKeySpec(KEY.toCharArray(), SALT.getBytes(), 65536, BIT_SIZE);
            tmp         = factory.generateSecret(spec);

            //set the aes secret key
            this.aesKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            //set the iv key
            encryptCipher   = Cipher.getInstance("AES/CBC/PKCS5Padding");
            encryptCipher.init(Cipher.ENCRYPT_MODE, this.aesKey);
            this.iv         = encryptCipher.getIV();
        }
        catch(Exception e)
        {

        }
    }

    /**
     *
     * @param plainText
     * @return
     * @throws Exception
     */
    public byte [] encrypt(String plainText) throws Exception
    {
        // Generate key
        Cipher                  encryptCipher;
        byte[]                  encryptedBytes;
        ByteArrayOutputStream outputStream;
        CipherOutputStream cipherOutputStream;

        // Encrypt cipher
        encryptCipher   = Cipher.getInstance("AES/CBC/PKCS5Padding");
        encryptCipher.init(Cipher.ENCRYPT_MODE, this.aesKey);

        // Encrypt
        outputStream        = new ByteArrayOutputStream();
        cipherOutputStream  = new CipherOutputStream(outputStream, encryptCipher);
        cipherOutputStream.write(plainText.getBytes());
        cipherOutputStream.flush();
        cipherOutputStream.close();
        encryptedBytes      = outputStream.toByteArray();

        return encryptedBytes;
    }

    /**
     *
     * @param encryptedBytes
     * @return
     * @throws Exception
     */
    public String decrypt(byte [] encryptedBytes) throws Exception
    {
        // Generate key
        byte[]                  buf;
        ByteArrayOutputStream   outputStream;
        Cipher                  decryptCipher;
        IvParameterSpec ivParameterSpec;
        ByteArrayInputStream inStream;
        CipherInputStream cipherInputStream;
        int                     bytesRead;

        // Decrypt cipher
        decryptCipher   = Cipher.getInstance("AES/CBC/PKCS5Padding");
        ivParameterSpec = new IvParameterSpec(this.iv);
        decryptCipher.init(Cipher.DECRYPT_MODE, this.aesKey, ivParameterSpec);

        // Decrypt
        outputStream        = new ByteArrayOutputStream();
        inStream            = new ByteArrayInputStream(encryptedBytes);
        cipherInputStream   = new CipherInputStream(inStream, decryptCipher);
        buf                 = new byte[1024];

        while ((bytesRead = cipherInputStream.read(buf)) >= 0)
            outputStream.write(buf, 0, bytesRead);

        return new String(outputStream.toByteArray());
    }
}