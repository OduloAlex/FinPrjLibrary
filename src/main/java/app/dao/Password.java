package app.dao;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class for working with passwords
 *
 * @author Alex Odulo
 */
public class Password implements Serializable {

    private static final long serialVersionUID = -7403047418039946710L;

    /**
     * Get hash MD5 (Message-Digest Algorithm) with salt
     *
     * @param password password
     * @param salt     salt
     * @return hash password
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     */
    public static String getHash(String password, String salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        String hashSalt = getSaltHash(salt);
        md.update(hashSalt.getBytes());
        byte[] bytes = md.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    /**
     * Get salt hash MD5 (Message-Digest Algorithm)
     *
     * @param salt salt
     * @return hash salt
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     */
    public static String getSaltHash(String salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(salt.getBytes());
        byte[] bytes = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    /**
     * Checks whether given plaintext password corresponds
     * to a stored hash of the password.
     *
     * @param password password
     * @param salt     salt
     * @param stored   hash
     * @return true if equals
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     */
    public static boolean check(String password, String salt, String stored) throws NoSuchAlgorithmException {

        return getHash(password, salt).equals(stored);
    }
}