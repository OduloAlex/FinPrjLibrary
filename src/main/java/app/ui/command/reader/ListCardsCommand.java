package app.ui.command.reader;

import app.Path;
import app.dao.*;
import app.domain.Card;
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

/**
 * Lists Cards items.
 *
 * @author
 */
public class ListCardsCommand extends Command {

    private static final long serialVersionUID = 7732286214029478505L;

    private static final Logger log = Logger.getLogger(ListCardsCommand.class);

    @Override
    public String executeGet(HttpServletRequest request,
                             HttpServletResponse response) throws IOException, ServletException {

        log.debug("Command starts");

        User user = (User) request.getSession().getAttribute("user");

//      Get cards
        List<Card> cardsItems = null;
        int page;
        String show = request.getParameter("show");
        if ((show != null && !show.isEmpty()) && ("all".equals(show))) {
            try {
                cardsItems = CardDao.findAllCardByUsersId(user.getId());
            } catch (DBException e) {
                String errorMessage = e.getMessage();
                request.setAttribute("errorMessage", errorMessage);
                log.error("errorMessage --> " + errorMessage);
                return Path.PAGE__ERROR_PAGE;
            }
            log.trace("Found in DB: findAllCards --> " + cardsItems);
            page = 1;
        } else {
            cardsItems = (List<Card>) request.getSession().getAttribute("cardsItems");
            log.trace("Found in Attribute: findAllCards --> " + cardsItems);
            page = (int) request.getSession().getAttribute("page");
        }

//      Find by name
        String find = request.getParameter("findName");
        if (find != null && !find.isEmpty()) {
            cardsItems = cardsItems.stream()
                    .filter(c -> c.getBook().getCatalogObj().getName().equals(find))
                    .collect(Collectors.toList());
        }

//      Find by author
        String findAuthor = request.getParameter("findAuthor");
        if (findAuthor != null && !findAuthor.isEmpty()) {
            cardsItems = cardsItems.stream()
                    .filter(c -> c.getBook().getCatalogObj().getAuthor().getName().equals(findAuthor))
                    .collect(Collectors.toList());
        }

//      Sort
        String sort = request.getParameter("sort");
        if (sort != null && !sort.isEmpty()) {
            if ("name".equals(sort)) {
                cardsItems = cardsItems.stream()
                        .sorted(Comparator.comparing(c -> c.getBook().getCatalogObj().getName()))
                        .collect(Collectors.toList());
            } else if ("author".equals(sort)) {
                cardsItems = cardsItems.stream()
                        .sorted(Comparator.comparing(c -> c.getBook().getCatalogObj().getAuthor().getName()))
                        .collect(Collectors.toList());
            } else if ("publishing".equals(sort)) {
                cardsItems = cardsItems.stream()
                        .sorted(Comparator.comparing(c -> c.getBook().getCatalogObj().getPublishing().getName()))
                        .collect(Collectors.toList());
            } else if ("year".equals(sort)) {
                cardsItems = cardsItems.stream()
                        .sorted(Comparator.comparing(c -> c.getBook().getCatalogObj().getYear()))
                        .collect(Collectors.toList());
            }
        }

//      Pagination
        List<Card> cardsPage = null;
        if (cardsItems != null) {
            String goPage = request.getParameter("goPage");
            if (goPage != null && !goPage.isEmpty()) {
                log.debug("Go page ------>>>>> " + goPage);
                if ("next".equals(goPage)) {
                    if (cardsItems.size() > (page * 5)) {
                        page++;
                    }
                } else if (("previous".equals(goPage)) && (page != 1)) {
                    page--;
                }
            }
            int lastPage = page * 5;
            if (lastPage >= cardsItems.size()) {
                lastPage = cardsItems.size();
            }
            cardsPage = new ArrayList<>(cardsItems.subList(((page * 5) - 5), lastPage));
        }
        request.setAttribute("cardsPage", cardsPage);
        log.debug("Set the request attribute: cardsPage --> " + cardsPage);

        request.getSession().setAttribute("cardsItems", cardsItems);
        log.trace("Set the request attribute: cards --> " + cardsItems);

        request.getSession().setAttribute("page", page);
        log.debug("Set page --> " + page);

        log.debug("Command finished");
        return Path.PAGE__LIST_CARDS;
    }

    @Override
    public String executePost(HttpServletRequest request,
                              HttpServletResponse response) throws IOException, ServletException {

        log.debug("Command Post starts");

        User user = (User) request.getSession().getAttribute("user");

//      Set locale
        String localeToSet = request.getParameter("localeToSet");
        if (localeToSet != null && !localeToSet.isEmpty()) {
            HttpSession session = request.getSession();
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

        log.debug("Command Post finished");
        return Path.COMMAND__LIST_CARDS;
    }
}