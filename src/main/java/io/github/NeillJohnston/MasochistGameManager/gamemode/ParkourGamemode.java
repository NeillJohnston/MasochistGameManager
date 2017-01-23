package io.github.NeillJohnston.MasochistGameManager.gamemode;

import io.github.NeillJohnston.MasochistGameManager.MapYml;
import io.github.NeillJohnston.MasochistGameManager.MasochistGameManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.UUID;

/**
 * ParkourGamemode gamemode manager.
 *
 * @author Neill Johnston
 * @deprecated Deprecated along with Gamemode.
 */
public class ParkourGamemode extends Gamemode {

    /**
     * Default commands to execute when loading a map
     */
    public final static String[] LOAD_COMMANDS = {
            "gamerule doMobSpawning false",
            "difficulty 0",
            "gamerule doDaylightCycle false",
            "time set 6000",
    };

    private final Location startButton, endButton;
    private final Material checkpointMat, backpointMat;

    private HashMap<UUID, ParkourPlayerTracker> trackers;

    /**
     * Initialize the manager with required attributes from map.yml.
     *
     * @param plugin    Bukkit plugin instance
     * @param world     Current world that the pkr game is being run in
     * @param mapYml    Map's map.yml object
     * @throws NullPointerException When mapYml does not have all the required attributes
     */
    public ParkourGamemode(MasochistGameManager plugin, World world, MapYml mapYml, String gameId) throws NullPointerException {

        super(plugin, world, mapYml, gameId);

        this.trackers = new HashMap<>();

        startButton = MasochistGameManager.locationFromCoords(world, mapYml.coordinates("start_button"));
        endButton = MasochistGameManager.locationFromCoords(world, mapYml.coordinates("end_button"));

        checkpointMat = Material.getMaterial(mapYml.get("mat_checkpoint", "EYE_OF_ENDER"));
        backpointMat = Material.getMaterial(mapYml.get("mat_backpoint", "ENDER_PEARL"));

    }

    /**
     * Start running the gamemode
     */
    @Override
    public void start() {

        // Set default world settings/gamerules
        for(String s : LOAD_COMMANDS)
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), s);

        // Schedule a task that updates player times (in tab) every tick
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {

            @Override
            public void run() {

                // Loop through each online player, update each player time
                for(Player p : Bukkit.getServer().getOnlinePlayers()) {

                    UUID uuid = p.getUniqueId();
                    long time = trackers.get(uuid).getTime();
                    if(time != -1) {

                        double timeInSeconds = time / 1000.0;
                        p.setPlayerListName(String.format("%s >> %.2f", p.getName(), timeInSeconds));

                    }

                }

            }
        }, 0L, 1L);

    }

    /**
     * Track a player, specifically for pkr
     *
     * @param player    Player to be added
     * @return The new tracker
     */
    @Override
    public ParkourPlayerTracker track(Player player) {

        ParkourPlayerTracker tracker = new ParkourPlayerTracker(player.getUniqueId(), gameId);
        trackers.put(player.getUniqueId(), tracker);
        return tracker;

    }

    /**
     * Remove players who log out from the tracker.
     *
     * @param event The player leaving event
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        trackers.remove(event.getPlayer().getUniqueId());

    }

    /**
     * Listen for player interaction with the start/end buttons.
     * Listen for player interaction with the checkpoint stick.
     *
     * @param event Event of player interacting with block
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        Action action = event.getAction();
        Block block = event.getClickedBlock();

        // Handle button presses
        if(action == Action.RIGHT_CLICK_BLOCK && block.getType() == Material.STONE_BUTTON) {

            // If the start button was clicked
            if(block.getLocation().equals(startButton)) {

                // Start keeping track of the player's time
                Bukkit.getLogger().info(player.getName() + " started");
                player.sendMessage("Started!");
                trackers.get(uuid).setTime();

            // Else, if the end button was clicked
            } else if(block.getLocation().equals(endButton) && trackers.get(uuid).getTime() != -1) {

                // Send a message for the player's time
                double time = trackers.get(uuid).getTime() / 1000.0;
                Bukkit.getLogger().info(player.getName() + " finished (" + time +"s)");
                player.sendMessage("Finished in " + String.format("%.2f", time) + "s.");

                // Remove player from the time map
                trackers.get(uuid).resetTime();

            }

        }

        // Handle the checkpoint stick
        if(player.getInventory().getItemInHand().getType() == Material.STICK &&
                trackers.get(uuid).CHECKPOINT_FLAG) {

            // If right-clicking, save a checkpoint
            if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK &&
                    player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR) {

                trackers.get(uuid).saveCheckpoint(player.getLocation());
                player.sendMessage((double) trackers.get(uuid).getTime() / 1000.0 + " - Checkpoint saved.");

                // If left-clicking, load a checkpoint
            } else if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK &&
                    trackers.get(uuid).getCheckpoint() != null) {

                player.teleport(trackers.get(uuid).getCheckpoint());
                player.sendMessage((double) trackers.get(uuid).getTime() / 1000.0 + " - Checkpoint loaded.");

            }

        }

    }

    /**
     * If a player is under the map with no chance of getting back, kill them.
     * If a player hits a special block, do a special action.
     *
     * @param event Player move event
     */
    @EventHandler
    public void onPlayerMovement(PlayerMoveEvent event) {

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN); // Block that the player stands on

        if(player.getLocation().getBlockY() < -10) {

            player.teleport(trackers.get(uuid).getCheckpoint());
            player.setVelocity(new Vector(0.0, 0.0, 0.0));

        }

        // If the player is standing on a checkpoint material, save a checkpoint
        if(block.getType() == checkpointMat) {

            trackers.get(uuid).saveCheckpoint(player.getLocation().getBlock().getLocation());
            player.sendMessage((double) trackers.get(uuid).getTime() / 1000.0 + " - Checkpoint saved.");

        // If the player is standing on a backpoint material, load the last checkpoint
        } else if(block.getType() == backpointMat) {

            player.teleport(trackers.get(uuid).getCheckpoint());
            player.setVelocity(new Vector(0.0, 0.0, 0.0));
            player.sendMessage((double) trackers.get(uuid).getTime() / 1000.0 + " - Checkpoint loaded.");

        }

    }

    /**
     * Make sure that players cannot be damaged.
     */
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {

        if(event.getEntityType().equals(EntityType.PLAYER) && (event.getCause() == EntityDamageEvent.DamageCause.VOID ||
                event.getCause() == EntityDamageEvent.DamageCause.FALL))
            event.setCancelled(true);

    }

}
