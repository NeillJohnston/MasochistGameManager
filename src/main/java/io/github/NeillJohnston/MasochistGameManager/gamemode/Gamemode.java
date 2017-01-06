package io.github.NeillJohnston.MasochistGameManager.gamemode;

import org.bukkit.event.Listener;

/**
 * Basic specification for a gamemode manager.
 * Would have been called "GamemodeManager" but that's annoyingly long.
 */
public interface Gamemode extends Listener {

    public void start();
    public void pause();

}
