import gui.GuiMain;

import java.awt.*;

/**
 * User: Roman V.F.
 * Date: 01.07.2020
 * Time: 10:58
 */
public class Main {
    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            GuiMain guiMain = new GuiMain();
        });

    }
}
