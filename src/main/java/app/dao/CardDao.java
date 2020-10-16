package app.dao;

import app.domain.Card;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Card's Dao class
 *
 * @author Alex Odulo
 */
public class CardDao {
    private static final Logger logger = Logger.getLogger(CardDao.class);

    private static final String SQL_FIND_CARD_BY_USERS_ID =
            "SELECT * FROM cards WHERE users_id=?";

    private static final String SQL_ADD_CARD =
            "INSERT INTO cards (create_time, return_time, books_id, users_id)" +
                    " VALUES (?, ?, ?, ?)";

    private static final String SQL_DEL_CARD_BY_USERS_ID =
            "DELETE FROM cards WHERE books_id=? AND users_id=?";

    /**
     * Returns all Card object with the given identifier User.
     *
     * @param id Card object identifier.
     * @return List<Card> object entity.
     */
    public static List<Card> findAllCardByUsersId(int id) throws DBException {
        CardMapper mapper = new CardMapper();
        DBCrud<Card> dbCrud = new DBCrud<>();
        return dbCrud.findAllByParam(SQL_FIND_CARD_BY_USERS_ID, String.valueOf(id), mapper);
    }

    /**
     * Del Card and update state book and update quantity catalog
     *
     * @param bookId          bookId
     * @param userId          bookId
     * @param bookState       bookState
     * @param catalogQuantity catalogQuantity
     * @param catalogId       catalogId
     */
    public static void delCard(int bookId, int userId, int bookState, int catalogQuantity, int catalogId) throws DBException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBManager.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL_DEL_CARD_BY_USERS_ID);
            int k = 1;
            pstmt.setInt(k++, bookId);
            pstmt.setInt(k++, userId);
            pstmt.executeUpdate();

            pstmt = con.prepareStatement(BookDao.SQL_UPDATE_BOOK_STATE);
            k = 1;
            pstmt.setInt(k++, bookState);
            pstmt.setInt(k++, bookId);
            pstmt.executeUpdate();

            pstmt = con.prepareStatement(CatalogObjDao.SQL_UPDATE_CATALOG_QUANTITY);
            k = 1;
            pstmt.setInt(k++, catalogQuantity);
            pstmt.setInt(k++, catalogId);
            pstmt.executeUpdate();

            con.commit();
        } catch (SQLException ex) {
            logger.error("SQLException exception when working with a database", ex);
            DBManager.rollback(con);
            throw new DBException("DB operation cannot be performed");
        } finally {
            DBManager.closePreparedStatement(pstmt);
            DBManager.closeConnect(con);
        }
    }

    /**
     * Add Card and update state book and update quantity catalog
     *
     * @param card            Card to add
     * @param bookState       bookState
     * @param bookId          bookId
     * @param catalogQuantity catalogQuantity
     * @param catalogId       catalogId
     * @param userId          userId
     */
    public static void addCard(Card card, int bookState, int bookId, int catalogQuantity, int catalogId, int userId) throws DBException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBManager.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL_ADD_CARD);
            int k = 1;
            pstmt.setDate(k++, new java.sql.Date(card.getCreateTime().getTime().getTime()));
            pstmt.setDate(k++, new java.sql.Date(card.getReturnTime().getTime().getTime()));
            pstmt.setInt(k++, card.getBook().getId());
            pstmt.setInt(k++, card.getUser().getId());
            pstmt.executeUpdate();

            pstmt = con.prepareStatement(BookDao.SQL_UPDATE_BOOK_STATE);
            k = 1;
            pstmt.setInt(k++, bookState);
            pstmt.setInt(k++, bookId);
            pstmt.executeUpdate();

            pstmt = con.prepareStatement(CatalogObjDao.SQL_UPDATE_CATALOG_QUANTITY);
            k = 1;
            pstmt.setInt(k++, catalogQuantity);
            pstmt.setInt(k++, catalogId);
            pstmt.executeUpdate();

            pstmt = con.prepareStatement(OrderDao.SQL_DEL_ORDER_BY_USERS_ID);
            k = 1;
            pstmt.setInt(k++, catalogId);
            pstmt.setInt(k++, userId);
            pstmt.executeUpdate();

            con.commit();
        } catch (SQLException ex) {
            logger.error("SQLException exception when working with a database", ex);
            DBManager.rollback(con);
            throw new DBException("DB operation cannot be performed");
        } finally {
            DBManager.closePreparedStatement(pstmt);
            DBManager.closeConnect(con);
        }
    }

    /**
     * Extracts a user from the result set row.
     */
    private static class CardMapper implements EntityMapper<Card> {

        @Override
        public Card mapRow(ResultSet rs) {
            try {
                Card card = new Card();
                card.setCreateTime(dateToCalendar(rs.getTimestamp(Fields.CARD_CREATE_TIME)));
                card.setReturnTime(dateToCalendar(rs.getTimestamp(Fields.CARD_RETURN_TIME)));
                card.setBook(BookDao.findBookById(rs.getInt(Fields.CARD_BOOKS_ID)));
                card.setUser(UserDao.findUserById(rs.getInt(Fields.CARD_USERS_ID)));
                return card;
            } catch (SQLException | DBException e) {
                throw new IllegalStateException(e);
            }
        }

        /**
         * Convert Date to Calendar
         *
         * @param date date in Date
         * @return Calendar
         */
        private static Calendar dateToCalendar(Date date) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar;
        }
    }


}
