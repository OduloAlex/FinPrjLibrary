package app.dao;

import app.domain.Book;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class BookDao {
    private static final Logger logger = Logger.getLogger(BookDao.class);

    private static final String SQL_FIND_BOOK_BY_ID =
            "SELECT * FROM books WHERE id=?";

    private static final String SQL_ADD_BOOK =
            "INSERT INTO books (state, inv_number, catalog_id)" +
                    " VALUES (?, ?, ?)";

    private static final String SQL_FIND_ALL_BOOK =
            "SELECT * FROM books ORDER BY id";

    private static final String SQL_DEL_BOOK_BY_ID =
            "DELETE FROM books WHERE id=?";

    /**
     * Returns a Book object with the given identifier.
     *
     * @param id
     *            Book object identifier.
     * @return Book object entity.
     */
    public static Book findBookById(int id) {
        BookMapper mapper = new BookMapper();
        DBCrud<Book> dbCrud = new DBCrud<>();
        return dbCrud.findOne(SQL_FIND_BOOK_BY_ID, String.valueOf(id), mapper);
    }

    /**
     * Returns all Book.
     *
     * @return List<Book> entities.
     */
    public static List<Book> findAllBook() {
        BookMapper mapper = new BookMapper();
        DBCrud<Book> dbCrud = new DBCrud<>();
        return dbCrud.findAll(SQL_FIND_ALL_BOOK, mapper);
    }

    /**
     * Del Book with the given id.
     *
     * @param id
     *            Book id.
     * @return boolean true if del
     */
    public static boolean delBookById(int id) {
        DBCrud<Book> dbCrud = new DBCrud<>();
        return dbCrud.delete(SQL_DEL_BOOK_BY_ID, String.valueOf(id));
    }

    /**
     * Add Book.
     *
     * @param book
     *            catalog to add.
     */
    public static void addBook(Book book) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBManager.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL_ADD_BOOK);
            int k = 1;
            pstmt.setInt(k++, book.getStatusBookId());
            pstmt.setString(k++, book.getInvNumber());
            pstmt.setInt(k++, book.getCatalogObj().getId());
            pstmt.executeUpdate();
            con.commit();
        } catch (SQLException ex) {
            logger.error("SQLException when connecting to db", ex);
            DBManager.rollback(con);
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
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
