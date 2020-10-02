package app.dao;

import app.domain.Author;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorDao {
    private static final Logger logger = Logger.getLogger(AuthorDao.class);

    private static final String SQL_FIND_AUTHOR_BY_ID =
            "SELECT * FROM author WHERE id=?";

    /**
     * Returns a Author object with the given identifier.
     *
     * @param id
     *            Author identifier.
     * @return Author object entity.
     */
    public static Author findAuthorById(int id) {
        AuthorMapper mapper = new AuthorMapper();
        DBCrud<Author> dbCrud = new DBCrud<>();
        return dbCrud.findOne(SQL_FIND_AUTHOR_BY_ID, String.valueOf(id), mapper);
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
