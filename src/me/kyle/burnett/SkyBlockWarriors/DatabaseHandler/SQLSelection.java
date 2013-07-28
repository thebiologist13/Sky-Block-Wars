package me.kyle.burnett.SkyBlockWarriors.DatabaseHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import me.kyle.burnett.SkyBlockWarriors.Main;

public class SQLSelection {

    public static Connection con;

    public static String address = (String) Main.getInstance().Config.get("MySQL.Address");
    public static String database = (String) Main.getInstance().Config.get("MySQL.Database");
    public static String username = (String) Main.getInstance().Config.get("MySQL.Username");
    public static String password = (String) Main.getInstance().Config.get("MySQL.Password");
    public static int port = Main.getInstance().Config.getInt("MySQL.Port");

    public static Connection getConnection() throws SQLException, ClassNotFoundException {

        String prefix = "[Sky-Block Wars] ";
        if (Main.getInstance().Config.getBoolean("MySQL.Enable")) {

            try {

                Class.forName("com.mysql.jdbc.Driver");

                if (con != null && con.isClosed() == false) {

                }

                else {

                    con = DriverManager.getConnection("jdbc:mysql://" + address + ":" + port + "/" + database, username, password);

                }

            } catch (SQLException | ClassNotFoundException e) {

                System.out.println(prefix + "MySQL connection failed. Switching to SQLite.");

                Main.getInstance().Config.set("MySQL.Enable", false);

                con = getConnection();

            }

        }

        else {

            try {

                Class.forName("org.sqlite.JDBC");

                if (con != null && con.isClosed() == false) {

                }

                else {

                    con = DriverManager.getConnection("jdbc:sqlite:plugins/Sky-Block Wars/Data.sql");

                }

            } catch (SQLException | ClassNotFoundException e) {

                e.printStackTrace();

            }

        }

        return con;
    }

    public static Statement getStatement() throws SQLException, ClassNotFoundException {

        if (Main.getInstance().Config.getBoolean("MySQL.Enable")) {

            return getConnection().createStatement();

        } else {

            return getConnection().createStatement();
        }
    }
}
