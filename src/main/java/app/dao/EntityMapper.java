package app.dao;

import java.sql.ResultSet;

/**
 * DAO class interface
 *
 * @author Alex Odulo
 */
public interface EntityMapper<T> {
    T mapRow(ResultSet rs);
}
