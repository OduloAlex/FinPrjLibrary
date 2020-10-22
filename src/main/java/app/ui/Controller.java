package app.ui;

import app.ui.command.Command;
import app.ui.command.CommandContainer;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Main servlet controller.
 *
 * @author Alex Odulo
 */
public class Controller extends HttpServlet {

    private static final long serialVersionUID = 2423353715955164816L;

    private static final Logger log = Logger.getLogger(Controller.class);

    /**
     * Controller method Get request
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException ServletException
     * @throws IOException      IOException
     */
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        processGet(request, response);
    }

    /**
     * Controller method Post request
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException ServletException
     * @throws IOException      IOException
     */
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        processPost(request, response);
    }

    /**
     * Main method of this controller Get
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException ServletException
     * @throws IOException      IOException
     */
    private void processGet(HttpServletRequest request,
                            HttpServletResponse response) throws IOException, ServletException {

        log.debug("Controller Get starts");

        // extract command name from the request
        String commandName = request.getParameter("command");
        log.trace("Request parameter: command --> " + commandName);

        // obtain command object by its name
        Command command = CommandContainer.get(commandName);
        log.trace("Obtained command --> " + command);

        // execute command
        String path = command.executeGet(request, response);

        // if the forward address is not null go to the address
        if (path != null) {
            log.trace("Forward to address --> " + path);
            RequestDispatcher disp = request.getRequestDispatcher(path);
            disp.forward(request, response);
        }
        log.debug("Controller Get finished");
    }

    /**
     * Main method of this controller Post
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException ServletException
     * @throws IOException      IOException
     */
    private void processPost(HttpServletRequest request,
                             HttpServletResponse response) throws IOException, ServletException {

        log.debug("Controller Post starts");

        // extract command name from the request
        String commandName = request.getParameter("command");
        log.trace("Request parameter: command --> " + commandName);

        // obtain command object by its name
        Command command = CommandContainer.get(commandName);
        log.trace("Obtained command --> " + command);

        // execute command
        String path = command.executePost(request, response);

        // if the forward address is not null go to the address
        if (path != null) {
            log.trace("Redirect to address --> " + path);
            response.sendRedirect(path);
        }
        log.debug("Controller Post finished");
    }
}