package me.kyle.burnett.SkyBlockWarriors.Commands;

import java.util.HashMap;

import me.kyle.burnett.SkyBlockWarriors.Game;
import me.kyle.burnett.SkyBlockWarriors.GameManager;
import me.kyle.burnett.SkyBlockWarriors.Main;

import org.bukkit.ChatColor;
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
					
					if(Main.getInstance().teleportToLobby(p)){
						
						p.sendMessage(ChatColor.GREEN + "Arena not specifed teleporting to lobby.");
					
					} else if(!Main.getInstance().teleportToLobby(p)){
						
						p.sendMessage(ChatColor.RED + "Tryed to teleport to lobby but it was not found. Please tell server staff.");
					}
					
				}
				
				if(args[0].equalsIgnoreCase("leave")){
					
					
				}
				
				if(args[0].equalsIgnoreCase("list")){
					
					
				}
				
				if(args[0].equalsIgnoreCase("listarenas")){
					
					
				}
				
				if(args[0].equalsIgnoreCase("join")){
					
					
				}
				
				return true;
			}
			
			if(args.length == 2){
				
				if(args[0].equalsIgnoreCase("join")){
					
					if(GameManager.getInstance().getGames().contains(args[1])){
						
						Game game = GameManager.getInstance().getGames().get(Integer.parseInt(args[1]));
						
							if(game.state.equals(Game.ArenaState.WAITING)){
								
								game.addPlayer(p);
								
								p.sendMessage(ChatColor.GREEN + "Joining game " + ChatColor.GOLD +args[1] + ChatColor.GREEN + ".");
								
							}else if(game.state != Game.ArenaState.WAITING){
								
								p.sendMessage(ChatColor.RED + "Can not join the arena because it is " + game.state.toString());
							}
	
					}else if(!GameManager.getInstance().getGames().contains(args[1])){
						
						p.sendMessage(ChatColor.RED + "That game does not exist.");
					}
					return true;
				}
				
				if(args[0].equalsIgnoreCase("set")){
					
					if(args[1].equalsIgnoreCase("lobby")){
						
						Main.getInstance().setLobby(p);
						p.sendMessage(ChatColor.GREEN + "Lobby set succesfully.");
					}
					
				}
				
				if(args[0].equalsIgnoreCase("edit")){
					
					if(GameManager.getInstance().getGames().contains(args[1])){
						
						GameManager.getInstance().getGames().get(Integer.parseInt(args[1])).addEditer(p);
						GameManager.getInstance().getGames().get(Integer.parseInt(args[1])).setState(Game.ArenaState.EDITING);
						
						p.sendMessage(ChatColor.GREEN + "Now editing arena " + ChatColor.GOLD + args[1] + ChatColor.GREEN + ".");
						
					}else if(!GameManager.getInstance().getGames().contains(args[1])){
						
						p.sendMessage(ChatColor.RED + "That arena does not exist.");
					}
					

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
			
				return true;
			}
			
			if(args.length == 3){
				
				return true;
			}
			
			return true;
		}
		return false;
	}
	
	public static enum ArenaState {
		
		DISABLED, INGAME, STARTING, RESETING, WAITING, FINISHING, EDITING, LOADING, FULL, OTHER
	}
}
