package io.github.NeillJohnston.MasochistGameManager.gamemode;

import io.github.NeillJohnston.MasochistGameManager.MapYml;
import io.github.NeillJohnston.MasochistGameManager.MasochistGame;
import io.github.NeillJohnston.MasochistGameManager.MasochistGameManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.HashMap;

/**
 * Basic specification for a gamemode manager.
 * Would have been called "GamemodeManager" but that's annoyingly long.
 *
 * @deprecated Yep! Already. Removing this shit in favor of MasochistGame, which will include basically a copy of
 * all of this in itself.
 */
public class Gamemode implements Listener {

    final MasochistGameManager plugin;
    final MasochistGame game;
    final World world;
    final MapYml mapYml;
    final String gameId;
    final Location spawn;

    private HashMap<Player, PlayerTracker> trackers;

    /**
     * Initialize the manager with required attributes from map.yml.
     *
     * @param plugin    Bukkit plugin instance
     * @param world     Current world that the pkr game is being run in
     * @param mapYml    Map's map.yml object
     * @param id        The gameId of the game
     * @throws NullPointerException When mapYml does not have all the required attributes
     */
    public Gamemode(MasochistGameManager plugin, MasochistGame game, World world, MapYml mapYml, String id) throws NullPointerException {

        this.plugin = plugin;
        this.game = game;
        this.world = world;
        this.mapYml = mapYml;
        this.gameId = id;
        this.trackers = new HashMap<>();

        spawn = MasochistGameManager.locationFromCoords(world, mapYml.coordinates("spawn"));

    }

    /**
     * Doesn't actually do anything here.
     */
    public void start() {

        Bukkit.getLogger().info("Started gamemode.");

    }

    /**
     * Teleport a player to this game, and issue a tracker.
     *
     * @param player    Player to be added
     */
    public void add(Player player) {

        player.teleport(spawn);
        track(player);

    }

    /**
     * Begin tracking a player for this game.
     *
     * @param player    Player to be added
     * @return A new tracker for the player
     */
    public PlayerTracker track(Player player) {

        PlayerTracker tracker = new PlayerTracker(player.getUniqueId(), gameId);
        trackers.put(player, tracker);
        return tracker;

    }

    /**
     * On player respawn, respawn at the spawn.
     *
     * @param event
     */
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {

        event.setRespawnLocation(spawn);

    }

}
