package app.ui.command;

import app.Path;
import app.dao.*;
import app.domain.Role;
import app.domain.User;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * Login command.
 *
 * @author Alex Odulo
 */
public class LoginCommand extends Command {

    private static final long serialVersionUID = -3071536593627692473L;

    private static final Logger log = Logger.getLogger(LoginCommand.class);

    /**
     * Execute command to Get request
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return path to jsp pages or controller commands
     * @throws IOException IOException
     * @throws ServletException ServletException
     */
    @Override
    public String executeGet(HttpServletRequest request,
                             HttpServletResponse response) throws IOException, ServletException {

        log.debug("Command starts");

        log.debug("Command Get finished");
        return Path.PAGE__LOGIN;
    }

    /**
     * Execute command to Post request
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return path to jsp pages or controller commands
     * @throws IOException IOException
     * @throws ServletException ServletException
     */
    @Override
    public String executePost(HttpServletRequest request,
                              HttpServletResponse response) throws IOException, ServletException {

        log.debug("Command Post starts");
        HttpSession session = request.getSession();

        //      Set locale
        String localeToSet = request.getParameter("localeToSet");
        if (localeToSet != null && !localeToSet.isEmpty()) {
            log.debug("Set locale------>>>>> " + localeToSet);
            Config.set(session, "javax.servlet.jsp.jstl.fmt.locale", localeToSet);
            session.setAttribute("defaultLocale", localeToSet);
        }

        // obtain login and password from the request
        String login = request.getParameter("login");
        log.trace("Request parameter: login --> " + login);

        String password = request.getParameter("password");

        // error handler
        String errorMessage = null;
        String forward = Path.COMMAND__LOGIN;
        if (login != null && password != null) {
            forward = Path.COMMAND__ERROR;
            if (login.isEmpty() || password.isEmpty() || (login.length() > 45) || (password.length() > 45)) {
                DBException.outputException(session, "ErrorLoginPassEmpty");
                return forward;
            }

            User user = null;
            try {
                user = UserDao.findUserByLogin(login);
            } catch (DBException e) {
                DBException.outputException(session, e.getMessage());
                return forward;
            }

            log.trace("Found in DB: user --> " + user);

            if (user == null) {
                DBException.outputException(session, "ErrorNotFindUser");
                return forward;
            } else {
                boolean check = false;
                try {
                    check = Password.check(password, login, user.getPassword());
                } catch (NoSuchAlgorithmException e) {
                    DBException.outputException(session, e.getMessage());
                    return forward;
                }
                if(!check){
                    DBException.outputException(session, "ErrorWrongPassword");
                    return forward;
                }
                if (!user.isActive()) {
                    DBException.outputException(session, "ErrorUserBlocked");
                    return forward;
                } else {
                    Role userRole = Role.getRole(user);
                    log.trace("userRole --> " + userRole);

                    if (userRole == Role.ADMIN)
                        forward = Path.COMMAND__LIST_ADMIN_CATALOG;

                    if (userRole == Role.LIBRARIAN)
                        forward = Path.COMMAND__LIST_LIB_CATALOG;

                    if (userRole == Role.READER)
                        forward = Path.COMMAND__LIST_CATALOG;

                    session.setAttribute("user", user);
                    log.trace("Set the session attribute: user --> " + user);

                    session.setAttribute("userRole", userRole);
                    log.trace("Set the session attribute: userRole --> " + userRole);

                    log.info("User " + user + " logged as " + userRole.toString().toLowerCase());

                    // work with i18n
                    String userLocaleName = user.getLocaleName();
                    log.trace("userLocalName --> " + userLocaleName);

                    if (userLocaleName != null && !userLocaleName.isEmpty()) {
                        Config.set(session, "javax.servlet.jsp.jstl.fmt.locale", userLocaleName);

                        session.setAttribute("defaultLocale", userLocaleName);
                        log.trace("Set the session attribute: defaultLocaleName --> " + userLocaleName);

                        log.info("Locale for user: defaultLocale --> " + userLocaleName);
                    }
                }
            }
        }
        log.debug("Command Post finished");
        return forward;
    }

}