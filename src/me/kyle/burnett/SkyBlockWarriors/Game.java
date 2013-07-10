package me.kyle.burnett.SkyBlockWarriors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import me.kyle.burnett.SkyBlockWarriors.Configs.ConfigManager;
import me.kyle.burnett.SkyBlockWarriors.Events.PlayerJoinArenaEvent;
import me.kyle.burnett.SkyBlockWarriors.Events.PlayerLeaveArenaEvent;
import me.kyle.burnett.SkyBlockWarriors.Utils.ChestFiller;
import me.kyle.burnett.SkyBlockWarriors.Utils.WorldEditUtility;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.data.DataException;

public class Game {
	
	
	ScoreboardManager manager = Bukkit.getScoreboardManager();
	Scoreboard board = manager.getNewScoreboard();
	Team BLUE = board.registerNewTeam("Blue Team");
	Team RED = board.registerNewTeam("Red Team");
	Team YELLOW = board.registerNewTeam("Yellow Team");
	Team GREEN = board.registerNewTeam("Green Team");
	public ArenaState state = ArenaState.LOADING;
	private ArrayList<String> players = new ArrayList<String>();
	private ArrayList<String> voted = new ArrayList<String>();
	private HashMap<String, Team> team = new HashMap<String, Team>();
	private ArrayList<String> editors = new ArrayList<String>();
	private int gameID;
	
	public Game(int gameID){
		
		this.gameID = gameID;
		
		prepareArena(false);
		
	}
	
