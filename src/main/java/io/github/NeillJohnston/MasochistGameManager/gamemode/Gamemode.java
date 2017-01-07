package io.github.NeillJohnston.MasochistGameManager.gamemode;

import io.github.NeillJohnston.MasochistGameManager.MapYml;
import io.github.NeillJohnston.MasochistGameManager.MasochistGameManager;
import org.bukkit.World;
import org.bukkit.event.Listener;

/**
 * Basic specification for a gamemode manager.
 * Would have been called "GamemodeManager" but that's annoyingly long.
 */
public interface Gamemode extends Listener {

    /**
     * Starts the gamemode.
     */
    public void start();

}
