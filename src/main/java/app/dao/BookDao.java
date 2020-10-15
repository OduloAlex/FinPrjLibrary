package app.dao;

import app.domain.Book;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class BookDao {
    public static final int STATE_LIB = 1;
    public static final int STATE_HAND = 2;
    public static final int STATE_ROOM = 3;

    private static final Logger logger = Logger.getLogger(BookDao.class);

    private static final String SQL_FIND_BOOK_BY_ID =
            "SELECT * FROM books WHERE id=?";

    private static final String SQL_FIND_BOOK_BY_CATALOG_ID_STATE_LIB =
            "SELECT * FROM books WHERE catalog_id=? AND state=1";

    private static final String SQL_ADD_BOOK =
            "INSERT INTO books (state, inv_number, catalog_id)" +
                    " VALUES (?, ?, ?)";

    private static final String SQL_DEL_BOOK_BY_ID =
            "DELETE FROM books WHERE id=?";

    private static final String SQL_UPDATE_BOOK_STATE =
            "UPDATE books SET state=? WHERE id=?";

    /**
     * Returns a Book object with the given identifier.
     *
     * @param id Book object identifier.
     * @return Book object entity.
     */
    public static Book findBookById(int id) throws DBException {
        BookMapper mapper = new BookMapper();
        DBCrud<Book> dbCrud = new DBCrud<>();
        return dbCrud.findOne(SQL_FIND_BOOK_BY_ID, String.valueOf(id), mapper);
    }

    /**
     * Returns all Book object with the given id Catalog where State - Librarian.
     *
     * @param id Catalog object identifier.
     * @return List<Book> object entity.
     */
    public static List<Book> findBookByCatalogIdStateLib(int id) throws DBException {
        BookMapper mapper = new BookMapper();
        DBCrud<Book> dbCrud = new DBCrud<>();
        return dbCrud.findAllByParam(SQL_FIND_BOOK_BY_CATALOG_ID_STATE_LIB, String.valueOf(id), mapper);
    }

    /**
     * Del Book with the given id.
     *
     * @param id Book id.
     * @return boolean true if del
     */
    public static boolean delBookById(int id) throws DBException {
        DBCrud<Book> dbCrud = new DBCrud<>();
        return dbCrud.delete(SQL_DEL_BOOK_BY_ID, String.valueOf(id));
    }

    /**
     * Update book's state.
     *
     * @param state state to update.
     */
    public static void updateBookState(int state, int id) throws DBException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBManager.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL_UPDATE_BOOK_STATE);
            int k = 1;
            pstmt.setInt(k++, state);
            pstmt.setInt(k++, id);
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
     * Add Book.
     *
     * @param state     state to add.
     * @param invNumber invNumber to add.
     * @param catalogId catalogId to add.
     */
    public static void addBook(int state, String invNumber, int catalogId) throws DBException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBManager.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL_ADD_BOOK);
            int k = 1;
            pstmt.setInt(k++, state);
            pstmt.setString(k++, invNumber);
            pstmt.setInt(k++, catalogId);
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
    private static class BookMapper implements EntityMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs) {
            try {
                Book book = new Book();
                book.setId(rs.getInt(Fields.ENTITY_ID));
                book.setStatusBookId(rs.getInt(Fields.BOOK_STATE));
                book.setInvNumber(rs.getString(Fields.BOOK_INV_NUMBER));
                book.setCatalogObj(CatalogObjDao.findCatalogObjById(rs.getInt(Fields.BOOK_CATALOG_ID)));
                return book;
            } catch (SQLException | DBException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
