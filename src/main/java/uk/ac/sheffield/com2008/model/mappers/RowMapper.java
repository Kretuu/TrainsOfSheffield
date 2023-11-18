package uk.ac.sheffield.com2008.model.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowMapper<T> {
    T mapResultSetToEntity(ResultSet resultSet) throws SQLException;
}
