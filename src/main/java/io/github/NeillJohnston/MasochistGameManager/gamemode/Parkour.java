package io.github.NeillJohnston.MasochistGameManager.gamemode;

import io.github.NeillJohnston.MasochistGameManager.MapYml;
import io.github.NeillJohnston.MasochistGameManager.MasochistGameManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.world.WorldLoadEvent;

import java.util.HashMap;
import java.util.UUID;

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

    private HashMap<UUID, Long> playerTimeMap;

    /**
     * Initialize the manager with required attributes from map.yml.
     *
     * @param plugin    Bukkit plugin instance
     * @param world     Current world that the pkr game is being run in
     * @param mapYml    Map's map.yml object
     * @throws NullPointerException When mapYml does not have all the required attributes
     */
    public Parkour(MasochistGameManager plugin, World world, MapYml mapYml) throws NullPointerException {

        this.plugin = plugin;
        this.world = world;
        this.mapYml = mapYml;
        this.playerTimeMap = new HashMap<>();

        startButton = MasochistGameManager.locationFromCoords(world, mapYml.startButton);
        endButton = MasochistGameManager.locationFromCoords(world, mapYml.endButton);

    }

    /**
     *
     */
    @Override
    public void start() {

        // Set default world settings/gamerules
        for (String s : LOAD_COMMANDS)
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), s);

        // Schedule a task that updates player times (in tab) every tick
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {

            @Override
            public void run() {

                for(Player p : Bukkit.getServer().getOnlinePlayers()) {

                    if(playerTimeMap.containsKey(p.getUniqueId())) {

                        double time = (System.currentTimeMillis() - playerTimeMap.get(p.getUniqueId())) / 1000.0;
                        p.setPlayerListName(String.format("%s | %.2f", p.getName(), time));

                    }

                }

            }
        }, 0L, 1L);

    }

    /**
     * Listen for player interaction with the start/end buttons.
     *
     * @param event Event of player interacting with block
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if(event.getAction() == Action.RIGHT_CLICK_BLOCK &&
                event.getClickedBlock().getType() == Material.STONE_BUTTON) {

            Block b = event.getClickedBlock();

            // If the start button was clicked
            if(b.getLocation().equals(startButton)) {

                // Start keeping track of the player's time
                Bukkit.getLogger().info(player.getName() + " started");
                playerTimeMap.put(uuid, System.currentTimeMillis());

            // Else, if the end button was clicked
            } else if(b.getLocation().equals(endButton) && playerTimeMap.containsKey(uuid)) {

                // Send a message for the player's time
                double time = (System.currentTimeMillis() - playerTimeMap.get(uuid)) / 1000.0;
                Bukkit.getLogger().info(player.getName() + " finished (" + time +"s)");
                player.sendMessage("Finished in " + String.format("%.2f", time) + "s.");

                // Remove player from the time map
                playerTimeMap.remove(uuid);

            }


        }

    }

    /**
     * If a player is under the map with no chance of getting back, kill them.
     *
     * @param event Player move event
     */
    @EventHandler
    public void onPlayerMovement(PlayerMoveEvent event) {

        Player player = event.getPlayer();
        if(player.getLocation().getBlockY() < -5)
            player.setHealth(0.0);

    }

}
