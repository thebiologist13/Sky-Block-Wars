package me.kyle.burnett.SkyBlockWarriors;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.kyle.burnett.SkyBlockWarriors.Commands.SW;
import me.kyle.burnett.SkyBlockWarriors.Configs.ConfigManager;
import me.kyle.burnett.SkyBlockWarriors.Listeners.PlayerDeath;
import me.kyle.burnett.SkyBlockWarriors.Listeners.PlayerLeave;
import me.kyle.burnett.SkyBlockWarriors.Utils.InventoryUtil;
import me.kyle.burnett.SkyBlockWarriors.Utils.SchematicLoadSave;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{

	// Manages the configs.
	public static ConfigManager configManager;

	// Manages the games.
	public static GameManager gameManager;

	// Utility for saving inventorys.
	public static InventoryUtil invent;

	// Methods to do with world edit.
	public static SchematicLoadSave worldedit;

	// Main config.
	public static File configFile;
	public static FileConfiguration Config;

	// Arena config.
	public static File arenaFile;
	public static FileConfiguration Arena;

	// Inv config.
	public static File invFile;
	public static FileConfiguration Inv;

	// Chest config.
	public static File chestFile;
	public static FileConfiguration Chest;

	// Plugin manager
	public static PluginManager pm = Bukkit.getServer().getPluginManager();

	public static Logger log = Bukkit.getLogger();

	@Override
	public void onEnable(){
		
		// Set accessors
		Main.configManager = new ConfigManager(this);
		Main.gameManager = new GameManager();
		Main.invent = new InventoryUtil();
		Main.worldedit = new SchematicLoadSave(this);

		// Define files
		configFile = new File(getDataFolder(), "config.yml");
		arenaFile = new File(getDataFolder(), "arena.yml");
		invFile = new File(getDataFolder(), "inventorys.yml");
		chestFile = new File(getDataFolder(), "chests.yml");

		try {
			// Try to setup the configs.
			Main.configManager.firstRun();

		} catch (Exception e) {

			e.printStackTrace();
		}

		// Load save YAMLS.
		Main.Config = new YamlConfiguration();
		Main.Arena = new YamlConfiguration();
		Main.Inv = new YamlConfiguration();
		Main.Chest = new YamlConfiguration();
		Main.configManager.loadYamls();
		Main.configManager.saveYamls();
		
		// Register events.
		pm.registerEvents(new PlayerDeath(), this);
		pm.registerEvents(new PlayerLeave(), this);

		// Add commands
		getCommand("skyblockw").setExecutor(new SW());
       
	}
	
	@Override
	public void onDisable(){

		
	}    
	
	public static Location getLobby(){
		
		if(Main.Config.contains("Lobby")){
		
			World world = Bukkit.getServer().getWorld(Main.Config.getString("Lobby.World"));
			
			int X = Main.Config.getInt("Lobby.X");
			int Y = Main.Config.getInt("Lobby.Y");
			int Z = Main.Config.getInt("Lobby.Z");
			float Yaw = Main.Config.getLong("Lobby.Yaw");
			float Pitch = Main.Config.getLong("Lobby.Pitch");
			
			Location lobby = new Location(world, X, Y, Z, Yaw, Pitch);
			
			return lobby;
		}
		log.log(Level.SEVERE, "Skyblock Wars lobby not found.");
		return null;
		
	}

}
