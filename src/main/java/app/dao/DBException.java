package app.dao;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpSession;

/**
 * Application exception class
 *
 * @author Alex Odulo
 */
public class DBException extends Exception {
    private static final long serialVersionUID = -1845513285982677152L;
    private static final Logger logger = Logger.getLogger(DBException.class);

    public DBException() {
    }

    public DBException(String message) {
        super(message);
    }

    public DBException(String message, Throwable cause) {
        super(message, cause);
    }

    public static void outputException(HttpSession session, String errorMessage){
        session.setAttribute("errorMessage", errorMessage);
        logger.error("errorMessage --> " + errorMessage);
    }
}
