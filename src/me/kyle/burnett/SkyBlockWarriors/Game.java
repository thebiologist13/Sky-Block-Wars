package me.kyle.burnett.SkyBlockWarriors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import me.kyle.burnett.SkyBlockWarriors.Utils.ChestFiller;
import me.kyle.burnett.SkyBlockWarriors.Utils.WorldEditUtility;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.data.DataException;

public class Game {
	
	
	ScoreboardManager manager = Bukkit.getScoreboardManager();
	Scoreboard board = manager.getNewScoreboard();
	Team blue = board.registerNewTeam("Blue Team");
	Team red = board.registerNewTeam("Red Team");
	Team yellow = board.registerNewTeam("Yellow Team");
	Team green = board.registerNewTeam("Green Team");
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
		
		return this.yellow;
	}
	
	public Team getGreenTeam(){
		
		return this.green;
	}
	
	public Team getBlueTeam(){
		
		return this.blue;
	}
	
	public Team getRedTeam(){
		
		return this.red;
	}
	
	public ArrayList<String> getVoted(){
		
		return voted;
	}
	
	public Team getPlayerTeam(Player p){
		
		Team team = this.team.get(p.getName());
		
		if(team.equals(this.red)){
			return this.red;
		
		}else if(team.equals(this.green)){
			
			return this.green;
			
		}else if(team.equals(this.blue)){
			
			return this.blue;
		
		}else if(team.equals(this.yellow)){
			
			return this.yellow;
		
		}else{
			return null;
		}
		
	}
	
	public void setTeamRed(Player p){
		
		this.removeFromTeam(p);
		this.team.put(p.getName(), this.red);
		this.red.addPlayer(p);
	}
	
	public void setTeamBlue(Player p){
		
		this.removeFromTeam(p);
		this.team.put(p.getName(), this.blue);
		this.red.addPlayer(p);
	}
	
	public void setTeamGreen(Player p){
		
		this.removeFromTeam(p);
		this.team.put(p.getName(), this.green);
		this.red.addPlayer(p);
	}
	
	public void setTeamYellow(Player p){
		
		this.removeFromTeam(p);
		this.team.put(p.getName(), this.yellow);
		this.red.addPlayer(p);
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
	
	public static enum ArenaState {
		
		DISABLED, INGAME, STARTING, RESETING, WAITING, FINISHING, EDITING, LOADING, FULL, OTHER
	}
}