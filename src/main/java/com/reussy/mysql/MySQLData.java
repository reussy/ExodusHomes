package com.reussy.mysql;

import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MySQLData {

    public boolean hasHomes(Connection connection, UUID uuid) {

        try {

            String hasHome = "SELECT * FROM homes WHERE (UUID=?)";
            PreparedStatement statement = connection.prepareStatement(hasHome);
            statement.setString(1, uuid.toString());

            ResultSet set = statement.executeQuery();

            if (set.next()) return true;

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return false;
    }

    public void createHomes(Connection connection, UUID uuid, Player player, String world, String home, double x, double y, double z, float pitch, float yaw) {

        try {

            String setHome = "INSERT INTO homes (UUID, Player, World, Home, X, Y, Z, Pitch, Yaw) VALUE (?,?,?,?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(setHome);
            statement.setString(1, uuid.toString());
            statement.setString(2, player.getName());
            statement.setString(3, world);
            statement.setString(4, home);
            statement.setDouble(5, x);
            statement.setDouble(6, y);
            statement.setDouble(7, z);
            statement.setFloat(8, pitch);
            statement.setFloat(9, yaw);
            statement.executeUpdate();

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    public void setNewName(Connection connection, UUID uuid, String home, String newName) {

        try {

            String Name = "UPDATE homes SET Home=? WHERE UUID=? AND Home=?";
            PreparedStatement statement = connection.prepareStatement(Name);
            statement.setString(1, newName);
            statement.setString(2, uuid.toString());
            statement.setString(3, home);
            statement.executeUpdate();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteHomes(Connection connection, UUID uuid, String home) {

        try {

            String Home = "DELETE FROM homes WHERE UUID=? AND HOME=?";
            PreparedStatement statement = connection.prepareStatement(Home);
            statement.setString(1, uuid.toString());
            statement.setString(2, home);
            statement.executeUpdate();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAll(Connection connection, UUID uuid) {

        try {
            String homes = "DELETE FROM homes WHERE UUID=?";
            PreparedStatement statement = connection.prepareStatement(homes);
            statement.setString(1, uuid.toString());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getHomes(Connection connection, UUID uuid) {

        List<String> homes = new ArrayList<>();
        String getHomes;

        try {

            if (hasHomes(connection, uuid)) {

                String Home = "SELECT * FROM homes WHERE (UUID=?)";
                PreparedStatement statement = connection.prepareStatement(Home);
                statement.setString(1, uuid.toString());
                ResultSet set = statement.executeQuery();

                while (set.next()) {

                    getHomes = set.getString("Home");
                    homes.add(getHomes);

                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return homes;
    }

    public String getWorld(Connection connection, UUID uuid, String home) {

        try {

            if (hasHomes(connection, uuid)) {

                String World = "SELECT World FROM homes WHERE UUID=? AND HOME=?";
                PreparedStatement statement = connection.prepareStatement(World);
                statement.setString(1, uuid.toString());
                statement.setString(2, home);
                ResultSet set = statement.executeQuery();
                if (set.next()) return set.getString("World");
                set.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public double getX(Connection connection, UUID uuid, String home) {

        try {

            if (hasHomes(connection, uuid)) {

                String X = "SELECT X FROM homes WHERE UUID=? AND HOME=?";
                PreparedStatement statement = connection.prepareStatement(X);
                statement.setString(1, uuid.toString());
                statement.setString(2, home);
                ResultSet set = statement.executeQuery();
                if (set.next()) return set.getDouble("X");
                set.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public double getY(Connection connection, UUID uuid, String home) {

        try {

            if (hasHomes(connection, uuid)) {

                String Y = "SELECT Y FROM homes WHERE UUID=? AND HOME=?";
                PreparedStatement statement = connection.prepareStatement(Y);
                statement.setString(1, uuid.toString());
                statement.setString(2, home);
                ResultSet set = statement.executeQuery();
                if (set.next()) return set.getDouble("Y");
                set.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public double getZ(Connection connection, UUID uuid, String home) {

        try {

            if (hasHomes(connection, uuid)) {

                String Z = "SELECT Z FROM homes WHERE UUID=? AND HOME=?";
                PreparedStatement statement = connection.prepareStatement(Z);
                statement.setString(1, uuid.toString());
                statement.setString(2, home);
                ResultSet set = statement.executeQuery();
                if (set.next()) return set.getDouble("Z");
                set.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public float getPitch(Connection connection, UUID uuid, String home) {

        try {
            String Pitch = "SELECT Pitch FROM homes WHERE UUID=? AND HOME=?";
            PreparedStatement statement = connection.prepareStatement(Pitch);
            statement.setString(1, uuid.toString());
            statement.setString(2, home);
            ResultSet set = statement.executeQuery();
            if (set.next()) return set.getFloat("Pitch");

            set.close();
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return 0;
    }

    public float getYaw(Connection connection, UUID uuid, String home) {

        try {
            String Yaw = "SELECT Yaw FROM homes WHERE UUID=? AND HOME=?";
            PreparedStatement statement = connection.prepareStatement(Yaw);
            statement.setString(1, uuid.toString());
            statement.setString(2, home);
            ResultSet set = statement.executeQuery();
            if (set.next()) return set.getFloat("Yaw");

            set.close();
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return 0;
    }
}
