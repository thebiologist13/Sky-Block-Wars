package me.kyle.burnett.SkyBlockWarriors.Commands;

import java.util.HashMap;
import me.kyle.burnett.SkyBlockWarriors.GameManager;
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
				
				if(args[0].equalsIgnoreCase("join")){
					
					
				}
				
				if(args[0].equalsIgnoreCase("leave")){
					
					
				}
				
				if(args[0].equalsIgnoreCase("list")){
					
					
				}
				
				if(args[0].equalsIgnoreCase("listarenas")){
					
					
				}
				
				if(args[0].equalsIgnoreCase("join")){
					
					
				}
				
			}
			
			if(args.length == 2){
				
				if(args[0].equalsIgnoreCase("join")){
					
					if(GameManager.getInstance().getGames().contains(args[1])){
						
						GameManager.getInstance().getGames().get(Integer.parseInt(args[1])).addPlayer(p);
						
						p.sendMessage(ChatColor.GREEN + "Joining game " + ChatColor.GOLD +args[1] + ChatColor.GREEN + ".");
						
						
					}else if(!GameManager.getInstance().getGames().contains(args[1])){
						
						p.sendMessage(ChatColor.RED + "That game does not exist.");
					}
					
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
