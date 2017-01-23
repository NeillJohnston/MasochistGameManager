package io.github.NeillJohnston.MasochistGameManager;

import io.github.NeillJohnston.MasochistGameManager.gamemode.Gamemode;
import org.bukkit.World;

import java.io.File;

/**
 * Manager for a single game. Tons of methods for managing the game.
 *
 * @author Neill Johnston
 */
public class MasochistGame {

    /**
     * The game lobby.
     */
    public static final String LOBBY = "lobby";

    public final String gameId;

    public World world;

    /**
     * Construct.
     */
    public MasochistGame(String gameId) {

        this.gameId = gameId;

        this.world = new MapLoader(LOBBY, gameId).loadMap();

    }

    /**
     * Initialize self - load map, set up the game mode.
     */
    public void init() {

        // TODO this :D

    }

    public void loadMap(String mapName) {



    }

}
