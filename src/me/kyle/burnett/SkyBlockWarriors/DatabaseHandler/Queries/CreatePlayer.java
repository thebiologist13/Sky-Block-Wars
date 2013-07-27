package me.kyle.burnett.SkyBlockWarriors.DatabaseHandler.Queries;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.entity.Player;

import me.kyle.burnett.SkyBlockWarriors.DatabaseHandler.SQLSelection;

public class CreatePlayer {

    public static void enterNewUser(Player player) throws SQLException, ClassNotFoundException {

        ResultSet rs = SQLSelection.getStatement().executeQuery("SELECT COUNT(*) FROM sbw WHERE username='" + player.getName() + "';");

        rs.next();

        if (rs.getInt(1) == 0) {

            SQLSelection.getStatement().execute("INSERT INTO sbw (username, kills, deaths, wins, losses, played) VALUES('" + player.getName() + "', 0, 0, 0, 0, 0,0);");
            SQLSelection.getConnection().close();
        }
    }
}
