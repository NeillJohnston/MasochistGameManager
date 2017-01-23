package io.github.NeillJohnston.MasochistGameManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * /play [game]: go to a different game.
 *
 * @author Neill Johnston
 */
public class PlayExecutor implements CommandExecutor {


    /**
     * Handle execution of /play.
     *
     * @param commandSender Who sent the command
     * @param command       The command itself
     * @param s             -
     * @param strings       Argument array
     * @return True if the game is created/destroyed without error.
     */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(commandSender instanceof Player) {

            Player p = ((Player) commandSender).getPlayer();


        }

        return false;

    }

}
