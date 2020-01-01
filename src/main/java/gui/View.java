package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class View {
    //private Model model;
    private Controller controller; // behaviour for the view
    JFrame viewFrame;
    JPanel viewPanel;
    JMenuBar menuBar;
    
    public View(Controller controller) {
        this.controller = controller;
    }
    
    public void createView() {
        viewPanel = new JPanel();
        viewPanel.setPreferredSize(new Dimension(500,500));
        viewFrame = new JFrame("to-do list");
        viewFrame.setJMenuBar(createMenuBar());
        viewFrame.setContentPane(viewPanel);
        viewFrame.pack();
        viewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        viewFrame.setVisible(true);
    }

    public JMenuBar createMenuBar() {
        menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem openList = new JMenuItem("Open list");
        openList.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("open list menu item clicked");
                JFileChooser fc = new JFileChooser();
                int returnVal = fc.showOpenDialog(viewFrame);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    //This is where a real application would open the file.
                    controller.openList(file);
                    //log.append("Opening: " + file.getName() + "." + newline);
                } else {
                    //log.append("Open command cancelled by user." + newline);
                }
            }
        });
        JMenuItem newList = new JMenuItem("New list");
        newList.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("new list menu item clicked");
                controller.newList();
            }
        });
        JMenuItem saveList = new JMenuItem("Save list");
        saveList.setEnabled(false);
        menu.add(newList);
        menu.add(openList);
        menu.add(saveList);
        menuBar.add(menu);
        return menuBar;
    }
}
