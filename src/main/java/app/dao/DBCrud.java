package app.dao;

import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBCrud<T> {
    private static final Logger logger = Logger.getLogger(DBCrud.class);

    public T findOne(String sqlQuery, String param, EntityMapper<T> mapper) throws DBException {
        T entity = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            pstmt = con.prepareStatement(sqlQuery);
            pstmt.setString(1, param);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                entity = mapper.mapRow(rs);
            }
            con.commit();
        } catch (SQLException ex) {
            logger.error("SQLException when connecting to db", ex);
            DBManager.rollback(con);
            throw new DBException("Unable to find data in DB");
        } finally {
            DBManager.closeResultSet(rs);
            DBManager.closePreparedStatement(pstmt);
            DBManager.closeConnect(con);
        }
        return entity;
    }

    public List<T> findAll(String sqlQuery, EntityMapper<T> mapper) throws DBException {
        List<T> entities = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(sqlQuery);
            while (rs.next()) {
                entities.add(mapper.mapRow(rs));
            }
            con.commit();
        } catch (SQLException ex) {
            logger.error("SQLException when connecting to db", ex);
            DBManager.rollback(con);
            throw new DBException("Unable to find data in DB");
        } finally {
            DBManager.closeResultSet(rs);
            DBManager.closeStatement(stmt);
            DBManager.closeConnect(con);
        }
        return entities;
    }

    public List<T> findAllByParam(String sqlQuery, String param, EntityMapper<T> mapper) throws DBException {
        List<T> entities = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            pstmt = con.prepareStatement(sqlQuery);
            pstmt.setString(1, param);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                entities.add(mapper.mapRow(rs));
            }
            con.commit();
        } catch (SQLException ex) {
            logger.error("SQLException when connecting to db", ex);
            DBManager.rollback(con);
            throw new DBException("Unable to find data in DB");
        } finally {
            DBManager.closeResultSet(rs);
            DBManager.closeStatement(pstmt);
            DBManager.closeConnect(con);
        }
        return entities;
    }

    public boolean delete(String sqlQuery, String ... param) throws DBException {
        PreparedStatement pstmt = null;
        Connection con = null;
        boolean result = false;
        try {
            con = DBManager.getInstance().getConnection();
            pstmt = con.prepareStatement(sqlQuery);
            for(int i = 0; i < param.length; i++) {
                pstmt.setString((i+1), param[i]);
            }
            if(pstmt.executeUpdate() != 0){
                result = true;
            }
            con.commit();
        } catch (SQLException ex) {
            logger.error("SQLException when connecting to db", ex);
            DBManager.rollback(con);
            throw new DBException("Unable to delete data in DB");
        } finally {
            DBManager.closePreparedStatement(pstmt);
            DBManager.closeConnect(con);
        }
        return result;
    }
}
