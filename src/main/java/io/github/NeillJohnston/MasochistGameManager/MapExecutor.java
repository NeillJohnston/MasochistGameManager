package io.github.NeillJohnston.MasochistGameManager;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * /map [name] command: Switch the server map to the specified map
 *
 * @author Neill Johnston
 */
public class MapExecutor implements CommandExecutor {

    // Constants
    public final static String LOBBY_WORLD = "world";
    public final static String WORLD_PREFIX = "mgmworld_";
    public final static String MAPS_PATH = ".\\maps";

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

        if(strings.length == 0) {

            Bukkit.getServer().dispatchCommand(commandSender, "maplist");
            return true;

        } else if(strings.length >= 1) {

            // Load the world with a MapLoader
            MapLoader mapLoader = new MapLoader(plugin, strings[0], "0");
            mapLoader.loadMap();

            return true;

        }

        return false;

    }

}
