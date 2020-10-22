package app.ui.command.admin;

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
import java.util.List;

/**
 * EditAdminBook command.
 *
 * @author Alex Odulo
 */
public class EditAdminBookCommand extends Command {

    private static final long serialVersionUID = 77322654602928505L;

    private static final Logger log = Logger.getLogger(EditAdminBookCommand.class);

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
        String editId = request.getParameter("editId");
        if (editId != null) {
            session.setAttribute("editId", editId);
        } else {
            editId = (String) session.getAttribute("editId");
        }

        try {
            int catalogId = Integer.parseInt(editId);
            List<Book> books = BookDao.findBookByCatalogIdStateLib(catalogId);
            session.setAttribute("books", books);
        } catch (NumberFormatException e) {
            log.trace("Catalog itemId doesn't parse --> " + e);
        } catch (DBException e) {
            DBException.outputException(session, e.getMessage());
            return Path.COMMAND__ERROR;
        }

        log.debug("Command finished");
        return Path.PAGE__EDIT_ADMIN_BOOK;
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
        String editId = (String) session.getAttribute("editId");

        try {
            UserSettingsCommand.setUserLocale(request);
        } catch (DBException e) {
            DBException.outputException(session, e.getMessage());
            return Path.COMMAND__ERROR;
        }

//      Cancel Book
        String itemId = request.getParameter("cancelId");
        if (itemId != null) {
            try {
                int bookId = Integer.parseInt(itemId);
                if (!BookDao.delBookById(bookId)) {
                    DBException.outputException(session, "Can't del Book in DB");
                    return Path.COMMAND__ERROR;
                }
                int catalogId = Integer.parseInt(editId);
                CatalogObj catalogObj = CatalogObjDao.findCatalogObjById(catalogId);
                int quantity = catalogObj.getQuantity() - 1;
                if (quantity < 0) {
                    quantity = 0;
                    log.trace("Error Catalog quantity less than 0 ");
                }
                CatalogObjDao.updateCatalogObjQuantity(quantity, catalogObj.getId());
            } catch (NumberFormatException e) {
                log.trace("Book itemId doesn't parse --> " + e);
            } catch (DBException e) {
                DBException.outputException(session, e.getMessage());
                return Path.COMMAND__ERROR;
            }
        }

//      Make Book
        String invNumber = request.getParameter("invNumber");
        if (invNumber != null) {
            if (invNumber.isEmpty() || (invNumber.length() > 45)) {
                DBException.outputException(session, "ErrorMoreThan45ch");
                return Path.COMMAND__ERROR;
            }
            try {
                int catalogId = Integer.parseInt(editId);
                BookDao.addBook(BookDao.STATE_LIB, invNumber, catalogId);
                CatalogObj catalogObj = CatalogObjDao.findCatalogObjById(catalogId);
                int quantity = catalogObj.getQuantity() + 1;
                CatalogObjDao.updateCatalogObjQuantity(quantity, catalogId);
            } catch (NumberFormatException e) {
                log.trace("Catalog itemId doesn't parse --> " + e);
            } catch (DBException e) {
                DBException.outputException(session, "ErrorInvNumber");
                return Path.COMMAND__ERROR;
            }
        }

        return Path.COMMAND__EDIT_ADMIN_BOOK;
    }
}