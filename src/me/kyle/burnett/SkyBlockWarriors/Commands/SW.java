package me.kyle.burnett.SkyBlockWarriors.Commands;

import java.io.IOException;

import me.kyle.burnett.SkyBlockWarriors.ArenaState;
import me.kyle.burnett.SkyBlockWarriors.ChestType;
import me.kyle.burnett.SkyBlockWarriors.Game;
import me.kyle.burnett.SkyBlockWarriors.GameManager;
import me.kyle.burnett.SkyBlockWarriors.Main;
import me.kyle.burnett.SkyBlockWarriors.Utils.WorldEditUtility;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.data.DataException;

public class SW implements CommandExecutor {


    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        if (cmd.getName().equalsIgnoreCase("sw") || cmd.getName().equalsIgnoreCase("skyblockw")) {

            Player p = (Player) sender;

            GameManager gm = GameManager.getInstance();

            String prefix = ChatColor.GOLD + "[" + ChatColor.BLUE + "SBW" + ChatColor.GOLD + "]";
            String noperms = ChatColor.RED + "You do not have permission to do this.";
            String perm = prefix + noperms;


            if (p.hasPermission("skyblockwars.user")) {
                if (args.length == 0) {

                    p.sendMessage(ChatColor.GOLD + "----------" + ChatColor.GREEN + "Sky Block War's Command's" + ChatColor.GOLD + "----------");
                    p.sendMessage(ChatColor.GOLD + "/sw" + ChatColor.GREEN + "Show's this help screen.");
                    p.sendMessage(ChatColor.GOLD + "/sw join [arena number]" + ChatColor.GREEN + "Join's an arena if specified or goes to the lobby.");
                    p.sendMessage(ChatColor.GOLD + "/sw leave" + ChatColor.GREEN + "Leave's the arena and teleports you to the lobby.");

                    return true;
                }

                if (args.length == 1) {

                    if (args[0].equalsIgnoreCase("join")) {

                        if (p.hasPermission("skyblockwars.join")) {

                            if (gm.isPlayerInGame(p)) {

                                if (Main.getInstance().teleportToLobby(p)) {

                                    p.sendMessage(prefix + ChatColor.GREEN + "Arena not specifed teleporting to lobby.");

                                } else if (!Main.getInstance().teleportToLobby(p)) {

                                    p.sendMessage(prefix + ChatColor.RED + "Tryed to teleport to lobby but it was not found. Please tell server staff.");
                                }
                            } else if (!gm.isPlayerInGame(p)) {
                                p.sendMessage(prefix + ChatColor.RED + "You have already joined.");
                            }
                        } else if (!p.hasPermission("skyblockwars.join")) {
                            p.sendMessage(perm);
                        }

                        return true;
                    }

                    else if (args[0].equalsIgnoreCase("leave")) {

                        if (p.hasPermission("skyblockwars.user")) {

                            if (gm.isPlayerInGame(p)) {
                                
                                Game game = gm.getPlayerGame(p);
                                
                                if(gm.hasPlayerGameStarted(p)){
                                                       
                                    p.sendMessage(prefix + ChatColor.GREEN + "You have left the arena.");
                                    
                                    game.removeFromGame(p, false, false, false);
                                        
                                }else if(!gm.hasPlayerGameStarted(p)){
                                    
                                    p.sendMessage(prefix + ChatColor.GREEN + "You have left the arena.");
                                    
                                    game.removeFromGame(p, false, false, true);                                
    
                                }
                                
                            } else if (!gm.isPlayerInGame(p)) {

                                p.sendMessage(prefix + ChatColor.RED + "You are not in a game.");
                            }
                        } else if (!p.hasPermission("skyblockwars.join")) {
                            p.sendMessage(perm);
                        }
                        return true;
                    }

                    else if (args[0].equalsIgnoreCase("list")) {

                        if (p.hasPermission("skyblockwars.list")) {

                            if (gm.isPlayerInGame(p)) {

                                Game g = gm.getPlayerGame(p);
                                if (g.getState().equals(ArenaState.IN_GAME)) {
                                    p.sendMessage(prefix + ChatColor.GOLD + "Player's Alive:");
                                    p.sendMessage(gm.getPlayerGame(p).getPlayersAsList());
                                } else if (!g.getState().equals(ArenaState.IN_GAME)) {
                                    p.sendMessage(prefix + ChatColor.RED + "The game has not started yet.");
                                }

                            } else if (!gm.isPlayerInGame(p)) {
                                p.sendMessage(prefix + ChatColor.RED + "You are not in a game.");
                            }
                        } else if (!p.hasPermission("skyblockwars.list")) {
                            p.sendMessage(perm);
                        }
                        return true;
                    }

                    else if (args[0].equalsIgnoreCase("listgames")) {

                        if (p.hasPermission("skyblockwars.listgames")) {
                            String arenas = gm.listGames();

                            p.sendMessage(prefix + ChatColor.GOLD + "Arena List:");
                            p.sendMessage(ChatColor.GRAY + arenas);

                        } else if (!p.hasPermission("skyblockwars.listgames")) {
                            p.sendMessage(perm);
                        }
                        return true;
                    }

                    else if (args[0].equalsIgnoreCase("create")) {

                        if (p.hasPermission("skyblockwars.create")) {
                            if (WorldEditUtility.getInstance().doesSelectionExist(p)) {

                                int arena = gm.createGame(p);
                                p.sendMessage(prefix + ChatColor.GREEN + "Arena " + ChatColor.GOLD + arena + ChatColor.GREEN + " created. Once you have edited it use '" + ChatColor.GOLD + "/sw activate arena" + ChatColor.GREEN + "' to allow people to join.");

                            } else if (!WorldEditUtility.getInstance().doesSelectionExist(p)) {
                                p.sendMessage(prefix + ChatColor.RED + "Please make a selection of the arena first.");
                            }
                        } else if (!p.hasPermission("skyblockwars.create")) {
                            p.sendMessage(perm);
                        }
                        return true;
                    }

                    else if (args[0].equalsIgnoreCase("confirm")) {

                        if (p.hasPermission("skyblockwars.confirm")) {
                            if (gm.getConfirming().containsKey(p.getName())) {

                                gm.overrideArena(p, gm.getConfirming().get(p.getName()));

                            } else if (!gm.getConfirming().containsKey(p.getName())) {

                                p.sendMessage(prefix + ChatColor.RED + "You are not waiting to confirm anything.");
                            }
                        } else if (!p.hasPermission("skyblockwars.confirm")) {
                            p.sendMessage(perm);
                        }
                        return true;
                    }

                    else if (args[0].equalsIgnoreCase("setlobby")) {
                        if (p.hasPermission("skyblockwars.setlobby")) {
                            Main.getInstance().setLobby(p);
                            p.sendMessage(prefix + ChatColor.GREEN + "Lobby set succesfully.");
                        } else if (!p.hasPermission("skyblockwars.setlobby")) {
                            p.sendMessage(perm);
                        }
                        return true;
                    }

                    else if (args[0].equalsIgnoreCase("finish")) {
                        if (p.hasPermission("skyblockwars.edit")) {
                            if (gm.isEditing(p)) {

                                p.sendMessage(prefix + ChatColor.GREEN + "Finished editing arena " + ChatColor.GOLD + gm.getEditing().get(p.getName()) + ChatColor.GREEN + ".");
                                WorldEditUtility.getInstance().resaveArena(gm.getPlayerEditing(p));
                                gm.removeEditor(p);

                            } else if (!gm.isEditing(p)) {

                                p.sendMessage(prefix + ChatColor.RED + "You are not editing an arena.");
                            }
                        } else if (!p.hasPermission("skyblockwars.edit")) {
                            p.sendMessage(perm);
                        }
                        return true;
                    }

                    else if (args[0].equalsIgnoreCase("vote")) {
                        
                        if (p.hasPermission("skyblockwars.vote")) {
                            
                            if (gm.isPlayerInGame(p)) {

                                if(!gm.hasPlayerGameStarted(p)){
                                
                                    if (!gm.getPlayerGame(p).hasVoted(p)) {
                                                
                                            p.sendMessage(prefix + ChatColor.GREEN + "Voted!");
                                            gm.getPlayerGame(p).addVoted(p);                                            
                                            gm.getPlayerGame(p).broadCastGame(prefix + p.getDisplayName() + ChatColor.GREEN + " has voted to start.");
                                        
                                    } else if (gm.getPlayerGame(p).hasVoted(p)) {
    
                                        p.sendMessage(prefix + ChatColor.RED + "You have already voted.");
                                    }
                                }else if(gm.hasPlayerGameStarted(p)){
                                    p.sendMessage(prefix + ChatColor.RED + "You can not vote when the game has started.");
                                }
                                
                            } else if (!gm.isPlayerInGame(p)) {
                                p.sendMessage(prefix + ChatColor.RED + "You are not in an arena.");
                            }
                        } else if (!p.hasPermission("skyblockwars.vote")) {
                            p.sendMessage(perm);
                        }
                        return true;
                    }

                    p.sendMessage(prefix + ChatColor.RED + "Unknown command. Use '/sw help' for a list of commands.");

                    return true;
                }

                if (args.length == 2) {

                    if (args[0].equalsIgnoreCase("setspawn")) {
                        if (p.hasPermission("skyblockwars.setspawn")) {
                            if (gm.isEditing(p)) {

                                if (args[1].equalsIgnoreCase("red")) {

                                    Game g = gm.getGameEditing(p);

                                    g.addRedSpawn(p);

                                    p.sendMessage(prefix + ChatColor.RED + "Red " + ChatColor.GREEN + "team spawn set for arena " + ChatColor.GOLD + g.getGameID() + ChatColor.GREEN + ".");

                                } else if (args[1].equalsIgnoreCase("blue")) {

                                    Game g = gm.getGameEditing(p);

                                    g.addBlueSpawn(p);

                                    p.sendMessage(prefix + ChatColor.BLUE + "Blue " + ChatColor.GREEN + "team spawn set for arena " + ChatColor.GOLD + g.getGameID() + ChatColor.GREEN + ".");

                                } else if (args[1].equalsIgnoreCase("green")) {

                                    Game g = gm.getGameEditing(p);

                                    g.addGreenSpawn(p);

                                    p.sendMessage(prefix + ChatColor.GREEN + "Green team spawn set for arena " + ChatColor.GOLD + g.getGameID() + ChatColor.GREEN + ".");

                                } else if (args[1].equalsIgnoreCase("yellow")) {

                                    Game g = gm.getGameEditing(p);

                                    g.addYellowSpawn(p);

                                    p.sendMessage(prefix + ChatColor.YELLOW + "Yellow " + ChatColor.GREEN + "team spawn set for arena " + ChatColor.GOLD + g.getGameID() + ChatColor.GREEN + ".");

                                }
                            } else if (!gm.isEditing(p)) {

                                p.sendMessage(prefix + ChatColor.RED + "You are not editing an arena.");
                            }
                        } else if (!p.hasPermission("skyblockwars.setspawn")) {
                            p.sendMessage(perm);
                        }
                        return true;
                    }

                    else if (args[0].equalsIgnoreCase("create")) {
                        if (p.hasPermission("skyblockwars.create.override")) {
                            if (gm.isInteger(args[1])) {

                                int id = Integer.parseInt(args[1]);

                                if (!(id > gm.getArenaAmount())) {

                                    p.sendMessage(prefix + ChatColor.RED + "You are away to override a previous arena. Do '/sw confirm' to confirm this action.");

                                    gm.getConfirming().put(p.getName(), id);


                                } else if (id > gm.getArenaAmount()) {

                                    p.sendMessage(prefix + ChatColor.RED + "That number is bigger than your amount of arenas. Use '/sw create' to add arenas.");
                                }
                            }
                        } else if (!p.hasPermission("skyblockwars.create.override")) {
                            p.sendMessage(perm);
                        }
                        return true;
                    }

                    else if (args[0].equalsIgnoreCase("join")) {          
                        
                        if (p.hasPermission("skyblockwars.join." + args[1]) || p.hasPermission("skyblockwars.join")) {
                            
                            if (gm.checkGameByID(Integer.parseInt(args[1]))) {                         
                                
                                if(!gm.isPlayerInGame(p)){
                                    
                                    if (gm.isActive(Integer.parseInt(args[1]))) {
    
                                        Game game = gm.getGameByID(Integer.parseInt(args[1]));
    
                                        if (game.getState().equals(ArenaState.WAITING)) {
    
                                            game.addPlayer(p);                                            
    
                                        } else if (game.getState() != ArenaState.WAITING) {
    
                                            p.sendMessage(prefix + ChatColor.RED + "Can not join the arena because it is " + game.getState().toString().toLowerCase() + ".");
                                        }
                                        
                                    } else if (!gm.isActive(Integer.parseInt(args[1]))) {
                                        p.sendMessage(prefix + ChatColor.RED + "That arena is disabled.");
                                    }
                                    
                                }else if(gm.isPlayerInGame(p)){
                                    p.sendMessage(prefix + ChatColor.RED + "You are already in a game.");
                                }
                                
                            } else if (!gm.checkGameByID(Integer.parseInt(args[1]))) {
    
                                p.sendMessage(prefix + ChatColor.RED + "That game does not exist.");
                            }
                            
                        } else if (p.hasPermission("skyblockwars.join." + args[1]) || p.hasPermission("skyblockwars.join")) {
                            p.sendMessage(perm);
                        }
                        return true;
                    }

                    else if (args[0].equalsIgnoreCase("edit")) {

                        if (p.hasPermission("skyblockwars.edit")) {
                            if (gm.checkGameByID(Integer.parseInt(args[1]))) {

                                gm.addEditor(p, Integer.parseInt(args[1]));

                                gm.getGameByID(Integer.parseInt(args[1])).setState(ArenaState.GETTING_EDITED);

                                p.sendMessage(prefix + ChatColor.GREEN + "Now editing arena " + ChatColor.GOLD + args[1] + ChatColor.GREEN + ".");

                            } else if (gm.checkGameByID(Integer.parseInt(args[1]))) {

                                p.sendMessage(prefix + ChatColor.RED + "That arena does not exist.");
                            }
                        } else if (p.hasPermission("skyblockwars.edit")) {
                            p.sendMessage(perm);
                        }
                        return true;
                    }

                    else if (args[0].equalsIgnoreCase("addchest")) {
                        if (p.hasPermission("skyblockwars.addchest")) {
                            if (gm.isEditing(p)) {

                                if (WorldEditUtility.getInstance().doesSelectionExist(p)) {

                                    if (WorldEditUtility.getInstance().isChest(p)) {

                                        Location loc = WorldEditUtility.getInstance().getChestLocation(p);

                                        if (loc.equals(null)) {

                                            p.sendMessage(prefix + ChatColor.RED + "An error occured. Please try again.");

                                            return true;
                                        }

                                        if (args[1].equalsIgnoreCase("side")) {

                                            gm.getGameEditing(p).addChest(ChestType.SIDE, loc);

                                            p.sendMessage(prefix + ChatColor.GREEN + "You have added a " + ChatColor.GOLD + "side " + ChatColor.GREEN + "chest to arena " + ChatColor.GOLD + gm.getPlayerEditing(p) + ChatColor.GREEN + ".");

                                        } else if (args[1].equalsIgnoreCase("center")) {

                                            gm.getGameEditing(p).addChest(ChestType.CENTER, loc);

                                            p.sendMessage(prefix + ChatColor.GREEN + "You have added a " + ChatColor.GOLD + "center " + ChatColor.GREEN + "chest to arena " + ChatColor.GOLD + gm.getPlayerEditing(p) + ChatColor.GREEN + ".");

                                        } else if (args[1].equalsIgnoreCase("spawn")) {

                                            gm.getGameEditing(p).addChest(ChestType.SPAWN, loc);

                                            p.sendMessage(prefix + ChatColor.GREEN + "You have added a " + ChatColor.GOLD + "spawn " + ChatColor.GREEN + "chest to arena " + ChatColor.GOLD + gm.getPlayerEditing(p) + ChatColor.GREEN + ".");

                                        }

                                    } else if (!WorldEditUtility.getInstance().isChest(p)) {

                                        p.sendMessage(prefix + ChatColor.RED + "Your selection is either more than one block or is not a chest.");
                                    }

                                } else if (!WorldEditUtility.getInstance().doesSelectionExist(p)) {

                                    p.sendMessage(prefix + ChatColor.RED + "You do not have a selection of a chest.");
                                }

                            } else if (!gm.isEditing(p)) {
                                p.sendMessage(prefix + ChatColor.RED + "You are not editing an arena.");
                            }
                        } else if (p.hasPermission("skyblockwars.addchest")) {
                            p.sendMessage(perm);
                        }
                        return true;
                    }

                    else if (args[0].equalsIgnoreCase("enable")) {

                        if (p.hasPermission("skyblockwars.enable")) {
                            if (gm.checkGameByID(Integer.parseInt(args[1]))) {

                                if (!gm.isEnabled(Integer.parseInt(args[1]))) {

                                    gm.enableGame(Integer.parseInt(args[1]));

                                    p.sendMessage(prefix + ChatColor.GREEN + "You enabled arena " + ChatColor.GOLD + args[1] + ChatColor.GREEN + ".");

                                } else if (gm.isEnabled(Integer.parseInt(args[1]))) {

                                    p.sendMessage(prefix + ChatColor.RED + "Arena is already enabled.");
                                }

                            } else if (gm.checkGameByID(Integer.parseInt(args[1]))) {

                                p.sendMessage(prefix + ChatColor.RED + "That is not an arena.");
                            }
                        } else if (p.hasPermission("skyblockwars.enable")) {
                            p.sendMessage(perm);
                        }
                        return true;
                    }

                    else if (args[0].equalsIgnoreCase("disable")) {
                                            
                        if (p.hasPermission("skyblockwars.disable")) {
                            
                            if (gm.checkGameByID(Integer.parseInt(args[1]))) {
                                
                                Game g = gm.getGameByID(Integer.parseInt(args[1]));

                                if (gm.isEnabled(Integer.parseInt(args[1]))) {

                                    if (gm.isActive(Integer.parseInt(args[1]))) {
                                        
                                        if(g.getState().equals(ArenaState.WAITING)){
                                            
                                           gm.disableGame(Integer.parseInt(args[1]));
                                           p.sendMessage(prefix + ChatColor.GREEN + "Arena " + ChatColor.GOLD + args[1] + ChatColor.GREEN + " has been disabled.");
                                         
                                        }else if(g.getState().equals(ArenaState.GETTING_EDITED)) {
                                            p.sendMessage(prefix + ChatColor.RED + "Could not disable arena " + ChatColor.GOLD + args[1] + ChatColor.GREEN + " because it is being edited.");
                                       
                                        }else if(g.getState().equals(ArenaState.STARTING)) {
                                            g.endGameStarting();
                                            p.sendMessage(prefix + ChatColor.RED + "Could not disable arena " + ChatColor.GOLD + args[1] + ChatColor.GREEN + " because it is being edited.");                                  
                                        }
                                    }else if (!gm.isActive(Integer.parseInt(args[1]))) {
                                        
                                        gm.disableGame(Integer.parseInt(args[1]));
                                        
                                        p.sendMessage(prefix + ChatColor.GREEN + "You disabled arena " + ChatColor.GOLD + args[1] + ChatColor.GREEN + ".");
                                    }
                                } else if (!gm.isEnabled(Integer.parseInt(args[1]))) {

                                    p.sendMessage(prefix + ChatColor.RED + "Arena is already disabled.");
                                }

                            } else if (gm.checkGameByID(Integer.parseInt(args[1]))) {

                                p.sendMessage(prefix + ChatColor.RED + "That is not an arena.");
                            }
                        } else if (p.hasPermission("skyblockwars.disable")) {
                            p.sendMessage(perm);
                        }
                        return true;
                    }

                    else if (args[0].equalsIgnoreCase("activate")) {
                        if (p.hasPermission("skyblockwars.activate")) {
                            if (gm.checkGameByID(Integer.parseInt(args[1]))) {

                                gm.activate(Integer.parseInt(args[1]));
                                p.sendMessage(prefix + ChatColor.GREEN + "Arena " + ChatColor.GOLD + args[1] + ChatColor.GREEN + " has been activated.");

                            } else if (gm.checkGameByID(Integer.parseInt(args[1]))) {
                                p.sendMessage(prefix + ChatColor.RED + "That arena does not exist.");
                            }
                        } else if (p.hasPermission("skyblockwars.activate")) {
                            p.sendMessage(perm);
                        }
                        return true;
                    }

                    if (args[0].equalsIgnoreCase("deactivate")) {
                        
                        if (p.hasPermission("skyblockwars.deactivate")) {
                            
                            if (gm.checkGameByID(Integer.parseInt(args[1]))) {

                                if (gm.isActive(Integer.parseInt(args[1]))) {
                                    
                                    if(gm.getGameByID(Integer.parseInt(args[1])).getState().equals(ArenaState.WAITING)){
                                        
                                        gm.deactivate(Integer.parseInt(args[1]));
                                        
                                        p.sendMessage(prefix + ChatColor.GREEN + "Arena " + ChatColor.GOLD + args[1] + ChatColor.GREEN + " has been deactivated.");
                                    
                                    }else if(gm.getGameByID(Integer.parseInt(args[1])).getState().equals(ArenaState.IN_GAME)){
                                        
                                        p.sendMessage(prefix + ChatColor.GREEN + "Arena " + ChatColor.GOLD + args[1] + ChatColor.GREEN + " will be deactivated after the game is finished.");
                                    }
                                    
                                } else if (!gm.isActive(Integer.parseInt(args[1]))) {
                                    p.sendMessage(prefix + ChatColor.RED + "Arena " + ChatColor.GOLD + args[1] + ChatColor.GREEN + " is already deactivated.");
                                }
                            } else if (gm.checkGameByID(Integer.parseInt(args[1]))) {
                                p.sendMessage(prefix + ChatColor.RED + "That arena does not exist.");
                            }
                        } else if (p.hasPermission("skyblockwars.deactivate")) {
                            p.sendMessage(perm);
                        }
                        return true;
                    }

                    else if (args[0].equalsIgnoreCase("save")) {
                        if (p.hasPermission("skyblockwars.save")) {
                            if (gm.checkGameByID(Integer.parseInt(args[1]))) {

                                WorldEditUtility.getInstance().resaveArena(Integer.parseInt(args[1]));

                                p.sendMessage(prefix + ChatColor.GREEN + "Arena " + ChatColor.GOLD + args[1] + ChatColor.GREEN + " has been saved.");


                            } else if (gm.checkGameByID(Integer.parseInt(args[1]))) {

                                p.sendMessage(prefix + ChatColor.RED + "That arena does not exist.");
                            }
                        } else if (p.hasPermission("skyblockwars.save")) {
                            p.sendMessage(perm);
                        }
                        return true;
                    }

                    else if (args[0].equalsIgnoreCase("load")) {
                        if (p.hasPermission("skyblockwars.load")) {
                            if (gm.checkGameByID(Integer.parseInt(args[1]))) {

                                try {

                                    WorldEditUtility.getInstance().loadIslandSchematic(Integer.parseInt(args[1]));

                                } catch (NumberFormatException | MaxChangedBlocksException | DataException | IOException e) {

                                    e.printStackTrace();
                                }

                                p.sendMessage(prefix + ChatColor.GREEN + "Arena " + ChatColor.GOLD + args[1] + ChatColor.GREEN + " has been loaded.");

                            } else if (gm.checkGameByID(Integer.parseInt(args[1]))) {

                                p.sendMessage(prefix + ChatColor.RED + "That arena does not exist.");
                            }
                        } else if (p.hasPermission("skyblockwars.load")) {
                            p.sendMessage(perm);
                        }
                        return true;
                    }

                    else if (args[0].equalsIgnoreCase("team")) {
                        if (p.hasPermission("skyblockwars.create")) {
                            if (gm.isPlayerInGame(p)) {

                                Game g = gm.getPlayerGame(p);

                                if (g.getState().equals(ArenaState.WAITING)) {

                                    if (args[1].equalsIgnoreCase("red")) {

                                        if (g.isRedAvailable()) {

                                            if (g.isPlayerInTeam(p)) {
                                                g.removeFromTeam(p);
                                            }

                                            g.setTeamRed(p);
                                            p.sendMessage(prefix + ChatColor.GREEN + "Joined team " + ChatColor.RED + "red" + ChatColor.GREEN + ".");

                                        } else if (!g.isRedAvailable()) {
                                            p.sendMessage(prefix + ChatColor.RED + "You can not join red, try joining another team or waiting for the teams to even out.");
                                        }

                                    } else if (args[1].equalsIgnoreCase("blue")) {

                                        if (g.isBlueAvailable()) {

                                            if (g.isPlayerInTeam(p)) {
                                                g.removeFromTeam(p);
                                            }

                                            g.setTeamRed(p);
                                            p.sendMessage(prefix + ChatColor.GREEN + "Joined team " + ChatColor.BLUE + "blue" + ChatColor.GREEN + ".");

                                        } else if (!g.isBlueAvailable()) {
                                            p.sendMessage(prefix + ChatColor.RED + "You can not join" + ChatColor.BLUE + " blue" + ChatColor.RED + ", try joining another team or waiting for the teams to even out.");
                                        }

                                    } else if (args[1].equalsIgnoreCase("green")) {

                                        if (g.isGreenAvailable()) {

                                            if (g.isPlayerInTeam(p)) {
                                                g.removeFromTeam(p);
                                            }

                                            g.setTeamRed(p);
                                            p.sendMessage(prefix + ChatColor.GREEN + "Joined team green.");

                                        } else if (!g.isGreenAvailable()) {
                                            p.sendMessage(prefix + ChatColor.RED + "You can not join" + ChatColor.GREEN + " green" + ChatColor.RED + ", try joining another team or waiting for the teams to even out.");
                                        }

                                    } else if (args[1].equalsIgnoreCase("yellow")) {

                                        if (g.isYellowAvailable()) {

                                            if (g.isPlayerInTeam(p)) {
                                                g.removeFromTeam(p);
                                            }

                                            g.setTeamRed(p);
                                            p.sendMessage(prefix + ChatColor.GREEN + "Joined team " + ChatColor.YELLOW + "yellow" + ChatColor.GREEN + ".");

                                        } else if (!g.isYellowAvailable()) {
                                            p.sendMessage(prefix + ChatColor.RED + "You can not join" + ChatColor.YELLOW + " yellow" + ChatColor.RED + ", try joining another team or waiting for the teams to even out.");
                                        }
                                    }
                                } else if (!g.getState().equals(ArenaState.WAITING)) {
                                    p.sendMessage(prefix + ChatColor.RED + "You can not change team when the game is starting or has already started.");
                                }

                            } else if (!gm.isPlayerInGame(p)) {
                                p.sendMessage(prefix + ChatColor.RED + "You are not in a game.");
                            }
                        } else if (p.hasPermission("skyblockwars.team")) {
                            p.sendMessage(perm);
                        }
                        return true;
                    }
                    p.sendMessage(prefix + ChatColor.RED + "Unknown command. Use '/sw help' for a list of commands.");
                    return true;
                }

                if (args.length >= 3) {
                    p.sendMessage(prefix + ChatColor.RED + "Unknown command. Use '/sw help' for a list of commands.");
                    return true;
                }
            } else if (!p.hasPermission("skyblockwars.user")) {
                p.sendMessage(perm);
            }
            return true;
        }
        return false;
    }

}
