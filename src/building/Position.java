package building;

import gui.RunningPoint;

/**
 * User: Roman V.F.
 * Date: 01.07.2020
 * Time: 13:44
 */
public class Position {

    private final int toRight;
    private final int toDown;

    public Position(int toDown, int toRight) {
        this.toRight = toRight;
        this.toDown = toDown;
    }

    public int getToRight() {
        return toRight;
    }

    public int getToDown() {
        return toDown;
    }

    public boolean isSamePlace(Position position) {
        return (position.getToRight() == getToRight() && position.getToDown() == getToDown());
    }


    @Override
    public String toString() {
        return "building.Position{" +
                "toRight=" + toRight +
                ", toDown=" + toDown +
                '}';
    }
}
