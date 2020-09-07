package gui;

import building.Maze;

import javax.swing.*;
import java.awt.*;

/**
 * User: Roman V.F.
 * Date: 05.07.2020
 * Time: 15:36
 */
public class GuiMain extends JFrame {
    private final FieldJPanel field;
    public Maze maze;
    private static final int startWidth = 30;
    private static final int startHeight = 30;

    public GuiMain() throws HeadlessException {
        maze = new Maze(startWidth, startHeight);
        maze.buildMaze();
        field = new FieldJPanel(maze);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Maze");
        this.setSize(660, 820);

        this.add(field);
        this.add(getChangeFieldSizeElements(), BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        this.setVisible(true);

    }

    private JPanel getChangeFieldSizeElements() {
        JPanel jPanel = new JPanel();
        JTextField height = new JTextField(10);
        height.setText(String.valueOf(startHeight));
        JTextField width = new JTextField(10);
        width.setText(String.valueOf(startWidth));

        JLabel heightLabel = new JLabel("Height:");
        JLabel widthLabel = new JLabel("Width:");

        JButton create = new JButton("Create");
        create.addActionListener(event -> {
            try {
                int hSize = Integer.parseInt(height.getText());
                int wSize = Integer.parseInt(width.getText());
                repaintField(hSize, wSize);
            } catch (Exception ex) {
            }
        });

        JButton findWayButton = new JButton("Find the exit");
        findWayButton.addActionListener(event -> {
            new Thread(field::findTheWay).start();
        });

        JButton stopFindWayButton = new JButton("Stop");
        stopFindWayButton.addActionListener(event -> field.stopRunning());

        JButton resetFindWayButton = new JButton("Reset");
        resetFindWayButton.addActionListener(event -> field.resetDrawingAndRepaint());

        JSlider speedSlider = new JSlider(SwingConstants.HORIZONTAL, 10, 100, 15);
        speedSlider.setMajorTickSpacing(10);
        speedSlider.setPaintLabels(true);
        speedSlider.setPaintTicks(true);
        speedSlider.addChangeListener(e -> {
            JSlider source = (JSlider) e.getSource();
            if (!source.getValueIsAdjusting()) {
                int speed = source.getValue();
                field.setSleepTimeBeforeNextStep(700 / speed);
            }
        });
        JLabel sliderLabel = new JLabel("SPEED: ");

        JPanel lineButtonsPanel = new JPanel();
        lineButtonsPanel.setLayout(new BoxLayout(lineButtonsPanel, BoxLayout.Y_AXIS));
        JPanel lineButton1 = new JPanel();
        JPanel lineButton2 = new JPanel();
        JPanel lineSliderLabel = new JPanel();
        JPanel lineSlider = new JPanel();
        lineButtonsPanel.add(lineButton1);
        lineButtonsPanel.add(lineButton2);
        lineButtonsPanel.add(lineSliderLabel);
        lineButtonsPanel.add(lineSlider);

        lineButton1.add(heightLabel);
        lineButton1.add(height);
        lineButton1.add(widthLabel);
        lineButton1.add(width);
        lineButton1.add(create);
        lineButton2.add(findWayButton);
        lineButton2.add(stopFindWayButton);
        lineButton2.add(resetFindWayButton);
        lineSlider.add(sliderLabel);
        lineSlider.add(speedSlider);

        jPanel.add(lineButtonsPanel);

        return jPanel;
    }

    private void repaintField(int height, int width) {
        maze = new Maze(width, height);
        maze.buildMaze();
        field.setNewMaze(maze);

    }


    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            GuiMain guiMain = new GuiMain();
        });
    }


}
