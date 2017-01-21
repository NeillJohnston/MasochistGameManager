package io.github.NeillJohnston.MasochistGameManager;

import io.github.NeillJohnston.MasochistGameManager.gamemode.Gamemode;
import io.github.NeillJohnston.MasochistGameManager.gamemode.Parkour;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
 * Load a map from a file in the maps directory.
 *
 * @author Neill Johnston
 */
class MapLoader {

    /**
     * Gamemode possibilities, I have no better place to put these.
     */
    public static final String GAMEMODE_PKR = "pkr";
    public static final String GAMEMODE_PDM = "pdm";
    public static final String GAMEMODE_NONE = "none";

    private final MasochistGameManager plugin;
    private String name, id;

    private File sourcePath, targetPath, sourceYml;
    private MapYml mapYml;

    /**
     * Construct a MapLoader.
     *
     * @param plugin    Instance of the Bukkit plugin
     * @param name      World name (in /maps)
     * @param id        World id
     */
    public MapLoader(MasochistGameManager plugin, String name, String id) {

        this.plugin = plugin;
        this.name = name;
        this.id = WORLD_PREFIX + id;

        // Create the from- and to- paths for the world
        sourcePath = new File(MAPS_PATH + "\\" + name);
        targetPath = new File(".\\" + this.id);
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
     * TODO: Allow tracking of which game players are in, only teleport the relevant players.
     *
     * @return The newly created world
     */
    public World loadMap() {

        try {

            // TP all players to the lobby so that we can safely unload the world
            for(Player p : Bukkit.getServer().getOnlinePlayers())
                p.teleport(Bukkit.getServer().getWorld(LOBBY_WORLD).getSpawnLocation());

            // Copy world files over
            FileUtils.copyDirectory(sourcePath, targetPath);
            Bukkit.getLogger().info("Copied world.");

            // Create a temp world, unload it immediately - this is done solely to get a usable World object
            World temp = new WorldCreator(id).createWorld();
            Bukkit.getServer().unloadWorld(temp, false);

            // Re-load the temp world, now from the new files - also set world spawn
            World world = Bukkit.getServer().createWorld(new WorldCreator(id));
            MasochistGameManager.spawn = MasochistGameManager.locationFromCoords(world, mapYml.spawn);
            for (Player p : Bukkit.getServer().getOnlinePlayers())
                p.teleport(MasochistGameManager.spawn);

            // Create a Gamemode based on the gamemode in map.yml
            Gamemode gamemode = null;
            switch(mapYml.gamemode) {

                case GAMEMODE_PKR:
                    gamemode = new Parkour(plugin, world, mapYml);
                    break;

                case GAMEMODE_NONE:
                    gamemode = new Gamemode(plugin, world, mapYml);
                    break;

                default:
                    gamemode = new Parkour(plugin, world, mapYml);
                    break;

            }

            // Register and start the Gamemode
            Bukkit.getServer().getPluginManager().registerEvents(gamemode, plugin);
            gamemode.start();

            return world;

        } catch(NullPointerException e) {
            Bukkit.getLogger().info("map.yml may be missing something");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
    }

        return null;

    }

}