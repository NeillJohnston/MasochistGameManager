package io.github.NeillJohnston.MasochistGameManager.gamemode;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.UUID;

/**
 * @author Neill Johnston
 */
public class ParkourPlayerTracker extends PlayerTracker {

    // Flags...
    public boolean CHECKPOINT_FLAG;

    private ArrayList<Location> checkpoints;
    private long startTime;

    /**
     * Initialize UUID, flags, and other settings.
     *
     * @param uuid  Player UUID
     */
    public ParkourPlayerTracker(UUID uuid) {

        super(uuid);

        CHECKPOINT_FLAG = true;

        checkpoints = new ArrayList<>();
        startTime = -1;

    }

    /**
     * Save the most recent checkpoint.
     *
     * @param location  The location last checkpoint'd
     */
    public void saveCheckpoint(Location location) {

        if(!checkpoints.contains(location))
            checkpoints.add(location);

    }

    /**
     * Save the most recent checkpoint.
     *
     * @param location  The location last checkpoint'd
     * @param fromMap   True if the checkpoint is from the map (not a player-saved checkpoint)
     */
    public void saveCheckpoint(Location location, boolean fromMap) {

        if(!checkpoints.contains(location)) {

            checkpoints.add(location);

        }
    }

    /**
     * Get the most recent checkpoint.
     *
     * @return  The most recent checkpoint
     */
    public Location getCheckpoint() {

        if(checkpoints.size() >= 1)
            return checkpoints.get(checkpoints.size() - 1);
        else
            return null;

    }

    /**
     * Get the most recent checkpoint.
     *
     * @return  The most recent checkpoint
     */
    public Location getCheckpoint(boolean fromMap) {

        if(checkpoints.size() >= 1)
            return checkpoints.get(checkpoints.size() - 1);
        else
            return null;

    }

    /**
     * Start the timer (setting the start time to the current time), also purges checkpoints.
     */
    public void setTime() {

        startTime = System.currentTimeMillis();
        checkpoints.clear();

    }

    /**
     * Get the player's time in milliseconds.
     *
     * @return  How long the player has been running for, if not running return -1
     */
    public long getTime() {

        if(startTime != -1)
            return System.currentTimeMillis() - startTime;
        else
            return -1;

    }

    /**
     * Set the start time back to -1.
     */
    public void resetTime() {

        startTime = -1;

    }

}
