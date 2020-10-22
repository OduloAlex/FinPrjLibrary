package app.ui.command.librarian;

import app.Path;
import app.dao.*;
import app.domain.*;
import app.ui.command.Command;
import app.ui.command.UserSettingsCommand;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * MakeLibCard command.
 *
 * @author Alex Odulo
 */
public class MakeLibCardCommand extends Command {

    private static final long serialVersionUID = 7732266253902928505L;

    private static final Logger log = Logger.getLogger(MakeLibCardCommand.class);

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

        //      Get books
        String itemId = request.getParameter("giveId");
        if (itemId != null) {
            try {
                List<Book> books = null;
                int catalogId = Integer.parseInt(itemId);
                books = BookDao.findBookByCatalogIdStateLib(catalogId);
                session.setAttribute("books", books);
            } catch (NumberFormatException e) {
                log.trace("Catalog itemId doesn't parse --> " + e);
            } catch (DBException e) {
                DBException.outputException(session, e.getMessage());
                return Path.COMMAND__ERROR;
            }
        }

        log.debug("Command finished");
        return Path.PAGE__MAKE_LIB_CARD;
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

        User reader = (User) request.getSession().getAttribute("reader");
        HttpSession session = request.getSession();

        try {
            UserSettingsCommand.setUserLocale(request);
        } catch (DBException e) {
            DBException.outputException(session, e.getMessage());
            return Path.COMMAND__ERROR;
        }

//      Make Card
        String itemId = request.getParameter("makeId");
        String date = request.getParameter("date");
        String state = request.getParameter("state");
        if ((itemId != null) && (date != null) && (state != null)) {
            Calendar dateReturn = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                dateReturn.setTime(sdf.parse(date));
            } catch (ParseException e) {
                DBException.outputException(session, "ErrorWrongData");
                return Path.COMMAND__ERROR;
            }
            Calendar today = Calendar.getInstance();
            today.setTime(Calendar.getInstance().getTime());
            if (dateReturn.compareTo(today) < 0) {
                DBException.outputException(session, "ErrorWrongData");
                return Path.COMMAND__ERROR;
            }
            log.debug("Today date --> " + Card.calendarToString(today));
            log.debug("Return date --> " + Card.calendarToString(dateReturn));
            try {
                int bookId = Integer.parseInt(itemId);
                int readerId = reader.getId();
                Book book = BookDao.findBookById(bookId);

                Card card = new Card();
                card.setCreateTime(today);
                card.setReturnTime(dateReturn);
                card.setUser(reader);
                card.setBook(book);

                int stateId = 2;
                if ("room".equals(state)) {
                    stateId = 3;
                }

                CatalogObj catalogObj = book.getCatalogObj();
                int quantity = catalogObj.getQuantity() - 1;
                if (quantity < 0) {
                    quantity = 0;
                    log.trace("Error Catalog quantity less than 0 ");
                }

                int catalogId = book.getCatalogObj().getId();

                CardDao.addCard(card, stateId, bookId, quantity, catalogId, readerId);
            } catch (NumberFormatException e) {
                log.trace("Book itemId doesn't parse --> " + e);
            } catch (DBException e) {
                DBException.outputException(session, e.getMessage());
                return Path.COMMAND__ERROR;
            }
            log.debug("Command Post finished");
            return Path.COMMAND__LIST_LIB_ORDERS;
        }

        return Path.COMMAND__MAKE_LIB_CARD;
    }
}