package me.kyle.burnett.SkyBlockWarriors.Commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.kyle.burnett.SkyBlockWarriors.Main;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SW implements CommandExecutor{

	
	public HashMap<String, Integer> editing = new HashMap<String, Integer>();
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		
		if(cmd.getName().equalsIgnoreCase("sw") || cmd.getName().equalsIgnoreCase("skyblockw")){
			
			Player p = (Player) sender;
			
			if(args.length == 0){
				
				p.sendMessage(ChatColor.GOLD + "----------" + ChatColor.GREEN +"Sky Block War's Command's" + ChatColor.GOLD  +"----------");
				p.sendMessage(ChatColor.GOLD + "/sw" + ChatColor.GREEN +"Show's this help screen." + ChatColor.GOLD  +"----------");
				p.sendMessage(ChatColor.GOLD + "/sw join [arena number]" + ChatColor.GREEN +"Join's an arena if specified or goes to the lobby." + ChatColor.GOLD  +"----------");
				p.sendMessage(ChatColor.GOLD + "/sw leave" + ChatColor.GREEN +"Leave's the arena and teleports you to the lobby." + ChatColor.GOLD  +"----------");
				return true;
			}
			
			if(args.length == 1){
				
				if(args[0].equalsIgnoreCase("finish")){
					
					
				}
				
				if(args[0].equalsIgnoreCase("create")){
					

				}
				
				if(args[0].equalsIgnoreCase("leave")){
					p.teleport(Main.getLobby());
					//Leave from game.
				}
				
				if(args[0].equalsIgnoreCase("list")){
					
				}
				
				if(args[0].equalsIgnoreCase("arenas")){
					
					List<Integer> arena = new ArrayList<Integer>();
					
					for(int amount = Main.Arena.getInt("Arenas.Amount"); amount > 0; amount--){
						
						arena.add(amount);						
					}
					
					p.sendMessage(ChatColor.GREEN + "--------Arenas--------");
					p.sendMessage(ChatColor.GOLD + arena.toString().replace("[", " ").replace("]", " "));
					
				}
				
				if(args[0].equalsIgnoreCase("board")){
					
				}
				
				if(args[0].equalsIgnoreCase("join")){
					p.teleport(Main.getLobby());
					p.sendMessage(ChatColor.GREEN + "Teleporting to Sky-Block War's lobby. Use /sw join <arena> to join an arena.");
				}
				
				if(args[0].equalsIgnoreCase("chest")){
					p.sendMessage(ChatColor.RED + "Usage: /sw chest [center/side/start]");
				}
				
			}
			
			if(args.length == 2){
				
				if(args[0].equalsIgnoreCase("join")){
					
				}
				
				if(args[0].equalsIgnoreCase("set")){
					
					if(args[1].equalsIgnoreCase("lobby")){
						
						int X = p.getLocation().getBlockX();
						int Y = p.getLocation().getBlockY();
						int Z = p.getLocation().getBlockZ();
						float Yaw = p.getLocation().getYaw();
						float Pitch = p.getLocation().getPitch();
						World world = p.getWorld();
						String name = world.getName();
						
						Main.Config.set("Lobby.X", X);
						Main.Config.set("Lobby.Y", Y);
						Main.Config.set("Lobby.Z", Z);
						Main.Config.set("Lobby.Yaw", Yaw);
						Main.Config.set("Lobby.Pitch", Pitch);
						Main.Config.set("Lobby.World", name);
						
						Main.configManager.saveYamls();
						p.sendMessage(ChatColor.GREEN + "Lobby made succesfully.");
					}
					
				}
				
				if(args[0].equalsIgnoreCase("edit")){
					

				}
				
				if(args[0].equalsIgnoreCase("chest")){

					
				}
				
				if(args[0].equalsIgnoreCase("enable")){

				}
				
				if(args[0].equalsIgnoreCase("disable")){
					

				}
				
				if(args[0].equalsIgnoreCase("save")){
	
					
				}
				
				if(args[0].equalsIgnoreCase("load")){
					

				}
			
			}
			
			if(args.length == 3){
				
			}
			
			return true;
		}
		return false;
	}
}
