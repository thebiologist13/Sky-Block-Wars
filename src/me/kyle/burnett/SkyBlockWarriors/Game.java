package me.kyle.burnett.SkyBlockWarriors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import me.kyle.burnett.SkyBlockWarriors.Commands.SW.ArenaState;
import me.kyle.burnett.SkyBlockWarriors.Configs.ConfigManager;
import me.kyle.burnett.SkyBlockWarriors.Utils.ChestFiller;
import me.kyle.burnett.SkyBlockWarriors.Utils.WorldEditUtility;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
	private ArrayList<String> editing = new ArrayList<String>();
	private int gameID;
	private Arena arena;
	
	public Game(int gameID){
		
		this.gameID = gameID;
		
		prepareArena();
		
	}
	
	public void prepareArena(){
		
		this.state = ArenaState.LOADING;
		
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
		
	}
	
	public ArrayList<String> getPlayers(){
		return players;
	}
	
	public int getGameID(){
		return this.gameID;
	}
	
	public Arena getArena(){
		
		return this.arena;
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
		
		this.players.remove(p.getName());
		
		if(this.voted.contains(p.getName())){
			
			this.voted.remove(p.getName());
		}
		
		this.removeFromTeam(p);
		
	}
	
	public void removeFromTeam(Player p){
		
		this.team.get(p.getName()).removePlayer(p);
		this.team.remove(p.getName());
	}
	
	public ArenaState addPlayer(Player p){
		
		if(this.players.size() > Main.getInstance().Config.getInt("Max-People-In-A-Team") * 4){
			
			return ArenaState.FULL;
		
		}else if(this.state != ArenaState.WAITING){
			
			return this.state;
		
		}else if(this.state == ArenaState.WAITING){
			
			this.players.add(p.getName());
			
		}
		
		return ArenaState.OTHER;
	}
	
	public ArrayList<String> getEditors(){
		
		return this.editing;
	}
	
	public ArenaState getState(){
		return this.state;
	}
	
	public void setState(ArenaState state){
		this.state = state;
	}
	
	public void addEditer(Player p){
		
		this.editing.add(p.getName());
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
	
	public void addChest(ChestType chest, Player p){
		
		if(chest.equals(ChestType.SPAWN)){
			
			ArrayList<String> spawnChests = (ArrayList<String>) Main.getInstance().Chest.getStringList(this.getGameID() + "Spawn");
			
			int x, y, z;
			
			x = p.getLocation().getBlockX();
			
			y = p.getLocation().getBlockY();
			
			z = p.getLocation().getBlockZ();
			
			spawnChests.add(Integer.toString(x) + "," + Integer.toString(y) + "," + Integer.toString(z));
			
			Main.getInstance().Chest.set(Integer.toString(this.getGameID()) + "Spawn", spawnChests);
			
			ConfigManager.getInstance().saveYamls();
			
		}else if(chest.equals(ChestType.SIDE)){
			
			ArrayList<String> spawnChests = (ArrayList<String>) Main.getInstance().Chest.getStringList(this.getGameID() + "Side");
			
			int x, y, z;
			
			x = p.getLocation().getBlockX();
			
			y = p.getLocation().getBlockY();
			
			z = p.getLocation().getBlockZ();
			
			spawnChests.add(Integer.toString(x) + "," + Integer.toString(y) + "," + Integer.toString(z));
			
			Main.getInstance().Chest.set(Integer.toString(this.getGameID()) + "Side", spawnChests);
			
			ConfigManager.getInstance().saveYamls();
			
			
		}else if(chest.equals(ChestType.CENTER)){
			
			ArrayList<String> spawnChests = (ArrayList<String>) Main.getInstance().Chest.getStringList(this.getGameID() + "Center");
			
			int x, y, z;
			
			x = p.getLocation().getBlockX();
			
			y = p.getLocation().getBlockY();
			
			z = p.getLocation().getBlockZ();
			
			spawnChests.add(Integer.toString(x) + "," + Integer.toString(y) + "," + Integer.toString(z));
			
			Main.getInstance().Chest.set(Integer.toString(this.getGameID()) + "Center", spawnChests);
			
			ConfigManager.getInstance().saveYamls();
			
		}
		
	}
}