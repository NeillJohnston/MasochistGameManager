package io.github.NeillJohnston.MasochistGameManager;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Helper class to hold map settings. Basically just a fancy data structure.
 * Used for loading map preferences and giving those to the gamemode manager.
 *
 * @author Neill Johnston
 */
public class MapYml {

    private final HashMap<String, Object> mapYml;

    // Basic settings
    public final String name;
    public final String author;
    public final double[] spawn;
    public final String gamemode;

    /**
     * Generate the MapYml object from a SnakeYaml-generated hashmap.
     *
     * @param sourceYml    File to grab yaml from
     */
    public MapYml(File sourceYml) throws FileNotFoundException {

        // Initialize mapYml HashMap
        this.mapYml = (HashMap<String, Object>) new Yaml().load(new FileInputStream(sourceYml));

        // Basic settings
        author = (String) mapYml.get("author");
        name = (String) mapYml.get("name");
        spawn = coordinates("spawn");
        gamemode = (String) mapYml.get("gamemode");

    }

    /**
     * Convenience method to get coordinates (double array) from the three-item ArrayList
     * that SnakeYaml will generate.
     *
     * @param id    The name of the coordinate array in map.yml
     */
    public double[] coordinates(String id) {

        ArrayList<Double> coordsMapYml = (ArrayList<Double>) mapYml.get(id);
        double[] coords = new double[coordsMapYml.size()];
        for(int i = 0; i < coordsMapYml.size(); i++)
            coords[i] = coordsMapYml.get(i);

        return coords;

    }

    /**
     * Convenience method to return an item from the map, or default to def if it doesn't exist.
     */
    public <T> T get(String id, T def) {

        return (mapYml.get(id) != null) ? (T) mapYml.get(id) : def;

    }

}
