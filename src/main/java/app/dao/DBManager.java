package app.dao;

import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;

/**
 * Database manager
 *
 * @author Alex Odulo
 */
public class DBManager {
    private static final Logger logger = Logger.getLogger(DBManager.class);

    private static DBManager instance;

    private DBManager() {
        super();
    }

    public static synchronized DBManager getInstance() {
        if (instance == null)
            instance = new DBManager();
        return instance;
    }

    /**
     * Returns a DB connection from the Pool Connections. Before using this
     * method you must configure the Date Source and the Connections Pool in your
     * WEB_APP_ROOT/META-INF/context.xml file.
     *
     * @return A DB connection.
     */
    public Connection getConnection() throws SQLException {
        Connection con = null;
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");

            DataSource ds = (DataSource) envContext.lookup("jdbc/fp2library");
            con = ds.getConnection();
            logger.debug("==============>>  connection to DB - OK");
        } catch (NamingException ex) {
            logger.error("Cannot obtain a connection from the pool", ex);
        }
        return con;
    }

    /**
     * Close ResultSet
     *
     * @param rs ResultSet
     */
    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ex) {
                logger.error("SQLException:", ex);
            }
        }
    }

    /**
     * Close PreparedStatement
     *
     * @param ps PreparedStatement
     */
    public static void closePreparedStatement(PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException ex) {
                logger.error("SQLException:", ex);
            }
        }
    }

    /**
     * Close Statement
     *
     * @param st Statement
     */
    public static void closeStatement(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException ex) {
                logger.error("SQLException:", ex);
            }
        }
    }

    /**
     * Close connect
     *
     * @param con connect
     */
    public static void closeConnect(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException ex) {
                logger.error("SQLException:", ex);
            }
        }
    }

    /**
     * Rollback operation
     *
     * @param con connection
     */
    public static void rollback(Connection con) {
        if (con != null) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                logger.error("SQLException:", ex);
            }
        }
    }
}
