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

    public static ResultSet select(PreparedStatement statement) {
        Connection connection = null;
        ResultSet result = null;
        try {
            connection = getConnection();
            result = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeQuery(connection, statement);
        }
        return result;
    }

    public static void update(PreparedStatement statement) {
        Connection connection = null;
        ResultSet result = null;
        try {
            connection = getConnection();
            result = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeQuery(connection, statement);
        }
    }

    private static void closeQuery(Connection connection, PreparedStatement statement) {
        if(connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(statement != null){
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
