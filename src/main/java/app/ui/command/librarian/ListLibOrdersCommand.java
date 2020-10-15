package app.ui.command.librarian;

import app.Path;
import app.dao.BookDao;
import app.dao.DBException;
import app.dao.OrderDao;
import app.dao.UserDao;
import app.domain.Book;
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
import java.util.List;

public class ListLibOrdersCommand extends Command {

    private static final long serialVersionUID = 7732286253202978505L;

    private static final Logger log = Logger.getLogger(ListLibOrdersCommand.class);

    @Override
    public String executeGet(HttpServletRequest request,
                             HttpServletResponse response) throws IOException, ServletException {

        log.debug("Command starts");

        User user = (User) request.getSession().getAttribute("user");
        User reader = (User) request.getSession().getAttribute("reader");
        HttpSession session = request.getSession();

//      Get Orders
        List<Order> ordersItems = null;
        int page;
        String show = request.getParameter("show");
        if ((show != null && !show.isEmpty()) && ("all".equals(show))) {
            log.debug("Show all orders------>>>>> " + show);
            try {
                ordersItems = OrderDao.findAllOrderByUsersId(reader.getId());
            } catch (DBException e) {
                String errorMessage = e.getMessage();
                request.setAttribute("errorMessage", errorMessage);
                log.error("errorMessage --> " + errorMessage);
                log.debug("Command Post finished");
                return Path.PAGE__ERROR_PAGE;
            }
            log.trace("Found in DB: findAllOrders --> " + ordersItems);
            page = 1;
        } else {
            ordersItems= (List<Order>) request.getSession().getAttribute("ordersItems");
            log.trace("Found in Attribute: findAllOrders --> " + ordersItems);
            page = (int) request.getSession().getAttribute("page");
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
        log.trace("Set the request attribute: ordersItems --> " + ordersItems);

        request.getSession().setAttribute("page", page);
        log.debug("Set page --> " + page);

        log.debug("Command finished");
        return Path.PAGE__LIST_LIB_ORDERS;
    }

    @Override
    public String executePost(HttpServletRequest request,
                              HttpServletResponse response) throws IOException, ServletException {

        log.debug("Command Post starts");

        User user = (User) request.getSession().getAttribute("user");
        User reader = (User) request.getSession().getAttribute("reader");
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

//      Cancel Order
        String itemId = request.getParameter("cancelId");
        if (itemId != null) {
                try {
                    int catalogId = Integer.parseInt(itemId);
                    int readerId = reader.getId();
                    log.debug("Del Order --> catalogId " + catalogId + ", userId " + readerId);
                    if(!OrderDao.delOrder(catalogId, readerId)){
                        String errorMessage = "Can't del Order in DB";
                        session.setAttribute("errorMessage", errorMessage);
                        log.error("errorMessage --> " + errorMessage);
                        log.debug("Command Post finished");
                        return  Path.COMMAND__ERROR;
                    }
                    log.debug("Del Order successful");
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

        log.debug("Command Post finished");
        return Path.COMMAND__LIST_LIB_ORDERS;
    }
}