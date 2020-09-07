package gui;

import building.Maze;
import finding.DFSSeeker;
import finding.Seeker;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;


/**
 * User: Roman V.F.
 * Date: 05.07.2020
 * Time: 15:35
 */
public class FieldJPanel extends JPanel {
    public enum States {START, RUN, STOP, FINISH}

    public final static int startOffsetX = 20;
    public final static int startOffsetY = 20;
    private int x, y;
    private Maze maze;
    private final ArrayList<CanDrawItSelf> toDrawMazeField;
    private ArrayList<CanDrawItSelf> toDrawPath;
    private ArrayList<CanDrawItSelf> allPathSteps;
    private Deque<RunningPoint> doneWay;
    //    private volatile boolean stop = false;
    private volatile int sleepTimeBeforeNextStep = 50;
    //    private boolean running = false;
    private CanDrawItSelf stoppedPosition;
        private boolean goBack = false;
    private States state = States.START;

    public void setX(int x) {
        this.x = x * Cell.CELL_SIZE;
    }

    public void setY(int y) {
        this.y = y * Cell.CELL_SIZE;
    }

    public FieldJPanel(Maze maze) {
        setX(maze.getWidth());
        setY(maze.getHeight());
        this.maze = maze;
        toDrawMazeField = new ArrayList<>();
        allPathSteps = new ArrayList<>();
        toDrawPath = new ArrayList<>();
        doneWay = new ArrayDeque<>();
        addToDrawLabOnField();
    }

    private void addToDrawLabOnField() {
        int[][] field = maze.getField();
        toDrawMazeField.clear();

        for (int i = 0; i < maze.getHeight(); i++) {
            for (int j = 0; j < maze.getWidth(); j++) {
                int xCell = j * Cell.CELL_SIZE + startOffsetX;
                int yCell = i * Cell.CELL_SIZE + startOffsetY;

                Cell cell = new Cell(xCell, yCell, field[i][j]);
//                toDrawElements.add(cell);
                toDrawMazeField.add(cell);
//                cell.drawMe(g2);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        drawBorders(g2);
        drawWalls(g2);
        drawPath(g2);
    }

    private void drawBorders(Graphics2D g2) {
        int toEnter = startOffsetY + (maze.getEnter() * Cell.CELL_SIZE);
        int toExit = startOffsetY + (maze.getExit() * Cell.CELL_SIZE);
        // drawing borders start
        Stroke basicStroke = g2.getStroke();
        g2.setStroke(new BasicStroke(3));
        g2.drawLine(startOffsetX, startOffsetY, startOffsetX + x, startOffsetY); // top
        g2.drawLine(startOffsetX, startOffsetY + y, startOffsetX + x, startOffsetY + y); // down
        g2.drawLine(startOffsetX, startOffsetY, startOffsetX, toEnter); // left top
        g2.drawLine(startOffsetX, toEnter + Cell.CELL_SIZE, startOffsetX, startOffsetY + y); // left down
        g2.drawLine(startOffsetX + x, startOffsetY, startOffsetX + x, toExit); // right top
        g2.drawLine(startOffsetX + x, toExit + Cell.CELL_SIZE, startOffsetX + x, startOffsetY + y); // right down
        g2.setStroke(basicStroke);
        // drawing borders end
    }

    private void drawWalls(Graphics2D g2) {
        if (toDrawMazeField != null && !toDrawMazeField.isEmpty()) {
            for (CanDrawItSelf pf : toDrawMazeField) {
                pf.drawMe(g2);
            }
        }
    }

    private void drawPath(Graphics2D g2) {
        if (toDrawPath != null && !toDrawPath.isEmpty()) {
            for (int i = 0; i < toDrawPath.size(); i++) {
                toDrawPath.get(i).drawMe(g2);
            }
        }
    }

    public void setNewMaze(Maze maze) {
        if (maze != null) {
            this.maze = maze;
            setX(this.maze.getWidth());
            setY(this.maze.getHeight());
            resetField();
            addToDrawLabOnField();
            repaint();
        }
    }

    public void findTheWay() {
        if (state != States.START && state != States.STOP) return; // we are already running

        int startId = -1;
        Seeker seeker = new DFSSeeker(maze);
        if (stoppedPosition == null) {
            if (seeker.findWay(maze.getStartPos(), maze.getEndPos(), null, allPathSteps)) {
                startId = 0;
            }
        } else {// if we were stopped, start from the stopped position
            startId = allPathSteps.indexOf(stoppedPosition);
        }

        state = States.RUN;
        stepDrawingPath(startId);
    } // findTheWay

    private void stepDrawingPath(int startPosition) {
        if (startPosition < 0) return;

        RunningPoint lastStep = null;
        RunningPoint step = null;

        for (int i = startPosition; i < allPathSteps.size(); ) {
            if (state == States.STOP) {
                stoppedPosition = step;
                break;
            }

            if (goBack) {
                if(step == null) { // was stopped
                    step = (RunningPoint) allPathSteps.get(i);
                }
                if (!doneWay.isEmpty()) {
                    RunningPoint stepBack = doneWay.pop();
                    if (stepBack.getType() == RunningPoint.TypePosition.INTERSECTION
                            && step.getFromIntersection() != null
                            && step.getFromIntersection().getMyPosition().isSamePlace(stepBack.getMyPosition())) {
                        goBack = false; // found the start intersection
                        toDrawPath.add(step);
                        doneWay.push(stepBack);
                        doneWay.push(step);
                        if(lastStep != null) { // after it was stopped it may be null
                            lastStep.setRoundMain(false);
                            lastStep.setColor(Color.red);
                        }
                        lastStep = step;
                    } else {
                        stepBack.setColor(Color.blue);
                        stepBack.setRoundMain(true);
                        if (lastStep != null) {
                            lastStep.setColor(Color.red);
                            lastStep.setRoundMain(false);
                        }
                        lastStep = stepBack;
                    }
                    stepBack.setWaysFromMe(stepBack.getWaysFromMe() - 1);
                    if (stepBack.getWaysFromMe() < 1) {
                        stepBack.setColor(Color.red);
                        stepBack.setRoundMain(false);
                    }
                } else {
                    goBack = false;
                }
            } else {
                step = (RunningPoint) allPathSteps.get(i);
                i++;
                if (lastStep != null) {
                    lastStep.setRoundMain(false);
                    if (lastStep.getType() == RunningPoint.TypePosition.DEAD_END) {
                        lastStep.setColor(Color.red);
                        goBack = true;
                        if(step.getType() == RunningPoint.TypePosition.END) {
                            i--; // if the step is finish we have'n next loop
                        }
                    }
                }
                if (!goBack) {
                    toDrawPath.add(step);
                    doneWay.push(step);
                    lastStep = step;
                }
            }

            try {
                Thread.sleep(sleepTimeBeforeNextStep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            repaint();
        } // for all path
    }

    public void stopRunning() {
        state = States.STOP;
    }


    public void resetDrawingAndRepaint() {
        resetField();
        repaint();
    }

    private void resetField() {
        state = States.START;
        goBack = false;
        stoppedPosition = null;
        allPathSteps = new ArrayList<>();
        toDrawPath = new ArrayList<>();
        doneWay = new ArrayDeque<>();
    }

    public void setSleepTimeBeforeNextStep(int sleepTimeBeforeNextStep) {
        this.sleepTimeBeforeNextStep = sleepTimeBeforeNextStep;
    }

}
