package me.kyle.burnett.SkyBlockWarriors.DatabaseHandler.Queries;

import java.sql.ResultSet;
import java.sql.SQLException;

import me.kyle.burnett.SkyBlockWarriors.DatabaseHandler.SQLSelection;

import org.bukkit.entity.Player;

public class PlayerDeaths {

    public static void setPlayerDeaths(Player player, int value) throws SQLException, ClassNotFoundException {
        int deaths = 0;
        ResultSet rs = SQLSelection.getStatement().executeQuery("SELECT deaths FROM sbw WHERE username ='" + player.getName() + "';");
        if (rs.next()) {
            deaths = rs.getInt(1) + value;
            SQLSelection.getStatement().execute("UPDATE sbw SET deaths=" + deaths + " WHERE username='" + player.getName() + "';");
        }
        SQLSelection.getConnection().close();
    }
}
