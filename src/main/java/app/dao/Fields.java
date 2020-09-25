package app.dao;

/**
 * Holder for fields names of DB tables and beans.
 *
 * @author
 *
 */
public final class Fields {

    // entities
    public static final String ENTITY__ID = "id";

    public static final String USER__LOGIN = "username";
    public static final String USER__PASSWORD = "password";
    public static final String USER__ACTIVE = "active";
    public static final String USER__DESCRIPTION = "description";
    public static final String USER__ROLE_ID = "role_id";

    public static final String ORDER__BILL = "bill";
    public static final String ORDER__USER_ID = "user_id";
    public static final String ORDER__STATUS_ID= "status_id";

    public static final String CATEGORY__NAME = "name";

    public static final String MENU_ITEM__PRICE = "price";
    public static final String MENU_ITEM__NAME = "name";
    public static final String MENU_ITEM__CATEGORY_ID = "category_id";

    // beans
    public static final String USER_ORDER_BEAN__ORDER_ID = "id";
    public static final String USER_ORDER_BEAN__USER_FIRST_NAME = "first_name";
    public static final String USER_ORDER_BEAN__USER_LAST_NAME = "last_name";
    public static final String USER_ORDER_BEAN__ORDER_BILL = "bill";
    public static final String USER_ORDER_BEAN__STATUS_NAME = "name";



}