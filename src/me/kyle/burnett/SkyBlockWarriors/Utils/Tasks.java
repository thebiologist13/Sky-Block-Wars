package me.kyle.burnett.SkyBlockWarriors.Utils;

import me.kyle.burnett.SkyBlockWarriors.GameManager;
import me.kyle.burnett.SkyBlockWarriors.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class Tasks {
	
	private int id;
	private int initial;
	private int after;
	private Player p;
	
	public Tasks(int id, int initial, int after, Player p, boolean start){
		this.id = id;
		this.initial = initial;
		this.after = after;
		this.p = p;
		
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
	
	public Player getPlayer(){
		
		return this.p;
	}
	public void run(){
		
		this.id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable(){
			
			int count = 10;
			@Override
			public void run() {
				
				if(count == 10){
					getPlayer().sendMessage(ChatColor.GREEN + "10");
					
				}
				
				if(count == 9){
					getPlayer().sendMessage(ChatColor.GREEN + "9");
					
				}
				
				if(count == 8){
					getPlayer().sendMessage(ChatColor.GREEN + "8");
					
				}
				
				if(count == 7){
					getPlayer().sendMessage(ChatColor.GREEN + "7");
					
				}
				
				if(count == 6){
					getPlayer().sendMessage(ChatColor.GREEN + "6");
					
				}
				
				if(count == 5){
					getPlayer().sendMessage(ChatColor.GREEN + "5");
					
				}
				
				if(count == 4){
					getPlayer().sendMessage(ChatColor.GREEN + "4");
					
				}
				
				if(count == 3){
					getPlayer().sendMessage(ChatColor.GREEN + "3");
					
				}
				
				if(count == 2){
					getPlayer().sendMessage(ChatColor.GREEN + "2");
					
				}
				
				if(count == 1){
					getPlayer().sendMessage(ChatColor.GREEN + "1");
					
				}
				
				
				if(count == 0){
					getPlayer().sendMessage(ChatColor.GREEN + "0");
					GameManager.getInstance().confirm.remove(p.getName());
					cancel();
				}
				
				
			}
		}, this.initial, this.after);
		
	}
	
	
	
	

}
