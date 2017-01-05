package io.github.NeillJohnston.MasochistGameManager;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

/**
 * /maplist command: list all available maps.
 *
 * @author Neill Johnston
 */
public class MaplistExecutor implements CommandExecutor {

    private final MasochistGameManager plugin;
    private final File mapsPath;

    /**
     * Construct the MaplistExecutor command.
     *
     * @param plugin    The Bukkit plugin instance
     */
    public MaplistExecutor(MasochistGameManager plugin) {

        this.plugin = plugin;
        this.mapsPath = new File(".\\maps");

    }

    /**
     * Handle execution of /maplist.
     *
     * @param commandSender Who sent the command
     * @param command       The command itself
     * @param s             -
     * @param strings       Argument array
     * @return True if the command executed correctly
     */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        // Filter out non-directory files that do not contain map.yml
        String[] verifiedMaps = mapsPath.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                File f = new File(dir, name);
                return f.isDirectory() && new File(f, "map.yml").exists();
            }
        });

        // Send the message
        commandSender.sendMessage(StringUtils.join(verifiedMaps, ", "));

        return true;

    }
}
