
package core;

import gui.View;
import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        View view = new View();
        JFrame viewFrame = new JFrame("to-do list");
        viewFrame.setJMenuBar(view.createMenuBar());
        viewFrame.setContentPane(view);
        viewFrame.pack();
        viewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        viewFrame.setVisible(true);
    }
}
