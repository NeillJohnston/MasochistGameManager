package io.github.NeillJohnston.MasochistGameManager.gamemode;

import io.github.NeillJohnston.MasochistGameManager.MapYml;
import io.github.NeillJohnston.MasochistGameManager.MasochistGameManager;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Basic specification for a gamemode manager.
 * Would have been called "GamemodeManager" but that's annoyingly long.
 */
public interface Gamemode extends Listener {

    /**
     * Starts the gamemode.
     */
    public void start();

    /**
     * Register a player with a tracker.
     *
     * @param player    Player to be registered
     * @return  A new PlayerTracker for the player
     */
    public PlayerTracker register(Player player);

    /**
     * Make sure to listen for player joining events.
     *
     * @param event The player joining event
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event);

}
