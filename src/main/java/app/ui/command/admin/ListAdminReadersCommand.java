package app.ui.command.admin;

import app.Path;
import app.dao.CardDao;
import app.dao.DBException;
import app.dao.UserDao;
import app.domain.User;
import app.ui.command.Command;
import app.ui.command.UserSettingsCommand;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ListAdminReadersCommand command.
 *
 * @author Alex Odulo
 */
public class ListAdminReadersCommand extends Command {

    private static final long serialVersionUID = 7732286257029478505L;

    private static final Logger log = Logger.getLogger(ListAdminReadersCommand.class);

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

//      Get Users
        List<User> usersItems = null;
        try {
            usersItems = UserDao.findAllUserReader();
        } catch (DBException e) {
            DBException.outputException(session, e.getMessage());
            return Path.PAGE__ERROR_PAGE;
        }

        List<Integer> cardsItems = new ArrayList();

        for (User item: usersItems) {
            try {
                cardsItems.add(CardDao.findAllCardByUsersId(item.getId()).size());
            } catch (DBException e) {
                DBException.outputException(session, e.getMessage());
                return Path.PAGE__ERROR_PAGE;
            }
        }

        request.setAttribute("cardsItems", cardsItems);

        request.getSession().setAttribute("usersItems", usersItems);
        log.trace("Set the request attribute: usersItems --> " + usersItems);

        log.debug("Command finished");
        return Path.PAGE__LIST_ADMIN_READERS;
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
        return Path.COMMAND__LIST_ADMIN_READERS;
    }
}
