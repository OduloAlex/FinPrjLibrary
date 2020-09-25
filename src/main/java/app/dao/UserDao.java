package app.dao;

import app.domain.User;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private static final Logger logger = Logger.getLogger(UserDao.class);

    private static final String SQL_FIND_USER_BY_LOGIN =
            "SELECT * FROM users WHERE username=?";

    private static final String SQL_FIND_USER_BY_ID =
            "SELECT * FROM users WHERE id=?";

    private static final String SQL_UPDATE_USER =
            "UPDATE users SET password=?, active=?, description=?, role_id=?"+
                    " WHERE id=?";

    private static final String SQL_ADD_USER =
            "INSERT INTO users (username, password, active, description, role_id)" +
                    " VALUES (?, ?, ?, ?, ?)";

    private static final String SQL_FIND_ALL_USER =
            "SELECT * FROM users ORDER BY id";

    /**
     * Returns a user with the given identifier.
     *
     * @param id
     *            User identifier.
     * @return User entity.
     */
    public User findUser(Long id) {
        User user = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            UserMapper mapper = new UserMapper();
            pstmt = con.prepareStatement(SQL_FIND_USER_BY_ID);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            if (rs.next())
                user = mapper.mapRow(rs);
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con);
            logger.error("SQLException when connecting to db", ex);
        } finally {
            DBManager.getInstance().commitAndClose(con);
        }
        return user;
    }

    /**
     * Returns a user with the given login.
     *
     * @param login
     *            User login.
     * @return User entity.
     */
    public User findUserByLogin(String login) {
        User user = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            UserMapper mapper = new UserMapper();
            pstmt = con.prepareStatement(SQL_FIND_USER_BY_LOGIN);
            pstmt.setString(1, login);
            rs = pstmt.executeQuery();
            if (rs.next())
                user = mapper.mapRow(rs);
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con);
            logger.error("SQLException when connecting to db", ex);
        } finally {
            DBManager.getInstance().commitAndClose(con);
        }
        return user;
    }

    /**
     * Returns all users.
     *
     * @return List<User> entities.
     */
    public List<User> findAllUser() {
        List<User> userList = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            UserMapper mapper = new UserMapper();
            pstmt = con.prepareStatement(SQL_FIND_ALL_USER);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                userList.add(mapper.mapRow(rs));
            }
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con);
            logger.error("SQLException when connecting to db", ex);
        } finally {
            DBManager.getInstance().commitAndClose(con);
        }
        return userList;
    }

    /**
     * Update user.
     *
     * @param user
     *            user to update.
     */
    public void updateUser(User user) {
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            PreparedStatement pstmt = con.prepareStatement(SQL_UPDATE_USER);
            int k = 1;
            pstmt.setString(k++, user.getPassword());
            pstmt.setBoolean(k++, user.isActive());
            pstmt.setString(k++, user.getDescription());
            pstmt.setInt(k++, user.getRoleId());
            pstmt.setLong(k, user.getId());
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con);
            logger.error("SQLException when connecting to db", ex);
        } finally {
            DBManager.getInstance().commitAndClose(con);
        }
    }

    /**
     * Add user.
     *
     * @param user
     *            user to add.
     */
    public void addUser(User user) {
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            PreparedStatement pstmt = con.prepareStatement(SQL_ADD_USER);
            int k = 1;
            pstmt.setString(k++, user.getUsername());
            pstmt.setString(k++, user.getPassword());
            pstmt.setBoolean(k++, user.isActive());
            pstmt.setString(k++, user.getDescription());
            pstmt.setInt(k, user.getRoleId());
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con);
            logger.error("SQLException when connecting to db", ex);
        } finally {
            DBManager.getInstance().commitAndClose(con);
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
                user.setId(rs.getLong(Fields.ENTITY__ID));
                user.setUsername(rs.getString(Fields.USER__LOGIN));
                user.setPassword(rs.getString(Fields.USER__PASSWORD));
                user.setActive(rs.getBoolean(Fields.USER__ACTIVE));
                user.setDescription(rs.getString(Fields.USER__DESCRIPTION));
                user.setRoleId(rs.getInt(Fields.USER__ROLE_ID));
                return user;
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
