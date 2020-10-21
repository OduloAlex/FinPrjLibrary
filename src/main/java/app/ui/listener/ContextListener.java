package app.ui.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;

/**
 * ContextListener class
 *
 * @author Alex Odulo
 */
public class ContextListener implements ServletContextListener {
    private static final Logger log = Logger.getLogger(ContextListener.class);

    /**
     * Servlet context destruction
     *
     * @param event ServletContextEvent
     */
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        log("Servlet context destruction starts");
        // do nothing
        log("Servlet context destruction finished");
    }

    /**
     * Servlet context initialization
     *
     * @param event ServletContextEvent
     */
    @Override
    public void contextInitialized(ServletContextEvent event) {
        log("Servlet context initialization starts");

        ServletContext servletContext = event.getServletContext();
        initLog4J(servletContext);
        initI18N(servletContext);
        initOnlyOneLogin(servletContext);
        log("Servlet context initialization finished");
    }

    /**
     * Initializes log4j framework.
     *
     * @param servletContext ServletContext
     */
    private void initLog4J(ServletContext servletContext) {
        log("Log4J initialization started");
        try {
            PropertyConfigurator.configure(servletContext.getRealPath(
                    "WEB-INF/log4j.properties"));
        } catch (Exception ex) {
            ex.printStackTrace();
            log("Exception!!!!! " + ex.getMessage());
        }

        log("Log4J initialization finished");
    }

    /**
     * Initializes i18n subsystem.
     *
     * @param servletContext ServletContext
     */
    private void initI18N(ServletContext servletContext) {
        String localesValue = servletContext.getInitParameter("locales");
        if (localesValue == null || localesValue.isEmpty()) {
            log.warn("'locales' init parameter is empty, the default encoding will be used");
        } else {
            List<String> locales = new ArrayList<String>();
            StringTokenizer st = new StringTokenizer(localesValue);
            while (st.hasMoreTokens()) {
                String localeName = st.nextToken();
                locales.add(localeName);
            }

            servletContext.setAttribute("locales", locales);
        }
    }

    /**
     * Initialization system OnlyOneLogin
     *
     * @param servletContext ServletContext
     */
    private void initOnlyOneLogin(ServletContext servletContext) {
        HashSet<String> loggedUsers = new HashSet<>();
        servletContext.setAttribute("loggedUsers", loggedUsers);
    }

    /**
     * Log for ContextListener only
     *
     * @param msg message
     */
    private void log(String msg) {
        System.out.println("[ContextListener] " + msg);
    }
}