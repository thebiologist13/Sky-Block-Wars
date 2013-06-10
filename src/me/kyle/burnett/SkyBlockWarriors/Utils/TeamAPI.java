package me.kyle.burnett.SkyBlockWarriors.Utils;

import me.kyle.burnett.SkyBlockWarriors.Main;

import org.bukkit.entity.Player;

public class TeamAPI {
	
	public String getTeam(Player p){
		
		return Main.teamManager.playersTeam.get(Main.gameAPI.getPlayerGame(p)).get(p.getName());
	}
	
	public boolean isInTeam(Player p, String team){
		
		String inTeam = Main.teamManager.playersTeam.get(Main.gameAPI.getPlayerGame(p)).get(p.getName());
		
		if(this.getTeam(p) == inTeam){
		
		return true;
		}
		
		return false;
	}
	
	public boolean hasTeam(Player p){
		
		if(this.getTeam(p) != null){
			return true;
		}
		
		return false;
	}
	
	public void removeFromTeam(Player p){
		
		if(this.hasTeam(p)){
			
			String team = this.getTeam(p);
			
			Main.teamManager.playersTeam.get(Main.gameAPI.getPlayerGame(p)).remove(p.getName());
			Main.teamManager.arenaTeams.get(Main.gameAPI.getPlayerGame(p)).get(team).remove(p.getName());
		}
	}
}
