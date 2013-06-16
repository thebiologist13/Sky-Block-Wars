package me.kyle.burnett.SkyBlockWarriors;

import java.util.ArrayList;
import java.util.HashMap;

import me.kyle.burnett.SkyBlockWarriors.Utils.ChestFiller;

import org.bukkit.entity.Player;

public class Game {
	
	public ArenaState state = ArenaState.DISABLED;
	private ArrayList<String> players = new ArrayList<String>();
	private ArrayList<String> yellow = new ArrayList<String>();
	private ArrayList<String> red = new ArrayList<String>();
	private ArrayList<String> green = new ArrayList<String>();
	private ArrayList<String> blue = new ArrayList<String>();
	private ArrayList<String> voted = new ArrayList<String>();
	private HashMap<String, String> team = new HashMap<String, String>();
	private int gameID;
	private Arena arena;
	
	public Game(int gameID){
		
		this.gameID = gameID;
		
		prepareArena();
		
	}
	
	public void prepareArena(){
		
		this.state = ArenaState.LOADING;
		
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
	
	
	public ArrayList<String> getYellowTeam(){
		
		return this.yellow;
	}
	
	public ArrayList<String> getGreenTeam(){
		
		return this.green;
	}
	
	public ArrayList<String> getBlueTeam(){
		
		return this.blue;
	}
	
	public ArrayList<String> getRedTeam(){
		
		return this.red;
	}
	
	public ArrayList<String> getVoted(){
		
		return voted;
	}
	
	public ArrayList<String> getPlayerTeam(Player p){
		
		String team = this.team.get(p.getName());
		
		if(team.equalsIgnoreCase("red")){
			return this.red;
		
		}else if(team.equalsIgnoreCase("green")){
			
			return this.green;
			
		}else if(team.equalsIgnoreCase("blue")){
			
			return this.blue;
		
		}else if(team.equalsIgnoreCase("yellow")){
			
			return this.yellow;
		
		}else{
			return null;
		}
		
	}
	
	public void setTeamRed(Player p){
		
		this.removeFromTeam(p);
		this.team.put(p.getName(), "red");
		this.red.add(p.getName());
	}
	
	public void setTeamBlue(Player p){
		
		this.removeFromTeam(p);
		this.team.put(p.getName(), "blue");
		this.red.add(p.getName());
	}
	
	public void setTeamGreen(Player p){
		
		this.removeFromTeam(p);
		this.team.put(p.getName(), "green");
		this.red.add(p.getName());
	}
	
	public void setTeamYellow(Player p){
		
		this.removeFromTeam(p);
		this.team.put(p.getName(), "yellow");
		this.red.add(p.getName());
	}
	
	public void removeFromGame(Player p){
		
		this.players.remove(p.getName());
		this.getPlayerTeam(p).remove(p.getName());
		this.team.remove(p.getName());
		
	}
	
	public void removeFromTeam(Player p){
		
		this.getPlayerTeam(p).remove(p.getName());
		this.team.remove(p.getName());
	}
	
	public enum ArenaState {
		
		DISABLED, INGAME, STARTING, RESETING, WAITING, FINISHING, EDITING, LOADING
	}
}