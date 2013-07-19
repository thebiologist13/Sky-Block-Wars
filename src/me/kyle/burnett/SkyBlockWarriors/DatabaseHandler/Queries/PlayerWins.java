package me.kyle.burnett.SkyBlockWarriors.DatabaseHandler.Queries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import me.kyle.burnett.SkyBlockWarriors.DatabaseHandler.SQLSelection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class PlayerWins implements Listener {

    public static void setPlayerWins(List<String> players, int value) throws SQLException, ClassNotFoundException {
        int wins = 0;
        for (int i = 0; i < players.size(); i++) {
            Player player = Bukkit.getServer().getPlayer(players.get(i));
            ResultSet rs = SQLSelection.getStatement().executeQuery("SELECT wins FROM sbw WHERE username='" + player.getName() + "';");
            if (rs.next()) {
                wins = rs.getInt(1) + value;
                SQLSelection.getStatement().execute("UPDATE sbw SET wins=" + wins + " WHERE username='" + player.getName() + "';");
            }
        }
    }
}
