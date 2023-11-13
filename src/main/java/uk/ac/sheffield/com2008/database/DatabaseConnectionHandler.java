package uk.ac.sheffield.com2008.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseConnectionHandler {
    private static final HikariConfig config = new HikariConfig("mysql.properties");
    private static HikariDataSource ds;

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public static void connectToDatabase() {
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setMaximumPoolSize(10);
        ds = new HikariDataSource(config);
    }

    public static ResultSet select(String query, Object... params) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(query);

        for(int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]);
        }

        return statement.executeQuery();
    }

    public static Boolean insert(String query, Object... params) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(query);

        for(int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]);
        }

        return statement.execute();
    }
}
