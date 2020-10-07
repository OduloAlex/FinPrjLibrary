package app.ui;

import app.ui.command.Command;
import app.ui.command.CommandContainer;
import app.ui.command.RequestType;
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
 * @author
 *
 */
public class Controller extends HttpServlet {

    private static final long serialVersionUID = 2423353715955164816L;

    private static final Logger log = Logger.getLogger(Controller.class);

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        process(request, response, RequestType.GET);
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        process(request, response, RequestType.POST);
    }

    /**
     * Main method of this controller.
     */
    private void process(HttpServletRequest request,
                         HttpServletResponse response, RequestType requestType) throws IOException, ServletException {

        log.debug("Controller starts");

        // extract command name from the request
        String commandName = request.getParameter("command");
        log.trace("Request parameter: command --> " + commandName);

        // obtain command object by its name
        Command command = CommandContainer.get(commandName);
        log.trace("Obtained command --> " + command);

        // execute command and get forward address
        String forward = command.execute(request, response);


        // if the forward address is not null go to the address
        if (forward != null) {
            if(requestType == RequestType.GET) {
                log.trace("Forward to address --> " + forward);
                RequestDispatcher disp = request.getRequestDispatcher(forward);
                disp.forward(request, response);
            } else if(requestType == RequestType.POST)  {
                log.trace("Redirect to address --> " + forward);
                response.sendRedirect(forward);
            }
        }
        log.debug("Controller finished $$$$$$$$$$$$$$$$$$$$$$$$$$$");
    }

}