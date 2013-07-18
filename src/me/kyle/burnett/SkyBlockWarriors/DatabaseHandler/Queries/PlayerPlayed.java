package me.kyle.burnett.SkyBlockWarriors.DatabaseHandler.Queries;

import java.sql.ResultSet;
import java.sql.SQLException;

import me.kyle.burnett.SkyBlockWarriors.DatabaseHandler.SQLSelection;

import org.bukkit.entity.Player;

public class PlayerPlayed {

    public static void setPlayerPlayed(Player player, int value) throws SQLException, ClassNotFoundException {
        int played = 0;
        ResultSet rs = SQLSelection.getStatement().executeQuery("SELECT played FROM sbw WHERE username ='" + player.getName() + "';");
        if (rs.next()) {
            played = rs.getInt(1) + value;
            SQLSelection.getStatement().execute("UPDATE sbw SET played=" + played + " WHERE username='" + player.getName() + "'");
        }
        SQLSelection.getConnection().close();
    }
}
