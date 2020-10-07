package app.ui.command;

import app.Path;
import app.dao.CatalogObjDao;
import app.dao.OrderDao;
import app.dao.UserDao;
import app.domain.CatalogObj;
import app.domain.Order;
import app.domain.User;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Lists Catalog items.
 *
 * @author
 */
public class ListCatalogCommand extends Command {

    private static final long serialVersionUID = 7732286214029478505L;

    private static final Logger log = Logger.getLogger(ListCatalogCommand.class);

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response) throws IOException, ServletException {

        log.debug("Command starts");

        User user = (User) request.getSession().getAttribute("user");

//      Set locale
        String localeToSet = request.getParameter("localeToSet");
        if (localeToSet != null && !localeToSet.isEmpty()) {
            log.debug("Set locale------>>>>> " + localeToSet);
            HttpSession session = request.getSession();
            Config.set(session, "javax.servlet.jsp.jstl.fmt.locale", localeToSet);
            session.setAttribute("defaultLocale", localeToSet);
            user.setLocaleName(localeToSet);
            UserDao.updateUser(user);
        }

//      Get Catalog
        List<CatalogObj> catalogItems;
        int page;
        String show = request.getParameter("show");
        if ((show != null && !show.isEmpty()) && ("all".equals(show))) {
            log.debug("Show all catalog------>>>>> " + show);
            catalogItems = CatalogObjDao.findAllCatalogObjQNN();
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
            log.debug("Find by name in catalog ------>>>>> " + find);
            catalogItems = catalogItems.stream()
                    .filter(c -> c.getName().equals(find))
                    .collect(Collectors.toList());
        }

//      Find by author
        String findAuthor = request.getParameter("findAuthor");
        if (findAuthor != null && !findAuthor.isEmpty()) {
            log.debug("Find by author in catalog ------>>>>> " + findAuthor);
            catalogItems = catalogItems.stream()
                    .filter(c -> c.getAuthor().getName().equals(findAuthor))
                    .collect(Collectors.toList());
        }

//      Sort
        String sort = request.getParameter("sort");
        if (sort != null && !sort.isEmpty()) {
            if ("name".equals(sort)) {
                log.debug("Sort catalog by------>>>>> " + sort);
                catalogItems = catalogItems.stream()
                        .sorted(Comparator.comparing(CatalogObj::getName))
                        .collect(Collectors.toList());
            } else if ("author".equals(sort)) {
                log.debug("Sort catalog by------>>>>> " + sort);
                catalogItems = catalogItems.stream()
                        .sorted(Comparator.comparing(c -> c.getAuthor().getName()))
                        .collect(Collectors.toList());
            } else if ("publishing".equals(sort)) {
                log.debug("Sort catalog by------>>>>> " + sort);
                catalogItems = catalogItems.stream()
                        .sorted(Comparator.comparing(c -> c.getPublishing().getName()))
                        .collect(Collectors.toList());
            } else if ("year".equals(sort)) {
                log.debug("Sort catalog by------>>>>> " + sort);
                catalogItems = catalogItems.stream()
                        .sorted(Comparator.comparing(CatalogObj::getYear))
                        .collect(Collectors.toList());
            }
        }

//      Pagination
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
        List<CatalogObj> catalogPage = new ArrayList<>(catalogItems.subList(((page * 5) - 5), lastPage));

//      Make Order
        String[] itemIds = request.getParameterValues("itemId");
        if (itemIds != null) {
            for (String item : itemIds) {
                try {
                    int result = Integer.parseInt(item);
                    Order order = new Order();
                    order.setUser(user);
                    order.setState(1);
                    order.setCatalogObj(catalogPage.get(result - 1));
                    OrderDao.addOrder(order);
                    log.debug("Add new Order --> " + order);
                } catch (NumberFormatException e) {
                    log.trace("Order itemId doesn't parse --> " + e);
                }
            }
            log.debug("Command finished");
            log.debug("array " + Arrays.toString(itemIds));
            return Path.COMMAND__LIST_ORDERS;
        }

        request.setAttribute("catalogPage", catalogPage);
        log.debug("Set the request attribute: catalogPage --> " + catalogPage);

        request.getSession().setAttribute("catalogItems", catalogItems);
        log.trace("Set the request attribute: catalogItems --> " + catalogItems);

        request.getSession().setAttribute("page", page);
        log.debug("Set page --> " + page);

        log.debug("Command finished");
        return Path.PAGE__LIST_CATALOG;
    }

}
