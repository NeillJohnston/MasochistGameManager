package io.github.NeillJohnston.MasochistGameManager;

/**
 * Created by Neill on 1/8/2017.
 */

import io.github.NeillJohnston.MasochistGameManager.gamemode.Gamemode;
import io.github.NeillJohnston.MasochistGameManager.gamemode.Parkour;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static io.github.NeillJohnston.MasochistGameManager.MapExecutor.LOBBY_WORLD;
import static io.github.NeillJohnston.MasochistGameManager.MapExecutor.MAPS_PATH;
import static io.github.NeillJohnston.MasochistGameManager.MapExecutor.WORLD_PREFIX;

/**
 * Convenience class to load a map.
 */
class MapLoader {

    private final MasochistGameManager plugin;
    private String name;

    private File sourcePath, targetPath, sourceYml;
    private MapYml mapYml;

    MapLoader(MasochistGameManager plugin, String name) {

        this.plugin = plugin;
        this.name = name;

        // Create the from- and to- paths for the world
        sourcePath = new File(MAPS_PATH + "\\" + name);
        targetPath = new File(".\\" + WORLD_PREFIX);
        sourceYml = new File(MAPS_PATH + "\\" + name + "\\map.yml");

        // Load map options from map.yml
        try {

            // Turn the YAML file into a MapYml object
            mapYml = new MapYml(sourceYml);

        } catch(FileNotFoundException e) {
            Bukkit.getLogger().info("Map does not have a map.yml!");
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Load the new map and TP all online players to it.
     *
     * @return True if the map was successfully copied, false otherwise
     */
    boolean loadMap() {

        try {

            // TP all players to the lobby so that we can safely unload the world
            for(Player p : Bukkit.getServer().getOnlinePlayers())
                p.teleport(Bukkit.getServer().getWorld(LOBBY_WORLD).getSpawnLocation());

            // Create a temp world, unload it immediately - this is done solely to get a usable World object
            plugin.getServer().unloadWorld(new WorldCreator(name).createWorld(), false);

            // Copy
            FileUtils.copyDirectory(sourcePath, targetPath);
            Bukkit.getLogger().info("Copied world.");

            // Re-load the temp world, now from the new files - also set world spawn
            World world = Bukkit.getServer().createWorld(new WorldCreator(name));
            MasochistGameManager.spawn = MasochistGameManager.locationFromCoords(world, mapYml.spawn);
            for (Player p : Bukkit.getServer().getOnlinePlayers())
                p.teleport(MasochistGameManager.spawn);

            try {

                // Create a Gamemode based on the game type
                Gamemode gamemode = null;
                switch(mapYml.gamemode) {

                    case MapYml.GAMEMODE_PKR:
                        gamemode = new Parkour(plugin, world, mapYml);
                        break;

                    case MapYml.GAMEMODE_PDM:
                        // Should create a PDM gamemode object, doesn't exist yet
                        break;

                    default:
                        gamemode = new Parkour(plugin, world, mapYml);
                        break;

                }

                if(gamemode == null)
                    gamemode = new Parkour(plugin, world, mapYml);

                // Register and start the Gamemode
                Bukkit.getServer().getPluginManager().registerEvents(gamemode, plugin);
                gamemode.start();

                return true;

            } catch(NullPointerException e) {
                Bukkit.getLogger().info("map.yml may be missing something");
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;

    }

}