package uk.ac.sheffield.com2008.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import uk.ac.sheffield.com2008.model.mappers.RowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnectionHandler {
    private static final HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public static void connectToDatabase() {
        //Set JDBC URL
        config.setJdbcUrl("jdbc:mysql://<your mysql server uri>"
                + "&allowPublicKeyRetrieval=false&useSSL=false");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setMaximumPoolSize(5);
        ds = new HikariDataSource(config);
    }


    public static <T> List<T> select(RowMapper<T> mapper, String query, Object... params) throws SQLException {
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            ResultSet resultSet = statement.executeQuery();
            List<T> resultList = new ArrayList<>();

            while (resultSet.next()) {
                resultList.add(mapper.mapResultSetToEntity(resultSet));
            }
            return resultList;
        }
    }

    public static long count(String query, Object... params) throws SQLException {
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }

            ResultSet rs = statement.executeQuery();
            if (rs.next()) return rs.getLong("COUNT(*)");

        }
        return 0L;
    }

    public static void insert(String query, Object... params) throws SQLException {
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }

            statement.execute();
        }
    }

    public static boolean update(String query, Object... params) throws SQLException {
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }

            return statement.executeUpdate() > 0;
        }
    }

    public static boolean delete(String query, Object... params) throws SQLException {
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }

            return statement.executeUpdate() > 0;
        }
    }
}
