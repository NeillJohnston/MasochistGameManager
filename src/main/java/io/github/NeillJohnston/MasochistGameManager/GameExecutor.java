package io.github.NeillJohnston.MasochistGameManager;

import io.github.NeillJohnston.MasochistGameManager.gamemode.Gamemode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * /game [create,destroy,_] [name]: Create a new game
 *
 * @author Neill Johnston
 */
public class GameExecutor implements CommandExecutor {

    /**
     * Constants.
     */
    public static final String LOBBY = "lobby";

    private final MasochistGameManager plugin;

    /**
     * Construct the GameExecutor.
     *
     * @param plugin    Instance of the Bukkit plugin
     */
    public GameExecutor(MasochistGameManager plugin) {

        this.plugin = plugin;

    }

    /**
     * Handle execution of /game.
     *
     * @param commandSender Who sent the command
     * @param command       The command itself
     * @param s             -
     * @param strings       Argument array
     * @return True if the game is created/destroyed without error.
     */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(strings.length >= 2) {

            String operator = strings[0];
            String name = strings[1];

            // Switch through the different operators of /game: create, destroy
            switch(operator) {

                case "create":
                    // When a game is created, teleport relevant players to a new lobby world.
                    MasochistGameManager.games.put(name, new MapLoader(plugin, LOBBY, name).loadMap());
                    return true;

                case "destroy":
                    return MasochistGameManager.games.remove(name) != null;

                default:
                    commandSender.sendMessage("I don't recognize that command. /help game for more info.");
                    return false;

            }

        } else {

            String list = "";
            for(String name : MasochistGameManager.games.keySet())
                list += name + ", ";
            commandSender.sendMessage(list);

        }

        return false;

    }
}
