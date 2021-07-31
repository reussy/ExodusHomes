package com.reussy.databases.sql;

import com.reussy.ExodusHomes;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectionPool {

    private final ExodusHomes plugin;
    private HikariDataSource hikariDataSource;
    private String hostName, port, databaseName, userName, password;
    private int maximumConnections, minimumConnections;
    private long connectionTime;

    public ConnectionPool(ExodusHomes plugin) {
        this.plugin = plugin;
        setProperties();
        setupPool();
        makeTable();
    }

    private void setProperties() {

        hostName = plugin.getConfig().getString("Database-Properties.Host");
        port = plugin.getConfig().getString("Database-Properties.Port");
        databaseName = plugin.getConfig().getString("Database-Properties.Database");
        userName = plugin.getConfig().getString("Database-Properties.Username");
        password = plugin.getConfig().getString("Database-Properties.Password");
        maximumConnections = plugin.getConfig().getInt("Database-Properties.Max-Connections");
        minimumConnections = plugin.getConfig().getInt("Database-Properties.Min-Connections");
        connectionTime = plugin.getConfig().getLong("Database-Properties.Connection-Time");
    }

    private void setupPool() {

        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl("jdbc:mysql://" + hostName + ":" + port + "/" + databaseName);
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.setUsername(userName);
        hikariConfig.setPassword(password);
        hikariConfig.setMinimumIdle(minimumConnections);
        hikariConfig.setMaximumPoolSize(maximumConnections);
        hikariConfig.setMaxLifetime(connectionTime);
        hikariConfig.setConnectionTestQuery("SELECT 1;");
        hikariDataSource = new HikariDataSource(hikariConfig);
    }

    private void makeTable() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = hikariDataSource.getConnection();
            preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + plugin.getConfig().getString("Database-Properties.Table") + " (`UUID` VARCHAR(80) NOT NULL , `Player` VARCHAR(60) NOT NULL , `Home` VARCHAR(60) NOT NULL , `World` VARCHAR(80) NOT NULL , `X` INT(10) NOT NULL , `Y` INT(10) NOT NULL , `Z` INT(10) NOT NULL , `Pitch` FLOAT(15) , `Yaw` FLOAT(15) ) ENGINE = InnoDB");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection, preparedStatement, null);
        }
    }

    public Connection getConnection() throws SQLException {

        return hikariDataSource.getConnection();
    }

    public void closeConnection(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ignored) {
            }
        }

        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException ignored) {
            }
        }

        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException ignored) {
            }
        }
    }

    public void closePool() {

        if (hikariDataSource != null && !hikariDataSource.isClosed()) {
            hikariDataSource.close();
        }
    }
}
