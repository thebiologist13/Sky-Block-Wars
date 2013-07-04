package me.kyle.burnett.SkyBlockWarriors.Utils;

import me.kyle.burnett.SkyBlockWarriors.Game;
import me.kyle.burnett.SkyBlockWarriors.Main;

import org.bukkit.Bukkit;


public class Tasks {
	
	private int id;
	private long initial;
	private long after;
	private Game g;
	
	public Tasks(int id, long initial, long after, Game g, boolean start){
		this.id = id;
		this.initial = initial;
		this.after = after;
		this.g = g;
		
		if(start){
			run();
		}
	}

	public int getID(){
		return this.id;
	}
	
	public void cancel(){
		Bukkit.getServer().getScheduler().cancelTask(this.id);
	}
	
	public Game getGame(){
		
		return this.g;
	}
	public void run(){
		
		this.id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable(){
			
			int count = Main.getInstance().Config.getInt("Auto-Start-Time");
			int currentCount = count;
			@Override
			public void run() {
				
			//Need a timer to say at intervals of the specified countdown. Then when it reaches ten do 10 9 8 7 6 5 4 3 2 1 Started.
				
				
			
				
				
			}
		}, this.initial, this.after);
		
	}
	
	
	
	

}
