package io.github.NeillJohnston.MasochistGameManager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

/**
 * :D --> D:
 *
 * @author Neill Johnston
 */
public class MasochistGameManager extends JavaPlugin implements Listener {

    public static Location spawn = null;

    @Override
    public void onEnable() {

        getLogger().info("Enabled MasochistGameManager.");

        // Register event listening.
        getServer().getPluginManager().registerEvents(this, this);

        // Add command executors.
        this.getCommand("dingo").setExecutor(new DingoExecutor(this));
        this.getCommand("map").setExecutor(new MapExecutor(this));
        this.getCommand("maplist").setExecutor(new MaplistExecutor(this));

    }

    /**
     * TODO This shit is NOT working.
     *
     * @param worldName
     * @param id
     * @return
     */
    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {

        return new BlankChunkGenerator();

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

    }

    /**
     * Once the world loads, set the correct spawn.
     *
     * @param event
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onWorldLoad(WorldLoadEvent event) {

        spawn = new Location(Bukkit.getServer().getWorld("world"), 0.5, 4.0, 0.5);

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
