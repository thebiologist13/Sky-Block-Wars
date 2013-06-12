package me.kyle.burnett.SkyBlockWarriors;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public class Game {
	
	public ArenaState state = ArenaState.DISABLED;
	private ArrayList<Player> players = new ArrayList<Player>();
	private ArrayList<Player> yellow = new ArrayList<Player>();
	private ArrayList<Player> red = new ArrayList<Player>();
	private ArrayList<Player> green = new ArrayList<Player>();
	private ArrayList<Player> blue = new ArrayList<Player>();
	private ArrayList<Player> voted = new ArrayList<Player>();
	private int gameID;
	private Arena arena;
	
	public Game(int gameID){
		
		this.gameID = gameID;
		
		prepareArena();
		
	}
	
	public void prepareArena(){
		
		this.state = ArenaState.LOADING;
		
		//Add the setup
		
	}
	
	
	public enum ArenaState {
		
		DISABLED, INGAME, STARTING, RESETING, WAITING, FINISHING, EDITING, LOADING
	}
}