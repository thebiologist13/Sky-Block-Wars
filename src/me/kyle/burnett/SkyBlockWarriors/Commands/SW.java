package me.kyle.burnett.SkyBlockWarriors.Commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.kyle.burnett.SkyBlockWarriors.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.data.DataException;

public class SW implements CommandExecutor{
	
/*	
    String setlobby = "setlobby";
    String join = "join";
	String leave = "leave";
	String list = "list";
	String team = "team";
	String start = "start";
	String stop = "stop";
	String rebuild = "rebuild";
	String board = "board";
	String vote = "vote";
	String arenas = "arenas";

*/
	
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
					if(Main.arenaAPI.createArena(p)){
						p.sendMessage(ChatColor.GREEN + "Arena " + ChatColor.GOLD + Main.Arena.getInt("Amount") + ChatColor.GREEN + " created successfully.");
						return true;
					}
					p.sendMessage(ChatColor.RED + "You do not have a world edit selection of the arena.");
					return true;
				}
				
				if(args[0].equalsIgnoreCase("leave")){
					p.teleport(Main.getLobby());
					Main.playerAPI.removeFromAll(p, ChatColor.RED + "You have left the arena.");
				}
				
				if(args[0].equalsIgnoreCase("list")){
					
					int game = Main.gameAPI.getPlayerGame(p);
					
					p.sendMessage(ChatColor.GREEN + "--------Player's In Your Game--------");
					p.sendMessage(ChatColor.RED + Main.gameManager.games.get(game).toString().replace("[", " ").replace("]", " "));
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
					
					if(p.hasPermission("skyblockwars.join")){
						
						if(Main.arenaAPI.doesArenaExist(Integer.parseInt(args[1]))){
							
							if(Main.arenaAPI.isArenaEnabled(Integer.parseInt(args[1]))){
								
									if(!Main.arenaAPI.isArenaInUse(Integer.parseInt(args[1]))){
										
										Main.playerAPI.addPlayerToGame(Integer.parseInt(args[1]), p);
										p.sendMessage(ChatColor.GREEN + "You joined " + ChatColor.GOLD + Integer.parseInt(args[1]) + ".");
										
									}else if(Main.arenaAPI.isArenaInUse(Integer.parseInt(args[1]))){
										
										p.sendMessage(ChatColor.RED + "This arena has already started.");
									}
							}else if(!Main.arenaAPI.isArenaEnabled(Integer.parseInt(args[1]))){
								
								p.sendMessage(ChatColor.RED + "This arena has been disabled.");
							}
						}else if(!Main.arenaAPI.doesArenaExist(Integer.parseInt(args[1]))){
							
							p.sendMessage(ChatColor.RED + "Arena does not exist. Type /sbw arenas - for a list of arenas");
						}
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
					
					if(Main.arenas.containsKey(Integer.parseInt(args[1]))){
						
						if(Main.arenas.get(Integer.parseInt(args[1]))){
							
							editing.put(p.getName(), Integer.parseInt(args[1]));
							
							p.sendMessage(ChatColor.GREEN + "You are now editing arena " + ChatColor.GOLD + args[1]);
							
						}else if(!Main.arenas.get(Integer.parseInt(args[1]))){
							
							p.sendMessage(ChatColor.RED + "That arena is disabled. Use /sw enable <arena> to edit it.");
						}
						
					}else if(!Main.arenas.containsKey(Integer.parseInt(args[1]))){
						
						p.sendMessage(ChatColor.RED + "That arena does not exist.");
					}
				}
				
				if(args[0].equalsIgnoreCase("chest")){
					
					if(this.editing.containsKey(p.getName())){
						
						if(p.getLocation().getBlock().getType() == Material.CHEST){
					
							if(args[1].equalsIgnoreCase("center")){
								
								Main.arenaAPI.addChest(Main.playerAPI.arenaPlayerIsEditing(p), "center", p);
								
								p.sendMessage(ChatColor.GREEN + "Center chest added for arena " + ChatColor.GOLD + Main.playerAPI.arenaPlayerIsEditing(p));
								
							}
							
							else if(args[1].equalsIgnoreCase("side")){
								
								Main.arenaAPI.addChest(Main.playerAPI.arenaPlayerIsEditing(p), "side", p);
			
								p.sendMessage(ChatColor.GREEN + "Side chest added for arena " + ChatColor.GOLD + Main.playerAPI.arenaPlayerIsEditing(p));
								
							}
							
							else if(args[1].equalsIgnoreCase("start")){
							
								Main.arenaAPI.addChest(Main.playerAPI.arenaPlayerIsEditing(p), "start", p);
								
								p.sendMessage(ChatColor.GREEN + "Start chest added for arena " + ChatColor.GOLD + Main.playerAPI.arenaPlayerIsEditing(p));
								
							}
							
							else{
								p.sendMessage(ChatColor.RED + "That is not a type of chest. Usage: /sw chest [center/side/start]");
							}
						}else if(p.getLocation().getBlock().getType() != Material.CHEST){
							p.sendMessage(ChatColor.RED + "You are not standing on a chest.");
						}
					} else if(!Main.playerAPI.playerIsEditing(p)){
						
						p.sendMessage(ChatColor.RED + "You are not editing an arena");
					}
					
				}
				
				if(args[0].equalsIgnoreCase("enable")){
						
					if(Main.arenas.containsKey(Integer.parseInt(args[1]))){
						
						if(!Main.arenas.get(Integer.parseInt(args[1]))){
							
							Main.arenaAPI.setEnabled(Integer.parseInt(args[1]));
							
						}else if(!Main.arenas.get(Integer.parseInt(args[1]))){
							
							p.sendMessage(ChatColor.RED + "That arena is already enabled.");
						}
						
					}else if(!Main.arenas.containsKey(Integer.parseInt(args[1]))){
						
						p.sendMessage(ChatColor.RED + "That arena does not exist.");
					}
				}
				
				if(args[0].equalsIgnoreCase("disable")){
					
						if(Main.arenas.containsKey(Integer.parseInt(args[1]))){
							
							if(Main.arenas.get(Integer.parseInt(args[1]))){
								
								Main.arenaAPI.setDisabled(Integer.parseInt(args[1]));
								
							}else if(!Main.arenas.get(Integer.parseInt(args[1]))){
								
								p.sendMessage(ChatColor.RED + "That arena is already disabled.");
							}
							
						}else if(!Main.arenas.containsKey(Integer.parseInt(args[1]))){
							
							p.sendMessage(ChatColor.RED + "That arena does not exist.");
						}
				}
				
				if(args[0].equalsIgnoreCase("save")){
					
					int arena = Integer.parseInt(args[1]);
					
					if(Main.arenaAPI.doesArenaExist(arena)){
						Main.worldedit.saveArena(p, arena);
						p.sendMessage(ChatColor.GREEN + "Arena " + ChatColor.GOLD + arena + ChatColor.GREEN + " saved.");
					}
					
				}
				
				if(args[0].equalsIgnoreCase("load")){
					
					int arena = Integer.parseInt(args[1]);
					System.out.println("Loaded 1.");
					if(Main.arenaAPI.doesArenaExist(arena)){
						System.out.println("Loaded 2.");
						try {
							Main.worldedit.loadIslandSchematic(Bukkit.getWorld(Main.Arena.getString(arena + ".World")), Main.arenaAPI.getArenaFile(arena), arena);
							System.out.println("Loaded 3.");
						} catch (MaxChangedBlocksException e) {
							
							e.printStackTrace();
							
						} catch (DataException e) {
							
							e.printStackTrace();
							
						} catch (IOException e) {
							
							e.printStackTrace();
						}
					}
				}
			
			}
			
			if(args.length == 3){
				
			}
			
			return true;
		}
		return false;
	}
}
