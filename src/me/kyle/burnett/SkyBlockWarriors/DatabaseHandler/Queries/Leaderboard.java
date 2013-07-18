package me.kyle.burnett.SkyBlockWarriors.DatabaseHandler.Queries;

import java.sql.ResultSet;
import java.sql.SQLException;

import me.kyle.burnett.SkyBlockWarriors.Main;
import me.kyle.burnett.SkyBlockWarriors.DatabaseHandler.SQLSelection;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Leaderboard {

    public static void getTopPlayers(Player player, String category) throws SQLException, ClassNotFoundException {

        int limit = Main.getInstance().Config.getInt("Leaderboard-Print-Limit");

        ResultSet rs = SQLSelection.getStatement().executeQuery("SELECT username, " + category.toLowerCase() + " FROM sbw ORDER BY " + category.toLowerCase() + " DESC LIMIT " + limit + ";");
        category = category.substring(0, 1).toUpperCase() + category.substring(1, category.length());
        player.sendMessage(ChatColor.GOLD + "[" + ChatColor.BLUE + "SBW" + ChatColor.GOLD + "]Leaderboard sorted by " + category + ":");
        limit = 1;
        while (rs.next()) {
            player.sendMessage(ChatColor.GOLD + "" + limit + ". " + ChatColor.BLUE + rs.getString(1) + " - " + rs.getInt(2));
            limit++;
        }
        SQLSelection.getConnection().close();
    }
}
