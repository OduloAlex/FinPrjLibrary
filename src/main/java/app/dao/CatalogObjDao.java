package app.dao;

import app.domain.CatalogObj;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CatalogObjDao {
    private static final Logger logger = Logger.getLogger(CatalogObjDao.class);

    private static final String SQL_FIND_CATALOG_BY_NAME =
            "SELECT * FROM catalog WHERE name=?";

    private static final String SQL_FIND_CATALOG_BY_ID =
            "SELECT * FROM catalog WHERE id=?";

    private static final String SQL_ADD_CATALOG =
            "INSERT INTO catalog (name, year, fine, description, quantity, author_id, publishing_id)" +
                    " VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_FIND_ALL_CATALOG =
            "SELECT * FROM catalog ORDER BY id";

    private static final String SQL_FIND_ALL_CATALOG_QNN =
            "SELECT * FROM catalog WHERE quantity!=0 ORDER BY id";

    private static final String SQL_DEL_CATALOG_BY_ID =
            "DELETE FROM catalog WHERE id=?";

    private static final String SQL_UPDATE_CATALOG_QUANTITY =
            "UPDATE catalog SET quantity=? WHERE id=?";

    private static final String SQL_UPDATE_CATALOG =
            "UPDATE catalog SET name=?, year=?, fine=?, description=?, author_id=?, publishing_id=? WHERE id=?";
    /**
     * Returns a catalog object with the given identifier.
     *
     * @param id
     *            catalog object identifier.
     * @return catalog object entity.
     */
    public static CatalogObj findCatalogObjById(int id) throws DBException {
        CatalogMapper mapper = new CatalogMapper();
        DBCrud<CatalogObj> dbCrud = new DBCrud<>();
        return dbCrud.findOne(SQL_FIND_CATALOG_BY_ID, String.valueOf(id), mapper);
    }

    /**
     * Returns a CatalogObj with the given name.
     *
     * @param name
     *            CatalogObj name.
     * @return CatalogObj entity.
     */
    public static CatalogObj findCatalogObjByName(String name) throws DBException {
        CatalogMapper mapper = new CatalogMapper();
        DBCrud<CatalogObj> dbCrud = new DBCrud<>();
        return dbCrud.findOne(SQL_FIND_CATALOG_BY_NAME, name, mapper);
    }

    /**
     * Returns all CatalogObj.
     *
     * @return List<CatalogObj> entities.
     */
    public static List<CatalogObj> findAllCatalogObj() throws DBException {
        CatalogMapper mapper = new CatalogMapper();
        DBCrud<CatalogObj> dbCrud = new DBCrud<>();
        return dbCrud.findAll(SQL_FIND_ALL_CATALOG, mapper);
    }

    /**
     * Returns all CatalogObj where quantity more than 0.
     *
     * @return List<CatalogObj> entities.
     */
    public static List<CatalogObj> findAllCatalogObjQNN() throws DBException {
        CatalogMapper mapper = new CatalogMapper();
        DBCrud<CatalogObj> dbCrud = new DBCrud<>();
        return dbCrud.findAll(SQL_FIND_ALL_CATALOG_QNN, mapper);
    }

    /**
     * Del CatalogObj with the given id.
     *
     * @param id
     *            CatalogObj id.
     * @return boolean true if del
     */
    public static boolean delCatalogObjById(int id) throws DBException {
        DBCrud<CatalogObj> dbCrud = new DBCrud<>();
        return dbCrud.delete(SQL_DEL_CATALOG_BY_ID, String.valueOf(id));
    }

    /**
     * Add CatalogObj.
     *
     * @param name name
     * @param year year
     * @param fine fine
     * @param description description
     * @param quantity quantity
     * @param authorId author_id
     * @param publishingId publishing_id
     */
    public static void addCatalogObj(String name, int year, int fine, String description, int quantity,
                                     int authorId, int publishingId) throws DBException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBManager.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL_ADD_CATALOG);
            int k = 1;
            pstmt.setString(k++, name);
            pstmt.setInt(k++, year);
            pstmt.setInt(k++, fine);
            pstmt.setString(k++, description);
            pstmt.setInt(k++, quantity);
            pstmt.setInt(k++, authorId);
            pstmt.setInt(k++, publishingId);
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
     * Update catalogs's quantity.
     *
     * @param quantity
     *            quantity to update.
     */
    public static void updateCatalogObjQuantity(int quantity, int id) throws DBException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBManager.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL_UPDATE_CATALOG_QUANTITY);
            int k = 1;
            pstmt.setInt(k++, quantity);
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
     * Update catalog.
     *
     * @param name name
     * @param year year
     * @param fine fine
     * @param description description
     * @param authorId author_id
     * @param publishingId publishing_id
     */
    public static void updateCatalogObj(String name, int year, int fine, String description,
                                        int authorId, int publishingId, int id) throws DBException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBManager.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL_UPDATE_CATALOG);
            int k = 1;
            pstmt.setString(k++, name);
            pstmt.setInt(k++, year);
            pstmt.setInt(k++, fine);
            pstmt.setString(k++, description);
            pstmt.setInt(k++, authorId);
            pstmt.setInt(k++, publishingId);
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
     * Extracts a user from the result set row.
     */
    private static class CatalogMapper implements EntityMapper<CatalogObj> {

        @Override
        public CatalogObj mapRow(ResultSet rs) {
            try {
                CatalogObj catalogObj = new CatalogObj();
                catalogObj.setId(rs.getInt(Fields.ENTITY_ID));
                catalogObj.setName(rs.getString(Fields.CATALOG_NAME));
                catalogObj.setYear(rs.getInt(Fields.CATALOG_YEAR));
                catalogObj.setFine(rs.getInt(Fields.CATALOG_FINE));
                catalogObj.setDescription(rs.getString(Fields.CATALOG_DESCRIPTION));
                catalogObj.setQuantity(rs.getInt(Fields.CATALOG_QUANTITY));
                catalogObj.setAuthor(AuthorDao.findAuthorById(rs.getInt(Fields.CATALOG_AUTHOR_ID)));
                catalogObj.setPublishing(PublishingDao.findPublishingById(rs.getInt(Fields.CATALOG_PUBLISHING_ID)));
                return catalogObj;
            } catch (SQLException | DBException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
