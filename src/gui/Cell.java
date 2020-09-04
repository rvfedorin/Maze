package gui;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * User: Roman V.F.
 * Date: 05.07.2020
 * Time: 16:36
 */
public class Cell implements CanDrawItSelf {
    public static final int CELL_SIZE = 20;
    private int x;
    private int y;
    private int wall;

    public Cell(int x, int y, int wall) {
        this.x = x;
        this.y = y;
        this.wall = wall;
    }

    @Override
    public void drawMe(Graphics2D g2) {
        String cellToBin = String.format("%2s", Integer.toBinaryString(wall)).replace(' ', '0');
        String[] walls = cellToBin.split("");
        if (walls.length == 2) {
            if (walls[0].equals("1")) { // floor
                g2.drawLine(x, y + CELL_SIZE, x + CELL_SIZE, y + CELL_SIZE);
            }
            if (walls[1].equals("1")) { // right wall
                g2.drawLine(x + CELL_SIZE, y + CELL_SIZE, x + CELL_SIZE, y);
            }
        } else {
            g2.fillRect(x, y, x + CELL_SIZE, y + CELL_SIZE);
        }
    }

}
