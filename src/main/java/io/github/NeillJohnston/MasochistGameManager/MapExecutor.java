package io.github.NeillJohnston.MasochistGameManager;

import io.github.NeillJohnston.MasochistGameManager.gamemode.Parkour;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

/**
 * /map [name] command: Switch the server map to the specified map
 *
 * @author Neill Johnston
 */
public class MapExecutor implements CommandExecutor {

    // Constants
    public final static String WORLD = "world_course";

    private final MasochistGameManager plugin;

    /**
     * Construct the MapExecutor command.
     *
     * @param plugin    The Bukkit plugin instance
     */
    public MapExecutor(MasochistGameManager plugin) {

        this.plugin = plugin;

    }

    /**
     * Handle execution of /map.
     *
     * @param commandSender Who sent the command
     * @param command       The command itself
     * @param s             -
     * @param strings       Argument array
     * @return True if the map is loaded without error
     */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(strings.length >= 1) {

            // TP all players to the lobby
            for(Player p : Bukkit.getServer().getOnlinePlayers())
                p.teleport(Bukkit.getServer().getWorld("world").getSpawnLocation());

            // Create a temp world, unload it immediately - this is done solely to get a usable World object
            World world = new WorldCreator(WORLD).createWorld();
            plugin.getServer().unloadWorld(world, false);

            // Load the world with a MapLoader
            MapLoader mapLoader = new MapLoader(strings[0]);
            mapLoader.loadMap();

            return true;

        }

        return false;

    }

    /**
     * Convenience class to load a map.
     */
    class MapLoader {

        private final File sourcePath, targetPath, sourceYml;
        private MapYml mapYml;

        MapLoader(String name) {

            // Create the from- and to- paths for the world
            sourcePath = new File(".\\maps\\" + name);
            targetPath = new File(".\\" + MapExecutor.WORLD);
            sourceYml = new File(".\\maps\\" + name + "\\map.yml");

            // Load map options from map.yml
            try {

                // Turn the YAML file into a MapYml object
                mapYml = new MapYml((HashMap<String, Object>) new Yaml().load(new FileInputStream(sourceYml)));

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

                // Copy
                FileUtils.copyDirectory(sourcePath, targetPath);
                Bukkit.getLogger().info("Copied world.");

                // Re-load the temp world, now from the new files - also set world spawn
                World world = Bukkit.getServer().createWorld(new WorldCreator(MapExecutor.WORLD));
                MasochistGameManager.spawn = MasochistGameManager.locationFromCoords(world, mapYml.spawn);
                for (Player p : Bukkit.getServer().getOnlinePlayers())
                    p.teleport(MasochistGameManager.spawn);

                try {

                    // Create a game manager based on the game type
                    switch(mapYml.gamemode) {

                        case MapYml.GAMEMODE_PKR:
                            Parkour parkour = new Parkour(plugin, world, mapYml);
                            Bukkit.getServer().getPluginManager().registerEvents(parkour, plugin);
                            parkour.start();
                            break;

                        case MapYml.GAMEMODE_PDM:
                            // Create a PDM gamemode object, etc.
                            break;

                    }

                    return true;

                } catch(NullPointerException e) {
                    Bukkit.getLogger().info("map.yml may be missing something");
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch(Exception e) {
                e.printStackTrace();
            }

            return false;

        }

    }

}
