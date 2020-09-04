package finding;

import building.Maze;
import building.Position;
import gui.CanDrawItSelf;

import java.util.List;

/**
 * User: Roman V.F.
 * Date: 02.09.2020
 * Time: 16:23
 */
public abstract class Seeker {
    private final Maze maze;

    public Seeker(Maze maze) {
        this.maze = maze;
    }

    public Maze getMaze() {
        return maze;
    }

    public boolean findWay(Position start, Position exit, int[][] discoverField) {
        return findWay(start, exit, discoverField, null);
    }
    public abstract boolean findWay(Position start, Position exit, int[][] discoverField, List<CanDrawItSelf> goPositions);
}
