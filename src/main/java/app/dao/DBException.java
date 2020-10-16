package app.dao;

/**
 * Application exception class
 *
 * @author Alex Odulo
 */
public class DBException extends Exception {
    public DBException() {
    }

    public DBException(String message) {
        super(message);
    }

    public DBException(String message, Throwable cause) {
        super(message, cause);
    }
}
