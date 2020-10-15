package app.dao;

import app.domain.Card;
import app.domain.CatalogObj;
import app.domain.Order;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CardDao {
    private static final Logger logger = Logger.getLogger(CardDao.class);

    private static final String SQL_FIND_CARD_BY_USERS_ID =
            "SELECT * FROM cards WHERE users_id=?";

    private static final String SQL_ADD_CARD =
            "INSERT INTO cards (create_time, return_time, books_id, users_id)" +
                    " VALUES (?, ?, ?, ?)";

    private static final String SQL_FIND_ALL_CARD =
            "SELECT * FROM cards ORDER BY users_id";

    private static final String SQL_DEL_CARD_BY_USERS_ID =
            "DELETE FROM cards WHERE books_id=? AND users_id=?";
    /**
     * Returns all Card object with the given identifier User.
     *
     * @param id
     *            Card object identifier.
     * @return List<Card> object entity.
     */
    public static List<Card> findAllCardByUsersId(int id) throws DBException {
        CardMapper mapper = new CardMapper();
        DBCrud<Card> dbCrud = new DBCrud<>();
        return dbCrud.findAllByParam(SQL_FIND_CARD_BY_USERS_ID, String.valueOf(id), mapper);
    }

    /**
     * Returns all Card.
     *
     * @return List<Card> entities.
     */
    public static List<Card> findAllCard() throws DBException {
        CardMapper mapper = new CardMapper();
        DBCrud<Card> dbCrud = new DBCrud<>();
        return dbCrud.findAll(SQL_FIND_ALL_CARD, mapper);
    }

    /**
     * Del Card.
     *
     * @param bookId id.
     * @param userId id.
     * @return boolean true if del
     */
    public static boolean delCard(int bookId, int userId) throws DBException {
        DBCrud<Card> dbCrud = new DBCrud<>();
        return dbCrud.delete(SQL_DEL_CARD_BY_USERS_ID, String.valueOf(bookId), String.valueOf(userId));
    }

    /**
     * Add Card
     *
     * @param card
     *            Card to add.
     */
    public static void addCard(Card card) throws DBException {
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

        //Convert Date to Calendar
        private static Calendar dateToCalendar(Date date) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar;
        }
    }


}
