package app.ui.command.librarian;

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
 * ListLibReaders command.
 *
 * @author Alex Odulo
 */
public class ListLibReadersCommand extends Command {

    private static final long serialVersionUID = 7732286257029478505L;

    private static final Logger log = Logger.getLogger(ListLibReadersCommand.class);

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

        HttpSession session = request.getSession();

//      Choose Reader Orders
        String itemId = request.getParameter("itemIdOrders");
        if (itemId != null) {

            User reader = null;
            try {
                int result = Integer.parseInt(itemId);
                reader = UserDao.findUserById(result);
            } catch (NumberFormatException e) {
                log.trace("User itemId doesn't parse --> " + e);
            } catch (DBException e) {
                DBException.outputException(session, e.getMessage());
                return Path.COMMAND__ERROR;
            }
            session.setAttribute("reader", reader);
            log.debug("Set the session attribute: reader --> " + reader);

            log.debug("Command finished");
            return Path.COMMAND__LIST_LIB_ORDERS;
        }

//      Choose Reader Cards
        itemId = request.getParameter("itemIdCards");
        if (itemId != null) {

            User reader = null;
            try {
                int result = Integer.parseInt(itemId);
                reader = UserDao.findUserById(result);
            } catch (NumberFormatException e) {
                log.trace("User itemId doesn't parse --> " + e);
            } catch (DBException e) {
                DBException.outputException(session, e.getMessage());
                return Path.COMMAND__ERROR;
            }
            session.setAttribute("reader", reader);
            log.debug("Set the session attribute: reader --> " + reader);

            log.debug("Command finished");
            return Path.COMMAND__LIST_LIB_CARDS;
        }

//      Get Users
        List<User> usersItems = null;
        int page;
        String show = request.getParameter("show");
        if ((show != null && !show.isEmpty()) && ("all".equals(show))) {
            try {
                usersItems = UserDao.findAllUserReader();
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
        log.debug("Set the request attribute: catalogPage --> " + readersPage);

        request.getSession().setAttribute("usersItems", usersItems);
        log.trace("Set the request attribute: usersItems --> " + usersItems);

        request.getSession().setAttribute("page", page);
        log.debug("Set page --> " + page);

        log.debug("Command finished");
        return Path.PAGE__LIST_LIB_READERS;
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
                DBException.outputException(session, e.getMessage());
                return Path.COMMAND__ERROR;
            }
        }

        log.debug("Command Post finished");
        return Path.COMMAND__LIST_LIB_READERS;
    }
}
