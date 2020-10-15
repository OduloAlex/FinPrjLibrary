package app.dao;

import app.domain.Author;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AuthorDao {
    private static final Logger logger = Logger.getLogger(AuthorDao.class);

    private static final String SQL_FIND_AUTHOR_BY_ID =
            "SELECT * FROM author WHERE id=?";

    private static final String SQL_FIND_ALL_AUTHOR =
            "SELECT * FROM author ORDER BY id";

    private static final String SQL_DEL_AUTHOR_BY_ID =
            "DELETE FROM author WHERE id=?";

    private static final String SQL_ADD_AUTHOR =
            "INSERT INTO author (name)" +
                    " VALUES (?)";

    /**
     * Returns a Author object with the given identifier.
     *
     * @param id Author identifier.
     * @return Author object entity.
     */
    public static Author findAuthorById(int id) throws DBException {
        AuthorMapper mapper = new AuthorMapper();
        DBCrud<Author> dbCrud = new DBCrud<>();
        return dbCrud.findOne(SQL_FIND_AUTHOR_BY_ID, String.valueOf(id), mapper);
    }

    /**
     * Returns all Author.
     *
     * @return List<Author> entities.
     */
    public static List<Author> findAllAuthor() throws DBException {
        AuthorMapper mapper = new AuthorMapper();
        DBCrud<Author> dbCrud = new DBCrud<>();
        return dbCrud.findAll(SQL_FIND_ALL_AUTHOR, mapper);
    }

    /**
     * Del Author with the given id.
     *
     * @param id Author id.
     * @return boolean true if del
     */
    public static boolean delAuthorById(int id) throws DBException {
        DBCrud<Author> dbCrud = new DBCrud<>();
        return dbCrud.delete(SQL_DEL_AUTHOR_BY_ID, String.valueOf(id));
    }

    /**
     * Add Author.
     *
     * @param author Author to add.
     */
    public static void addAuthor(Author author) throws DBException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBManager.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL_ADD_AUTHOR);
            int k = 1;
            pstmt.setString(k++, author.getName());
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
     * Extracts a Author from the result set row.
     */
    private static class AuthorMapper implements EntityMapper<Author> {

        @Override
        public Author mapRow(ResultSet rs) {
            try {
                Author author = new Author();
                author.setId(rs.getInt(Fields.ENTITY_ID));
                author.setName(rs.getString(Fields.AUTHOR_NAME));
                return author;
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
