package me.kyle.burnett.SkyBlockWarriors.DatabaseHandler.Queries;

import java.sql.ResultSet;
import java.sql.SQLException;

import me.kyle.burnett.SkyBlockWarriors.DatabaseHandler.SQLSelection;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PlayerSearch {


    public static void getPlayerData(Player player, String target) throws SQLException, ClassNotFoundException {
        String prefix = ChatColor.GOLD + "[" + ChatColor.BLUE + "SBW" + ChatColor.GOLD + "]";
        String username = null;
        int played = 0;
        double kills = 0, deaths = 0, wins = 0, losses = 0;
        ResultSet rs = SQLSelection.getStatement().executeQuery("SELECT COUNT(*) FROM sbw WHERE username LIKE '%" + target + "';");
        rs.next();
        if (rs.getInt(1) != 0) {
            rs = SQLSelection.getStatement().executeQuery("SELECT username FROM sbw WHERE username LIKE '%" + target + "';");
            if (rs.next()) {
                username = rs.getString(1);
            }
            rs = SQLSelection.getStatement().executeQuery("SELECT kills FROM sbw WHERE username LIKE '%" + target + "';");
            if (rs.next()) {
                kills = rs.getInt(1);
            }
            rs = SQLSelection.getStatement().executeQuery("SELECT deaths FROM sbw WHERE username LIKE '%" + target + "';");
            if (rs.next()) {
                deaths = rs.getInt(1);
            }
            rs = SQLSelection.getStatement().executeQuery("SELECT wins FROM sbw WHERE username LIKE '%" + target + "';");
            if (rs.next()) {
                wins = rs.getInt(1);
            }
            rs = SQLSelection.getStatement().executeQuery("SELECT losses FROM sbw WHERE username LIKE '%" + target + "';");
            if (rs.next()) {
                losses = rs.getInt(1);
            }
            rs = SQLSelection.getStatement().executeQuery("SELECT played FROM sbw WHERE username LIKE '%" + target + "';");
            if (rs.next()) {
                played = rs.getInt(1);
            }
            player.sendMessage(prefix + "Stats for " + username + ":");
            player.sendMessage(ChatColor.BLUE + "Kills: " + ChatColor.GRAY + (int) kills);
            player.sendMessage(ChatColor.BLUE + "Deaths: " + ChatColor.GRAY + (int) deaths);

            if (deaths != 0) {

                double kd = kills / deaths;
                double kdd = (double) Math.round(kd * 100) / 100;

                player.sendMessage(ChatColor.BLUE + "K/D: " + ChatColor.GRAY + kdd);
            }

            player.sendMessage(ChatColor.BLUE + "Wins: " + ChatColor.GRAY + (int) wins);
            player.sendMessage(ChatColor.BLUE + "Losses: " + ChatColor.GRAY + (int) losses);

            if (losses != 0) {

                double wl = wins / losses;
                double wld = (double) Math.round(wl * 100) / 100;

                player.sendMessage(ChatColor.BLUE + "W/L: " + ChatColor.GRAY + wld);
            }

            player.sendMessage(ChatColor.BLUE + "Played: " + ChatColor.GRAY + played);
        } else {
            player.sendMessage(prefix + ChatColor.RED + "The player you are looking for could not be found.");
        }
    }
}
