package me.kyle.burnett.SkyBlockWarriors.Listeners;

import me.kyle.burnett.SkyBlockWarriors.GameManager;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scoreboard.Team;

public class PlayerDamageEvent implements Listener {


    @EventHandler
    public void playerDamage(EntityDamageByEntityEvent e) {
        Entity ent = e.getEntity();

        if (ent instanceof Player) {

            GameManager gm = GameManager.getInstance();
            Player p = (Player) ent;

            if (gm.isPlayerInGame(p)) {

                if (gm.hasPlayerGameStarted(p)) {

                    Entity ed = e.getDamager();

                    if (ed instanceof Player) {

                        Player pd = (Player) ed;
                        Team team = gm.getPlayerGame(p).getPlayerTeam(p);
                        if (team.getPlayers().contains(pd.getName())) {
                            e.setCancelled(true);
                        }
                    }
                }

            }


        }
    }

}
