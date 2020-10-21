package app.ui.command.admin;

import app.Path;
import app.dao.*;
import app.domain.Author;
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
 * ListAdminAuthors command.
 *
 * @author Alex Odulo
 */
public class ListAdminAuthorsCommand extends Command {

    private static final long serialVersionUID = 7732286225029675505L;

    private static final Logger log = Logger.getLogger(ListAdminAuthorsCommand.class);

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
//      Get Authors
        List<Author> authorItems = null;
        int page;
        String show = request.getParameter("show");
        if ((show != null && !show.isEmpty()) && ("all".equals(show))) {
            try {
                authorItems = AuthorDao.findAllAuthor();
            } catch (DBException e) {
                DBException.outputException(session, e.getMessage());
                return Path.PAGE__ERROR_PAGE;
            }
            log.trace("Found in DB: findAllCatalogObj --> " + authorItems);
            page = 1;
        } else {
            authorItems = (List<Author>) request.getSession().getAttribute("authorItems");
            log.trace("Found in Attribute: findAllCatalogObj --> " + authorItems);
            page = (int) request.getSession().getAttribute("page");
        }

//      Pagination
        List<Author> authorPage = null;
        if (authorItems != null) {
            String goPage = request.getParameter("goPage");
            if (goPage != null && !goPage.isEmpty()) {
                log.debug("Go page ------>>>>> " + goPage);
                if ("next".equals(goPage)) {
                    if (authorItems.size() > (page * 5)) {
                        page++;
                    }
                } else if (("previous".equals(goPage)) && (page != 1)) {
                    page--;
                }
            }

            int lastPage = page * 5;
            if (lastPage >= authorItems.size()) {
                lastPage = authorItems.size();
            }
            authorPage = new ArrayList<>(authorItems.subList(((page * 5) - 5), lastPage));
        }
        request.setAttribute("authorPage", authorPage);
        log.debug("Set the request attribute: authorPage --> " + authorPage);

        request.getSession().setAttribute("authorItems", authorItems);
        log.trace("Set the request attribute: authorItems --> " + authorItems);

        request.getSession().setAttribute("page", page);
        log.debug("Set page --> " + page);

        log.debug("Command finished");
        return Path.PAGE__LIST_ADMIN_AUTHORS;
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

        try {
            UserSettingsCommand.setUserLocale(request);
        } catch (DBException e) {
            DBException.outputException(session, e.getMessage());
            return Path.COMMAND__ERROR;
        }

//      Cancel Author
        String itemId = request.getParameter("cancelId");
        if (itemId != null) {
            try {
                int authorId = Integer.parseInt(itemId);
                if (!AuthorDao.delAuthorById(authorId)) {
                    String errorMessage = "Can't del Author in DB";
                    session.setAttribute("errorMessage", errorMessage);
                    log.error("errorMessage --> " + errorMessage);
                    return Path.COMMAND__ERROR;
                }
            } catch (NumberFormatException e) {
                log.trace("Author itemId doesn't parse --> " + e);
            } catch (DBException e) {
                DBException.outputException(session, e.getMessage());
                return Path.COMMAND__ERROR;
            }
        }

//      Make Author
        String name = request.getParameter("makeAuthor");
        if (name != null) {
            if (name.isEmpty() || (name.length() > 45)) {
                String errorMessage = "ErrorMoreThan45ch";
                request.getSession().setAttribute("errorMessage", errorMessage);
                log.error("errorMessage --> " + errorMessage);
                return Path.COMMAND__ERROR;
            }
            Author author = new Author();
            author.setName(name);
            try {
                AuthorDao.addAuthor(author);
            } catch (DBException e) {
                DBException.outputException(session, e.getMessage());
                return Path.COMMAND__ERROR;
            }
        }

        log.debug("Command Post finished");
        return Path.COMMAND__LIST_ADMIN_AUTHORS;
    }
}
