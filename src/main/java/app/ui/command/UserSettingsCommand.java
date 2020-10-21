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
 * UserSettings command.
 *
 * @author Alex Odulo
 */
public class UserSettingsCommand extends Command {

    private static final Logger log = Logger.getLogger(UserSettingsCommand.class);

    private static final long serialVersionUID = 8432018125609598753L;

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
        log.debug("Command Get starts");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        // back to list
        String back = request.getParameter("back");
        if (back != null) {
            Role userRole = Role.getRole(user);
            log.trace("userRole --> " + userRole);

            if (userRole == Role.ADMIN)
                return Path.COMMAND__LIST_ADMIN_CATALOG;

            if (userRole == Role.LIBRARIAN)
                return Path.COMMAND__LIST_LIB_CATALOG;

            if (userRole == Role.READER)
                return Path.COMMAND__LIST_CATALOG;

            return Path.COMMAND__LOGIN;
        }
        log.debug("Command Get finished");
        return Path.PAGE__USER_SETTINGS;
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
        User user = (User) session.getAttribute("user");

//      Set locale
        String localeToSet = request.getParameter("localeToSet");
        if (localeToSet != null && !localeToSet.isEmpty()) {
            Config.set(session, "javax.servlet.jsp.jstl.fmt.locale", localeToSet);
            session.setAttribute("defaultLocale", localeToSet);
            user.setLocaleName(localeToSet);
            try {
                UserDao.updateUser(user);
            } catch (DBException e) {
                DBException.outputException(session, e.getMessage());
                return Path.COMMAND__ERROR;
            }
        }

//      Update User
        String password = request.getParameter("password");
        String description = request.getParameter("description");
        String save = request.getParameter("save");

        if (save != null) {
            if ((password != null) && (description != null)) {

                if (password.isEmpty() || (password.length() > 45) || description.isEmpty() || (description.length() > 120)) {
                    String errorMessage = "ErrorMoreThan45ch";
                    request.getSession().setAttribute("errorMessage", errorMessage);
                    log.error("errorMessage --> " + errorMessage);
                    return Path.COMMAND__ERROR;
                }
                String hashPsw;
                try {
                    hashPsw = Password.getHash(password, user.getUsername());
                } catch (NoSuchAlgorithmException e) {
                    String errorMessage = e.getMessage();
                    session.setAttribute("errorMessage", errorMessage);
                    log.error("errorMessage --> " + errorMessage);
                    return Path.COMMAND__ERROR;
                }
                user.setPassword(hashPsw);
                user.setDescription(description);
                try {
                    UserDao.updateUser(user);
                } catch (DBException e) {
                    DBException.outputException(session, e.getMessage());
                    return Path.COMMAND__ERROR;
                }
                session.setAttribute("user", user);
                log.trace("Set the session attribute: user --> " + user);

                Role userRole = Role.getRole(user);
                log.trace("userRole --> " + userRole);

                if (userRole == Role.ADMIN)
                    return Path.COMMAND__LIST_ADMIN_CATALOG;

                if (userRole == Role.LIBRARIAN)
                    return Path.COMMAND__LIST_LIB_CATALOG;

                if (userRole == Role.READER)
                    return Path.COMMAND__LIST_CATALOG;

                return Path.COMMAND__LOGIN;
            } else {
                String errorMessage = "ErrorSetAll";
                request.getSession().setAttribute("errorMessage", errorMessage);
                log.error("errorMessage --> " + errorMessage);
                return Path.COMMAND__ERROR;
            }
        }

        return Path.COMMAND__USER_SETTINGS;
    }
}