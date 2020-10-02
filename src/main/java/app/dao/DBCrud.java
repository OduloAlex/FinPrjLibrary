package app.dao;

import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBCrud<T> {
    private static final Logger logger = Logger.getLogger(DBCrud.class);

    public T findOne(String sqlQuery, String param, EntityMapper<T> mapper) {
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
            DBManager.getInstance().rollback(con);
        } finally {
            DBManager.getInstance().closeResultSet(rs);
            DBManager.getInstance().closePreparedStatement(pstmt);
            DBManager.getInstance().closeConnect(con);
        }
        return entity;
    }

    public List<T> findAll(String sqlQuery, EntityMapper<T> mapper) {
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
            DBManager.getInstance().rollback(con);
        } finally {
            DBManager.getInstance().closeResultSet(rs);
            DBManager.getInstance().closeStatement(stmt);
            DBManager.getInstance().closeConnect(con);
        }
        return entities;
    }

    public List<T> findAllByParam(String sqlQuery, String param, EntityMapper<T> mapper) {
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
            DBManager.getInstance().rollback(con);
        } finally {
            DBManager.getInstance().closeResultSet(rs);
            DBManager.getInstance().closeStatement(pstmt);
            DBManager.getInstance().closeConnect(con);
        }
        return entities;
    }

    public boolean delete(String sqlQuery, String param) {
        PreparedStatement pstmt = null;
        Connection con = null;
        boolean result = false;
        try {
            con = DBManager.getInstance().getConnection();
            pstmt = con.prepareStatement(sqlQuery);
            pstmt.setString(1, param);
            if(pstmt.executeUpdate() != 0){
                result = true;
            }
            con.commit();
        } catch (SQLException ex) {
            logger.error("SQLException when connecting to db", ex);
            DBManager.getInstance().rollback(con);
        } finally {
            DBManager.getInstance().closePreparedStatement(pstmt);
            DBManager.getInstance().closeConnect(con);
        }
        return result;
    }
}
