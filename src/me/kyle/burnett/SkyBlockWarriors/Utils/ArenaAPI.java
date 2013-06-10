package me.kyle.burnett.SkyBlockWarriors.Utils;

import java.io.File;
import java.util.List;

import org.bukkit.entity.Player;

import me.kyle.burnett.SkyBlockWarriors.Main;

public class ArenaAPI {
	
	public static Main plugin;
	public ArenaAPI(Main instance){
		plugin = instance;
	}
	
	public boolean setEnabled(Integer arena){
		
		if(this.doesArenaExist(arena)){
			
			if(!this.isArenaEnabled(arena)){
				Main.Arena.set("Arenas.Arena." + arena + ".Enabled", true);
				Main.arenas.put(arena, true);
				Main.configManager.saveYamls();
				return true;
			}
		}
		return false;
	}
	
	public boolean setDisabled(Integer arena){
		if(this.doesArenaExist(arena)){
		
			if(this.isArenaEnabled(arena)){
				Main.Arena.set(arena + ".Enabled", false);
				Main.arenas.put(arena, false);
				Main.configManager.saveYamls();
				return true;
			}
		}
		return false;
	}
	
	public boolean doesArenaExist(Integer arena){
		if(Main.arenas.containsKey(arena)){
			return true;
		}
		return false;
	}
	
	public boolean isArenaInUse(Integer arena){
		
		if(this.doesArenaExist(arena)){
			
			if(Main.gameManager.arenaActive.get(arena)){
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isArenaEnabled(Integer arena){
		
		if(this.doesArenaExist(arena)){
		
			if(Main.arenas.get(arena)){
				return true;
			}
		}
		return false;
	}
	
	public boolean createArena(Player p){
		
		if(!Main.worldedit.doesSelectionExist(p)){
			return false;
		}
		
		int amount = getArenaNumber();
		
		Main.Arena.createSection(Integer.toString(amount));
		Main.Arena.set(amount + ".Enabled", true);
		Main.Arena.set(amount + ".BlockX", p.getLocation().getBlockX());
		Main.Arena.set(amount + ".BlockY", p.getLocation().getBlockY());
		Main.Arena.set(amount + ".BlockZ", p.getLocation().getBlockZ());
		Main.Arena.set(amount + ".World", p.getWorld().getName());
		
		Main.worldedit.saveArena(p, amount);
		
		Main.Arena.set("Amount", amount + 1);
		
		Main.configManager.saveYamls();
		
		Main.arenas.put(amount, true);
		
		return true;
	}
	
	public int getArenaNumber(){
		
		int size = Main.Arena.getInt("Amount");
		
		for(int x = 1; x < size; x++){
			
			if(!Main.Arena.contains(Integer.toString(x))){
				if(!(x >= size)){
					return x;
				}
			}
		}
		
		return Main.Arena.getInt("Amount");
	}
	
	public void addChest(Integer arena, String type, Player p){
		
		if(this.doesArenaExist(arena)){
			
			if(type.equalsIgnoreCase("center")){
				
				List<String> center = Main.Chest.getStringList(arena + ".Center");
				
				center.add(p.getLocation().getBlockX() + "," + p.getLocation().getBlockY() + "," + p.getLocation().getBlockZ());
				
				Main.Chest.set(arena + ".Center", center);
				
				Main.configManager.saveYamls();
				
			}
			
			else if(type.equalsIgnoreCase("side")){
				
				List<String> side = Main.Chest.getStringList(arena + ".Center");
				
				side.add(p.getLocation().getBlockX() + "," + p.getLocation().getBlockY() + "," + p.getLocation().getBlockZ());
				
				Main.Chest.set(arena + ".Side", side);
				
				Main.configManager.saveYamls();
			}
			
			else if(type.equalsIgnoreCase("start")){
				
				List<String> start = Main.Chest.getStringList(arena + ".Center");
				
				start.add(p.getLocation().getBlockX() + "," + p.getLocation().getBlockY() + "," + p.getLocation().getBlockZ());
				
				Main.Chest.set(arena + ".Start", start);
				
				Main.configManager.saveYamls();
			}
			
		}
		
	}
	
	public File getArenaFile(Integer arena){
		
		if(this.doesArenaExist(arena)){
			
			File file = new File(plugin.getDataFolder() + File.separator + "Schematics" + File.separator + arena + ".schematic");
			System.out.println(file);
			return file;
		}
		
		return null;
	}
	
	public void finishGame(Integer arena){
		
	}
	

}
