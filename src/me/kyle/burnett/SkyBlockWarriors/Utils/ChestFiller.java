package me.kyle.burnett.SkyBlockWarriors.Utils;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import me.kyle.burnett.SkyBlockWarriors.Main;

public class ChestFiller {

    public static Main plugin;

    public ChestFiller(Main instance) {
        plugin = instance;
    }

    public static void loadChests(int arena) {
        Main plugin = Main.getInstance();
        World w = Bukkit.getWorld(Main.getInstance().Arena.getString("Arena." + arena + ".World"));

        fillChests(w, plugin.Chest.getStringList("Chest." + arena + ".Spawn"), plugin.Config.getStringList("Chests.Spawn-Chests.ItemID/Amount"));
        fillChests(w, plugin.Chest.getStringList("Chest." + arena + ".Side"), plugin.Config.getStringList("Chests.Side-Chests.ItemID/Amount"));
        fillChests(w, plugin.Chest.getStringList("Chest." + arena + ".Center"), plugin.Config.getStringList("Chests.Middle-Chests.ItemID/Amount"));
    }

    private static void fillChests(World world, List<String> chestLocations, List<String> chestContents) {
        ItemStack[] items = new ItemStack[27];

        int i = 0;
        for (String item : chestContents) {
            items[i++] = itemFromString(item);
        }

        for (String locString : chestLocations) {
            Block b = world.getBlockAt(vecFromString(locString).toLocation(world));

            if (b.getType().equals(Material.CHEST)) {
                Chest c = (Chest) b.getState();
                c.getInventory().setContents(items);
            } else {
                Main.getInstance().getLogger().warning("Failed to find chest at " + locString + ", skipping...");
            }
        }
    }

    private static Vector vecFromString(String string) {
        String[] split = string.split(",");
        return new Vector(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
    }

    private static ItemStack itemFromString(String string) {

        String[] split = string.split(",");

        for (int x = 0; x < split.length; x++) {
            split[x] = split[x].toLowerCase().trim();
        }
        if (split.length < 1)
            return null;
        if (split.length == 1)
            return new ItemStack(Integer.parseInt(split[0]));
        if (split.length == 2)
            return new ItemStack(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
        if (split.length == 3) {
            return new ItemStack(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Short.parseShort(split[2]));
        }

        return null;
    }
}
