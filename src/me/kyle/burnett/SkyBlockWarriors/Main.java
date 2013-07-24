package me.kyle.burnett.SkyBlockWarriors;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.logging.Logger;

import me.kyle.burnett.SkyBlockWarriors.Commands.SW;
import me.kyle.burnett.SkyBlockWarriors.Configs.ConfigManager;
import me.kyle.burnett.SkyBlockWarriors.DatabaseHandler.SQLSelection;
import me.kyle.burnett.SkyBlockWarriors.Listeners.Interact;
import me.kyle.burnett.SkyBlockWarriors.Listeners.PlayerDamageEvent;
import me.kyle.burnett.SkyBlockWarriors.Listeners.PlayerDeath;
import me.kyle.burnett.SkyBlockWarriors.Listeners.PlayerLeave;
import me.kyle.burnett.SkyBlockWarriors.Listeners.PlayerMove;
import me.kyle.burnett.SkyBlockWarriors.Listeners.SignChange;
import me.kyle.burnett.SkyBlockWarriors.Utils.WorldEditUtility;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin {

    private static Main instance;

    public File configFile;
    public FileConfiguration Config;

    public File arenaFile;
    public FileConfiguration Arena;

    public File invFile;
    public FileConfiguration Inv;

    public File chestFile;
    public FileConfiguration Chest;

    public File spawnFile;
    public FileConfiguration Spawns;

    public File signFile;
    public FileConfiguration Signs;

    public Logger log = Bukkit.getLogger();

    private PluginManager pm = Bukkit.getServer().getPluginManager();

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {

        instance = this;

        configFile = new File(getDataFolder(), "config.yml");
        arenaFile = new File(getDataFolder(), "arena.yml");
        invFile = new File(getDataFolder(), "inventorys.yml");
        chestFile = new File(getDataFolder(), "chests.yml");
        spawnFile = new File(getDataFolder(), "spawns.yml");
        signFile = new File(getDataFolder(), "signs.yml");

        try {

            ConfigManager.getInstance().firstRun();

        } catch (Exception e) {

            e.printStackTrace();
        }

        this.Config = new YamlConfiguration();
        this.Arena = new YamlConfiguration();
        this.Inv = new YamlConfiguration();
        this.Chest = new YamlConfiguration();
        this.Spawns = new YamlConfiguration();
        this.Signs = new YamlConfiguration();
        ConfigManager.getInstance().loadYamls();
        ConfigManager.getInstance().saveYamls();

        pm.registerEvents(new PlayerDeath(), this);
        pm.registerEvents(new PlayerLeave(), this);
        pm.registerEvents(new PlayerDamageEvent(), this);
        pm.registerEvents(new PlayerMove(), this);
        pm.registerEvents(new SignChange(), this);
        pm.registerEvents(new Interact(), this);

        getCommand("skyblockw").setExecutor(new SW());

        setUp();


        try {
            this.checkDatabase();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            log.severe("Connection failed. Defaulting to SQLite.");
            this.Config.set("MySQL.Enable", false);
        }

        try {
            this.checkDatabase();

        } catch (ClassNotFoundException | SQLException e) {

            e.printStackTrace();
            log.severe("An error has occured. Shutting down sky block wars.");
            pm.disablePlugin(this);
        }


    }

    @Override
    public void onDisable() {

        Iterator<String> it = GameManager.getInstance().getPlayers().keySet().iterator();

        while (it.hasNext()) {

            Player p = Bukkit.getServer().getPlayer(it.next());

            if (p != null) {
                this.teleportToLobby(p);
            }
        }
    }

    public void setUp() {

        new BukkitRunnable() {

            @Override
            public void run() {

                GameManager.getInstance().setUp();
                WorldEditUtility.getInstance().regenAllIslands();
            }
        }.run();
    }

    public void setLobby(Player p) {

        this.Config.set("Lobby.X", p.getLocation().getBlockX());
        this.Config.set("Lobby.Y", p.getLocation().getBlockY());
        this.Config.set("Lobby.Z", p.getLocation().getBlockZ());
        this.Config.set("Lobby.YAW", p.getLocation().getPitch());
        this.Config.set("Lobby.PITCH", p.getLocation().getYaw());
        this.Config.set("Lobby.WORLD", p.getLocation().getWorld().getName());
        ConfigManager.getInstance().saveYamls();
    }

    public void setWaiting(Player p) {

        this.Config.set("Waiting.X", p.getLocation().getBlockX());
        this.Config.set("Waiting.Y", p.getLocation().getBlockY());
        this.Config.set("Waiting.Z", p.getLocation().getBlockZ());
        this.Config.set("Waiting.YAW", p.getLocation().getPitch());
        this.Config.set("Waiting.PITCH", p.getLocation().getYaw());
        this.Config.set("Waiting.WORLD", p.getLocation().getWorld().getName());
        ConfigManager.getInstance().saveYamls();
    }

    public boolean doesLobbyExist(){

        if(this.Config.contains("Lobby")){
            return true;
        }

        return false;
    }

    public boolean doesWaitingExist(){

        if(this.Config.contains("Waiting")){
            return true;
        }

        return false;
    }

    public boolean teleportToLobby(Player p) {

        if (!this.Config.contains("Lobby")) {
            return false;
        }

        World world = Bukkit.getServer().getWorld(this.Config.getString("Lobby.WORLD"));
        double x = Main.getInstance().Config.getDouble("Lobby.X");
        double y = Main.getInstance().Config.getDouble("Lobby.Y");
        double z = Main.getInstance().Config.getDouble("Lobby.Z");
        long yaw = Main.getInstance().Config.getLong("Lobby.YAW");
        long pitch = Main.getInstance().Config.getLong("Lobby.PITCH");

        Location location = new Location(world, x, y, z, yaw, pitch);

        p.teleport(location);

        return true;
    }

    public boolean teleportToWaiting(Player p) {

        if (!this.Config.contains("Waiting")) {
            return false;
        }

        World world = Bukkit.getServer().getWorld(this.Config.getString("Waiting.WORLD"));
        double x = Main.getInstance().Config.getDouble("Waiting.X");
        double y = Main.getInstance().Config.getDouble("Waiting.Y");
        double z = Main.getInstance().Config.getDouble("Waiting.Z");
        long yaw = Main.getInstance().Config.getLong("Waiting.YAW");
        long pitch = Main.getInstance().Config.getLong("Waiting.PITCH");

        Location location = new Location(world, x, y, z, yaw, pitch);

        p.teleport(location);

        return true;
    }

    public void checkDatabase() throws SQLException, ClassNotFoundException {

        Connection con = null;

        con = SQLSelection.getConnection();

        con.createStatement().execute("CREATE TABLE IF NOT EXISTS sbw(username VARCHAR(255), kills INTEGER, deaths INTEGER, wins INTEGER, losses INTEGER, played INTEGER)");


    }

}
