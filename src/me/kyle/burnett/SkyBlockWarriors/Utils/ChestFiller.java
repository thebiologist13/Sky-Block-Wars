package me.kyle.burnett.SkyBlockWarriors.Utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.kyle.burnett.SkyBlockWarriors.Main;

public class ChestFiller {
	
	public static Main plugin;
	public ChestFiller(Main instance){
		plugin = instance;
	}
	
	
	public void loadChests(Integer arena){
		
		loadSpawn(arena);
		loadSide(arena);
		loadCenter(arena);

	}
	
	public void loadSpawn(Integer arena){
		
		List<String> chestLocSpawn = Main.Chest.getStringList(arena + ".Spawn");
		List<String> chestSpawnItems = Main.Config.getStringList("Chests.Start-Chests.ItemID/Amount");
		
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		ArrayList<Block> chests = new ArrayList<Block>();
		
		for(int x = 0; x < chestSpawnItems.size(); x++){
			
			items.add(getItem(chestSpawnItems.get(x)));
		}
		
		for(int x = 0; x < chestLocSpawn.size(); x++){
			Block b = Bukkit.getServer().getWorld(getWorld(chestLocSpawn.get(x))).getBlockAt(getLocation(chestLocSpawn.get(x)));
			if(b instanceof Chest){
				chests.add(b);
			}
		}
		
		for(int x = 0; x < chests.size(); x++){
			Inventory inv = ((Chest)chests.get(x).getState()).getBlockInventory();
			inv.clear();
			for(int y = 0; x < items.size(); x++){
				inv.addItem(items.get(y));
			}
		}
		
	}
	
	public void loadSide(Integer arena){
		
		List<String> chestLocSide = Main.Chest.getStringList(arena + ".Side");
		List<String> chestSideItems = Main.Config.getStringList("Chests.Side-Chests.ItemID/Amount");
		
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		ArrayList<Block> chests = new ArrayList<Block>();
		
		for(int x = 0; x < chestSideItems.size(); x++){
			
			items.add(getItem(chestSideItems.get(x)));
		}
		
		for(int x = 0; x < chestLocSide.size(); x++){
			Block b = Bukkit.getServer().getWorld(getWorld(chestLocSide.get(x))).getBlockAt(getLocation(chestLocSide.get(x)));
			if(b instanceof Chest){
				chests.add(b);
			}
		}
		
		for(int x = 0; x < chests.size(); x++){
			Inventory inv = ((Chest)chests.get(x).getState()).getBlockInventory();
			inv.clear();
			for(int y = 0; x < items.size(); x++){
				inv.addItem(items.get(y));
			}
		}
		
	}
	
	public void loadCenter(Integer arena){
		
		List<String> chestLocCenter = Main.Chest.getStringList(arena + ".Center");
		List<String> chestCenterItems = Main.Config.getStringList("Chests.Middle-Chest.ItemID/Amount");
		
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		ArrayList<Block> chests = new ArrayList<Block>();
		
		for(int x = 0; x < chestCenterItems.size(); x++){
			
			items.add(getItem(chestCenterItems.get(x)));
		}
		
		for(int x = 0; x < chestLocCenter.size(); x++){
			Block b = Bukkit.getServer().getWorld(getWorld(chestLocCenter.get(x))).getBlockAt(getLocation(chestLocCenter.get(x)));
			if(b instanceof Chest){
				chests.add(b);
			}
		}
		
		for(int x = 0; x < chests.size(); x++){
			Inventory inv = ((Chest)chests.get(x).getState()).getBlockInventory();
			inv.clear();
			for(int y = 0; x < items.size(); x++){
				inv.addItem(items.get(y));
			}
		}
		
		
	}
	
	
	public static ItemStack getItem(String string) {

		String[] split = string.split(",");

		for (int x = 0; x < split.length; x++) {
			split[x] = split[x].toLowerCase().trim();
		}
		if (split.length < 1)
			return null;
		if (split.length == 1)
			return new ItemStack(Integer.parseInt(split[0]));
		if (split.length == 2)
			return new ItemStack(Integer.parseInt(split[0]),
					Integer.parseInt(split[1]));
		if (split.length == 3) {
			return new ItemStack(Integer.parseInt(split[0]),
					Integer.parseInt(split[1]), Short.parseShort(split[2]));
		}

		return null;
	}
	
	public static Location getLocation(String str) {

		String[] split = str.split(",");

		for (int a = 0; a < split.length; a++) {
			split[a] = split[a].toLowerCase().trim();
		}
		if (split.length < 1)
			return null;
		
		if (split.length == 1)
			return null;
		
		if (split.length == 2)
			return null;
	
		if (split.length == 3) {
			return null;
		}
		
		if (split.length == 4) {
			return new Location(Bukkit.getServer().getWorld(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]));
		}
		return null;
	}
	
	public static String getWorld(String str) {

		String[] split = str.split(",");

		for (int a = 0; a < split.length; a++) {
			split[a] = split[a].toLowerCase().trim();
		}
		if (split.length < 1)
			return null;
		
		if (split.length == 1)
			return split[0];
		

		return null;
	}
    
}
