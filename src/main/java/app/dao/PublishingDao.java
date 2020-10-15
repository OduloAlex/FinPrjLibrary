package app.dao;

import app.domain.Publishing;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PublishingDao {
    private static final Logger logger = Logger.getLogger(PublishingDao.class);

    private static final String SQL_FIND_PUBLISHING_BY_ID =
            "SELECT * FROM publishing WHERE id=?";

    private static final String SQL_FIND_ALL_PUBLISHING =
            "SELECT * FROM publishing ORDER BY id";

    private static final String SQL_DEL_PUBLISHING_BY_ID =
            "DELETE FROM publishing WHERE id=?";

    private static final String SQL_ADD_PUBLISHING =
            "INSERT INTO publishing (name)" +
                    " VALUES (?)";

    /**
     * Returns a Publishing object with the given identifier.
     *
     * @param id Publishing identifier.
     * @return Publishing object entity.
     */
    public static Publishing findPublishingById(int id) throws DBException {
        PublishingMapper mapper = new PublishingMapper();
        DBCrud<Publishing> dbCrud = new DBCrud<>();
        return dbCrud.findOne(SQL_FIND_PUBLISHING_BY_ID, String.valueOf(id), mapper);
    }

    /**
     * Returns all Publishing.
     *
     * @return List<Publishing> entities.
     */
    public static List<Publishing> findAllPublishing() throws DBException {
        PublishingMapper mapper = new PublishingMapper();
        DBCrud<Publishing> dbCrud = new DBCrud<>();
        return dbCrud.findAll(SQL_FIND_ALL_PUBLISHING, mapper);
    }

    /**
     * Del Publishing with the given id.
     *
     * @param id Publishing id.
     * @return boolean true if del
     */
    public static boolean delPublishingById(int id) throws DBException {
        DBCrud<Publishing> dbCrud = new DBCrud<>();
        return dbCrud.delete(SQL_DEL_PUBLISHING_BY_ID, String.valueOf(id));
    }

    /**
     * Add Publishing.
     *
     * @param publishing Publishing to add.
     */
    public static void addPublishing(Publishing publishing) throws DBException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBManager.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL_ADD_PUBLISHING);
            int k = 1;
            pstmt.setString(k++, publishing.getName());
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
     * Extracts a Publishing from the result set row.
     */
    private static class PublishingMapper implements EntityMapper<Publishing> {

        @Override
        public Publishing mapRow(ResultSet rs) {
            try {
                Publishing publishing = new Publishing();
                publishing.setId(rs.getInt(Fields.ENTITY_ID));
                publishing.setName(rs.getString(Fields.PUBLISHING_NAME));
                return publishing;
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
