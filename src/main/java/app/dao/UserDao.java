package app.dao;

import app.domain.User;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.List;

public class UserDao {
    private static final Logger logger = Logger.getLogger(UserDao.class);

    private static final String SQL_FIND_USER_BY_LOGIN =
            "SELECT * FROM users WHERE username=?";

    private static final String SQL_FIND_USER_BY_ID =
            "SELECT * FROM users WHERE id=?";

    private static final String SQL_FIND_ALL_USER_READER =
            "SELECT * FROM users WHERE role_id=3 ORDER BY id";

    private static final String SQL_FIND_ALL_USER_READER_LIB =
            "SELECT * FROM users WHERE role_id=2 OR role_id=3 ORDER BY id";

    private static final String SQL_UPDATE_USER =
            "UPDATE users SET password=?, active=?, description=?, locale=?, role_id=?" +
                    " WHERE id=?";

    private static final String SQL_UPDATE_USER_ACTIVE =
            "UPDATE users SET active=? WHERE id=?";

    private static final String SQL_UPDATE_USER_ROLE =
            "UPDATE users SET role_id=? WHERE id=?";

    private static final String SQL_ADD_USER =
            "INSERT INTO users (username, password, active, description, locale, role_id)" +
                    " VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SQL_DEL_USER_BY_ID =
            "DELETE FROM users WHERE id=?";

    /**
     * Returns a user with the given id.
     *
     * @param id User id.
     * @return User entity.
     */
    public static User findUserById(int id) throws DBException {
        UserMapper mapper = new UserMapper();
        DBCrud<User> dbCrud = new DBCrud<>();
        return dbCrud.findOne(SQL_FIND_USER_BY_ID, String.valueOf(id), mapper);
    }

    /**
     * Returns a user with the given login.
     *
     * @param login User login.
     * @return User entity.
     */
    public static User findUserByLogin(String login) throws DBException {
        UserMapper mapper = new UserMapper();
        DBCrud<User> dbCrud = new DBCrud<>();
        return dbCrud.findOne(SQL_FIND_USER_BY_LOGIN, login, mapper);
    }

    /**
     * Returns all users with role reader.
     *
     * @return List<User> entities.
     */
    public static List<User> findAllUserReader() throws DBException {
        UserMapper mapper = new UserMapper();
        DBCrud<User> dbCrud = new DBCrud<>();
        return dbCrud.findAll(SQL_FIND_ALL_USER_READER, mapper);
    }

    /**
     * Returns all users with role reader or librarian.
     *
     * @return List<User> entities.
     */
    public static List<User> findAllUserReaderOrLib() throws DBException {
        UserMapper mapper = new UserMapper();
        DBCrud<User> dbCrud = new DBCrud<>();
        return dbCrud.findAll(SQL_FIND_ALL_USER_READER_LIB, mapper);
    }

    /**
     * Del user with the given id.
     *
     * @param id User id.
     * @return boolean true if del
     */
    public static boolean delUserById(int id) throws DBException {
        DBCrud<User> dbCrud = new DBCrud<>();
        return dbCrud.delete(SQL_DEL_USER_BY_ID, String.valueOf(id));
    }

    /**
     * Update user.
     *
     * @param user user to update.
     */
    public static void updateUser(User user) throws DBException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBManager.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL_UPDATE_USER);
            int k = 1;
            pstmt.setString(k++, user.getPassword());
            pstmt.setBoolean(k++, user.isActive());
            pstmt.setString(k++, user.getDescription());
            pstmt.setString(k++, user.getLocaleName());
            pstmt.setInt(k++, user.getRoleId());
            pstmt.setInt(k++, user.getId());
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
     * Update user's active.
     *
     * @param active active to active.
     */
    public static void updateUserActive(int active, int id) throws DBException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBManager.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL_UPDATE_USER_ACTIVE);
            int k = 1;
            pstmt.setInt(k++, active);
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
     * Update user's role.
     *
     * @param role role to active.
     */
    public static void updateUserRole(int role, int id) throws DBException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBManager.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL_UPDATE_USER_ROLE);
            int k = 1;
            pstmt.setInt(k++, role);
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
     * Add user.
     *
     * @param user user to add.
     */
    public static void addUser(User user) throws DBException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBManager.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL_ADD_USER);
            int k = 1;
            pstmt.setString(k++, user.getUsername());
            pstmt.setString(k++, user.getPassword());
            pstmt.setBoolean(k++, user.isActive());
            pstmt.setString(k++, user.getDescription());
            pstmt.setString(k++, user.getLocaleName());
            pstmt.setInt(k, user.getRoleId());
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
    private static class UserMapper implements EntityMapper<User> {

        @Override
        public User mapRow(ResultSet rs) {
            try {
                User user = new User();
                user.setId(rs.getInt(Fields.ENTITY_ID));
                user.setUsername(rs.getString(Fields.USER_LOGIN));
                user.setPassword(rs.getString(Fields.USER_PASSWORD));
                user.setActive(rs.getBoolean(Fields.USER_ACTIVE));
                user.setDescription(rs.getString(Fields.USER_DESCRIPTION));
                user.setRoleId(rs.getInt(Fields.USER_ROLE_ID));
                user.setLocaleName(rs.getString(Fields.USER_LOCALE));
                return user;
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
