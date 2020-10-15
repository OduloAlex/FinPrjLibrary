package app.dao;

import app.domain.Card;
import app.domain.Order;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OrderDao {
    private static final Logger logger = Logger.getLogger(OrderDao .class);

    private static final String SQL_FIND_ORDER_BY_USERS_ID =
            "SELECT * FROM orders WHERE users_id=?";

    private static final String SQL_ADD_ORDER =
            "INSERT INTO orders (catalog_id, users_id, state)" +
                    " VALUES (?, ?, ?)";

    private static final String SQL_FIND_ALL_ORDER =
            "SELECT * FROM orders ORDER BY users_id";

    private static final String SQL_DEL_ORDER_BY_USERS_ID =
            "DELETE FROM orders WHERE catalog_id=? AND users_id=?";
    /**
     * Returns all Order object with the given identifier User.
     *
     * @param id
     *            Order object identifier.
     * @return List<Order> object entity.
     */
    public static List<Order> findAllOrderByUsersId(int id) throws DBException {
        OrderMapper mapper = new OrderMapper();
        DBCrud<Order> dbCrud = new DBCrud<>();
        return dbCrud.findAllByParam(SQL_FIND_ORDER_BY_USERS_ID, String.valueOf(id), mapper);
    }

    /**
     * Returns all Order.
     *
     * @return List<Order> entities.
     */
    public static List<Order> findAllOrder() throws DBException {
        OrderMapper mapper = new OrderMapper();
        DBCrud<Order> dbCrud = new DBCrud<>();
        return dbCrud.findAll(SQL_FIND_ALL_ORDER, mapper);
    }

    /**
     * Del Order.
     *
     * @param catalogId id.
     * @param userId id.
     * @return boolean true if del
     */
    public static boolean delOrder(int catalogId, int userId) throws DBException {
        DBCrud<Order> dbCrud = new DBCrud<>();
        return dbCrud.delete(SQL_DEL_ORDER_BY_USERS_ID, String.valueOf(catalogId), String.valueOf(userId));
    }

    /**
     * Add Orders
     *
     * @param orders
     *            list Orders to add.
     */
    public static void addOrders(List<Order> orders) throws DBException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBManager.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL_ADD_ORDER);
            for (Order order: orders) {
                int k = 1;
                pstmt.setInt(k++, order.getCatalogObj().getId());
                pstmt.setInt(k++, order.getUser().getId());
                logger.debug("Add order - " +order.getUser().getId());
                pstmt.setInt(k++, order.getState());
                pstmt.executeUpdate();
            }
            con.commit();
        } catch (SQLException ex) {
            logger.error("SQLException when connecting to db", ex);
            DBManager.rollback(con);
            throw new DBException("Unable to insert data in DB");
        } finally {
            DBManager.closePreparedStatement(pstmt);
            DBManager.closeConnect(con);
        }
    }

    /**
     * Extracts a Order from the result set row.
     */
    private static class OrderMapper implements EntityMapper<Order> {

        @Override
        public Order mapRow(ResultSet rs) {
            try {
                Order order = new Order();
                order.setCatalogObj(CatalogObjDao.findCatalogObjById(rs.getInt(Fields.ORDER_CATALOG_ID)));
                order.setUser(UserDao.findUserById(rs.getInt(Fields.ORDER_USERS_ID)));
                order.setState(rs.getInt(Fields.ORDER_STATE));
                return order;
            } catch (SQLException | DBException e) {
                throw new IllegalStateException(e);
            }
        }
    }

}
