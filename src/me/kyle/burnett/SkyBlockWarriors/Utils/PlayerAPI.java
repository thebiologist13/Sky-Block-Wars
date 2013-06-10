package me.kyle.burnett.SkyBlockWarriors.Utils;

import java.util.List;

import me.kyle.burnett.SkyBlockWarriors.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PlayerAPI {
	
	public Integer getPlayerGame(Player p){
		return Main.gameManager.players.get(p.getName());
	}
	
	public void addPlayerToGame(Integer arena, Player p){
		
		return;
	}
	
	public void sendDeathMessage(Player p, String message){
		int game = Main.gameAPI.getPlayerGame(p);
		
		List<String> players = Main.gameManager.games.get(game);
		
		for(String player : players){
			Player play = Bukkit.getServer().getPlayer(player);
			
			if(play instanceof Player){
				p.sendMessage(message);
			}
		}
		
	}
	
	public void removeFromAll(Player p, String message){
		Main.gameAPI.removeFromGame(p);
		Main.teamAPI.removeFromTeam(p);
		p.sendMessage(ChatColor.RED + message);
	}
	
	public void savePlayersInventory(Player p){
		
	}
	
	public boolean playerIsEditing(Player p){
		
		if(Main.commands.editing.containsKey(p.getName())){
			p.sendMessage("1");
			return true;
		}
		
		return false;
	}
	
	public Integer arenaPlayerIsEditing(Player p){
		
		if(this.playerIsEditing(p)){
			int arena = Main.commands.editing.get(p.getName());
			p.sendMessage("2");
			p.sendMessage(arena + " Edited");
			return arena;
		}
		
		return null;
	}
	
	public void toLobby(Player p){
		if(Main.getLobby() != null){
			p.teleport(Main.getLobby());
		}
	}

}
