package me.kyle.burnett.SkyBlockWarriors.Listeners;

import me.kyle.burnett.SkyBlockWarriors.GameManager;
import me.kyle.burnett.SkyBlockWarriors.InvManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeave implements Listener {


    @EventHandler
    public void onLeave(PlayerQuitEvent e) {

        Player p = e.getPlayer();

        if (GameManager.getInstance().isPlayerInGame(p)) {

            GameManager gm = GameManager.getInstance();

            if (gm.hasPlayerGameStarted(p)) {

                gm.getPlayerGame(p).removeFromGame(p, false, false, false);

                InvManager.getInstance().restoreInv(p);

            } else if (!gm.hasPlayerGameStarted(p)) {
                gm.getPlayerGame(p).removeFromGame(p, false, false, true);

                InvManager.getInstance().restoreInv(p);
            }
        }

    }
}
