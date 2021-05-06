package sql;

import com.reussy.ExodusHomes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLMain {

    private final ExodusHomes plugin = ExodusHomes.getPlugin(ExodusHomes.class);
    private Connection connection;

    public SQLMain(String host, int port, String database, String username, String password) {

        try {

            synchronized (this) {

                if (connection != null && !connection.isClosed()) {

                    return;
                }

                Class.forName("com.mysql.cj.jdbc.Driver");

                this.connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);

                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&lExodusHomes &8| &aConnected to your database!"));
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
            }

        } catch (SQLException | ClassNotFoundException e) {

            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&lExodusHomes &8| &4Could not connect to the database!"));
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Error: &c" + e.getCause()));
        }
    }

    public void createTable() {

        try {

            PreparedStatement statement = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS homes (`UUID` VARCHAR(80) NOT NULL , `Player` VARCHAR(60) NOT NULL , `Home` VARCHAR(60) NOT NULL , `World` VARCHAR(80) NOT NULL , `X` INT(10) NOT NULL , `Y` INT(10) NOT NULL , `Z` INT(10) NOT NULL , `Pitch` FLOAT(15) , `Yaw` FLOAT(15) ) ENGINE = InnoDB");
            statement.executeUpdate();
            statement.close();

        } catch (SQLException | NullPointerException e) {

            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&lExodusHomes &8| &4Could not connect to the database!"));
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Error: &c" + e.getCause()));
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}