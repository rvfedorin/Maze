package finding;

import building.Maze;
import building.Position;
import com.sun.istack.internal.Nullable;
import gui.CanDrawItSelf;
import gui.RunningPoint;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * User: Roman V.F.
 * Date: 02.09.2020
 * Time: 16:27
 */
public class DFSSeeker extends Seeker {

    public DFSSeeker(Maze maze) {
        super(maze);
    }

    @Override
    public boolean findWay(Position start, Position exit, int[][] discoverField, @Nullable List<CanDrawItSelf> goPositions) {
        if(start == null) {
            start = getMaze().getStartPos();
        }

        if(exit == null) {
            start = getMaze().getEndPos();
        }

        if (discoverField == null) {
            int height = getMaze().getHeight();
            int width = getMaze().getWidth();
            discoverField = new int[height][width];
        }
        boolean hasWay = false;
        Deque<RunningPoint> nextChoose = new ArrayDeque<>();
        nextChoose.push(new RunningPoint(start, true, Color.green));

        while (!nextChoose.isEmpty()) {
            RunningPoint currentPoint = nextChoose.pop();
            if (goPositions != null) {
                goPositions.add(currentPoint);
            }

            if (currentPoint.getMyPosition().isSamePlace(exit)) {
                hasWay = true;
                currentPoint.setType(RunningPoint.TypePosition.END);
                break;
            }

            discoverField[currentPoint.getMyPosition().getToDown()][currentPoint.getMyPosition().getToRight()] += 1;
            List<RunningPoint> pointsToMove = canMoveTo(currentPoint.getMyPosition(), discoverField);
            if (pointsToMove.size() == 1) {
                nextChoose.push(pointsToMove.get(0));
                currentPoint.setType(RunningPoint.TypePosition.FLAT);
            } else if (pointsToMove.size() > 1) {
                currentPoint.setType(RunningPoint.TypePosition.INTERSECTION);
                currentPoint.setWaysFromMe(pointsToMove.size());
                pointsToMove.forEach(e -> {
                    e.setFromIntersection(currentPoint);
                    nextChoose.push(e);
                });
            } else {
                currentPoint.setType(RunningPoint.TypePosition.DEAD_END);
            }
        } // while()

        return hasWay;
    }

    private List<RunningPoint> canMoveTo(Position position, int[][] visited) {

        List<RunningPoint> freeMove = new ArrayList<>();
        int width = getMaze().getField()[0].length;
        int height = getMaze().getField().length;

        if (position.getToRight() != 0) {
            int cellLeft = getMaze().getField()[position.getToDown()][position.getToRight() - 1];
            int visitedCell = visited[position.getToDown()][position.getToRight() - 1];
            if ((cellLeft != 1 && cellLeft != 3) && visitedCell < 1) {
                Position newPosition = new Position(position.getToDown(), position.getToRight() - 1);
                freeMove.add(new RunningPoint(newPosition, true, Color.green));
            }
        } // to left

        if (position.getToDown() != 0) {
            int cellTop = getMaze().getField()[position.getToDown() - 1][position.getToRight()];
            int visitedCell = visited[position.getToDown() - 1][position.getToRight()];
            if ((cellTop != 2 && cellTop != 3) && visitedCell < 1) {
                Position newPosition = new Position(position.getToDown() - 1, position.getToRight());
                freeMove.add(new RunningPoint(newPosition, true, Color.green));
            }
        } // to up

        if (position.getToRight() != width - 1) {
            int cellRight = getMaze().getField()[position.getToDown()][position.getToRight()];
            int visitedCell = visited[position.getToDown()][position.getToRight() + 1];
            if ((cellRight != 1 && cellRight != 3) && visitedCell < 1) {
                Position newPosition = new Position(position.getToDown(), position.getToRight() + 1);
                freeMove.add(new RunningPoint(newPosition, true, Color.green));
            }
        } // to right

        if (position.getToDown() != height - 1) {
            int cellDown = getMaze().getField()[position.getToDown()][position.getToRight()];
            int visitedCell = visited[position.getToDown() + 1][position.getToRight()];
            if ((cellDown != 2 && cellDown != 3) && visitedCell < 1) {
                Position newPosition = new Position(position.getToDown() + 1, position.getToRight());
                freeMove.add(new RunningPoint(newPosition, true, Color.green));
            }
        } // to down

        return freeMove;
    } // canMoveTo()
}
