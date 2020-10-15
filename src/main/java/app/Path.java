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

    public static final String PAGE__LIST_ORDERS = "/jsp/reader/list_orders.jsp";
    public static final String PAGE__LIST_CATALOG = "/jsp/reader/list_catalog.jsp";
    public static final String PAGE__LIST_CARDS = "/jsp/reader/list_cards.jsp";

    public static final String PAGE__LIST_LIB_READERS = "/jsp/librarian/list_lib_readers.jsp";
    public static final String PAGE__LIST_LIB_ORDERS = "/jsp/librarian/list_lib_orders.jsp";
    public static final String PAGE__LIST_LIB_CARDS = "/jsp/librarian/list_lib_cards.jsp";
    public static final String PAGE__MAKE_LIB_CARD = "/jsp/librarian/make_lib_card.jsp";
    public static final String PAGE__LIST_LIB_CATALOG = "/jsp/librarian/list_lib_catalog.jsp";

    public static final String PAGE__LIST_ADMIN_CATALOG = "/jsp/admin/list_admin_catalog.jsp";
    public static final String PAGE__LIST_ADMIN_USERS = "/jsp/admin/list_admin_users.jsp";
    public static final String PAGE__LIST_ADMIN_AUTHORS = "/jsp/admin/list_admin_authors.jsp";
    public static final String PAGE__LIST_ADMIN_PUBLISHINGS = "/jsp/admin/list_admin_publishings.jsp";
    public static final String PAGE__EDIT_ADMIN_BOOK = "/jsp/admin/edit_admin_book.jsp";
    public static final String PAGE__SETTINGS_ADMIN_BOOK = "/jsp/admin/settings_admin_book.jsp";
    public static final String PAGE__MAKE_ADMIN_BOOK = "/jsp/admin/make_admin_book.jsp";

    // commands
    public static final String COMMAND__LOGIN = "/controller?command=login";
    public static final String COMMAND__ERROR = "/controller?command=error";
    public static final String COMMAND__REGISTRATION = "/controller?command=registration";
    public static final String COMMAND__LOGOUT = "/controller?command=logout";

    public static final String COMMAND__LIST_ORDERS = "/controller?command=listOrders&show=all";
    public static final String COMMAND__LIST_CATALOG = "/controller?command=listCatalog&show=all";
    public static final String COMMAND__LIST_CARDS = "/controller?command=listCards&show=all";

    public static final String COMMAND__LIST_LIB_READERS = "/controller?command=listLibReaders&show=all";
    public static final String COMMAND__LIST_LIB_ORDERS = "/controller?command=listLibOrders&show=all";
    public static final String COMMAND__LIST_LIB_CARDS = "/controller?command=listLibCards&show=all";
    public static final String COMMAND__MAKE_LIB_CARD = "/controller?command=makeLibCard&show=all";
    public static final String COMMAND__LIST_LIB_CATALOG = "/controller?command=listLibCatalog&show=all";

    public static final String COMMAND__LIST_ADMIN_CATALOG = "/controller?command=listAdminCatalog&show=all";
    public static final String COMMAND__LIST_ADMIN_USERS = "/controller?command=listAdminUsers&show=all";
    public static final String COMMAND__LIST_ADMIN_AUTHORS = "/controller?command=listAdminAuthors&show=all";
    public static final String COMMAND__LIST_ADMIN_PUBLISHINGS = "/controller?command=listAdminPublishings&show=all";
    public static final String COMMAND__EDIT_ADMIN_BOOK = "/controller?command=editAdminBook&show=all";
    public static final String COMMAND__SETTINGS_ADMIN_BOOK = "/controller?command=settingsAdminBook";
    public static final String COMMAND__MAKE_ADMIN_BOOK = "/controller?command=makeAdminBook";
}