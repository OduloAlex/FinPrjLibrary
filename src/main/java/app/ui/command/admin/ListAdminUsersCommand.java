package app.ui.command.admin;

import app.Path;
import app.dao.DBException;
import app.dao.UserDao;
import app.domain.User;
import app.ui.command.Command;
import app.ui.command.UserSettingsCommand;
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
        HttpSession session = request.getSession();
//      Get Users
        List<User> usersItems = null;
        int page;
        String show = request.getParameter("show");
        if ((show != null && !show.isEmpty()) && ("all".equals(show))) {
            try {
                usersItems = UserDao.findAllUserReaderOrLib();
            } catch (DBException e) {
                DBException.outputException(session, e.getMessage());
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
        if (usersItems != null) {
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

        request.getSession().setAttribute("usersItems", usersItems);
        log.trace("Set the request attribute: usersItems --> " + usersItems);

        request.getSession().setAttribute("page", page);
        log.debug("Set page --> " + page);

        log.debug("Command finished");
        return Path.PAGE__LIST_ADMIN_USERS;
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

        try {
            UserSettingsCommand.setUserLocale(request);
        } catch (DBException e) {
            DBException.outputException(session, e.getMessage());
            return Path.COMMAND__ERROR;
        }

//      Cancel User
        String itemId = request.getParameter("cancelId");
        if (itemId != null) {
            try {
                int userId = Integer.parseInt(itemId);
                if (!UserDao.delUserById(userId)) {
                    DBException.outputException(session, "Can't del User in DB");
                    return Path.COMMAND__ERROR;
                }
            } catch (NumberFormatException e) {
                log.trace("User itemId doesn't parse --> " + e);
            } catch (DBException e) {
                DBException.outputException(session, "ErrorCantDelUserHasCard");
                return Path.COMMAND__ERROR;
            }
        }

//      User state
        itemId = request.getParameter("stateOffId");
        if (itemId != null) {
            try {
                int userId = Integer.parseInt(itemId);
                UserDao.updateUserActive(0, userId);
            } catch (NumberFormatException e) {
                log.trace("Order itemId doesn't parse --> " + e);
            } catch (DBException e) {
                DBException.outputException(session, e.getMessage());
                return Path.COMMAND__ERROR;
            }
        }
        itemId = request.getParameter("stateOnId");
        if (itemId != null) {
            try {
                int userId = Integer.parseInt(itemId);
                UserDao.updateUserActive(1, userId);
            } catch (NumberFormatException e) {
                log.trace("Order itemId doesn't parse --> " + e);
            } catch (DBException e) {
                DBException.outputException(session, e.getMessage());
                return Path.COMMAND__ERROR;
            }
        }

//      User role lib
        itemId = request.getParameter("libOffId");
        if (itemId != null) {
            try {
                int userId = Integer.parseInt(itemId);
                UserDao.updateUserRole(3, userId);
            } catch (NumberFormatException e) {
                log.trace("Order itemId doesn't parse --> " + e);
            } catch (DBException e) {
                DBException.outputException(session, e.getMessage());
                return Path.COMMAND__ERROR;
            }
        }
        itemId = request.getParameter("libOnId");
        if (itemId != null) {
            try {
                int userId = Integer.parseInt(itemId);
                UserDao.updateUserRole(2, userId);
            } catch (NumberFormatException e) {
                log.trace("Order itemId doesn't parse --> " + e);
            } catch (DBException e) {
                DBException.outputException(session, e.getMessage());
                return Path.COMMAND__ERROR;
            }
        }

        log.debug("Command Post finished");
        return Path.COMMAND__LIST_ADMIN_USERS;
    }
}
