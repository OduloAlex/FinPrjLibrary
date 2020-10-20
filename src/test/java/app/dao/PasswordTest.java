package app.dao;

import org.junit.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.*;

public class PasswordTest {

    @Test
    public void getHash() throws NoSuchAlgorithmException {
        String actual = Password.getHash("1234", "admin");
        String expected = "352b80da9d0cb559d2e5b4edbbd83b60";
        assertEquals(expected, actual);
    }

    @Test
    public void check() throws NoSuchAlgorithmException {
        assertTrue(Password.check("1234", "admin","352b80da9d0cb559d2e5b4edbbd83b60"));
    }

    @Test
    public void getSaltHash() throws NoSuchAlgorithmException {
        String actual = Password.getSaltHash("admin");
        String expected = "21232f297a57a5a743894a0e4a801fc3";
        assertEquals(expected, actual);
    }
}