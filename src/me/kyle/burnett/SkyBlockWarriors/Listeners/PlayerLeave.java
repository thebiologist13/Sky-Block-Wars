package me.kyle.burnett.SkyBlockWarriors.Listeners;

import me.kyle.burnett.SkyBlockWarriors.Main;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeave implements Listener{
	
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e){
		
		Player p = e.getPlayer();
		
		if(Main.teamManager.arenaTeams.containsValue(p.getName())){
			p.teleport(Main.getLobby());
			Main.playerAPI.removeFromAll(p, " ");
		}
		
	}

	

}
