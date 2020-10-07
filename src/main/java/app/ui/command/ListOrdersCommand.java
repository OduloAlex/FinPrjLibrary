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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ListOrdersCommand extends Command {

    private static final long serialVersionUID = 7732286214029478505L;

    private static final Logger log = Logger.getLogger(ListOrdersCommand.class);

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response, RequestType requestType) throws IOException, ServletException {

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

//      Get orders
        List<Order> orders;
        int page;
        String show = request.getParameter("show");
        if ((show != null && !show.isEmpty()) && ("all".equals(show))) {
            log.debug("Show all order------>>>>> " + show);
            orders = OrderDao.findAllOrderByUsersId(user.getId());
            log.trace("Found in DB: findAllOrders --> " + orders);
            page = 1;
        } else {
            orders = (List<Order>) request.getSession().getAttribute("orders");
            log.trace("Found in Attribute: findAllOrder --> " + orders);
            page = (int) request.getSession().getAttribute("page");
        }

//      Find by name
        String find = request.getParameter("findName");
        if (find != null && !find.isEmpty()) {
            log.debug("Find by name in orders ------>>>>> " + find);
            orders = orders.stream()
                    .filter(c -> c.getCatalogObj().getName().equals(find))
                    .collect(Collectors.toList());
        }

//      Find by author
        String findAuthor = request.getParameter("findAuthor");
        if (findAuthor != null && !findAuthor.isEmpty()) {
            log.debug("Find by author in orders ------>>>>> " + findAuthor);
            orders = orders.stream()
                    .filter(c -> c.getCatalogObj().getAuthor().getName().equals(findAuthor))
                    .collect(Collectors.toList());
        }

//      Sort
        String sort = request.getParameter("sort");
        if (sort != null && !sort.isEmpty()) {
            if ("name".equals(sort)) {
                log.debug("Sort orders by------>>>>> " + sort);
                orders = orders.stream()
                        .sorted(Comparator.comparing(c -> c.getCatalogObj().getName()))
                        .collect(Collectors.toList());
            } else if ("author".equals(sort)) {
                log.debug("Sort orders by------>>>>> " + sort);
                orders = orders.stream()
                        .sorted(Comparator.comparing(c -> c.getCatalogObj().getAuthor().getName()))
                        .collect(Collectors.toList());
            } else if ("publishing".equals(sort)) {
                log.debug("Sort orders by------>>>>> " + sort);
                orders = orders.stream()
                        .sorted(Comparator.comparing(c -> c.getCatalogObj().getPublishing().getName()))
                        .collect(Collectors.toList());
            } else if ("year".equals(sort)) {
                log.debug("Sort orders by------>>>>> " + sort);
                orders = orders.stream()
                        .sorted(Comparator.comparing(c -> c.getCatalogObj().getYear()))
                        .collect(Collectors.toList());
            }
        }

//      Pagination
        String goPage = request.getParameter("goPage");
        if (goPage != null && !goPage.isEmpty()) {
            log.debug("Go page ------>>>>> " + goPage);
            if ("next".equals(goPage)) {
                if (orders.size() > (page * 5)) {
                    page++;
                }
            } else if (("previous".equals(goPage)) && (page != 1)) {
                page--;
            }
        }
        int lastPage = page * 5;
        if (lastPage >= orders.size()) {
            lastPage = orders.size();
        }
        List<Order> ordersPage = new ArrayList<>(orders.subList(((page * 5) - 5), lastPage));

        request.setAttribute("ordersPage", ordersPage);
        log.debug("Set the request attribute: ordersPage --> " + ordersPage);

        request.getSession().setAttribute("orders", orders);
        log.trace("Set the request attribute: orders --> " + orders);

        request.getSession().setAttribute("page", page);
        log.debug("Set page --> " + page);

        log.debug("Command finished");
        return Path.PAGE__LIST_ORDERS;
    }

}
