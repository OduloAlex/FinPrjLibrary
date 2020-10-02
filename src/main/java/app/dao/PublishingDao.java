package app.dao;

import app.domain.Publishing;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PublishingDao {
    private static final Logger logger = Logger.getLogger(PublishingDao.class);

    private static final String SQL_FIND_PUBLISHING_BY_ID =
            "SELECT * FROM publishing WHERE id=?";

    /**
     * Returns a Publishing object with the given identifier.
     *
     * @param id
     *            Publishing identifier.
     * @return Publishing object entity.
     */
    public static Publishing findPublishingById(int id) {
        PublishingMapper mapper = new PublishingMapper();
        DBCrud<Publishing> dbCrud = new DBCrud<>();
        return dbCrud.findOne(SQL_FIND_PUBLISHING_BY_ID, String.valueOf(id), mapper);
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
