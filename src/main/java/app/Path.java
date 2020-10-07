package app;
/**
 * Path holder (jsp pages, controller commands).
 *
 * @author
 *
 */
public final class Path {

    // pages
    public static final String PAGE__LOGIN = "/login.jsp";
    public static final String PAGE__ERROR_PAGE = "/jsp/error_page.jsp";
    public static final String PAGE__REGISTRATION_PAGE = "/jsp/registration.jsp";
    public static final String PAGE__LIST_CARDS = "/jsp/client/list_cards.jsp";
    public static final String PAGE__LIST_CATALOG = "/jsp/client/list_catalog.jsp";
    public static final String PAGE__LIST_ORDERS = "/jsp/client/list_orders.jsp";

    // commands
    public static final String COMMAND__LIST_ORDERS = "/controller?command=listOrders&show=all";
    public static final String COMMAND__LIST_CATALOG = "/controller?command=listCatalog&show=all";
    public static final String COMMAND__LIST_CARDS = "/controller?command=listCards&show=all";

}