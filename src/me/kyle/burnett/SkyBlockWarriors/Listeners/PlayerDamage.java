package me.kyle.burnett.SkyBlockWarriors.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerDamage implements Listener {

	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e){
		
		if(e.getEntity() instanceof Player ){
			Player p = (Player) e.getEntity();
			
			if(e.getDamager() instanceof Player){
				Player pd = (Player) e.getDamager();
				
			}
		}
	}
}
