package app.ui.listener;

import app.domain.User;
import app.ui.command.LoginCommand;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * SessionListener class
 *
 * @author Alex Odulo
 */
public class SessionListener implements HttpSessionListener {
    private static final Logger log = Logger.getLogger(SessionListener.class);

    /**
     * Session initialization
     *
     * @param se HttpSessionEvent
     */
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        log.debug("SessionListener Created starts");
    }

    /**
     * Session destruction
     *
     * @param se HttpSessionEvent
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        log.debug("SessionListener Destroyed starts");
        HttpSession session = se.getSession();
        User user = (User) session.getAttribute("user");

        LoginCommand.deleteUserInOnlyOneLogin(session, user.getUsername());
    }
}
