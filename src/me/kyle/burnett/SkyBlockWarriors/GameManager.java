package me.kyle.burnett.SkyBlockWarriors;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public class GameManager {

	static GameManager instance = new GameManager();

	private ArrayList<Game> games = new ArrayList<Game>();
	
	public static GameManager getInstance() {
		
		return instance;
	}
	
	public void setUp(){
		
	}

	public ArrayList<Game> getGames() {
		return this.games;
	}
	
	public int getPlayerGame(Player p){
		
		for(Game g : this.games){
			
			if(g.getPlayers().contains(p.getName())){
				return g.getGameID();
			}
		}
		return -1;
	}
	
	public int getArenaAmount(){
		return Main.Arena.getInt("Amount");
	}


}
