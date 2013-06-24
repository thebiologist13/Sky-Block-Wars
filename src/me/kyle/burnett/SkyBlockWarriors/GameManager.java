package me.kyle.burnett.SkyBlockWarriors;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;

public class GameManager {

	static GameManager instance = new GameManager();

	private ArrayList<Game> games = new ArrayList<Game>();
	private HashMap<String, Integer> playerGame = new HashMap<String, Integer>();
	
	public static GameManager getInstance() {
		
		return instance;
	}
	
	public void setUp(){
		this.games.clear();
	}

	public ArrayList<Game> getGames() {
		return this.games;
	}
	
	public int getPlayerGame(Player p){
		
		if(playerGame.get(p.getName()) != null){
			
			return playerGame.get(p.getName());
		}
		
		return -1;
	}
	
	public boolean isPlayerInGame(Player p){
		
		if(this.playerGame.containsKey(p.getName())){
			
			if(this.playerGame.get(p.getName()) != null){
				return true;
			}
		}
		
		return false;
	}
	
	public void setPlayerGame(Player p, Game g){
		
		playerGame.put(p.getName(), g.getGameID());
	}
	
	public int getArenaAmount(){
		return Main.getInstance().Arena.getInt("Amount");
	}
	
	public boolean leaveGame(Player p){
		
		if(getPlayerGame(p) != -1){
			
			
			this.games.get(this.getPlayerGame(p)).removeFromGame(p);
			playerGame.put(p.getName(), null);
			
			return true;
		}
		
		return false;
	}
}
