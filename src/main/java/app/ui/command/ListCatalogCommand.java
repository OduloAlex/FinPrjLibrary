package app.ui.command;

import app.Path;
import app.dao.CatalogObjDao;
import app.domain.CatalogObj;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;
import java.io.IOException;
import java.util.List;

/**
 * Lists Catalog items.
 *
 * @author
 *
 */
public class ListCatalogCommand extends Command{

    private static final long serialVersionUID = 7732286214029478505L;

    private static final Logger log = Logger.getLogger(ListCatalogCommand.class);

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response) throws IOException, ServletException {

        log.debug("Command starts");

        String localeToSet = request.getParameter("localeToSet");
        if (localeToSet != null && !localeToSet.isEmpty()) {
            log.debug("Set locale______------>>>>>");
            HttpSession session = request.getSession();
            Config.set(session, "javax.servlet.jsp.jstl.fmt.locale", localeToSet);
            session.setAttribute("defaultLocale", localeToSet);
//            user.setLocaleName(localeToSet);
//             new UserDao().updateUser(user);
        }


        List<CatalogObj> catalogItems = CatalogObjDao.findAllCatalogObj();
        log.trace("Found in DB: findAllCatalogObj --> " + catalogItems);

        // sort menu by category
//        Collections.sort(menuItems, new Comparator<MenuItem>() {
//            public int compare(MenuItem o1, MenuItem o2) {
//                return (int)(o1.getCategoryId() - o2.getCategoryId());
//            }
//        });

        request.setAttribute("catalogItems", catalogItems);
        log.trace("Set the request attribute: catalogItems --> " + catalogItems);

        log.debug("Command finished");
        return Path.PAGE__LIST_CATALOG;
    }

}
