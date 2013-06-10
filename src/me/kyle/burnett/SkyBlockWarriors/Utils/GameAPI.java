package me.kyle.burnett.SkyBlockWarriors.Utils;

import me.kyle.burnett.SkyBlockWarriors.Main;

import org.bukkit.entity.Player;

public class GameAPI {
	
	public int getPlayerGame(Player p){
		
		return Main.gameManager.players.get(p.getName());
	}
	
	public boolean isInGame(Player p){
		
		
		return Main.gameManager.players.containsKey(p.getName());
	}
	
	public void removeFromGame(Player p){
		if(this.isInGame(p)){
			int game = this.getPlayerGame(p);
			Main.gameManager.players.remove(p.getName());
			Main.gameManager.games.get(game).remove(p.getName());
		}
	}
	
}
