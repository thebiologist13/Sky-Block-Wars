package me.kyle.burnett.SkyBlockWarriors;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public class GameManager {

	static GameManager instance = new GameManager();

	private ArrayList<Game> games = new ArrayList<Game>();
	
	public static GameManager getInstance() {
		
		return instance;
	}

	public ArrayList<Game> getGames() {
		return this.games;
	}
	
	public void getPlayerGame(Player p){
		for(Game g : games){
			
		}
	}


}
