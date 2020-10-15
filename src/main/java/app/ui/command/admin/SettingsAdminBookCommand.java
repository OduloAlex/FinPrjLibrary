package app.ui.command.admin;

import app.Path;
import app.dao.*;
import app.domain.*;
import app.ui.command.Command;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;
import java.io.IOException;
import java.util.List;

public class SettingsAdminBookCommand extends Command {

    private static final Logger log = Logger.getLogger(SettingsAdminBookCommand.class);
    private static final long serialVersionUID = 5084633343942539656L;


    @Override
    public String executeGet(HttpServletRequest request,
                             HttpServletResponse response) throws IOException, ServletException {

        log.debug("Command starts");

        User user = (User) request.getSession().getAttribute("user");
        User reader = (User) request.getSession().getAttribute("reader");
        HttpSession session = request.getSession();

        //      Get books
        String editId = (String) session.getAttribute("editId");

        try {
            int catalogId = Integer.parseInt(editId);
            CatalogObj catalogObj = CatalogObjDao.findCatalogObjById(catalogId);
            session.setAttribute("catalog", catalogObj);
            List<Author> authorItems = AuthorDao.findAllAuthor();
            session.setAttribute("authors", authorItems);
            List<Publishing> publishingItems = PublishingDao.findAllPublishing();
            session.setAttribute("publishings", publishingItems);
        } catch (NumberFormatException e) {
            log.trace("Catalog itemId doesn't parse --> " + e);
        } catch (DBException e) {
            String errorMessage = e.getMessage();
            session.setAttribute("errorMessage", errorMessage);
            log.error("errorMessage --> " + errorMessage);
            log.debug("Command Post finished");
            return Path.COMMAND__ERROR;
        }


        log.debug("Command finished");
        return Path.PAGE__SETTINGS_ADMIN_BOOK;
    }

    @Override
    public String executePost(HttpServletRequest request,
                              HttpServletResponse response) throws IOException, ServletException {

        log.debug("Command Post starts");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        User reader = (User) session.getAttribute("reader");
        String editId = (String) session.getAttribute("editId");


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

//      Edit Book
        String name = request.getParameter("name");
        String strAuthorId = request.getParameter("authorId");
        String strPublishingId = request.getParameter("publishingId");
        String strYear = request.getParameter("year");
        String description = request.getParameter("description");
        String strFine = request.getParameter("fine");

        if ((name != null) && (strAuthorId != null) && (strPublishingId != null)
                && (strYear != null) && (description != null) && (strFine != null)) {

//            String name = new String(strName.getBytes("ISO-8859-1"), "utf-8");
//            String description = new String(strDescription.getBytes("ISO-8859-1"), "utf-8");

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
                int catalogId = Integer.parseInt(editId);
                int authorId = Integer.parseInt(strAuthorId);
                int publishingId = Integer.parseInt(strPublishingId);
                CatalogObjDao.updateCatalogObj(name, year,fine, description, authorId, publishingId, catalogId);
                log.debug("Update catalogObj --> catalogId: " + catalogId);
            } catch (NumberFormatException e) {
                log.trace("Catalog itemId doesn't parse --> " + e);
            } catch (DBException e) {
                String errorMessage = e.getMessage();
                session.setAttribute("errorMessage", errorMessage);
                log.error("errorMessage --> " + errorMessage);
                log.debug("Command Post finished");
                return Path.COMMAND__ERROR;
            }
            log.debug("Command Post finished");
            return Path.COMMAND__EDIT_ADMIN_BOOK;
        }

        log.debug("Command Post finished");
        return Path.COMMAND__SETTINGS_ADMIN_BOOK;
    }
}
