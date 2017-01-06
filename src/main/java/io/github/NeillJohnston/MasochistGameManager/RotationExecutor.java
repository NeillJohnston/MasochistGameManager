package io.github.NeillJohnston.MasochistGameManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.File;

/**
 * /rotation [name] command: Starts a rotation, a constantly cycling list of maps.
 * Rotation lists are stored as YML files in ./rotation.
 */
public class RotationExecutor implements CommandExecutor {

    private final MasochistGameManager plugin;
    private final File rotationsPath;

    /**
     * Construct and set up basic variables.
     *
     * @param plugin    The Bukkit plugin instance
     */
    public RotationExecutor(MasochistGameManager plugin) {

        this.plugin = plugin;
        this.rotationsPath = new File(".\\rotations");

    }

    /**
     *
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



        }

        return false;

    }
}
