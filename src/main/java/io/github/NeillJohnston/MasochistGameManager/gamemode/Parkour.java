package io.github.NeillJohnston.MasochistGameManager.gamemode;

import io.github.NeillJohnston.MasochistGameManager.MapYml;
import io.github.NeillJohnston.MasochistGameManager.MasochistGameManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Parkour gamemode manager.
 *
 * @author Neill Johnston
 */
public class Parkour implements Gamemode {

    /**
     * Default commands to execute when loading a map
     */
    public final static String[] LOAD_COMMANDS = {
            "gamerule doMobSpawning false",
            "difficulty 0",
            "gamerule doDaylightCycle false",
            "time set 6000",
    };

    private final MasochistGameManager plugin;
    private final World world;
    private final MapYml mapYml;

    private final Location startButton;
    private final Location endButton;

    public Parkour(MasochistGameManager plugin, World world, MapYml mapYml) {

        this.plugin = plugin;
        this.world = world;
        this.mapYml = mapYml;

        startButton = new Location(world, mapYml.startx, mapYml.starty, mapYml.startz);
        endButton = new Location(world, mapYml.endx, mapYml.endy, mapYml.endz);

    }

    @Override
    public void start() {

        // Set default world settings/gamerules
        for (String s : LOAD_COMMANDS)
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), s);

    }

    @Override
    public void pause() {
        //
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK &&
                event.getClickedBlock().getType() == Material.STONE_BUTTON) {

            Block b = event.getClickedBlock();
            if(b.getLocation().equals(startButton))
                Bukkit.getLogger().info("START BUTTON PRESSED");

            Bukkit.getLogger().info(event.getPlayer().getName() + " pressed a button");

        }

    }

}
