package me.kyle.burnett.SkyBlockWarriors.Commands;

import java.io.IOException;

import me.kyle.burnett.SkyBlockWarriors.ArenaState;
import me.kyle.burnett.SkyBlockWarriors.ChestType;
import me.kyle.burnett.SkyBlockWarriors.Game;
import me.kyle.burnett.SkyBlockWarriors.GameManager;
import me.kyle.burnett.SkyBlockWarriors.Main;
import me.kyle.burnett.SkyBlockWarriors.Events.PlayerLeaveArenaEvent;
import me.kyle.burnett.SkyBlockWarriors.Utils.WorldEditUtility;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.data.DataException;

public class SW implements CommandExecutor{

	
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
					
					return true;
				}
				
				if(args[0].equalsIgnoreCase("leave")){
					
					if(GameManager.getInstance().isPlayerInGame(p)){
						
						int game = GameManager.getInstance().getPlayerGame(p).getGameID();
						
						PlayerLeaveArenaEvent event = new PlayerLeaveArenaEvent(p, GameManager.getInstance().getPlayerGame(p));
						
						Bukkit.getServer().getPluginManager().callEvent(event);
					
						GameManager.getInstance().leaveGame(p);
						
						p.sendMessage(ChatColor.GREEN + "You have left the arena.");
												
						GameManager.getInstance().getGames().get(game).broadCastGame(ChatColor.GOLD +"Player " + GameManager.getInstance().getGames().get(game).getTeamColor(p) + p.getName() + ChatColor.GOLD + " has left the game.");
						
					}else if(!GameManager.getInstance().isPlayerInGame(p)){
						
						p.sendMessage(ChatColor.RED + "You are not in a game.");
					}
					
					return true;
				}
				
				if(args[0].equalsIgnoreCase("list")){
						
					if(GameManager.getInstance().isPlayerInGame(p)){
						
						GameManager.getInstance().getPlayerGame(p).getPlayersAsList();
					
					}else if(!GameManager.getInstance().isPlayerInGame(p)){
						p.sendMessage(ChatColor.RED + "You are not in a game.");
					}
				}
				
				if(args[0].equalsIgnoreCase("listgames")){
					
					String arenas = GameManager.getInstance().listGames();
					
					p.sendMessage(ChatColor.GOLD + "Arena List:");
					p.sendMessage(ChatColor.GRAY + arenas);
				}
				
				if(args[0].equalsIgnoreCase("create")){
					
					if(WorldEditUtility.getInstance().doesSelectionExist(p)){
						
						int arena = GameManager.getInstance().createGame(p);
						p.sendMessage(ChatColor.GREEN + "Arena " + ChatColor.GOLD + arena + ChatColor.GREEN + " created.");
					
					}else if(!WorldEditUtility.getInstance().doesSelectionExist(p)){
						p.sendMessage(ChatColor.RED + "Please make a selection of the arena first.");
					}
				
					//p.sendMessage(ChatColor.GREEN + "Arena " + ChatColor.GOLD + GameManager.getInstance().createGame() + ChatColor.GREEN + " has been created.");
				}
				
				if(args[0].equalsIgnoreCase("confirm")){
					
					if(GameManager.getInstance().getConfirming().containsKey(p.getName())){
						
						GameManager.getInstance().overrideArena(p, GameManager.getInstance().getConfirming().get(p.getName()));
						
					}else if(!GameManager.getInstance().getConfirming().containsKey(p.getName())){
						
						p.sendMessage(ChatColor.RED + "You are not waiting to confirm anything.");
					}
				}
				
				if(args[0].equalsIgnoreCase("setlobby")){
					
					Main.getInstance().setLobby(p);
					p.sendMessage(ChatColor.GREEN + "Lobby set succesfully.");
					
				}
				
				return true;
			}
			
			if(args.length == 2){
				
				if(args[0].equalsIgnoreCase("create")){
					
					if(GameManager.getInstance().isInteger(args[1])){
						
						int id = Integer.parseInt(args[1]);
						
						if(!(id > GameManager.getInstance().getArenaAmount())){
							
							p.sendMessage(ChatColor.RED + "You are away to override a previous arena. Do '/sw confirm' to confirm this action.");
							
							GameManager.getInstance().getConfirming().put(p.getName(), id);
							
						
						}else if(id > GameManager.getInstance().getArenaAmount()){
							
							p.sendMessage(ChatColor.RED + "That number is bigger than your amount of arenas. Use '/sw create' to add arenas.");
						}
					}
				
				}
				
				if(args[0].equalsIgnoreCase("join")){
					
					if(GameManager.getInstance().checkGameByID(Integer.parseInt(args[1]))){
						
						Game game = GameManager.getInstance().getGameByID(Integer.parseInt(args[1]));
						
							if(game.state.equals(ArenaState.WAITING)){
								
								game.addPlayer(p);
																								
							}else if(game.state != ArenaState.WAITING){
								
								p.sendMessage(ChatColor.RED + "Can not join the arena because it is " + game.getState().toString());
							}
	
					}else if(GameManager.getInstance().checkGameByID(Integer.parseInt(args[1]))){
						
						p.sendMessage(ChatColor.RED + "That game does not exist.");
					}
					return true;
				}
				
				if(args[0].equalsIgnoreCase("edit")){
					
					if(GameManager.getInstance().checkGameByID(Integer.parseInt(args[1]))){
						
						GameManager.getInstance().addEditor(p, Integer.parseInt(args[1]));
						
						GameManager.getInstance().getGames().get(Integer.parseInt(args[1])).setState(ArenaState.GETTING_EDITED);
						
						p.sendMessage(ChatColor.GREEN + "Now editing arena " + ChatColor.GOLD + args[1] + ChatColor.GREEN + ".");
						
					}else if(GameManager.getInstance().checkGameByID(Integer.parseInt(args[1]))){
						
						p.sendMessage(ChatColor.RED + "That arena does not exist.");
					}
					

				}
				
				if(args[0].equalsIgnoreCase("chest")){
					
					if(GameManager.getInstance().isEditing(p)){
						
						if(WorldEditUtility.getInstance().doesSelectionExist(p)){
						
							if(WorldEditUtility.getInstance().isChest(p)){
								
								Location loc = WorldEditUtility.getInstance().getChestLocation(p);
							
								if(args[1].equalsIgnoreCase("side")){
									
									GameManager.getInstance().getPlayerGame(p).addChest(ChestType.SIDE, loc);
								
									p.sendMessage(ChatColor.GREEN + "You have added a " + ChatColor.GOLD + "side " + ChatColor.GREEN + "chest to arena " + ChatColor.GOLD + GameManager.getInstance().getPlayerEditing(p));
									
								}else if(args[1].equalsIgnoreCase("center")){
									
									GameManager.getInstance().getPlayerGame(p).addChest(ChestType.CENTER, loc);
									
									p.sendMessage(ChatColor.GREEN + "You have added a " + ChatColor.GOLD + "center " + ChatColor.GREEN + "chest to arena " + ChatColor.GOLD + GameManager.getInstance().getPlayerEditing(p));
									
								}else if(args[1].equalsIgnoreCase("spawn")){
								
									GameManager.getInstance().getPlayerGame(p).addChest(ChestType.SPAWN, loc);
									
									p.sendMessage(ChatColor.GREEN + "You have added a " + ChatColor.GOLD + "spawn " + ChatColor.GREEN + "chest to arena " + ChatColor.GOLD + GameManager.getInstance().getPlayerEditing(p));
									
								}
								
							}else if(!WorldEditUtility.getInstance().isChest(p)){
								
								p.sendMessage(ChatColor.RED + "Your selection is either more than one block or is not a chest.");
							}
							
						}else if(!WorldEditUtility.getInstance().doesSelectionExist(p)){
							
							p.sendMessage(ChatColor.RED + "You do not have a selection of a chest.");
						}
						
					}else if(!GameManager.getInstance().isEditing(p)){
						p.sendMessage(ChatColor.RED  + "You are not editing an arena.");
					}
					
				}
				
				if(args[0].equalsIgnoreCase("enable")){
					
					if(GameManager.getInstance().checkGameByID(Integer.parseInt(args[1]))){
						
						if(!GameManager.getInstance().isEnabled(Integer.parseInt(args[1]))){
							
							GameManager.getInstance().enableGame(Integer.parseInt(args[1]));
							
							p.sendMessage(ChatColor.GREEN + "You enabled arena " + ChatColor.GOLD + args[1] + ChatColor.GREEN + ".");
							
						}else if(GameManager.getInstance().isEnabled(Integer.parseInt(args[1]))){
							
							p.sendMessage(ChatColor.RED + "Arena is already enabled.");
						}
					
					}else if(GameManager.getInstance().checkGameByID(Integer.parseInt(args[1]))){
						
						p.sendMessage(ChatColor.RED +"That is not an arena.");
					}

				}
				
				if(args[0].equalsIgnoreCase("disable")){
					
					if(GameManager.getInstance().checkGameByID(Integer.parseInt(args[1]))){
						
						if(GameManager.getInstance().isEnabled(Integer.parseInt(args[1]))){
							
							GameManager.getInstance().disableGame(Integer.parseInt(args[1]));
							
							p.sendMessage(ChatColor.GREEN + "You disabled arena " + ChatColor.GOLD + args[1] + ChatColor.GREEN + ".");
							
						}else if(!GameManager.getInstance().isEnabled(Integer.parseInt(args[1]))){
							
							p.sendMessage(ChatColor.RED + "Arena is already disabled.");
						}
					
					}else if(GameManager.getInstance().checkGameByID(Integer.parseInt(args[1]))){
						
						p.sendMessage(ChatColor.RED +"That is not an arena.");
					}
					
				}
				
				if(args[0].equalsIgnoreCase("save")){
	
					if(GameManager.getInstance().checkGameByID(Integer.parseInt(args[1]))){	
						
						WorldEditUtility.getInstance().resaveArena(Integer.parseInt(args[1]), p);
						
						p.sendMessage(ChatColor.GREEN + "Arena " + ChatColor.GOLD + args[1] + ChatColor.GREEN + "has been saved.");

					
					}else if(GameManager.getInstance().checkGameByID(Integer.parseInt(args[1]))){
						
						p.sendMessage(ChatColor.RED + "That arena does not exist.");
					}
				}
				
				if(args[0].equalsIgnoreCase("load")){
					
					if(GameManager.getInstance().checkGameByID(Integer.parseInt(args[1]))){
						
						try {
							
							WorldEditUtility.getInstance().loadIslandSchematic(Integer.parseInt(args[1]));
						
						} catch (NumberFormatException| MaxChangedBlocksException | DataException| IOException e) {
							e.printStackTrace();
						}
						
						p.sendMessage(ChatColor.GREEN + "Arena " + ChatColor.GOLD + args[1] + ChatColor.GREEN + "has been loaded.");
						
					}else if(GameManager.getInstance().checkGameByID(Integer.parseInt(args[1]))){
						
						p.sendMessage(ChatColor.RED + "That arena does not exist.");
					}

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
	
}
