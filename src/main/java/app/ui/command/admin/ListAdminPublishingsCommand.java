package app.ui.command.admin;

import app.Path;
import app.dao.DBException;
import app.dao.PublishingDao;
import app.dao.UserDao;
import app.domain.Publishing;
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
 * ListAdminPublishings command.
 *
 * @author Alex Odulo
 */
public class ListAdminPublishingsCommand extends Command {

    private static final long serialVersionUID = 7732286225029675505L;

    private static final Logger log = Logger.getLogger(ListAdminPublishingsCommand.class);

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
//      Get publishings
        List<Publishing> publishingItems = null;
        int page;
        String show = request.getParameter("show");
        if ((show != null && !show.isEmpty()) && ("all".equals(show))) {
            try {
                publishingItems = PublishingDao.findAllPublishing();
            } catch (DBException e) {
                DBException.outputException(session, e.getMessage());
                return Path.PAGE__ERROR_PAGE;
            }
            log.trace("Found in DB: findAllCatalogObj --> " + publishingItems);
            page = 1;
        } else {
            publishingItems = (List<Publishing>) request.getSession().getAttribute("publishingItems");
            log.trace("Found in Attribute: findAllCatalogObj --> " + publishingItems);
            page = (int) request.getSession().getAttribute("page");
        }

//      Pagination
        List<Publishing> publishingPage = null;
        if (publishingItems != null) {
            String goPage = request.getParameter("goPage");
            if (goPage != null && !goPage.isEmpty()) {
                log.debug("Go page ------>>>>> " + goPage);
                if ("next".equals(goPage)) {
                    if (publishingItems.size() > (page * 5)) {
                        page++;
                    }
                } else if (("previous".equals(goPage)) && (page != 1)) {
                    page--;
                }
            }

            int lastPage = page * 5;
            if (lastPage >= publishingItems.size()) {
                lastPage = publishingItems.size();
            }
            publishingPage = new ArrayList<>(publishingItems.subList(((page * 5) - 5), lastPage));
        }
        request.setAttribute("publishingPage", publishingPage);
        log.debug("Set the request attribute: publishingPage --> " + publishingPage);

        request.getSession().setAttribute("publishingItems", publishingItems);
        log.trace("Set the request attribute: publishingItems --> " + publishingItems);

        request.getSession().setAttribute("page", page);
        log.debug("Set page --> " + page);

        log.debug("Command finished");
        return Path.PAGE__LIST_ADMIN_PUBLISHINGS;
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

//      Cancel publishing
        String itemId = request.getParameter("cancelId");
        if (itemId != null) {
            try {
                int publishingId = Integer.parseInt(itemId);
                if (!PublishingDao.delPublishingById(publishingId)) {
                    String errorMessage = "Can't del publishing in DB";
                    session.setAttribute("errorMessage", errorMessage);
                    log.error("errorMessage --> " + errorMessage);
                    return Path.COMMAND__ERROR;
                }
            } catch (NumberFormatException e) {
                log.trace("publishing itemId doesn't parse --> " + e);
            } catch (DBException e) {
                DBException.outputException(session, e.getMessage());
                return Path.COMMAND__ERROR;
            }
        }

//      Make publishing
        String name = request.getParameter("makePublishing");
        if (name != null) {
            if (name.isEmpty() || (name.length() > 45)) {
                String errorMessage = "ErrorMoreThan45ch";
                request.getSession().setAttribute("errorMessage", errorMessage);
                log.error("errorMessage --> " + errorMessage);
                return Path.COMMAND__ERROR;
            }
            try {
                Publishing publishing = new Publishing();
                publishing.setName(name);
                PublishingDao.addPublishing(publishing);
            } catch (DBException e) {
                DBException.outputException(session, e.getMessage());
                return Path.COMMAND__ERROR;
            }
        }

        log.debug("Command Post finished");
        return Path.COMMAND__LIST_ADMIN_PUBLISHINGS;
    }
}