	public Game(int gameID, boolean just){
		
		this.gameID = gameID;
		
		prepareArena(just);
		
	}
	
	
	public void prepareArena(boolean just){
		
		this.state = ArenaState.LOADING;
		this.voted.clear();
		this.players.clear();
		this.team.clear();
		
		if(!just){
		
			try {
				
				WorldEditUtility.getInstance().loadIslandSchematic(this.gameID);
				
			} catch (MaxChangedBlocksException e) {
				
				e.printStackTrace();
			} catch (DataException e) {
				
				e.printStackTrace();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		
			ChestFiller.loadChests(this.gameID);
			
			this.state = ArenaState.WAITING;
			return;
		}
		
		this.state = ArenaState.IN_SETUP;
		
	}
	
	public ArrayList<String> getPlayers(){
		return players;
	}
	
	public int getGameID(){
		return this.gameID;
	}
	
	public Team getYellowTeam(){
		
		return this.YELLOW;
	}
	
	public Team getGreenTeam(){
		
		return this.GREEN;
	}
	
	public Team getBlueTeam(){
		
		return this.BLUE;
	}
	
	public Team getRedTeam(){
		
		return this.RED;
	}
	
	public ArrayList<String> getVoted(){
		
		return voted;
	}
	
	public Team getPlayerTeam(Player p){
		
		Team team = this.team.get(p.getName());
		
		if(team == null){
			return null;
		}
		
		if(team.equals(this.RED)){
			return this.RED;
		
		}else if(team.equals(this.GREEN)){
			
			return this.GREEN;
			
		}else if(team.equals(this.BLUE)){
			
			return this.BLUE;
		
		}else if(team.equals(this.YELLOW)){
			
			return this.YELLOW;
		
		}else{
			return null;
		}
		
	}
	
	public void setTeamRed(Player p){
		
		this.removeFromTeam(p);
		this.team.put(p.getName(), this.RED);
		this.RED.addPlayer(p);
	}
	
	public void setTeamBlue(Player p){
		
		this.removeFromTeam(p);
		this.team.put(p.getName(), this.BLUE);
		this.RED.addPlayer(p);
	}
	
	public void setTeamGreen(Player p){
		
		this.removeFromTeam(p);
		this.team.put(p.getName(), this.GREEN);
		this.RED.addPlayer(p);
	}
	
	public void setTeamYellow(Player p){
		
		this.removeFromTeam(p);
		this.team.put(p.getName(), this.YELLOW);
		this.RED.addPlayer(p);
	}
	
	public void removeFromGame(Player p){
		
		PlayerLeaveArenaEvent event = new PlayerLeaveArenaEvent(p, Game.this);
		Bukkit.getServer().getPluginManager().callEvent(event);
		
		this.players.remove(p.getName());
		
		this.removeFromTeam(p);
		
		this.broadCastGame(ChatColor.GOLD + p.getName() + ChatColor.GREEN + "has left the arena.");
		
	}
	
	public void removeFromTeam(Player p){
		if(this.team.containsKey(p.getName())){
			this.team.get(p.getName()).removePlayer(p);
			this.team.remove(p.getName());
		}
	}
	
	public ArenaState addPlayer(Player p){
		
		if(this.players.size() > Main.getInstance().Config.getInt("Max-People-In-A-Team") * 4){
			
			return ArenaState.FULL;
		
		}else if(this.state != ArenaState.WAITING){
			
			return this.state;
		
		}else if(this.state == ArenaState.WAITING){
			
			this.players.add(p.getName());
			
			PlayerJoinArenaEvent event = new PlayerJoinArenaEvent(p, Game.this);
			Bukkit.getServer().getPluginManager().callEvent(event);
			
			this.broadCastGame(p.getDisplayName() + ChatColor.GREEN + " has joined the arena.");
			
			int startPlayers = Main.getInstance().Config.getInt("Auto-Start-Players");
			
			p.sendMessage(ChatColor.GREEN + "The game will automatically start when there are " + startPlayers + " players.");
			this.checkStart();
		}
		
		return ArenaState.OTHER_REASON;
	}
	
	public ArenaState getState(){
		return this.state;
	}
	
	public void setState(ArenaState state){
		this.state = state;
	}
	
	public void broadCastGame(String s){
		
		for(int x = 0; x < players.size(); x++){
			
			Player p = Bukkit.getServer().getPlayer(players.get(x));
			
			p.sendMessage(s);
			
		}
		
	}
	
	
	public void broadCastServer(String s){
		
		for(Player p : Bukkit.getServer().getOnlinePlayers()){
			
			p.sendMessage(s);
			
		}
		
	}
	
	public ChatColor getTeamColor(Player p){
		
		Team team = this.getPlayerTeam(p);
		
		if(team == null){
			return null;
		}
		
		if(team.equals(this.RED)){
			
			return ChatColor.RED;
		
		}else if(team.equals(this.GREEN)){
			
			return ChatColor.GREEN;
		
		}else if(team.equals(this.BLUE)){
			
			return ChatColor.BLUE;
		
		}if(team.equals(this.YELLOW)){
			
			return ChatColor.YELLOW;
		}
		
		return null;
	}
	
	@SuppressWarnings("unused")
	public String getPlayersAsList(){
		
		ArrayList<String> playersColor = new ArrayList<String>();
		
		for(int x = 0; x < this.getPlayers().size(); x++){
			
			Player p = Bukkit.getServer().getPlayer(this.getPlayers().get(x));
			
			//Get the players add them to a list with there colors to send to the player.
		}
		
		return this.getPlayers().toString().replace("[", " ").replace("]", " ");
	}
	
	public void addChest(ChestType chest, Location loc){
		
		if(chest.equals(ChestType.SPAWN)){
			
			ArrayList<String> spawnChests = (ArrayList<String>) Main.getInstance().Chest.getStringList("Chest." + this.getGameID() + ".Spawn");
			
			int x, y, z;
			
			x = loc.getBlockX();
			
			y = loc.getBlockY();
			
			z = loc.getBlockZ();
			
			spawnChests.add(Integer.toString(x) + "," + Integer.toString(y) + "," + Integer.toString(z));
			
			Main.getInstance().Chest.set("Chest." + this.getGameID() + ".Spawn", spawnChests);
			
			ConfigManager.getInstance().saveYamls();
			
		}else if(chest.equals(ChestType.SIDE)){
			
			ArrayList<String> spawnChests = (ArrayList<String>) Main.getInstance().Chest.getStringList("Chest." + this.getGameID() + ".Side");
			
			int x, y, z;
			
			x = loc.getBlockX();
			
			y = loc.getBlockY();
			
			z = loc.getBlockZ();
			
			spawnChests.add(Integer.toString(x) + "," + Integer.toString(y) + "," + Integer.toString(z));
			
			Main.getInstance().Chest.set("Chest." + this.getGameID() + ".Side", spawnChests);
			
			ConfigManager.getInstance().saveYamls();
			
			
		}else if(chest.equals(ChestType.CENTER)){
			
			ArrayList<String> spawnChests = (ArrayList<String>) Main.getInstance().Chest.getStringList("Chest." + this.getGameID() + ".Center");
			
			int x, y, z;
			
			x = loc.getBlockX();
			
			y = loc.getBlockY();
			
			z = loc.getBlockZ();
			
			spawnChests.add(Integer.toString(x) + "," + Integer.toString(y) + "," + Integer.toString(z));
			
			Main.getInstance().Chest.set("Chest." + this.getGameID() + ".Center", spawnChests);
			
			ConfigManager.getInstance().saveYamls();
			
		}
		
	}
	
	public ArrayList<String> getEditors(){
		
		return editors;
	}
	
	public int getEditorsSize(){
		
		return getEditors().size();
	}
	
	public void addEditor(Player p){
		editors.add(p.getName());
	}
	
	public void removeEditor(Player p){
		if(editors.contains(p.getName())){
			editors.remove(p.getName());
		}
	}
	
	public void checkStart(){
		
		if(getPlayers().size() > Main.getInstance().Config.getInt("Auto-Start-Players")){
			
		}
		
	}
	
	public void endGame(){
		//Announce winner or reason of end.
		//Remove players.
		//Register stats.
		//Unregister teams or clear them.
	}
}