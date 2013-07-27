package me.kyle.burnett.SkyBlockWarriors.Listeners;

import me.kyle.burnett.SkyBlockWarriors.GameManager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreak implements Listener {

    @EventHandler
    public void blockBreak(BlockBreakEvent e) {

        if (!GameManager.getInstance().isPlayerInGame(e.getPlayer())) {

            if (!GameManager.getInstance().isEditing(e.getPlayer())) {

                if (GameManager.getInstance().isBlockInArena(e.getBlock())) {

                    e.setCancelled(true);
                }
            }

        }
    }

}
