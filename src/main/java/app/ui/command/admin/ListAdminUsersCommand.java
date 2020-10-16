package app.ui.command.admin;

import app.Path;
import app.dao.DBException;
import app.dao.UserDao;
import app.domain.User;
import app.ui.command.Command;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ListAdminUsers command.
 *
 * @author Alex Odulo
 */
public class ListAdminUsersCommand extends Command {

    private static final long serialVersionUID = 7362286257029478505L;

    private static final Logger log = Logger.getLogger(ListAdminUsersCommand.class);

    @Override
    public String executeGet(HttpServletRequest request,
                             HttpServletResponse response) throws IOException, ServletException {

        log.debug("Command starts");

//      Get Users
        List<User> usersItems = null;
        int page;
        String show = request.getParameter("show");
        if ((show != null && !show.isEmpty()) && ("all".equals(show))) {
            log.debug("Show all users------>>>>> " + show);
            try {
                usersItems = UserDao.findAllUserReaderOrLib();
            } catch (DBException e) {
                String errorMessage = e.getMessage();
                request.setAttribute("errorMessage", errorMessage);
                log.error("errorMessage --> " + errorMessage);
                log.debug("Command Post finished");
                return Path.PAGE__ERROR_PAGE;
            }
            log.trace("Found in DB: findAllUser --> " + usersItems);
            page = 1;
        } else {
            usersItems = (List<User>) request.getSession().getAttribute("usersItems");
            log.trace("Found in Attribute: findAllUser --> " + usersItems);
            page = (int) request.getSession().getAttribute("page");
        }

//      Pagination
        List<User> readersPage = null;
        if(usersItems!=null) {
            String goPage = request.getParameter("goPage");
            if (goPage != null && !goPage.isEmpty()) {
                log.debug("Go page ------>>>>> " + goPage);
                if ("next".equals(goPage)) {
                    if (usersItems.size() > (page * 5)) {
                        page++;
                    }
                } else if (("previous".equals(goPage)) && (page != 1)) {
                    page--;
                }
            }

            int lastPage = page * 5;
            if (lastPage >= usersItems.size()) {
                lastPage = usersItems.size();
            }
            readersPage = new ArrayList<>(usersItems.subList(((page * 5) - 5), lastPage));
        }
        request.setAttribute("readersPage", readersPage);
        log.debug("Set the request attribute: catalogPage --> " + readersPage);

        request.getSession().setAttribute("usersItems", usersItems);
        log.trace("Set the request attribute: usersItems --> " + usersItems);

        request.getSession().setAttribute("page", page);
        log.debug("Set page --> " + page);

        log.debug("Command finished");
        return Path.PAGE__LIST_ADMIN_USERS;
    }

    @Override
    public String executePost(HttpServletRequest request,
                              HttpServletResponse response) throws IOException, ServletException {

        log.debug("Command Post starts");

        User user = (User) request.getSession().getAttribute("user");
        HttpSession session = request.getSession();

//      Set locale
        String localeToSet = request.getParameter("localeToSet");
        if (localeToSet != null && !localeToSet.isEmpty()) {
            log.debug("Set locale------>>>>> " + localeToSet);
            Config.set(session, "javax.servlet.jsp.jstl.fmt.locale", localeToSet);
            session.setAttribute("defaultLocale", localeToSet);
            user.setLocaleName(localeToSet);
            try {
                UserDao.updateUser(user);
            } catch (DBException e) {
                String errorMessage = e.getMessage();
                session.setAttribute("errorMessage", errorMessage);
                log.error("errorMessage --> " + errorMessage);
                log.debug("Command Post finished");
                return Path.COMMAND__ERROR;
            }
        }

//      Cancel User
        String itemId = request.getParameter("cancelId");
        if (itemId != null) {
            try {
                int userId = Integer.parseInt(itemId);
                log.debug("Del User--> userId " + userId);
                if (!UserDao.delUserById(userId)) {
                    String errorMessage = "Can't del User in DB";
                    session.setAttribute("errorMessage", errorMessage);
                    log.error("errorMessage --> " + errorMessage);
                    log.debug("Command Post finished");
                    return Path.COMMAND__ERROR;
                }
                log.debug("Del User successful");
            } catch (NumberFormatException e) {
                log.trace("User itemId doesn't parse --> " + e);
            } catch (DBException e) {
                String errorMessage = e.getMessage();
                session.setAttribute("errorMessage", errorMessage);
                log.error("errorMessage --> " + errorMessage);
                log.debug("Command Post finished");
                return Path.COMMAND__ERROR;
            }
        }

//      User state
        itemId = request.getParameter("stateOffId");
        if (itemId != null) {
            try {
                int userId = Integer.parseInt(itemId);
                UserDao.updateUserActive(0, userId);
                log.debug("UpdateUserActive --> block, userId " + userId);
            } catch (NumberFormatException e) {
                log.trace("Order itemId doesn't parse --> " + e);
            } catch (DBException e) {
                String errorMessage = e.getMessage();
                session.setAttribute("errorMessage", errorMessage);
                log.error("errorMessage --> " + errorMessage);
                log.debug("Command Post finished");
                return  Path.COMMAND__ERROR;
            }
        }
        itemId = request.getParameter("stateOnId");
        if (itemId != null) {
            try {
                int userId = Integer.parseInt(itemId);
                UserDao.updateUserActive(1, userId);
                log.debug("UpdateUserActive --> unblock, userId " + userId);
            } catch (NumberFormatException e) {
                log.trace("Order itemId doesn't parse --> " + e);
            } catch (DBException e) {
                String errorMessage = e.getMessage();
                session.setAttribute("errorMessage", errorMessage);
                log.error("errorMessage --> " + errorMessage);
                log.debug("Command Post finished");
                return  Path.COMMAND__ERROR;
            }
        }

//      User role lib
        itemId = request.getParameter("libOffId");
        if (itemId != null) {
            try {
                int userId = Integer.parseInt(itemId);
                UserDao.updateUserRole(3, userId);
                log.debug("UpdateUserRole --> reader, userId " + userId);
            } catch (NumberFormatException e) {
                log.trace("Order itemId doesn't parse --> " + e);
            } catch (DBException e) {
                String errorMessage = e.getMessage();
                session.setAttribute("errorMessage", errorMessage);
                log.error("errorMessage --> " + errorMessage);
                log.debug("Command Post finished");
                return  Path.COMMAND__ERROR;
            }
        }
        itemId = request.getParameter("libOnId");
        if (itemId != null) {
            try {
                int userId = Integer.parseInt(itemId);
                UserDao.updateUserRole(2, userId);
                log.debug("UpdateUserRole --> lib, userId " + userId);
            } catch (NumberFormatException e) {
                log.trace("Order itemId doesn't parse --> " + e);
            } catch (DBException e) {
                String errorMessage = e.getMessage();
                session.setAttribute("errorMessage", errorMessage);
                log.error("errorMessage --> " + errorMessage);
                log.debug("Command Post finished");
                return  Path.COMMAND__ERROR;
            }
        }

        log.debug("Command Post finished");
        return Path.COMMAND__LIST_ADMIN_USERS;
    }
}
