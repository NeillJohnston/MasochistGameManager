package io.github.NeillJohnston.MasochistGameManager;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * :D --> D:
 *
 * @author Neill Johnston
 */
public class MasochistGameManager extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Enables MGM!");
        this.getCommand("dingo").setExecutor(new DingoExecutor(this));
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabled MGM!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Default: return false
        return false;

    }
}
