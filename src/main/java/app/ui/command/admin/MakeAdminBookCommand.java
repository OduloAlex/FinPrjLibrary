package app.ui.command.admin;

import app.Path;
import app.dao.*;
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
import java.util.List;

/**
 * MakeAdminBook command.
 *
 * @author Alex Odulo
 */
public class MakeAdminBookCommand extends Command {

    private static final Logger log = Logger.getLogger(MakeAdminBookCommand.class);

    private static final long serialVersionUID = 8124018125609598753L;

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

        HttpSession session = request.getSession();

        try {
            List<Author> authorItems = AuthorDao.findAllAuthor();
            session.setAttribute("authors", authorItems);
            List<Publishing> publishingItems = PublishingDao.findAllPublishing();
            session.setAttribute("publishings", publishingItems);
        } catch (DBException e) {
            String errorMessage = e.getMessage();
            session.setAttribute("errorMessage", errorMessage);
            log.error("errorMessage --> " + errorMessage);
            return Path.COMMAND__ERROR;
        }

        return Path.PAGE__MAKE_ADMIN_BOOK;
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
                String errorMessage = e.getMessage();
                session.setAttribute("errorMessage", errorMessage);
                log.error("errorMessage --> " + errorMessage);
                return Path.COMMAND__ERROR;
            }
        }

//      Make Book
        String name = request.getParameter("name");
        String strAuthorId = request.getParameter("authorId");
        String strPublishingId = request.getParameter("publishingId");
        String strYear = request.getParameter("year");
        String description = request.getParameter("description");
        String strFine = request.getParameter("fine");
        String save = request.getParameter("save");

        if (save != null) {
            if ((name != null) && (strAuthorId != null) && (strPublishingId != null)
                    && (strYear != null) && (description != null) && (strFine != null)) {

                if (name.isEmpty() || (name.length() > 45) || description.isEmpty() || (description.length() > 120)) {
                    String errorMessage = "ErrorMoreThan45ch";
                    request.getSession().setAttribute("errorMessage", errorMessage);
                    log.error("errorMessage --> " + errorMessage);
                    return Path.COMMAND__ERROR;
                }
                int year;
                int fine;
                try {
                    year = Integer.parseInt(strYear);
                    fine = Integer.parseInt(strFine);
                } catch (NumberFormatException e) {
                    String errorMessage = "ErrorYearAndFine";
                    request.getSession().setAttribute("errorMessage", errorMessage);
                    log.error("errorMessage --> " + errorMessage);
                    return Path.COMMAND__ERROR;
                }

                try {
                    int authorId = Integer.parseInt(strAuthorId);
                    int publishingId = Integer.parseInt(strPublishingId);
                    CatalogObjDao.addCatalogObj(name, year, fine, description, 0, authorId, publishingId);
                } catch (NumberFormatException e) {
                    log.trace("Catalog itemId doesn't parse --> " + e);
                } catch (DBException e) {
                    String errorMessage = e.getMessage();
                    session.setAttribute("errorMessage", errorMessage);
                    log.error("errorMessage --> " + errorMessage);
                    return Path.COMMAND__ERROR;
                }
                return Path.COMMAND__LIST_ADMIN_CATALOG;
            } else {
                String errorMessage = "ErrorSetAll";
                request.getSession().setAttribute("errorMessage", errorMessage);
                log.error("errorMessage --> " + errorMessage);
                return Path.COMMAND__ERROR;
            }
        }
        return Path.COMMAND__MAKE_ADMIN_BOOK;
    }
}