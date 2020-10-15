package app.ui.command.reader;

import app.Path;
import app.dao.DBException;
import app.dao.OrderDao;
import app.dao.UserDao;
import app.domain.Order;
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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ListOrdersCommand extends Command {

    private static final long serialVersionUID = 7732286214029478505L;

    private static final Logger log = Logger.getLogger(ListOrdersCommand.class);

    @Override
    public String executeGet(HttpServletRequest request,
                             HttpServletResponse response) throws IOException, ServletException {

        log.debug("Command Get starts");

        User user = (User) request.getSession().getAttribute("user");

//      Get orders
        List<Order> ordersItems = null;
        int page;
        String show = request.getParameter("show");
        if ((show != null && !show.isEmpty()) && ("all".equals(show))) {
            log.debug("Show all order------>>>>> " + show);
            try {
                ordersItems = OrderDao.findAllOrderByUsersId(user.getId());
            } catch (DBException e) {
                e.printStackTrace();
            }
            log.trace("Found in DB: findAllOrders --> " + ordersItems);
            page = 1;
        } else {
            ordersItems = (List<Order>) request.getSession().getAttribute("ordersItems");
            log.trace("Found in Attribute: findAllOrder --> " + ordersItems);
            page = (int) request.getSession().getAttribute("page");
        }

//      Find by name
        String find = request.getParameter("findName");
        if (find != null && !find.isEmpty()) {
            log.debug("Find by name in orders ------>>>>> " + find);
            ordersItems = ordersItems.stream()
                    .filter(c -> c.getCatalogObj().getName().equals(find))
                    .collect(Collectors.toList());
        }

//      Find by author
        String findAuthor = request.getParameter("findAuthor");
        if (findAuthor != null && !findAuthor.isEmpty()) {
            log.debug("Find by author in orders ------>>>>> " + findAuthor);
            ordersItems = ordersItems.stream()
                    .filter(c -> c.getCatalogObj().getAuthor().getName().equals(findAuthor))
                    .collect(Collectors.toList());
        }

//      Sort
        String sort = request.getParameter("sort");
        if (sort != null && !sort.isEmpty()) {
            if ("name".equals(sort)) {
                log.debug("Sort orders by------>>>>> " + sort);
                ordersItems = ordersItems.stream()
                        .sorted(Comparator.comparing(c -> c.getCatalogObj().getName()))
                        .collect(Collectors.toList());
            } else if ("author".equals(sort)) {
                log.debug("Sort orders by------>>>>> " + sort);
                ordersItems = ordersItems.stream()
                        .sorted(Comparator.comparing(c -> c.getCatalogObj().getAuthor().getName()))
                        .collect(Collectors.toList());
            } else if ("publishing".equals(sort)) {
                log.debug("Sort orders by------>>>>> " + sort);
                ordersItems = ordersItems.stream()
                        .sorted(Comparator.comparing(c -> c.getCatalogObj().getPublishing().getName()))
                        .collect(Collectors.toList());
            } else if ("year".equals(sort)) {
                log.debug("Sort orders by------>>>>> " + sort);
                ordersItems = ordersItems.stream()
                        .sorted(Comparator.comparing(c -> c.getCatalogObj().getYear()))
                        .collect(Collectors.toList());
            }
        }

//      Pagination
        List<Order> ordersPage = null;
        if(ordersItems!=null) {
            String goPage = request.getParameter("goPage");
            if (goPage != null && !goPage.isEmpty()) {
                log.debug("Go page ------>>>>> " + goPage);
                if ("next".equals(goPage)) {
                    if (ordersItems.size() > (page * 5)) {
                        page++;
                    }
                } else if (("previous".equals(goPage)) && (page != 1)) {
                    page--;
                }
            }
            int lastPage = page * 5;
            if (lastPage >= ordersItems.size()) {
                lastPage = ordersItems.size();
            }
            ordersPage = new ArrayList<>(ordersItems.subList(((page * 5) - 5), lastPage));
        }
        request.setAttribute("ordersPage", ordersPage);
        log.debug("Set the request attribute: ordersPage --> " + ordersPage);

        request.getSession().setAttribute("ordersItems", ordersItems);
        log.trace("Set the request attribute: orders --> " + ordersItems);

        request.getSession().setAttribute("page", page);
        log.debug("Set page --> " + page);

        log.debug("Command Get finished");
        return Path.PAGE__LIST_ORDERS;
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
                return  Path.COMMAND__ERROR;
            }
        }

//      Cancel Order
        String[] itemIds = request.getParameterValues("cancelId");
        if (itemIds != null) {
            for (String item : itemIds) {
                try {
                    int result = Integer.parseInt(item);
                    int userId = user.getId();
                    log.debug("Del Order --> catalogId " + result + ", userId " + userId);
                    if(!OrderDao.delOrder(result, userId)){
                        String errorMessage = "Can't del Order in DB";
                        session.setAttribute("errorMessage", errorMessage);
                        log.error("errorMessage --> " + errorMessage);
                        log.debug("Command Post finished");
                        return  Path.COMMAND__ERROR;
                    }
                } catch (NumberFormatException e) {
                    log.trace("Order itemId doesn't parse --> " + e);
                } catch (DBException e) {
                    String errorMessage = e.getMessage();
                    session.setAttribute("errorMessage", errorMessage);
                    log.error("errorMessage --> " + errorMessage);
                    log.debug("Command Post finished");
                    return  Path.COMMAND__ERROR;
                }
            }
        }

        log.debug("Command Post finished");
        return Path.COMMAND__LIST_ORDERS;
    }
}
