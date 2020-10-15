package app.ui.command.admin;

import app.Path;
import app.dao.AuthorDao;
import app.dao.DBException;
import app.dao.PublishingDao;
import app.dao.UserDao;
import app.domain.Author;
import app.domain.Publishing;
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

public class ListAdminPublishingsCommand extends Command {

    private static final long serialVersionUID = 7732286225029675505L;

    private static final Logger log = Logger.getLogger(ListAdminPublishingsCommand.class);

    @Override
    public String executeGet(HttpServletRequest request,
                             HttpServletResponse response) throws IOException, ServletException {

        log.debug("Command starts");

        User user = (User) request.getSession().getAttribute("user");

//      Get publishings
        List<Publishing> publishingItems = null;
        int page;
        String show = request.getParameter("show");
        if ((show != null && !show.isEmpty()) && ("all".equals(show))) {
            log.debug("Show all publishings ------>>>>> " + show);
            try {
                publishingItems = PublishingDao.findAllPublishing();
            } catch (DBException e) {
                String errorMessage = e.getMessage();
                request.setAttribute("errorMessage", errorMessage);
                log.error("errorMessage --> " + errorMessage);
                log.debug("Command Post finished");
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

//      Cancel publishing
        String itemId = request.getParameter("cancelId");
        if (itemId != null) {
            try {
                int publishingId = Integer.parseInt(itemId);
                log.debug("Del publishing--> publishingId " + publishingId);
                if (!PublishingDao.delPublishingById(publishingId)) {
                    String errorMessage = "Can't del publishing in DB";
                    session.setAttribute("errorMessage", errorMessage);
                    log.error("errorMessage --> " + errorMessage);
                    log.debug("Command Post finished");
                    return Path.COMMAND__ERROR;
                }
                log.debug("Del publishing successful");
            } catch (NumberFormatException e) {
                log.trace("publishing itemId doesn't parse --> " + e);
            } catch (DBException e) {
                String errorMessage = e.getMessage();
                session.setAttribute("errorMessage", errorMessage);
                log.error("errorMessage --> " + errorMessage);
                log.debug("Command Post finished");
                return Path.COMMAND__ERROR;
            }
        }

//      Make publishing
        String name = request.getParameter("makePublishing");
        if (name != null) {
//            String name = new String(strPublishing.getBytes("ISO-8859-1"),"utf-8");
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
                log.debug("Add publishing--> " + publishing);
            } catch (DBException e) {
                String errorMessage = e.getMessage();
                request.getSession().setAttribute("errorMessage", errorMessage);
                log.error("errorMessage --> " + errorMessage);
                log.debug("Command Post finished");
                return Path.COMMAND__ERROR;
            }
        }

        log.debug("Command Post finished");
        return Path.COMMAND__LIST_ADMIN_PUBLISHINGS;
    }
}
