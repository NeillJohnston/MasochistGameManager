package io.github.NeillJohnston.MasochistGameManager;

import io.github.NeillJohnston.MasochistGameManager.gamemode.Gamemode;
import io.github.NeillJohnston.MasochistGameManager.gamemode.PlayerTracker;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.UUID;

/**
 * :D --> D:
 *
 * @author Neill Johnston
 */
public class MasochistGameManager extends JavaPlugin implements Listener {

    public static Location spawn = null;
    public static HashMap<String, World> games = null;
    public static HashMap<UUID, PlayerTracker> trackers = null;

    @Override
    public void onEnable() {

        // Register event listening.
        getServer().getPluginManager().registerEvents(this, this);
        games = new HashMap<>();
        trackers = new HashMap<>();

        // Add command executors.
        this.getCommand("dingo").setExecutor(new DingoExecutor(this));
        this.getCommand("map").setExecutor(new MapExecutor(this));
        this.getCommand("game").setExecutor(new GameExecutor(this));

        getLogger().info("Enabled MasochistGameManager.");

    }

    /**
     * When a player joins, put them at either the lobby spawn (default) or the spawn of the current map.
     *
     * @param event
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {

        // Get the player and TP to 0,0 at the lobby.
        final Player p = event.getPlayer();
        if(spawn == null)
            p.teleport(new Location(Bukkit.getServer().getWorld("world"), 0.5, 4.0, 0.5));
        else
            p.teleport(spawn);
        p.setVelocity(new Vector(0, 0, 0));
        p.getInventory().clear();

        // Begin tracking the player.


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

    /**
     * Convenience method, creates a location from a world and double[3].
     *
     * @param world     World to use
     * @param coords    Coordinates of the new location
     */
    public static Location locationFromCoords(World world, double[] coords) {

        return new Location(world, coords[0], coords[1], coords[2]);

    }

}
