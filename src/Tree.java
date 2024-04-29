import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents a single tree within a forest simulation.
 * This class manages the tree's species, planting year, current height, and annual growth rate.
 */
class Tree implements Serializable {
    private static final long serialVersionUID = 1L;

    TreeSpecies species;
    int plantingYear;
    double height;
    double growthRate;

    /**
     * Constructs a new Tree instance.
     * @param species The species of the tree as a string, which will be converted to a {@link TreeSpecies} enum value.
     * @param plantingYear The year the tree was planted.
     * @param height The initial height of the tree in meters.
     * @param growthRate The initial annual growth rate of the tree as a percentage.
     */
    public Tree(String species, int plantingYear, double height, double growthRate) {
        this.species = TreeSpecies.valueOf(species.toUpperCase());
        this.plantingYear = plantingYear;
        this.height = height;
        this.growthRate = growthRate;
    }

    /**
     * Simulates the growth of the tree over one year.
     * The height of the tree increases based on its current growth rate.
     */
    public void grow() {
        height += height * (growthRate / 100.0);
    }

    /**
     * Returns a string representation of the tree, including its species, planting year, height, and growth rate.
     * @return A formatted string containing details about the tree.
     */
    @Override
    public String toString() {
        return String.format("%s %d %.2f %.1f", species, plantingYear, height, growthRate);
    }
} // End of Tree class

