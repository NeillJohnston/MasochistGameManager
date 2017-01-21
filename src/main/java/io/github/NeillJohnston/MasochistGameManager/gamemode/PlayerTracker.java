package io.github.NeillJohnston.MasochistGameManager.gamemode;

import java.util.UUID;

/**
 * @author Neill Johnston
 */
public class PlayerTracker {

    final UUID uuid;
    String game;

    public PlayerTracker(UUID uuid) {

        this.uuid = uuid;

    }

    /**
     * Return this tracker's UUID.
     *
     * @return uuid
     */

    /**
     * Set which game a player is in.
     *
     * @param game  The game the player is in right now
     */
    public void setGame(String game) {}

    /**
     * Get the game a player is in.
     *
     * @return game
     */
    public String getGame() { return null; }

}
