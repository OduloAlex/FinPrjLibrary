package app.ui.command.librarian;

import app.Path;
import app.dao.*;
import app.domain.*;
import app.ui.command.Command;
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

public class MakeLibCardCommand extends Command {

    private static final long serialVersionUID = 7732266253902928505L;

    private static final Logger log = Logger.getLogger(MakeLibCardCommand.class);

    @Override
    public String executeGet(HttpServletRequest request,
                             HttpServletResponse response) throws IOException, ServletException {

        log.debug("Command starts");

        User user = (User) request.getSession().getAttribute("user");
        User reader = (User) request.getSession().getAttribute("reader");
        HttpSession session = request.getSession();

        //      Get books
        String itemId = request.getParameter("giveId");
        if (itemId != null) {
            try {
                List<Book> books = null;
                int catalogId = Integer.parseInt(itemId);
                books = BookDao.findBookByCatalogIdStateLib(catalogId);
                session.setAttribute("books", books);
                log.debug("Set the session attribute: books --> " + books);
            } catch (NumberFormatException e) {
                log.trace("Catalog itemId doesn't parse --> " + e);
            } catch (DBException e) {
                String errorMessage = e.getMessage();
                session.setAttribute("errorMessage", errorMessage);
                log.error("errorMessage --> " + errorMessage);
                log.debug("Command Post finished");
                return Path.COMMAND__ERROR;
            }
        }

        log.debug("Command finished");
        return Path.PAGE__MAKE_LIB_CARD;
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
                String errorMessage = "ErrorWrongData";
                session.setAttribute("errorMessage", errorMessage);
                log.error("errorMessage --> " + errorMessage);
                log.debug("Command Post finished");
                return Path.COMMAND__ERROR;
            }
            Calendar today = Calendar.getInstance();
            today.setTime(Calendar.getInstance().getTime());
            if (dateReturn.compareTo(today) < 0) {
                String errorMessage = "ErrorWrongData";
                session.setAttribute("errorMessage", errorMessage);
                log.error("errorMessage --> " + errorMessage);
                log.debug("Command Post finished");
                return Path.COMMAND__ERROR;
            }
            log.debug("Today date --> " + Card.calendarToString(today));
            log.debug("Return date --> " + Card.calendarToString(dateReturn));
            try {
                int bookId = Integer.parseInt(itemId);
                int readerId = reader.getId();
                log.debug("Make card --> bookId " + bookId + ", userId " + readerId);
                Book book = BookDao.findBookById(bookId);

                Card card = new Card();
                card.setCreateTime(today);
                card.setReturnTime(dateReturn);
                card.setUser(reader);
                card.setBook(book);
                CardDao.addCard(card);
                log.debug("Add Card successful");
                int stateId = 2;
                if("room".equals(state)){
                    stateId = 3;
                }
                BookDao.updateBookState(stateId, bookId);
                log.debug("Update Book State " + stateId + " successful");
                CatalogObj catalogObj = book.getCatalogObj();
                int quantity = catalogObj.getQuantity() - 1;
                if(quantity<0){
                    quantity = 0;
                    log.trace("Error Catalog quantity less than 0 ");
                }
                CatalogObjDao.updateCatalogObjQuantity(quantity, catalogObj.getId());
                log.debug("Update CatalogObj quantity to " + quantity + " successful");

                int catalogId = book.getCatalogObj().getId();
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
                log.trace("Book itemId doesn't parse --> " + e);
            } catch (DBException e) {
                String errorMessage = e.getMessage();
                session.setAttribute("errorMessage", errorMessage);
                log.error("errorMessage --> " + errorMessage);
                log.debug("Command Post finished");
                return Path.COMMAND__ERROR;
            }
            log.debug("Command Post finished");
            return Path.COMMAND__LIST_LIB_ORDERS;
        }

        log.debug("Command Post finished");
        return Path.COMMAND__MAKE_LIB_CARD;
    }
}