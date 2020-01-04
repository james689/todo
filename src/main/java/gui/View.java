package gui;

import core.List;
import java.awt.Dimension;
import static java.awt.SystemColor.window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class View extends JPanel {

    private JMenuBar menuBar;
    private JMenuItem hideCompletedTasksMenuItem;
    private boolean completedTasksHidden = false;
    
    private List currentList; // the current list being edited
    private ListPanel currentListPanel; // graphical representation of current list
    private File editFile = null;  // The file that is being edited.  Set when user opens
    // or saves a file.  Value is null if no file is being edited. 

    public View() {
        setPreferredSize(new Dimension(500, 500));
    }

    public JMenuBar createMenuBar() {
        menuBar = new JMenuBar();

        // ------------------ file menu ----------------------------
        
        JMenu fileMenu = new JMenu("File");

        JMenuItem openListMenuItem = new JMenuItem("Open list");
        openListMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("open list menu item clicked");
                doOpenList();
            }
        });

        JMenuItem newList = new JMenuItem("New list");
        newList.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("new list menu item clicked");
                doNewList();
            }
        });

        JMenuItem saveList = new JMenuItem("Save list");
        saveList.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("save list menu item clicked");
                doSaveList();
            }
        });

        fileMenu.add(openListMenuItem);
        fileMenu.add(newList);
        fileMenu.add(saveList);

        // ------------------ options menu ----------------------------
        
        JMenu optionsMenu = new JMenu("Options");

        JMenuItem renameListMenuItem = new JMenuItem("Rename list");
        renameListMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentList != null) {
                    currentListPanel.renameList();
                }
            }
        });

        hideCompletedTasksMenuItem = new JMenuItem("Hide completed tasks");
        hideCompletedTasksMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentList == null) { // no list open
                    return;
                }

                if (!completedTasksHidden) {
                    currentListPanel.hideCompletedTasks();
                    hideCompletedTasksMenuItem.setText("show completed tasks");
                    completedTasksHidden = true;
                } else {
                    currentListPanel.showCompletedTasks();
                    hideCompletedTasksMenuItem.setText("Hide completed tasks");
                    completedTasksHidden = false;
                }
            }
        });

        optionsMenu.add(renameListMenuItem);
        optionsMenu.add(hideCompletedTasksMenuItem);

        menuBar.add(fileMenu);
        menuBar.add(optionsMenu);

        return menuBar;
    }

    private void doNewList() {
        // need to add functionality to ask user if they want to
        // save their changes to current list before creating a new list
        String s = (String) JOptionPane.showInputDialog(
                this,
                "Enter list title",
                "New List",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "untitled list");

        if (s == null || s.isEmpty()) {
            return;
        }

        currentList = new List(s); // create the underlying list object

        if (currentListPanel != null) {
            // remove the old list panel
            remove(currentListPanel); // remove the old list panel from the View (Container.remove(Component))
            System.out.println("removing old list panel");
        }

        currentListPanel = new ListPanel(currentList);
        add(currentListPanel);
        revalidate();
        repaint();
    }

    private void doOpenList() {
        // need to add functionality to ask user if they want to
        // save their changes to current list before opening a new list
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            currentList = new List("opened list"); // replace this with Model.openList(file)
            editFile = file;
            
            if (currentListPanel != null) {
                remove(currentListPanel); // remove the old list panel from the View (Container.remove(Component))
                System.out.println("removing old list panel");
            }
            currentListPanel = new ListPanel(currentList);
            add(currentListPanel);
            revalidate();
            repaint();
        }
    }

    private void doSaveList() {
        if (currentList == null) {
            return; // no list to save
        }

        doSaveAsText();
    }

    private void doSaveAsText() {
        
        JFileChooser fileDialog = new JFileChooser();
        if (editFile != null) {
            fileDialog.setSelectedFile(editFile);
        }
        
        int returnVal = fileDialog.showSaveDialog(this);
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            return; // user did not select a file
        }
        
        File selectedFile = fileDialog.getSelectedFile();
             
        // Note: User has selected a file AND if the file exists has
        // confirmed that it is OK to erase the exiting file.
        PrintWriter out;
        try {
            FileWriter stream = new FileWriter(selectedFile);
            out = new PrintWriter(stream);
        } catch (Exception e) {
            System.out.println(e);
            return;
        }
        
        try {
            out.println("<?xml version=\"1.0\"?>");
            out.println("<todo version=\"1.0\">");
            out.println(currentList.toXMLString());
            out.println("</todo>");
            
            out.flush();
            out.close();
            
            editFile = selectedFile;
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
