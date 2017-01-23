package io.github.NeillJohnston.MasochistGameManager.gamemode;

import io.github.NeillJohnston.MasochistGameManager.MasochistGame;
import io.github.NeillJohnston.MasochistGameManager.MasochistGameManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.UUID;

/**
 * @author Neill Johnston
 */
public class PlayerTracker {

    final UUID uuid;
    private String gameId;

    public PlayerTracker(UUID uuid, String gameId) {

        this.uuid = uuid;
        this.gameId = gameId;

    }

    /**
     * Set the game a player is in.
     *
     * @param gameId    New game id
     */
    public void setGame(String gameId) {

        this.gameId = gameId;
        Bukkit.getServer().getPlayer(uuid).teleport(MasochistGameManager.games.get(gameId));

    }

    /**
     * Get the game a player is in.
     *
     * @return game
     */
    public String getGameId() {

        return gameId;

    }

    /**
     * Convenience method to get the Player behind the tracker.
     */
    public Player getPlayer() {

        return Bukkit.getPlayer(uuid);

    }

}
