package me.kyle.burnett.SkyBlockWarriors.Events;

import me.kyle.burnett.SkyBlockWarriors.Game;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerLeaveArenaEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private Game game;
    private boolean before;

    public PlayerLeaveArenaEvent(Player p, Game g, boolean before) {
        this.player = p;
        this.game = g;
        this.before = before;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Game getGame() {
        return this.game;
    }
    
    public boolean leftBeforeStart(){
        return before;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
