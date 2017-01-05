package io.github.NeillJohnston.MasochistGameManager;

import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import java.io.File;

/**
 * /map [name] command: Switch the server map to the specified map
 *
 * @author Neill Johnston
 */
public class MapExecutor implements CommandExecutor {

    private final MasochistGameManager plugin;

    public MapExecutor(MasochistGameManager plugin) {

        this.plugin = plugin;

    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(strings.length >= 1) {

            try {

                World temp = new WorldCreator("temp").createWorld();
                plugin.getServer().unloadWorld(temp, false);

                // Create the from- and to- paths for the world.
                File sourcePath = new File(".\\maps\\" + strings[0]);
                File worldPath = new File(".\\temp");
                // Copy.
                FileUtils.copyDirectory(sourcePath, worldPath);
                plugin.getLogger().info("Copied world.");

                World w = plugin.getServer().createWorld(new WorldCreator("temp"));
                for(Player p : plugin.getServer().getOnlinePlayers())
                    p.teleport(new Location(w, -1000, 50, 0));

            } catch(Exception e) {

                e.printStackTrace();

            }

        }

        return false;

    }

}
