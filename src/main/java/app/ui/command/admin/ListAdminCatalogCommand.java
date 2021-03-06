package app.ui.command.admin;

import app.Path;
import app.dao.*;
import app.domain.CatalogObj;
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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ListAdminCatalog command.
 *
 * @author Alex Odulo
 */
public class ListAdminCatalogCommand extends Command {

    private static final long serialVersionUID = 7732286225029476505L;

    private static final Logger log = Logger.getLogger(ListAdminCatalogCommand.class);

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
//      Get Catalog
        List<CatalogObj> catalogItems = null;
        int page;
        String show = request.getParameter("show");
        if ((show != null && !show.isEmpty()) && ("all".equals(show))) {
            try {
                catalogItems = CatalogObjDao.findAllCatalogObj();
            } catch (DBException e) {
                DBException.outputException(session, e.getMessage());
                return Path.PAGE__ERROR_PAGE;
            }
            log.trace("Found in DB: findAllCatalogObj --> " + catalogItems);
            page = 1;
        } else {
            catalogItems = (List<CatalogObj>) request.getSession().getAttribute("catalogItems");
            log.trace("Found in Attribute: findAllCatalogObj --> " + catalogItems);
            page = (int) request.getSession().getAttribute("page");
        }

//      Find by name
        String find = request.getParameter("findName");
        if (find != null && !find.isEmpty()) {
            catalogItems = catalogItems.stream().filter(c -> c.getName().equals(find)).collect(Collectors.toList());
        }

//      Find by author
        String findAuthor = request.getParameter("findAuthor");
        if (findAuthor != null && !findAuthor.isEmpty()) {
            catalogItems = catalogItems.stream().filter(c -> c.getAuthor().getName().equals(findAuthor)).collect(Collectors.toList());
        }

//      Sort
        String sort = request.getParameter("sort");
        if (sort != null && !sort.isEmpty()) {
            if ("name".equals(sort)) {
                catalogItems = catalogItems.stream().sorted(Comparator.comparing(CatalogObj::getName)).collect(Collectors.toList());
            } else if ("author".equals(sort)) {
                catalogItems = catalogItems.stream().sorted(Comparator.comparing(c -> c.getAuthor().getName())).collect(Collectors.toList());
            } else if ("publishing".equals(sort)) {
                catalogItems = catalogItems.stream().sorted(Comparator.comparing(c -> c.getPublishing().getName())).collect(Collectors.toList());
            } else if ("year".equals(sort)) {
                catalogItems = catalogItems.stream().sorted(Comparator.comparing(CatalogObj::getYear)).collect(Collectors.toList());
            }
        }

//      Pagination
        List<CatalogObj> catalogPage = null;
        if (catalogItems != null) {
            String goPage = request.getParameter("goPage");
            if (goPage != null && !goPage.isEmpty()) {
                log.debug("Go page ------>>>>> " + goPage);
                if ("next".equals(goPage)) {
                    if (catalogItems.size() > (page * 5)) {
                        page++;
                    }
                } else if (("previous".equals(goPage)) && (page != 1)) {
                    page--;
                }
            }

            int lastPage = page * 5;
            if (lastPage >= catalogItems.size()) {
                lastPage = catalogItems.size();
            }
            catalogPage = new ArrayList<>(catalogItems.subList(((page * 5) - 5), lastPage));
        }
        request.setAttribute("catalogPage", catalogPage);
        log.debug("Set the request attribute: catalogPage --> " + catalogPage);

        request.getSession().setAttribute("catalogItems", catalogItems);
        log.trace("Set the request attribute: catalogItems --> " + catalogItems);

        request.getSession().setAttribute("page", page);
        log.debug("Set page --> " + page);

        log.debug("Command finished");
        return Path.PAGE__LIST_ADMIN_CATALOG;
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

//      Cancel Order
        String itemId = request.getParameter("cancelId");
        if (itemId != null) {
            try {
                int catalogId = Integer.parseInt(itemId);
                if (!CatalogObjDao.delCatalogObjById(catalogId)) {
                    String errorMessage = "Can't del Catalog in DB";
                    session.setAttribute("errorMessage", errorMessage);
                    log.error("errorMessage --> " + errorMessage);
                    return Path.COMMAND__ERROR;
                }
            } catch (NumberFormatException e) {
                log.trace("Catalog itemId doesn't parse --> " + e);
            } catch (DBException e) {
                DBException.outputException(session, e.getMessage());
                return Path.COMMAND__ERROR;
            }
        }

        log.debug("Command Post finished");
        return Path.COMMAND__LIST_ADMIN_CATALOG;
    }
}