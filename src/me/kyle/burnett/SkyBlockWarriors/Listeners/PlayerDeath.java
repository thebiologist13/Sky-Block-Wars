package me.kyle.burnett.SkyBlockWarriors.Listeners;

import me.kyle.burnett.SkyBlockWarriors.GameManager;
import me.kyle.burnett.SkyBlockWarriors.Main;
import net.minecraft.server.v1_6_R2.Packet205ClientCommand;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_6_R2.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeath implements Listener {

    @EventHandler
    public void onDeath(final PlayerDeathEvent e) {

        Entity ent = e.getEntity();

        if (ent instanceof Player) {

            final Player p = (Player) ent;

            if (GameManager.getInstance().isPlayerInGame(p)) {
                e.setDeathMessage(null);
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {

                    @Override
                    public void run() {
                        Packet205ClientCommand packet = new Packet205ClientCommand();
                        packet.a = 1;

                        ((CraftPlayer) p).getHandle().playerConnection.a(packet);

                        GameManager.getInstance().getPlayerGame(p).removeFromGame(p, false);

                        p.sendMessage(ChatColor.GOLD + "[" + ChatColor.BLUE + "SB" + ChatColor.GOLD + "]" + ChatColor.RED + "You were killed by " + ChatColor.GOLD + e.getEntity().getLastDamage() + ChatColor.RED + ".");

                        GameManager.getInstance().getPlayerGame(p).broadCastGame(ChatColor.GOLD + "[" + ChatColor.BLUE + "SB" + ChatColor.GOLD + "]" + ChatColor.GOLD + "Player " + p.getDisplayName() + ChatColor.GOLD + " has left the game.");
                        GameManager.getInstance().getPlayersGame().put(p.getName(), null);
                    }


                }, 1L);
            }
        }
    }

}
