package app.ui.command.librarian;

import app.Path;
import app.dao.*;
import app.domain.Card;
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
import java.util.List;

/**
 * ListLibCards command.
 *
 * @author Alex Odulo
 */
public class ListLibCardsCommand extends Command {

    private static final long serialVersionUID = 7732286253202978505L;

    private static final Logger log = Logger.getLogger(ListLibCardsCommand.class);

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
        User reader = (User) request.getSession().getAttribute("reader");

//      Get Cards
        List<Card> cardsItems = null;
        int page;
        String show = request.getParameter("show");
        if ((show != null && !show.isEmpty()) && ("all".equals(show))) {
            try {
                cardsItems = CardDao.findAllCardByUsersId(reader.getId());
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

//      Pagination
        List<Card> cardPage = null;

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
            cardPage = new ArrayList<>(cardsItems.subList(((page * 5) - 5), lastPage));
        }
        request.setAttribute("cardsPage", cardPage);
        log.debug("Set the request attribute: cardPage --> " + cardPage);

        request.getSession().setAttribute("cardsItems", cardsItems);
        log.trace("Set the request attribute: cardsItems --> " + cardsItems);

        request.getSession().setAttribute("page", page);
        log.debug("Set page --> " + page);

        log.debug("Command finished");
        return Path.PAGE__LIST_LIB_CARDS;
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

        User reader = (User) request.getSession().getAttribute("reader");
        HttpSession session = request.getSession();

        try {
            UserSettingsCommand.setUserLocale(request);
        } catch (DBException e) {
            DBException.outputException(session, e.getMessage());
            return Path.COMMAND__ERROR;
        }

//      Del Card
        String itemId = request.getParameter("deleteId");
        if (itemId != null) {
            try {
                int bookId = Integer.parseInt(itemId);
                int readerId = reader.getId();

                CatalogObj catalogObj = BookDao.findBookById(bookId).getCatalogObj();
                int quantity = catalogObj.getQuantity() + 1;

                CardDao.delCard(bookId, readerId, BookDao.STATE_LIB, quantity, catalogObj.getId());
            } catch (NumberFormatException e) {
                log.trace("Card itemId doesn't parse --> " + e);
            } catch (DBException e) {
                DBException.outputException(session, e.getMessage());
                return Path.COMMAND__ERROR;
            }
        }

        log.debug("Command Post finished");
        return Path.COMMAND__LIST_LIB_CARDS;
    }
}
