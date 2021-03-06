package app.ui.command.reader;

import app.Path;
import app.dao.*;
import app.domain.Card;
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
 * Lists Cards items.
 *
 * @author
 */
public class ListCardsCommand extends Command {

    private static final long serialVersionUID = 7732286214029478505L;

    private static final Logger log = Logger.getLogger(ListCardsCommand.class);

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
        User user = (User) request.getSession().getAttribute("user");

//      Get cards
        List<Card> cardsItems = null;
        int page;
        String show = request.getParameter("show");
        if ((show != null && !show.isEmpty()) && ("all".equals(show))) {
            try {
                cardsItems = CardDao.findAllCardByUsersId(user.getId());
            } catch (DBException e) {
                DBException.outputException(session, e.getMessage());
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
            cardsItems = cardsItems.stream().filter(c -> c.getBook().getCatalogObj().getName().equals(find)).collect(Collectors.toList());
        }

//      Find by author
        String findAuthor = request.getParameter("findAuthor");
        if (findAuthor != null && !findAuthor.isEmpty()) {
            cardsItems = cardsItems.stream().filter(c -> c.getBook().getCatalogObj().getAuthor().getName().equals(findAuthor)).collect(Collectors.toList());
        }

//      Sort
        String sort = request.getParameter("sort");
        if (sort != null && !sort.isEmpty()) {
            if ("name".equals(sort)) {
                cardsItems = cardsItems.stream().sorted(Comparator.comparing(c -> c.getBook().getCatalogObj().getName())).collect(Collectors.toList());
            } else if ("author".equals(sort)) {
                cardsItems = cardsItems.stream().sorted(Comparator.comparing(c -> c.getBook().getCatalogObj().getAuthor().getName())).collect(Collectors.toList());
            } else if ("publishing".equals(sort)) {
                cardsItems = cardsItems.stream().sorted(Comparator.comparing(c -> c.getBook().getCatalogObj().getPublishing().getName())).collect(Collectors.toList());
            } else if ("year".equals(sort)) {
                cardsItems = cardsItems.stream().sorted(Comparator.comparing(c -> c.getBook().getCatalogObj().getYear())).collect(Collectors.toList());
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

        log.debug("Command Post finished");
        return Path.COMMAND__LIST_CARDS;
    }
}