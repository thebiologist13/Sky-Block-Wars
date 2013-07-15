package me.kyle.burnett.SkyBlockWarriors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import me.kyle.burnett.SkyBlockWarriors.Configs.ConfigManager;
import me.kyle.burnett.SkyBlockWarriors.Events.PlayerJoinArenaEvent;
import me.kyle.burnett.SkyBlockWarriors.Events.PlayerLeaveArenaEvent;
import me.kyle.burnett.SkyBlockWarriors.Utils.ChestFiller;
import me.kyle.burnett.SkyBlockWarriors.Utils.WorldEditUtility;
import net.minecraft.server.v1_6_R2.Packet54PlayNoteBlock;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_6_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class Game {

    private ScoreboardManager manager = Bukkit.getScoreboardManager();
    private Scoreboard board = manager.getNewScoreboard();
    private Team BLUE = board.registerNewTeam("Blue Team");
    private Team RED = board.registerNewTeam("Red Team");
    private Team YELLOW = board.registerNewTeam("Yellow Team");
    private Team GREEN = board.registerNewTeam("Green Team");
    private Objective objective = board.registerNewObjective("test", "dummy");
    private ArenaState state = ArenaState.LOADING;
    private List<String> unteamed = new ArrayList<String>();
    private List<String> players = new ArrayList<String>();
    private List<String> voted = new ArrayList<String>();
    private List<String> editors = new ArrayList<String>();
    private HashMap<String, Team> team = new HashMap<String, Team>();
    private HashMap<String, GameMode> saveGM = new HashMap<String, GameMode>();
    private int gameID;
    private int count;
    private int task;
    private Score redT = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.RED + "Red: "));
    private Score greenT = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Green:"));
    private Score blueT = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.BLUE + "Blue: "));
    private Score yellowT = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.YELLOW + "Yellow: "));
    private String prefix = ChatColor.GOLD + "[" + ChatColor.BLUE + "SBW" + ChatColor.GOLD + "]";
    private boolean starting;
    GameManager gm = GameManager.getInstance();

    public Game(int gameID, boolean justCreated, boolean justRestarted) {

        this.gameID = gameID;
        this.task = gameID;
        prepareArena(justCreated, justRestarted);

    }

    public void prepareArena(boolean justCreated, boolean firstLoad) {

        this.state = ArenaState.LOADING;
        this.voted.clear();
        this.players.clear();
        this.team.clear();
        this.unteamed.clear();
        this.editors.clear();
        this.saveGM.clear();

        if (Main.getInstance().Arena.getBoolean("Arena." + this.gameID + ".Active")) {

            if (!justCreated) {

                WorldEditUtility.getInstance().loadIslandSchematic(this.gameID);

                this.RED.setDisplayName(ChatColor.RED + "Red");
                this.GREEN.setDisplayName(ChatColor.GREEN + "Green");
                this.BLUE.setDisplayName(ChatColor.RED + "Blue");
                this.YELLOW.setDisplayName(ChatColor.RED + "Yellow");

                ChestFiller.loadChests(this.gameID);
                this.state = ArenaState.WAITING;
                if (!firstLoad) {

                    Game.this.broadCastServer(prefix + ChatColor.GREEN + "Arena " + ChatColor.GOLD + Game.this.gameID + ChatColor.GREEN + " is ready to join.");

                }
                return;
            }
        }

        this.state = ArenaState.IN_SETUP;
    }

    public void start() {

        Bukkit.getServer().getScheduler().cancelTask(Game.this.task);

        this.setState(ArenaState.IN_GAME);

        this.addPlayersToTeams();

        this.unteamed.clear();

        this.broadCastGame(prefix + ChatColor.GREEN + "GO!");
        this.broadCastServer(prefix + ChatColor.GREEN + "Arena " + ChatColor.GOLD + this.gameID + ChatColor.GREEN + " has started.");

        this.teleportPlayers();

        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.objective.setDisplayName("Team's");
        this.redT.setScore(this.RED.getPlayers().size());
        this.greenT.setScore(this.GREEN.getPlayers().size());
        this.blueT.setScore(this.BLUE.getPlayers().size());
        this.yellowT.setScore(this.YELLOW.getPlayers().size());

        this.checkEnd();

    }

    public void checkStart() {

        if (this.getPlayers().size() >= Main.getInstance().Config.getInt("Auto-Start-Players")) {

            this.countdown();

        } else if (this.voted.size() * 100 / this.players.size() > 50) {

            this.countdown();
            this.broadCastServer(prefix + ChatColor.GREEN + "Arena " + ChatColor.GOLD + this.gameID + ChatColor.GREEN + " will be starting soon.");

        }

    }

    public void endGameNormal(final Team team) {

        this.setState(ArenaState.RESETING);

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {

            @Override
            public void run() {

                if (team != null) {
                    if (team.equals(Game.this.RED)) {
                        Game.this.broadCastGame(prefix + ChatColor.GREEN + "Well done " + ChatColor.RED + "red" + ChatColor.GREEN + " team you won.");
                    } else if (team.equals(Game.this.GREEN)) {
                        Game.this.broadCastGame(prefix + ChatColor.GREEN + "Well done " + ChatColor.GREEN + "green" + ChatColor.GREEN + " team you won.");
                    } else if (team.equals(Game.this.BLUE)) {
                        Game.this.broadCastGame(prefix + ChatColor.GREEN + "Well done " + ChatColor.BLUE + "blue" + ChatColor.GREEN + " team you won.");
                    } else if (team.equals(Game.this.YELLOW)) {
                        Game.this.broadCastGame(prefix + ChatColor.GREEN + "Well done " + ChatColor.YELLOW + "yellow" + ChatColor.GREEN + " team you won.");
                    }
                }
                for (int x = 0; x < Game.this.players.size(); x++) {

                    Player p = Bukkit.getServer().getPlayer(Game.this.players.get(x));

                    if (p != null) {
                        Game.this.removeFromGame(p, true, false, false);
                    }
                }

                Game.this.prepareArena(false, false);
                //Register stats.
            }
        }, 2L);

    }

    public void endGameDisable() {

    }

    public void endGameStarting() {
        this.setState(ArenaState.DISABLED);

        Bukkit.getScheduler().cancelTask(this.task);

        this.broadCastGame(prefix + ChatColor.RED + "Canceling game start. Arena is being disabled.");

        this.prepareArena(false, false);

    }

    public void checkEnd() {

        int red, green, yellow, blue;
        red = this.RED.getPlayers().size();
        green = this.GREEN.getPlayers().size();
        yellow = this.YELLOW.getPlayers().size();
        blue = this.BLUE.getPlayers().size();

        if (red <= 0 && green <= 0 && yellow <= 0 && blue > 0) {
            this.endGameNormal(this.BLUE);
        } else if (red <= 0 && green <= 0 && blue <= 0 && yellow > 0) {
            this.endGameNormal(this.YELLOW);
        }

        else if (red <= 0 && blue <= 0 && yellow <= 0 && green > 0) {
            this.endGameNormal(this.GREEN);
        }

        else if (green <= 0 && yellow <= 0 && blue <= 0 && red > 0) {
            this.endGameNormal(this.RED);
        }

        else if (green <= 0 && yellow <= 0 && blue <= 0 && yellow <= 0) {
            this.endGameNormal(null);
        }

    }

    public void addPlayer(Player p) {

        if (this.state == ArenaState.WAITING) {

            if (!((Main.getInstance().Config.getInt("Max-People-In-A-Team") * 4) == this.players.size())) {

                this.players.add(p.getName());
                this.unteamed.add(p.getName());
                gm.setPlayerGame(p, this.gameID);
                PlayerJoinArenaEvent event = new PlayerJoinArenaEvent(p, Game.this);
                Bukkit.getServer().getPluginManager().callEvent(event);

                this.broadCastGame(prefix + p.getDisplayName() + ChatColor.GREEN + " has joined the arena.");

                int startPlayers = Main.getInstance().Config.getInt("Auto-Start-Players");
                int max = Main.getInstance().Config.getInt("Max-People-In-A-Team") * 4;

                p.sendMessage(prefix + ChatColor.GREEN + "The game will automatically start when there are " + startPlayers + " players.");
                p.sendMessage(prefix + ChatColor.GREEN + "There are " + ChatColor.GOLD + this.players.size() + "/" + max + ChatColor.GREEN + " players in the game.");

                this.checkStart();
            }
        }
        return;

    }

    public void removeFromGame(Player p, boolean end, boolean died, boolean instart) {

        PlayerLeaveArenaEvent event = new PlayerLeaveArenaEvent(p, Game.this, false);
        Bukkit.getServer().getPluginManager().callEvent(event);

        this.players.remove(p.getName());
        this.voted.remove(p.getName());
        gm.removePlayer(p);

        if (!instart) {

            Scoreboard blankBoard = manager.getNewScoreboard();
            p.setScoreboard(blankBoard);

            if (this.getPlayerTeam(p).equals(this.RED)) {
                this.redT.setScore(this.RED.getPlayers().size());

            } else if (this.getPlayerTeam(p).equals(this.BLUE)) {
                this.blueT.setScore(this.BLUE.getPlayers().size());

            } else if (this.getPlayerTeam(p).equals(this.YELLOW)) {
                this.yellowT.setScore(this.YELLOW.getPlayers().size());

            } else if (this.getPlayerTeam(p).equals(this.GREEN)) {
                this.greenT.setScore(this.GREEN.getPlayers().size());
            }

            this.removeFromTeam(p);

            InvManager.getInstance().restoreInv(p);
            Main.getInstance().teleportToLobby(p);

            p.setHealth(20.00);
            p.setFoodLevel(20);
            p.setFireTicks(0);
            p.setSaturation(10);

            if (this.saveGM.containsKey(p.getName())) {
                p.setGameMode(this.saveGM.get(p.getName()));
                this.saveGM.keySet().remove(p.getName());
            }

            if (!end) {

                if (!died) {
                    this.broadCastGame(prefix + ChatColor.GOLD + p.getName() + ChatColor.GREEN + " has left the arena.");

                } else if (died) {
                    this.broadCastGame(prefix + ChatColor.GOLD + "Player " + p.getDisplayName() + ChatColor.GOLD + " has died.");
                }
                this.checkEnd();
            }
        } else if (instart) {
            if (starting) {
                if (checkEndStart()) {
                    this.endStart();
                    return;
                }
            }
        }
    }

    public boolean checkEndStart() {
        if (this.players.size() < Main.getInstance().Config.getInt("Minimum-Players-To-Start")) {
            return true;
        }
        return false;

    }

    public void endStart() {
        Bukkit.getScheduler().cancelTask(this.task);
        if (this.players.size() > 0) {
            this.broadCastGame(prefix + ChatColor.RED + "Countdown canceled. Not enough players to start.");
        }
        this.setState(ArenaState.WAITING);
    }

    public List<String> getPlayers() {
        return players;
    }

    public int getGameID() {
        return this.gameID;
    }

    public Team getYellowTeam() {

        return this.YELLOW;
    }

    public Team getGreenTeam() {

        return this.GREEN;
    }

    public Team getBlueTeam() {

        return this.BLUE;
    }

    public Team getRedTeam() {

        return this.RED;
    }

    public List<String> getVoted() {

        return voted;
    }

    public void addVoted(Player p) {
        this.voted.add(p.getName());
        checkStart();
    }

    public boolean hasVoted(Player p) {
        if (this.voted.contains(p.getName())) {
            return true;
        }

        return false;
    }

    public Team getPlayerTeam(Player p) {

        Team team = this.team.get(p.getName());

        if (team == null) {
            return null;
        }

        if (team.equals(this.RED)) {
            return this.RED;

        } else if (team.equals(this.GREEN)) {

            return this.GREEN;

        } else if (team.equals(this.BLUE)) {

            return this.BLUE;

        } else if (team.equals(this.YELLOW)) {

            return this.YELLOW;

        } else {
            return null;
        }

    }

    public void setTeamRed(Player p) {

        this.removeFromTeam(p);
        this.team.put(p.getName(), this.RED);
        this.RED.addPlayer(p);
    }

    public void setTeamBlue(Player p) {

        this.removeFromTeam(p);
        this.team.put(p.getName(), this.BLUE);
        this.RED.addPlayer(p);
    }

    public void setTeamGreen(Player p) {

        this.removeFromTeam(p);
        this.team.put(p.getName(), this.GREEN);
        this.RED.addPlayer(p);
    }

    public void setTeamYellow(Player p) {

        this.removeFromTeam(p);
        this.team.put(p.getName(), this.YELLOW);
        this.RED.addPlayer(p);
    }

    public void removeFromTeam(Player p) {
        if (this.team.containsKey(p.getName())) {
            this.team.get(p.getName()).removePlayer(p);
            this.team.remove(p.getName());
        }
    }

    public boolean isPlayerInTeam(Player p) {
        if (this.team.containsKey(p.getName())) {
            if (this.team.get(p.getName()) != null) {
                return true;
            }
        }

        return false;
    }


    public ArenaState getState() {
        return this.state;
    }

    public void setState(ArenaState state) {
        this.state = state;
    }


    public void broadCastGame(String s) {

        for (int x = 0; x < players.size(); x++) {

            Player p = Bukkit.getServer().getPlayer(players.get(x));

            p.sendMessage(s);

        }

    }


    public void broadCastServer(String s) {

        for (Player p : Bukkit.getServer().getOnlinePlayers()) {

            p.sendMessage(s);

        }

    }

    public ChatColor getTeamColor(Player p) {

        Team team = this.getPlayerTeam(p);

        if (team == null) {
            return null;
        }

        if (team.equals(this.RED)) {

            return ChatColor.RED;

        } else if (team.equals(this.GREEN)) {

            return ChatColor.GREEN;

        } else if (team.equals(this.BLUE)) {

            return ChatColor.BLUE;

        }
        if (team.equals(this.YELLOW)) {

            return ChatColor.YELLOW;
        }

        return null;
    }

    public String getPlayersAsList() {

        List<String> playersColor = new ArrayList<String>();

        for (int x = 0; x < this.getPlayers().size(); x++) {

            Player p = Bukkit.getServer().getPlayer(this.getPlayers().get(x));
            if (p != null) {


                Team team = this.getPlayerTeam(p);

                if (team == null) {

                    playersColor.add(ChatColor.GRAY + p.getDisplayName());
                } else if (team.equals(this.RED)) {
                    playersColor.add(ChatColor.RED + p.getDisplayName());

                } else if (team.equals(this.GREEN)) {
                    playersColor.add(ChatColor.GREEN + p.getDisplayName());

                } else if (team.equals(this.BLUE)) {
                    playersColor.add(ChatColor.BLUE + p.getDisplayName());

                }
                if (team.equals(this.YELLOW)) {
                    playersColor.add(ChatColor.YELLOW + p.getDisplayName());
                }
            }
        }

        return this.getPlayers().toString().replace("[", " ").replace("]", " ");
    }

    public void addChest(ChestType chest, Location loc) {

        if (chest.equals(ChestType.SPAWN)) {

            ArrayList<String> spawnChests = (ArrayList<String>) Main.getInstance().Chest.getStringList("Chest." + this.getGameID() + ".Spawn");

            int x, y, z;

            x = loc.getBlockX();

            y = loc.getBlockY();

            z = loc.getBlockZ();

            spawnChests.add(Integer.toString(x) + "," + Integer.toString(y) + "," + Integer.toString(z));

            Main.getInstance().Chest.set("Chest." + this.getGameID() + ".Spawn", spawnChests);

            ConfigManager.getInstance().saveYamls();

        } else if (chest.equals(ChestType.SIDE)) {

            ArrayList<String> spawnChests = (ArrayList<String>) Main.getInstance().Chest.getStringList("Chest." + this.getGameID() + ".Side");

            int x, y, z;

            x = loc.getBlockX();

            y = loc.getBlockY();

            z = loc.getBlockZ();

            spawnChests.add(Integer.toString(x) + "," + Integer.toString(y) + "," + Integer.toString(z));

            Main.getInstance().Chest.set("Chest." + this.getGameID() + ".Side", spawnChests);

            ConfigManager.getInstance().saveYamls();


        } else if (chest.equals(ChestType.CENTER)) {

            ArrayList<String> spawnChests = (ArrayList<String>) Main.getInstance().Chest.getStringList("Chest." + this.getGameID() + ".Center");

            int x, y, z;

            x = loc.getBlockX();

            y = loc.getBlockY();

            z = loc.getBlockZ();

            spawnChests.add(Integer.toString(x) + "," + Integer.toString(y) + "," + Integer.toString(z));

            Main.getInstance().Chest.set("Chest." + this.getGameID() + ".Center", spawnChests);

            ConfigManager.getInstance().saveYamls();

        }

    }

    public List<String> getEditors() {

        return editors;
    }

    public int getEditorsSize() {

        return getEditors().size();
    }

    public void addEditor(Player p) {
        editors.add(p.getName());
    }

    public void removeEditor(Player p) {
        if (editors.contains(p.getName())) {
            editors.remove(p.getName());
        }
    }

    public void addRedSpawn(Player p) {
        Main.getInstance().Spawns.set("Spawn." + this.gameID + ".Red.X", p.getLocation().getBlockX());
        Main.getInstance().Spawns.set("Spawn." + this.gameID + ".Red.Y", p.getLocation().getBlockY());
        Main.getInstance().Spawns.set("Spawn." + this.gameID + ".Red.Z", p.getLocation().getBlockZ());
        ConfigManager.getInstance().saveYamls();
    }

    public void addBlueSpawn(Player p) {
        Main.getInstance().Spawns.set("Spawn." + this.gameID + ".Blue.X", p.getLocation().getBlockX());
        Main.getInstance().Spawns.set("Spawn." + this.gameID + ".Blue.Y", p.getLocation().getBlockY());
        Main.getInstance().Spawns.set("Spawn." + this.gameID + ".Blue.Z", p.getLocation().getBlockZ());
        ConfigManager.getInstance().saveYamls();
    }

    public void addYellowSpawn(Player p) {
        Main.getInstance().Spawns.set("Spawn." + this.gameID + ".Yellow.X", p.getLocation().getBlockX());
        Main.getInstance().Spawns.set("Spawn." + this.gameID + ".Yellow.Y", p.getLocation().getBlockY());
        Main.getInstance().Spawns.set("Spawn." + this.gameID + ".Yellow.Z", p.getLocation().getBlockZ());
        ConfigManager.getInstance().saveYamls();
    }

    public void addGreenSpawn(Player p) {

        Main.getInstance().Spawns.set("Spawn." + this.gameID + ".Green.X", p.getLocation().getBlockX());
        Main.getInstance().Spawns.set("Spawn." + this.gameID + ".Green.Y", p.getLocation().getBlockY());
        Main.getInstance().Spawns.set("Spawn." + this.gameID + ".Green.Z", p.getLocation().getBlockZ());
        ConfigManager.getInstance().saveYamls();
    }

    public Location getSpawnRed() {

        World world = Bukkit.getServer().getWorld(Main.getInstance().Arena.getString("Arena." + this.gameID + ".World"));

        int x = Main.getInstance().Spawns.getInt("Spawn." + this.gameID + ".Red.X");
        int y = Main.getInstance().Spawns.getInt("Spawn." + this.gameID + ".Red.Y");
        int z = Main.getInstance().Spawns.getInt("Spawn." + this.gameID + ".Red.Z");

        return new Location(world, x, y + 1, z);
    }

    public Location getSpawnBlue() {

        World world = Bukkit.getServer().getWorld(Main.getInstance().Arena.getString("Arena." + this.gameID + ".World"));

        int x = Main.getInstance().Spawns.getInt("Spawn." + this.gameID + ".Blue.X");
        int y = Main.getInstance().Spawns.getInt("Spawn." + this.gameID + ".Blue.Y");
        int z = Main.getInstance().Spawns.getInt("Spawn." + this.gameID + ".Blue.Z");

        return new Location(world, x, y + 1, z);
    }

    public Location getSpawnYellow() {

        World world = Bukkit.getServer().getWorld(Main.getInstance().Arena.getString("Arena." + this.gameID + ".World"));

        int x = Main.getInstance().Spawns.getInt("Spawn." + this.gameID + ".Yellow.X");
        int y = Main.getInstance().Spawns.getInt("Spawn." + this.gameID + ".Yellow.Y");
        int z = Main.getInstance().Spawns.getInt("Spawn." + this.gameID + ".Yellow.Z");

        return new Location(world, x, y + 1, z);
    }

    public Location getSpawnGreen() {

        World world = Bukkit.getServer().getWorld(Main.getInstance().Arena.getString("Arena." + this.gameID + ".World"));

        int x = Main.getInstance().Spawns.getInt("Spawn." + this.gameID + ".Green.X");
        int y = Main.getInstance().Spawns.getInt("Spawn." + this.gameID + ".Green.Y");
        int z = Main.getInstance().Spawns.getInt("Spawn." + this.gameID + ".Green.Z");

        return new Location(world, x, y + 1, z);
    }

    public List<String> getUnteamed() {
        return this.unteamed;
    }

    public void playerSoundGame() {
        Packet54PlayNoteBlock packet = new Packet54PlayNoteBlock(1, 1, 1, 1, 1, 1);

        for (int x = 0; x < this.players.size(); x++) {

            Player p = Bukkit.getServer().getPlayer(this.players.get(x));

            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
        }
    }

    public void checkTeamEliminated() {
        if (this.RED.getPlayers().size() <= 0) {
            this.broadCastGame(prefix + ChatColor.GREEN + "The " + ChatColor.RED + "red " + ChatColor.GREEN + "team has been eliminated.");
        }

        if (this.BLUE.getPlayers().size() <= 0) {
            this.broadCastGame(prefix + ChatColor.GREEN + "The " + ChatColor.BLUE + "blue " + ChatColor.GREEN + "team has been eliminated.");
        }

        if (this.YELLOW.getPlayers().size() <= 0) {
            this.broadCastGame(prefix + ChatColor.GREEN + "The " + ChatColor.YELLOW + "yellow " + ChatColor.GREEN + "team has been eliminated.");
        }

        if (this.GREEN.getPlayers().size() <= 0) {
            this.broadCastGame(prefix + ChatColor.GREEN + "The " + ChatColor.GREEN + "green " + ChatColor.GREEN + "team has been eliminated.");
        }
    }

    public void addPlayersToTeams() {

        for (int y = 0; y < unteamed.size(); y++) {

            Team team = null;

            int red = this.RED.getPlayers().size();
            int blue = this.BLUE.getPlayers().size();
            int yellow = this.YELLOW.getPlayers().size();
            int green = this.GREEN.getPlayers().size();

            if (red < blue && red < yellow && red < green) {
                team = this.RED;
                System.out.println(1);
            }

            else if (blue < red && blue < yellow && blue < green) {
                team = this.BLUE;
                System.out.println(2);
            }

            else if (yellow < blue && yellow < red && yellow < green) {
                team = this.YELLOW;
                System.out.println(3);
            }

            else if (green < red && green < yellow && green < blue) {
                team = this.GREEN;
                System.out.println(4);
            }

            else if (red == blue && red == green && red == yellow) {
                team = chooseTeam();
                System.out.println(6);
            }

            else if (red == blue && red < green && red < yellow) {

                Random r = new Random();
                int x = r.nextInt(1);

                if (x == 1) {
                    team = this.BLUE;
                }
                team = this.RED;
                System.out.println(7);
            }

            else if (red == green && red < blue && red < yellow) {

                Random r = new Random();
                int x = r.nextInt(1);

                if (x == 1) {
                    team = this.GREEN;
                }
                team = this.RED;
                System.out.println(8);
            }

            else if (red == yellow && red < blue && red < green) {

                Random r = new Random();
                int x = r.nextInt(1);

                if (x == 1) {
                    team = this.YELLOW;
                }
                team = this.RED;
                System.out.println(9);
            }

            else if (blue == green && blue < red && blue < yellow) {

                Random r = new Random();
                int x = r.nextInt(1);

                if (x == 1) {
                    team = this.BLUE;
                }
                team = this.GREEN;
                System.out.println(10);
            }

            else if (blue == yellow && blue < red && blue < green) {

                Random r = new Random();
                int x = r.nextInt(1);

                if (x == 1) {
                    team = this.BLUE;
                }
                team = this.YELLOW;
                System.out.println(11);
            }

            else if (yellow == green && yellow < red && yellow < blue) {

                Random r = new Random();
                int x = r.nextInt(1);

                if (x == 1) {
                    team = this.YELLOW;
                }
                team = this.GREEN;
                System.out.println(12);
            }

            else if (blue == green && green == red && red < yellow) {

                Random r = new Random();
                int x = r.nextInt(2);

                if (x == 0) {
                    team = this.GREEN;
                } else if (x == 1) {
                    team = this.RED;
                } else if (x == 2) {
                    team = this.BLUE;
                }
                System.out.println(13);
            }

            else if (blue == green && green == yellow && yellow < red) {

                Random r = new Random();
                int x = r.nextInt(2);

                if (x == 0) {
                    team = this.YELLOW;
                } else if (x == 1) {
                    team = this.GREEN;
                } else if (x == 2) {
                    team = this.BLUE;
                }
                System.out.println(14);
            }

            else if (blue == red && red == yellow && yellow < green) {

                Random r = new Random();
                int x = r.nextInt(2);

                if (x == 0) {
                    team = this.YELLOW;
                } else if (x == 1) {
                    team = this.RED;
                } else if (x == 2) {
                    team = this.BLUE;
                }
                System.out.println(15);
            }

            else if (green == red && red == yellow && yellow < blue) {

                Random r = new Random();
                int x = r.nextInt(2);

                if (x == 0) {
                    team = this.YELLOW;
                } else if (x == 1) {
                    team = this.RED;
                } else if (x == 2) {
                    team = this.GREEN;
                }
                System.out.println(16);
            }

            Player p = Bukkit.getServer().getPlayer(unteamed.get(y));
            System.out.println(unteamed.get(y));
            team.addPlayer(p);
            this.team.put(p.getName(), team);
            InvManager.getInstance().saveInv(p);

            p.setScoreboard(this.board);
        }
    }

    public Team chooseTeam() {

        Random r = new Random();

        int x = r.nextInt(4) + 1;

        Team team = null;

        switch (x) {

        case 1:
            team = this.RED;
            break;
        case 2:
            team = this.GREEN;
            break;
        case 3:
            team = this.YELLOW;
            break;
        case 4:
            team = this.BLUE;
            break;
        }
        return team;
    }

    public boolean isRedAvailable() {

        int red = this.RED.getPlayers().size();
        int blue = this.BLUE.getPlayers().size();
        int yellow = this.YELLOW.getPlayers().size();
        int green = this.GREEN.getPlayers().size();

        if (red >= Main.getInstance().Config.getInt("Max-People-In-A-Team")) {
            return false;
        }

        if (red < blue && red < yellow && red < green) {
            return true;
        }

        else if (red == blue && red == green && red == yellow) {
            return true;
        }

        else if (red == blue && red < green && red < yellow) {
            return true;
        }

        else if (red == green && red < blue && red < yellow) {
            return true;
        }

        else if (red == yellow && red < blue && red < green) {
            return true;
        }

        else if (blue == green && green == red && red < yellow) {
            return true;
        }

        else if (blue == red && red == yellow && yellow < green) {
            return true;
        }

        else if (green == red && red == yellow && yellow < blue) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isGreenAvailable() {

        int red = this.RED.getPlayers().size();
        int blue = this.BLUE.getPlayers().size();
        int yellow = this.YELLOW.getPlayers().size();
        int green = this.GREEN.getPlayers().size();

        if (green >= Main.getInstance().Config.getInt("Max-People-In-A-Team")) {
            return false;
        }

        if (green < blue && green < yellow && green < red) {
            return true;
        }

        else if (green == blue && green == red && green == yellow) {
            return true;
        }

        else if (green == blue && green < red && green < yellow) {
            return true;
        }

        else if (green == red && green < blue && green < yellow) {
            return true;
        }

        else if (green == yellow && green < blue && green < red) {
            return true;
        }

        else if (blue == green && green == red && red < yellow) {
            return true;
        }

        else if (blue == green && green == yellow && yellow < red) {
            return true;
        }

        else if (green == red && red == yellow && yellow < blue) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isBlueAvailable() {

        int red = this.RED.getPlayers().size();
        int blue = this.BLUE.getPlayers().size();
        int yellow = this.YELLOW.getPlayers().size();
        int green = this.GREEN.getPlayers().size();

        if (blue >= Main.getInstance().Config.getInt("Max-People-In-A-Team")) {
            return false;
        }

        if (blue < green && blue < yellow && blue < red) {
            return true;
        }

        else if (blue == green && blue == red && blue == yellow) {
            return true;
        }

        else if (blue == green && blue < red && blue < yellow) {
            return true;
        }

        else if (blue == red && blue < green && blue < yellow) {
            return true;
        }

        else if (blue == yellow && blue < green && blue < red) {
            return true;
        }

        else if (blue == green && green == red && red < yellow) {
            return true;
        }

        else if (blue == green && green == yellow && yellow < red) {
            return true;
        }

        else if (blue == red && red == yellow && yellow < green) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isYellowAvailable() {

        int red = this.RED.getPlayers().size();
        int blue = this.BLUE.getPlayers().size();
        int yellow = this.YELLOW.getPlayers().size();
        int green = this.GREEN.getPlayers().size();

        if (yellow >= Main.getInstance().Config.getInt("Max-People-In-A-Team")) {
            return false;
        }

        if (yellow < green && yellow < blue && yellow < red) {
            return true;
        }

        else if (yellow == green && yellow == red && yellow == blue) {
            return true;
        }

        else if (yellow == green && yellow < red && yellow < blue) {
            return true;
        }

        else if (yellow == red && yellow < green && yellow < blue) {
            return true;
        }

        else if (yellow == blue && yellow < green && yellow < red) {
            return true;
        }

        else if (yellow == green && green == red && red < yellow) {
            return true;
        }

        else if (yellow == red && red == blue && yellow < green) {
            return true;
        }

        else if (yellow == blue && blue == green && yellow < red) {
            return true;
        } else {
            return false;
        }
    }

    public void teleportPlayers() {

        FileConfiguration a = Main.getInstance().Arena;
        FileConfiguration s = Main.getInstance().Spawns;

        World world = Bukkit.getServer().getWorld(a.getString("Arena." + this.gameID + ".World"));
        Location red = new Location(world, s.getDouble("Spawn." + this.gameID + ".Red.X"), s.getDouble("Spawn." + this.gameID + ".Red.Y") + 1, s.getDouble("Spawn." + this.gameID + ".Red.Z"));
        Location green = new Location(world, s.getDouble("Spawn." + this.gameID + ".Green.X"), s.getDouble("Spawn." + this.gameID + ".Green.Y") + 1, s.getDouble("Spawn." + this.gameID + ".Green.Z"));
        Location blue = new Location(world, s.getDouble("Spawn." + this.gameID + ".Blue.X"), s.getDouble("Spawn." + this.gameID + ".Blue.Y") + 1, s.getDouble("Spawn." + this.gameID + ".Blue.Z"));
        Location yellow = new Location(world, s.getDouble("Spawn." + this.gameID + ".Yellow.X"), s.getDouble("Spawn." + this.gameID + ".Yellow.Y") + 1, s.getDouble("Spawn." + this.gameID + ".Yellow.Z"));

        for (OfflinePlayer p : this.RED.getPlayers()) {
            p.getPlayer().teleport(red);
            if (!p.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
                this.saveGM.put(p.getName(), p.getPlayer().getGameMode());
            }
            p.getPlayer().setGameMode(GameMode.SURVIVAL);
            p.getPlayer().setHealth(20.00);
            p.getPlayer().setFoodLevel(20);
            p.getPlayer().setFireTicks(0);
            p.getPlayer().setSaturation(10);
        }

        for (OfflinePlayer p : this.GREEN.getPlayers()) {
            p.getPlayer().teleport(green);
            if (!p.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
                this.saveGM.put(p.getName(), p.getPlayer().getGameMode());
            }
            p.getPlayer().setGameMode(GameMode.SURVIVAL);
            p.getPlayer().setHealth(20.00);
            p.getPlayer().setFoodLevel(20);
            p.getPlayer().setFireTicks(0);
            p.getPlayer().setSaturation(10);
        }

        for (OfflinePlayer p : this.BLUE.getPlayers()) {
            p.getPlayer().teleport(blue);
            if (!p.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
                this.saveGM.put(p.getName(), p.getPlayer().getGameMode());
            }
            p.getPlayer().setGameMode(GameMode.SURVIVAL);
            p.getPlayer().setHealth(20.00);
            p.getPlayer().setFoodLevel(20);
            p.getPlayer().setFireTicks(0);
            p.getPlayer().setSaturation(10);
        }

        for (OfflinePlayer p : this.YELLOW.getPlayers()) {
            p.getPlayer().teleport(yellow);
            if (!p.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
                this.saveGM.put(p.getName(), p.getPlayer().getGameMode());
            }
            p.getPlayer().setGameMode(GameMode.SURVIVAL);
            p.getPlayer().setHealth(20.00);
            p.getPlayer().setFoodLevel(20);
            p.getPlayer().setFireTicks(0);
            p.getPlayer().setSaturation(10);
        }
    }


    public void countdown() {

        this.count = Main.getInstance().Config.getInt("Auto-Start-Time");

        if (this.state == ArenaState.WAITING) {

            this.setState(ArenaState.STARTING);

            this.starting = true;

            this.task = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {

                public void run() {

                    if (Game.this.players.size() < Main.getInstance().Config.getInt("Minimum-Players-To-Start")) {

                        Game.this.endStart();

                    } else if (Game.this.count > 0) {

                        if (Game.this.count % 10 == 0) {
                            Game.this.broadCastGame(Game.this.prefix + ChatColor.GREEN + "Starting in " + ChatColor.GOLD + count + ChatColor.GREEN + ".");
                        }
                        if (Game.this.count < 6) {
                            Game.this.broadCastGame(Game.this.prefix + ChatColor.GREEN + "Starting in " + ChatColor.GOLD + count + ChatColor.GREEN + ".");
                            Game.this.playerSoundGame();
                        }

                        Game.this.count -= 1;

                    } else {

                        Game.this.start();
                    }
                }
            }, 0L, 20L);
        }
    }
}
