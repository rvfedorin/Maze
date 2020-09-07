package gui;

import building.Position;

import java.awt.*;

/**
 * User: Roman V.F.
 * Date: 26.07.2020
 * Time: 14:56
 */
public class RunningPoint implements CanDrawItSelf {
    public enum TypePosition{DEAD_END("dead-end"), END("end"), INTERSECTION("intersection"), FLAT("flat");
        String name;

        TypePosition(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private Position myPosition;
    private Color color;
    private int roundSize;
    private RunningPoint.TypePosition type;
    private RunningPoint fromIntersection;
    private int waysFromMe;

    public RunningPoint(int x, int y, boolean main, Color color) {
        this(new Position(y, x), main, color);
    }

    public RunningPoint(Position myPosition, boolean main, Color color) {
        setRoundMain(main);
        this.myPosition = myPosition;
        this.color = color;
        this.type = RunningPoint.TypePosition.FLAT;
        this.waysFromMe = 0;
    }

    private int getCoordX(int x) {
        int coordX = FieldJPanel.startOffsetY + (x * Cell.CELL_SIZE);
        int newX = coordX + Cell.CELL_SIZE / 2 - roundSize / 2;
        return newX;
    }

    private int getCoordY(int y) {
        int coordY = FieldJPanel.startOffsetY + (y * Cell.CELL_SIZE);
        int newY = coordY + Cell.CELL_SIZE / 2 - roundSize / 2;
        return newY;
    }

    public void setRoundMain(boolean main) {
        if (main) {
            roundSize = Cell.CELL_SIZE - (Cell.CELL_SIZE * 40 / 100);
        } else {
            roundSize = Cell.CELL_SIZE - (Cell.CELL_SIZE * 60 / 100);
        }
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getWaysFromMe() {
        return waysFromMe;
    }

    public void setWaysFromMe(int waysFromMe) {
        this.waysFromMe = waysFromMe;
    }

    public RunningPoint getFromIntersection() {
        return fromIntersection;
    }

    public void setFromIntersection(RunningPoint fromIntersection) {
        this.fromIntersection = fromIntersection;
    }

    public RunningPoint.TypePosition getType() {
        return type;
    }

    public void setType(RunningPoint.TypePosition type) {
        this.type = type;
    }


    @Override
    public void drawMe(Graphics2D g2) {
        int x = myPosition.getToRight();
        int y = myPosition.getToDown();
        Color tempColor = g2.getColor();
        g2.setColor(color);
        g2.fillOval(getCoordX(x), getCoordY(y), roundSize, roundSize);
        g2.setColor(tempColor);
    }

    public Position getMyPosition() {
        return myPosition;
    }

    @Override
    public String toString() {
        return "RunningPoint{" +
                "position=" + myPosition +
                ", color=" + color +
                ", roundSize=" + roundSize +
                ", type=" + type +
                '}';
    }


}
