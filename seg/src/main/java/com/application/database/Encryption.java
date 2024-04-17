package com.application.database;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
/**
 * @info for the algorithm:
 *
 * https://crypto.stackexchange.com/questions/12677/strength-of-md5-in-finding-duplicate-files : last visited on 16/04/2024
 *
 * https://stackoverflow.com/questions/1240852/is-it-possible-to-decrypt-md5-hashes : last visited on 16/04/2024
 * */
public class Encryption {
    /* Driver Code */
    public static String encrypt(String text)
    {
        /* Plain-text password initialization. */
        String textToEncrypt = text;

        String encryptedpassword = null;

        try
        {
            /* MessageDigest instance for MD5. */
            MessageDigest m = MessageDigest.getInstance("MD5");

            /* Add plain-text password bytes to digest using MD5 update() method. */
            m.update(textToEncrypt.getBytes());

            /* Convert the hash value into bytes */
            byte[] bytes = m.digest();

            /* The bytes array has bytes in decimal form. Converting it into hexadecimal format. */
            StringBuilder s = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                s.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            /* Complete hashed password in hexadecimal format */
            encryptedpassword = s.toString();



        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        /* Display the unencrypted and encrypted passwords. */
//        System.out.println("Plain-text password: " + password);
//        System.out.println("Encrypted password using MD5: " + encryptedpassword);

        return encryptedpassword;

    }
}
