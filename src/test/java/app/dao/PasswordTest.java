package app.dao;

import org.junit.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.*;

public class PasswordTest {

    @Test
    public void getHash() throws NoSuchAlgorithmException {
        String actual = Password.getHash("1234");
        String expected = "81dc9bdb52d04dc20036dbd8313ed055";
        assertEquals(expected, actual);
    }

    @Test
    public void check() throws NoSuchAlgorithmException {
        assertTrue(Password.check("1234", "81dc9bdb52d04dc20036dbd8313ed055"));
    }
}