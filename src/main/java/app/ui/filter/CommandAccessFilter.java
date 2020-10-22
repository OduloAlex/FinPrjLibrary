package app.ui.filter;

import app.Path;
import app.domain.Role;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

/**
 * Security filter
 *
 * @author Alex Odulo
 */
public class CommandAccessFilter implements Filter {

    private static final Logger log = Logger.getLogger(CommandAccessFilter.class);

    // commands access
    private static Map<Role, List<String>> accessMap = new HashMap<Role, List<String>>();
    private static List<String> commons = new ArrayList<String>();
    private static List<String> outOfControl = new ArrayList<String>();

    /**
     * Access Filter destruction
     */
    @Override
    public void destroy() {
        // do nothing
    }

    /**
     * Access Filter execution
     *
     * @param request  ServletRequest
     * @param response ServletResponse
     * @param chain    FilterChain
     * @throws IOException      IOException
     * @throws ServletException ServletException
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        if (accessAllowed(request)) {
            chain.doFilter(request, response);
        } else {
            String errorMessage = "ErrorDontHavePermission";

            request.setAttribute("errorMessage", errorMessage);
            log.trace("Set the request attribute: errorMessage --> " + errorMessage);

            request.getRequestDispatcher(Path.PAGE__ERROR_PAGE)
                    .forward(request, response);
        }
    }

    /**
     * Access allowed
     *
     * @param request ServletRequest
     * @return true if access allowed
     */
    private boolean accessAllowed(ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String commandName = request.getParameter("command");
        if (commandName == null || commandName.isEmpty())
            return false;

        if (outOfControl.contains(commandName))
            return true;

//      out if session is null
        HttpSession session = httpRequest.getSession(false);
        if (session == null)
            return false;

        Role userRole = (Role) session.getAttribute("userRole");
        if (userRole == null)
            return false;

        return accessMap.get(userRole).contains(commandName)
                || commons.contains(commandName);
    }

    /**
     * Filter initialization
     *
     * @param fConfig FilterConfig
     * @throws ServletException ServletException
     */
    @Override
    public void init(FilterConfig fConfig) throws ServletException {
        // roles
        accessMap.put(Role.ADMIN, asList(fConfig.getInitParameter("admin")));
        accessMap.put(Role.LIBRARIAN, asList(fConfig.getInitParameter("librarian")));
        accessMap.put(Role.READER, asList(fConfig.getInitParameter("reader")));
        log.trace("Access map --> " + accessMap);

        // commons
        commons = asList(fConfig.getInitParameter("common"));
        log.trace("Common commands --> " + commons);

        // out of control
        outOfControl = asList(fConfig.getInitParameter("out-of-control"));
        log.trace("Out of control commands --> " + outOfControl);
    }

    /**
     * Extracts parameter values from string.
     *
     * @param str parameter values string.
     * @return list of parameter values.
     */
    private List<String> asList(String str) {
        List<String> list = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(str);
        while (st.hasMoreTokens()) {
            list.add(st.nextToken());
        }
        return list;
    }

}