package app.dao;

/**
 * Holder for fields names of DB tables and beans.
 *
 * @author
 *
 */
public final class Fields {

    // entities
    public static final String ENTITY_ID = "id";

    public static final String USER_LOGIN = "username";
    public static final String USER_PASSWORD = "password";
    public static final String USER_ACTIVE = "active";
    public static final String USER_DESCRIPTION = "description";
    public static final String USER_ROLE_ID = "role_id";
    public static final String USER_LOCALE = "locale";

    public static final String CATALOG_NAME = "name";
    public static final String CATALOG_YEAR = "year";
    public static final String CATALOG_FINE = "fine";
    public static final String CATALOG_DESCRIPTION = "description";
    public static final String CATALOG_QUANTITY = "quantity";
    public static final String CATALOG_AUTHOR_ID = "author_id";
    public static final String CATALOG_PUBLISHING_ID = "publishing_id";

    public static final String AUTHOR_NAME = "name";

    public static final String PUBLISHING_NAME = "name";

    public static final String BOOK_STATE = "state";
    public static final String BOOK_CATALOG_ID = "catalog_id";
    public static final String BOOK_INV_NUMBER = "inv_number";

    public static final String CARD_CREATE_TIME = "create_time";
    public static final String CARD_RETURN_TIME = "return_time";
    public static final String CARD_BOOKS_ID = "books_id";
    public static final String CARD_USERS_ID = "users_id";

    public static final String ORDER_CATALOG_ID = "catalog_id";
    public static final String ORDER_USERS_ID = "users_id";
    public static final String ORDER_STATE = "state";



}