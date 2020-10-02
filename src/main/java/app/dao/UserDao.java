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

    private static final String SQL_FIND_ALL_USER =
            "SELECT * FROM users ORDER BY id";

    private static final String SQL_UPDATE_USER =
            "UPDATE users SET password=?, active=?, description=?, role_id=?"+
                    " WHERE id=?";

    private static final String SQL_ADD_USER =
            "INSERT INTO users (username, password, active, description, role_id)" +
                    " VALUES (?, ?, ?, ?, ?)";

    private static final String SQL_DEL_USER_BY_ID =
            "DELETE FROM users WHERE id=?";

    /**
     * Returns a user with the given id.
     *
     * @param id
     *            User id.
     * @return User entity.
     */
    public static User findUserById(int id) {
        UserMapper mapper = new UserMapper();
        DBCrud<User> dbCrud = new DBCrud<>();
        return dbCrud.findOne(SQL_FIND_USER_BY_ID, String.valueOf(id), mapper);
    }

    /**
     * Returns a user with the given login.
     *
     * @param login
     *            User login.
     * @return User entity.
     */
    public static User findUserByLogin(String login) {
        UserMapper mapper = new UserMapper();
        DBCrud<User> dbCrud = new DBCrud<>();
        return dbCrud.findOne(SQL_FIND_USER_BY_LOGIN, login, mapper);
    }

    /**
     * Returns all users.
     *
     * @return List<User> entities.
     */
    public static List<User> findAllUser() {
        UserMapper mapper = new UserMapper();
        DBCrud<User> dbCrud = new DBCrud<>();
        return dbCrud.findAll(SQL_FIND_ALL_USER, mapper);
    }

    /**
     * Del user with the given id.
     *
     * @param id
     *            User id.
     * @return boolean true if del
     */
    public static boolean delUserById(int id) {
        DBCrud<User> dbCrud = new DBCrud<>();
        return dbCrud.delete(SQL_DEL_USER_BY_ID, String.valueOf(id));
    }

    /**
     * Update user.
     *
     * @param user
     *            user to update.
     */
    public static void updateUser(User user) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBManager.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL_UPDATE_USER);
            int k = 1;
            pstmt.setString(k++, user.getPassword());
            pstmt.setBoolean(k++, user.isActive());
            pstmt.setString(k++, user.getDescription());
            pstmt.setInt(k++, user.getRoleId());
            pstmt.setInt(k++, user.getId());
            pstmt.executeUpdate();
            con.commit();
        } catch (SQLException ex) {
            logger.error("SQLException when connecting to db", ex);
            DBManager.getInstance().rollback(con);
        } finally {
            DBManager.getInstance().closePreparedStatement(pstmt);
            DBManager.getInstance().closeConnect(con);
        }
    }

    /**
     * Add user.
     *
     * @param user
     *            user to add.
     */
    public static void addUser(User user) {
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
            pstmt.setInt(k, user.getRoleId());
            pstmt.executeUpdate();
            con.commit();
        } catch (SQLException ex) {
            logger.error("SQLException when connecting to db", ex);
            DBManager.getInstance().rollback(con);
        } finally {
            DBManager.getInstance().closePreparedStatement(pstmt);
            DBManager.getInstance().closeConnect(con);
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
                return user;
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
