package app.ui.command;

import app.Path;
import app.dao.*;
import app.domain.*;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * Registration command.
 *
 * @author Alex Odulo
 */
public class RegistrationCommand extends Command {

    private static final long serialVersionUID = 7732286214029478505L;

    private static final Logger log = Logger.getLogger(RegistrationCommand.class);

    /**
     * Execute command to Get request
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @return path to jsp pages or controller commands
     * @throws IOException      IOException
     * @throws ServletException ServletException
     */
    @Override
    public String executeGet(HttpServletRequest request,
                             HttpServletResponse response) throws IOException, ServletException {
        log.debug("Command starts");


        log.debug("Command finished");
        return Path.PAGE__REGISTRATION_PAGE;
    }

    /**
     * Execute command to Post request
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @return path to jsp pages or controller commands
     * @throws IOException      IOException
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
            log.debug("Command Post finished");
            return Path.COMMAND__REGISTRATION;
        }

        // obtain login and password from the request
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String description = request.getParameter("description");

        // error handler
        String forward = Path.COMMAND__ERROR;
        if (login != null && password != null && description != null) {
            log.trace("Request parameter: loging --> " + login);
            if (login.isEmpty() || password.isEmpty() || (login.length() > 45) || (password.length() > 45) || (description.length() > 120)) {
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

            if (user != null) {
                DBException.outputException(session, "ErrorUserExists");
                return forward;
            } else {
                String hashPsw;
                try {
                    hashPsw = Password.getHash(password, login);
                } catch (NoSuchAlgorithmException e) {
                    DBException.outputException(session, e.getMessage());
                    return forward;
                }
                user = new User();
                user.setUsername(login);
                user.setPassword(hashPsw);
                user.setActive(true);
                user.setDescription(description);
                user.setLocaleName("en");
                user.setRoleId(3);
                try {
                    UserDao.addUser(user);
                } catch (DBException e) {
                    DBException.outputException(session, e.getMessage());
                    return forward;
                }

                try {
                    user = UserDao.findUserByLogin(login);
                } catch (DBException e) {
                    DBException.outputException(session, e.getMessage());
                    return forward;
                }

                LoginCommand.checkOnlyOneLogin(session, user.getUsername());

                Role userRole = Role.getRole(user);
                log.trace("userRole --> " + userRole);

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

        log.debug("Command Post finished");
        return forward;
    }
}