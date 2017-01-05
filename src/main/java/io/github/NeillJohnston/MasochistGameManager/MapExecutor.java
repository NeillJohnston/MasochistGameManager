package io.github.NeillJohnston.MasochistGameManager;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ObjectUtils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.WorldCreator;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

}

/**
 * Convenience class to load a map.
 */
class MapLoader {

    private final File sourcePath, targetPath, sourceYml;
    private final MapYml mapYml;

    MapLoader(String name) {

        // Create the from- and to- paths for the world
        sourcePath = new File(".\\maps\\" + name);
        targetPath = new File(".\\" + MapExecutor.WORLD);
        sourceYml = new File(".\\maps\\" + name + "\\map.yml");
        mapYml = new MapYml();

        // Load map options from map.yml
        try {

            // Turn the YAML file into a hashmap with String-Object pairs
            HashMap<String, Object> ymlHashMap = (HashMap<String, Object>) new Yaml().load(new FileInputStream(sourceYml));

            // Place settings in mapYml
            mapYml.author = (String) ymlHashMap.get("author");
            mapYml.name = (String) ymlHashMap.get("name");
            mapYml.x = (Double) ymlHashMap.get("x");
            mapYml.y = (Double) ymlHashMap.get("y");
            mapYml.z = (Double) ymlHashMap.get("z");

        } catch(FileNotFoundException e) { Bukkit.getLogger().info("Map does not have a map.yml!"); }

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

            // Re-load the temp world, now from the new files
            World w = Bukkit.getServer().createWorld(new WorldCreator(MapExecutor.WORLD));
            for(Player p : Bukkit.getServer().getOnlinePlayers())
                p.teleport(new Location(w, mapYml.x, mapYml.y, mapYml.z));

            return true;

        } catch(IOException e) { e.printStackTrace(); }

        return false;

    }

    /**
     * Helper class to hold map settings.
     */
    private class MapYml {

        public String name;
        public String author;
        public double x, y, z;

    }

}
