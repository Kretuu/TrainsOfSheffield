package uk.ac.sheffield.com2008.model.mappers;

import uk.ac.sheffield.com2008.model.domain.data.AuthUser;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthUserMapper implements RowMapper<AuthUser> {
    @Override
    public AuthUser mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return new AuthUser(
          resultSet.getString("password"),
          resultSet.getString("salt")
        );
    }
}
