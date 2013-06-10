package me.kyle.burnett.SkyBlockWarriors;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.kyle.burnett.SkyBlockWarriors.Commands.SW;
import me.kyle.burnett.SkyBlockWarriors.Configs.ConfigManager;
import me.kyle.burnett.SkyBlockWarriors.Listeners.PlayerDeath;
import me.kyle.burnett.SkyBlockWarriors.Listeners.PlayerLeave;
import me.kyle.burnett.SkyBlockWarriors.Utils.API;
import me.kyle.burnett.SkyBlockWarriors.Utils.ArenaAPI;
import me.kyle.burnett.SkyBlockWarriors.Utils.GameAPI;
import me.kyle.burnett.SkyBlockWarriors.Utils.GameManager;
import me.kyle.burnett.SkyBlockWarriors.Utils.InventoryUtil;
import me.kyle.burnett.SkyBlockWarriors.Utils.PlayerAPI;
import me.kyle.burnett.SkyBlockWarriors.Utils.SchematicLoadSave;
import me.kyle.burnett.SkyBlockWarriors.Utils.TeamAPI;
import me.kyle.burnett.SkyBlockWarriors.Utils.TeamManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{

	public static ConfigManager configManager;	
	public static TeamAPI teamAPI;
	public static GameAPI gameAPI;
	public static TeamManager teamManager;
	public static GameManager gameManager;
	public static InventoryUtil invent;
	public static API api;
	public static ArenaAPI arenaAPI;
	public static PlayerAPI playerAPI;
	public static SchematicLoadSave worldedit;
	public static SW commands;
	
	public static File configFile;
	public static FileConfiguration Config;
	
	public static File arenaFile;
	public static FileConfiguration Arena;
	
	public static File invFile;
	public static FileConfiguration Inv;
	
	public static File chestFile;
	public static FileConfiguration Chest;
	
	static PluginManager pm = Bukkit.getServer().getPluginManager();
	
	public static HashMap<Integer, Boolean> arenas = new HashMap<Integer, Boolean>();

	boolean Enabled;
	
	public static Logger log = Bukkit.getLogger();
	
	@Override
	public void onEnable(){

       Main.configManager = new ConfigManager(this);
       Main.teamAPI = new TeamAPI();
       Main.gameAPI = new GameAPI();
       Main.gameManager = new GameManager();
       Main.teamManager = new TeamManager();
       Main.invent = new InventoryUtil();
       Main.api = new API();
       Main.playerAPI = new PlayerAPI();
       Main.arenaAPI = new ArenaAPI(this);
       Main.worldedit = new SchematicLoadSave(this);
       Main.commands = new SW();
       
       setUpConfigs();
       
       Main.configManager.saveYamls();
       
       getCommand("skyblockw").setExecutor(new SW());
       
       pm.registerEvents(new PlayerDeath(), this);
       pm.registerEvents(new PlayerLeave(), this);
       
       addArenas();
       
       System.out.println("DEBUG");
       
	}
	
	@Override
	public void onDisable(){
		
		for(Player p : Bukkit.getOnlinePlayers()){
			if(Main.gameAPI.isInGame(p)){
				Main.playerAPI.removeFromAll(p, ChatColor.RED + "Server is restarting. You will be teleported to the lobby. Sorry for the inconvenience.");
			}
		}
		
	}    
	
	public void setUpConfigs(){
		configFile = new File(getDataFolder(), "config.yml"); 
		arenaFile = new File(getDataFolder(), "arena.yml");
		invFile = new File(getDataFolder(), "inventorys.yml");
		chestFile = new File(getDataFolder(), "chests.yml");
        try {
            Main.configManager.firstRun();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
		Main.Config = new YamlConfiguration();
		Main.Arena = new YamlConfiguration();
		Main.Inv = new YamlConfiguration();
		Main.Chest = new YamlConfiguration();
        Main.configManager.loadYamls();
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
	
	public void addArenas(){
		int amount = Main.Arena.getInt("Amount");
		
		for(int x = -1; x <amount; x++){
			
			
			if(x != 0){
				
				if(Main.Arena.contains(Integer.toString(x))){
					arenas.put(x, Main.Arena.getBoolean(x + ".Enabled"));
					System.out.println(x);
				}
				
				
			}
		}
	}

}
