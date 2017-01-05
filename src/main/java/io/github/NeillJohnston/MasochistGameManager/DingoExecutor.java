package io.github.NeillJohnston.MasochistGameManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Neill Johnston
 */
public class DingoExecutor implements CommandExecutor {
    private final MasochistGameManager plugin;

    public DingoExecutor(MasochistGameManager plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(commandSender instanceof Player)
            commandSender.sendMessage("Dongo!");
        return true;

    }
}
