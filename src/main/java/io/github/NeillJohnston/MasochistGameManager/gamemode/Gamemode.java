package io.github.NeillJohnston.MasochistGameManager.gamemode;

import io.github.NeillJohnston.MasochistGameManager.MapYml;
import io.github.NeillJohnston.MasochistGameManager.MasochistGameManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.UUID;

/**
 * Basic specification for a gamemode manager.
 * Would have been called "GamemodeManager" but that's annoyingly long.
 */
public class Gamemode implements Listener {

    private final MasochistGameManager plugin;
    private final World world;
    private final MapYml mapYml;
    private final Location spawn;

    private HashMap<UUID, PlayerTracker> trackers;

    /**
     * Initialize the manager with required attributes from map.yml.
     *
     * @param plugin    Bukkit plugin instance
     * @param world     Current world that the pkr game is being run in
     * @param mapYml    Map's map.yml object
     * @throws NullPointerException When mapYml does not have all the required attributes
     */
    public Gamemode(MasochistGameManager plugin, World world, MapYml mapYml) throws NullPointerException {

        this.plugin = plugin;
        this.world = world;
        this.mapYml = mapYml;
        this.trackers = new HashMap<>();

        spawn = MasochistGameManager.locationFromCoords(world, mapYml.coordinates("spawn"));

    }

    /**
     * Doesn't actually do anything here.
     */
    public void start() {

        //

    }

    /**
     * Get a tracker.
     *
     * @param player    Player to be registered
     * @return A new "generic" player tracker.
     */
    public PlayerTracker register(Player player) {

        PlayerTracker tracker = new PlayerTracker(player.getUniqueId());
        trackers.put(player.getUniqueId(), tracker);
        return tracker;

    }

    /**
     * Add a tracker on player join.
     *
     * @param event The player joining event
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        MasochistGameManager.trackers.put(event.getPlayer().getUniqueId(), register(event.getPlayer()));

    }

}
