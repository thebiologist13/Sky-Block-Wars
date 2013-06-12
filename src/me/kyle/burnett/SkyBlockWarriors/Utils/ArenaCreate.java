package me.kyle.burnett.SkyBlockWarriors.Utils;

import org.bukkit.entity.Player;

import me.kyle.burnett.SkyBlockWarriors.Main;

public class ArenaCreate {

	public boolean createArena(Player p){

		if(!Main.worldedit.doesSelectionExist(p)){
			return false;
		}

		int amount = Main.Arena.getInt("Amount");

		Main.Arena.createSection(Integer.toString(amount));
		Main.Arena.set(amount + ".Enabled", true);
		Main.Arena.set(amount + ".BlockX", p.getLocation().getBlockX());
		Main.Arena.set(amount + ".BlockY", p.getLocation().getBlockY());
		Main.Arena.set(amount + ".BlockZ", p.getLocation().getBlockZ());
		Main.Arena.set(amount + ".World", p.getWorld().getName());

		Main.worldedit.saveArena(p, amount);

		Main.Arena.set("Amount", amount + 1);

		Main.configManager.saveYamls();

		return true;
	}
	
	// Add to the Game class setup.
	
}
