package io.github.NeillJohnston.MasochistGameManager;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * :D --> D:
 *
 * @author Neill Johnston
 */
public class MasochistGameManager extends JavaPlugin {
    private static long worldId;

    @Override
    public void onEnable() {

        getLogger().info(":D Enabled MasochistGameManager!");

        // Add command executors.
        this.getCommand("dingo").setExecutor(new DingoExecutor(this));
        this.getCommand("map").setExecutor(new MapExecutor(this));
        this.getCommand("maplist").setExecutor(new MaplistExecutor(this));

    }

    @Override
    public void onDisable() {

        getLogger().info("D: Disabled MasochistGameManager!");

    }

    // Generate a world id.
    public long nextWorldId() {

        worldId++;
        return worldId;

    }

    // Make the default world gen a blank chunk generator.
    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {

        return new BlankChunkGenerator();

    }

}
