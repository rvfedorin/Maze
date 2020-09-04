package building;

import finding.DFSSeeker;
import finding.Seeker;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * User: Roman V.F.
 * Date: 01.07.2020
 * Time: 11:00
 */
public class Maze {
    private int[][] field; // 00 = down right (min 1 max 3)
    private final int width;
    private final int height;
    private final int enter;
    private final int exit;
    private final Position startPos;
    private final Position endPos;

    public Maze(int width, int height) {
        this.width = width;
        this.height = height;
        enter = ThreadLocalRandom.current().nextInt(height);
        exit = ThreadLocalRandom.current().nextInt(height);
        startPos = new Position(enter, 0);
        endPos = new Position(exit, width - 1);
    }

    /*
     * Create random maze. It may has no exit.
     */
    public void buildMaze() {
        field = new int[height][width];
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                int walls;
                if (j == enter && i == 0) {
                    walls = 2;
                } else if (j == exit && i == width - 1) {
                    walls = 2;
                } else {
                    walls = ThreadLocalRandom.current().nextInt(2) + 1;
                }

                field[j][i] = walls;
            }
        }
        digPath();
    } // buildMaze()

    /*
     * If maze has no exit then we dig to exit.
     */
    private void digPath() {
        int[][] discoverField = new int[height][width];
        Seeker seeker = new DFSSeeker(this);

        boolean hasWay = seeker.findWay(getStartPos(), getEndPos(), discoverField);
        int countOfFinding = 0;

        while (!hasWay && countOfFinding < discoverField.length) {
            countOfFinding++;

            Position digFromDeadEnd = null;

            outer:
            for (int y = 0; y < discoverField.length; y++) {
                for (int x = 1; x < discoverField[0].length; x++) {
                    if (discoverField[y][x - 1] > 0 && discoverField[y][x] == 0) {
                        digFromDeadEnd = new Position(y, x - 1);
                        break outer;
                    }
                }
            }

            Position nextPosition = digStep(digFromDeadEnd, discoverField);

            // print if it is closed
//            if (nextPosition == null) {
//                System.out.println("CLOSED");
//                this.printFieldNumbers(discoverField);
//                return;
//            }

            hasWay = seeker.findWay(nextPosition, endPos, discoverField);

        } // while()

    } // digPath()



    /*
     * If we need to dig because we are at a dead end.
     * So we dig in any direction if we can and return position where we've digged.
     *
     * @param from  is our current position
     * @param discovered  is array where we store visited cells
     * @return <code>building.Position</code> is where we now, after digging
     */
    private Position digStep(Position from, int[][] discovered) {
        if (from == null) return null;

        Position newPos = null;
        int currentCell = field[from.getToDown()][from.getToRight()];

        if (from.getToRight() != width - 1 && discovered[from.getToDown()][from.getToRight() + 1] < 1) { // digged right
            String binCell = getBinStringFormat(currentCell);
            int newCellValue = Integer.parseInt(binCell.charAt(0) + "0", 2);
            field[from.getToDown()][from.getToRight()] = newCellValue;
            newPos = new Position(from.getToDown(), from.getToRight() + 1);
        } else if (from.getToDown() != 0 && discovered[from.getToDown() - 1][from.getToRight()] < 1) { // digged up
            int cell = field[from.getToDown() - 1][from.getToRight()];
            String cellBin = getBinStringFormat(cell);
            int newCellValue = Integer.parseInt("0" + cellBin.charAt(1), 2);
            field[from.getToDown() - 1][from.getToRight()] = newCellValue;
            newPos = new Position(from.getToDown() - 1, from.getToRight());
        } else if (from.getToRight() != 0 && discovered[from.getToDown()][from.getToRight() - 1] < 1) { // digged left
            int cell = field[from.getToDown()][from.getToRight() - 1];
            String cellBin = getBinStringFormat(cell);
            int newCellValue = Integer.parseInt(cellBin.charAt(0) + "0", 2);
            field[from.getToDown()][from.getToRight() - 1] = newCellValue;
            newPos = new Position(from.getToDown(), from.getToRight() - 1);
        } else if (from.getToDown() != height - 1 && discovered[from.getToDown() + 1][from.getToRight()] < 1) { // digged down
            String cellBin = getBinStringFormat(currentCell);
            int newCellValue = Integer.parseInt("0" + cellBin.charAt(1), 2);
            field[from.getToDown()][from.getToRight()] = newCellValue;
            newPos = new Position(from.getToDown() + 1, from.getToRight());
        }

        return newPos;
    }

    private String getBinStringFormat(int number) {
        return String.format("%2s", Integer.toBinaryString(number)).replace(' ', '0');
    }



    public int[][] getField() {
        return field;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getEnter() {
        return enter;
    }

    public int getExit() {
        return exit;
    }

    public Position getStartPos() {
        return startPos;
    }

    public Position getEndPos() {
        return endPos;
    }

    @Override
    public String toString() {
        String DOWN = "__";
        String SPACE = "  ";
        String WALL = "||";

        for (int i = 0; i < (width << 1) + 1; i++) {
            System.out.print(DOWN);
        }
        System.out.println();

        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < height; j++) {
            int[] line = field[j];
            for (int i = 0; i < width; i++) {
                int cell = line[i];
                String down = "X";
                String right = "X";

                String cellToBin = String.format("%2s", Integer.toBinaryString(cell)).replace(' ', '0');
                String[] walls = cellToBin.split("");
                if (walls.length == 2) {
                    if (j != enter & i == 0) {
                        sb.append(WALL);
                    } else if (i == 0) {
                        sb.append(DOWN);
                    }
                    down = walls[0].equals("0") ? SPACE : DOWN;
                    right = walls[1].equals("0") ? DOWN : WALL;

                    //// if it is border  start ///
                    if (j == height - 1) {
                        down = DOWN;
                    }
                    if (i == width - 1 && j != exit) {
                        right = WALL;
                    }
                    //// if it is border  end ///
                } else {
                    sb.append("X");
                }
                sb.append(down).append(right);
            }
            sb.append("\n");
//            sb.append(Arrays.toString(line)).append("\n");
        }
        return sb.toString();
    }

    public void printFieldNumbers() {
        printFieldNumbers(field);
    }

    public void printFieldNumbers(int[][] field) {
        System.out.println();
        for (int[] line : field) {
            System.out.println(Arrays.toString(line));
        }
        System.out.println();
    }
}
