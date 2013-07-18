package me.kyle.burnett.SkyBlockWarriors.DatabaseHandler.Queries;

import java.sql.ResultSet;
import java.sql.SQLException;

import me.kyle.burnett.SkyBlockWarriors.DatabaseHandler.SQLSelection;

import org.bukkit.entity.Player;

public class PlayerKills {

    public static void setPlayerKills(Player player, int value) throws SQLException, ClassNotFoundException {

        int losses = 0;

        ResultSet rs = SQLSelection.getStatement().executeQuery("SELECT kills FROM sbw WHERE username ='" + player.getName() + "';");

        if (rs.next()) {

            losses = rs.getInt(1) + value;
            SQLSelection.getStatement().execute("UPDATE sbw SET kills=" + losses + " WHERE username='" + player.getName() + "'");

        }

        SQLSelection.getConnection().close();
    }
}
