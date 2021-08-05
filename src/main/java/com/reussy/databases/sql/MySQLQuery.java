package com.reussy.databases.sql;

import com.reussy.ExodusHomes;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MySQLQuery {

    private final ExodusHomes plugin;
    private final ConnectionPool connectionPool;

    public MySQLQuery(ExodusHomes plugin) {
        this.plugin = plugin;
        connectionPool = new ConnectionPool(plugin);
        makeTable();
    }

    public boolean hasHomes(OfflinePlayer offlinePlayer) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {

            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM " + plugin.getConfig().getString("Database-Properties.Table") + " WHERE (UUID=?)");
            preparedStatement.setString(1, offlinePlayer.getUniqueId().toString());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) return true;

        } catch (SQLException e) {

            e.printStackTrace();
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }

        return false;
    }

    public void createHomes(UUID uuid, Player player, String world, String home, double x, double y, double z, float pitch, float yaw) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {

            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO " + plugin.getConfig().getString("Database-Properties.Table") + " (UUID, Player, World, Home, X, Y, Z, Pitch, Yaw) VALUE (?,?,?,?,?,?,?,?,?)");
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, player.getName());
            preparedStatement.setString(3, world);
            preparedStatement.setString(4, home);
            preparedStatement.setDouble(5, x);
            preparedStatement.setDouble(6, y);
            preparedStatement.setDouble(7, z);
            preparedStatement.setFloat(8, pitch);
            preparedStatement.setFloat(9, yaw);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, null);
        }
    }

    public void setNewName(UUID uuid, String home, String newName) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement("UPDATE " + plugin.getConfig().getString("Database-Properties.Table") + " SET Home=? WHERE UUID=? AND Home=?");
            preparedStatement.setString(1, newName);
            preparedStatement.setString(2, uuid.toString());
            preparedStatement.setString(3, home);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, null);
        }
    }

    public void deleteHomes(UUID uuid, String home) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement("DELETE FROM " + plugin.getConfig().getString("Database-Properties.Table") + " WHERE UUID=? AND HOME=?");
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, home);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, null);
        }
    }

    public void deleteAll(UUID uuid) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement("DELETE FROM " + plugin.getConfig().getString("Database-Properties.Table") + " WHERE UUID=?");
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, null);
        }
    }

    public UUID getUUID(String offlinePlayer) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement("SELECT UUID FROM " + plugin.getConfig().getString("Database-Properties.Table") + " WHERE Player=?");
            preparedStatement.setString(1, offlinePlayer);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) return UUID.fromString(resultSet.getString("UUID"));

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }
        return null;
    }

    public String getPlayer(String offlinePlayer) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {

            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement("SELECT Player FROM " + plugin.getConfig().getString("Database-Properties.Table") + " WHERE Player=?");
            preparedStatement.setString(1, offlinePlayer);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) return resultSet.getString("Player");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }
        return null;
    }

    public List<String> getHomes(OfflinePlayer offlinePlayer) {

        List<String> homes = new ArrayList<>();
        String getHomes = "";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {

            if (hasHomes(offlinePlayer)) {

                connection = connectionPool.getConnection();
                preparedStatement = connection.prepareStatement("SELECT * FROM " + plugin.getConfig().getString("Database-Properties.Table") + " WHERE (UUID=?)");
                preparedStatement.setString(1, offlinePlayer.getUniqueId().toString());
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    getHomes = resultSet.getString("Home");
                    homes.add(getHomes);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }

        return homes;
    }

    public String getWorld(OfflinePlayer offlinePlayer, String home) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            if (hasHomes(offlinePlayer)) {

                connection = connectionPool.getConnection();
                preparedStatement = connection.prepareStatement("SELECT World FROM " + plugin.getConfig().getString("Database-Properties.Table") + " WHERE UUID=? AND HOME=?");
                preparedStatement.setString(1, offlinePlayer.getUniqueId().toString());
                preparedStatement.setString(2, home);
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) return resultSet.getString("World");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }
        return null;
    }

    public double getX(OfflinePlayer offlinePlayer, String home) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            if (hasHomes(offlinePlayer)) {
                connection = connectionPool.getConnection();
                preparedStatement = connection.prepareStatement("SELECT X FROM " + plugin.getConfig().getString("Database-Properties.Table") + " WHERE UUID=? AND HOME=?");
                preparedStatement.setString(1, offlinePlayer.getUniqueId().toString());
                preparedStatement.setString(2, home);
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) return resultSet.getDouble("X");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }

        return 0;
    }

    public double getY(OfflinePlayer offlinePlayer, String home) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            if (hasHomes(offlinePlayer)) {

                connection = connectionPool.getConnection();
                preparedStatement = connection.prepareStatement("SELECT Y FROM " + plugin.getConfig().getString("Database-Properties.Table") + " WHERE UUID=? AND HOME=?");
                preparedStatement.setString(1, offlinePlayer.getUniqueId().toString());
                preparedStatement.setString(2, home);
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) return resultSet.getDouble("Y");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }

        return 0;
    }

    public double getZ(OfflinePlayer offlinePlayer, String home) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            if (hasHomes(offlinePlayer)) {

                connection = connectionPool.getConnection();
                preparedStatement = connection.prepareStatement("SELECT Z FROM " + plugin.getConfig().getString("Database-Properties.Table") + " WHERE UUID=? AND HOME=?");
                preparedStatement.setString(1, offlinePlayer.getUniqueId().toString());
                preparedStatement.setString(2, home);
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) return resultSet.getDouble("Z");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }

        return 0;
    }

    public float getPitch(OfflinePlayer offlinePlayer, String home) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement("SELECT Pitch FROM " + plugin.getConfig().getString("Database-Properties.Table") + " WHERE UUID=? AND HOME=?");
            preparedStatement.setString(1, offlinePlayer.getUniqueId().toString());
            preparedStatement.setString(2, home);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) return resultSet.getFloat("Pitch");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }
        return 0;
    }

    public float getYaw(OfflinePlayer offlinePlayer, String home) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement("SELECT Yaw FROM " + plugin.getConfig().getString("Database-Properties.Table") + " WHERE UUID=? AND HOME=?");
            preparedStatement.setString(1, offlinePlayer.getUniqueId().toString());
            preparedStatement.setString(2, home);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) return resultSet.getFloat("Yaw");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }
        return 0;
    }

    private void makeTable() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + plugin.getConfig().getString("Database-Properties.Table") + " (`UUID` VARCHAR(80) NOT NULL , `Player` VARCHAR(60) NOT NULL , `Home` VARCHAR(60) NOT NULL , `World` VARCHAR(80) NOT NULL , `X` INT(10) NOT NULL , `Y` INT(10) NOT NULL , `Z` INT(10) NOT NULL , `Pitch` FLOAT(15) , `Yaw` FLOAT(15) ) ENGINE = InnoDB");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, null);
        }
    }

    public void onDisable() {
        connectionPool.closePool();
    }
}
