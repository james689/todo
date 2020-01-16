package gui;

import core.List;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * The View class represents the main GUI of the program.
 */
public class View extends JPanel {

    private JMenuBar menuBar;
    private JMenuItem renameListMenuItem;
    private JMenuItem saveListMenuItem;
    private JMenuItem hideCompletedTasksMenuItem;

    private boolean completedTasksVisible = true;
    private List currentList = null; // the current list being edited
    private List savedList = null; // the state of the current list at the time of last save.
    // the savedList can be compared to the currentList to see if there have been any
    // changes since the last save.

    private ListPanel currentListPanel; // graphical representation of current list
    private File editFile = null;  // The file that is being edited. This is set when the user opens
    // or saves a file. Value is null if no file is being edited. 

    // constructor
    public View() {
        setPreferredSize(new Dimension(500, 500));
        createMenuBar();
    }

    // the outer JFrame needs access to the menuBar
    public JMenuBar getMenuBar() {
        return menuBar;
    }

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("File");

        JMenuItem openListMenuItem = new JMenuItem("Open list");
        openListMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doOpenList();
            }
        });

        JMenuItem newListMenuItem = new JMenuItem("New list");
        newListMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doNewList();
            }
        });

        saveListMenuItem = new JMenuItem("Save list");
        saveListMenuItem.setEnabled(false);
        saveListMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doSaveList();
            }
        });

        fileMenu.add(openListMenuItem);
        fileMenu.add(newListMenuItem);
        fileMenu.add(saveListMenuItem);

        return fileMenu;
    }

    private JMenu createOptionsMenu() {
        JMenu optionsMenu = new JMenu("Options");

        renameListMenuItem = new JMenuItem("Rename list");
        renameListMenuItem.setEnabled(false);
        renameListMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentList != null) {
                    currentListPanel.renameList();
                }
            }
        });

        hideCompletedTasksMenuItem = new JMenuItem("Hide completed tasks");
        hideCompletedTasksMenuItem.setEnabled(false);
        hideCompletedTasksMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doHideShowCompletedTasks();
            }
        });

        optionsMenu.add(renameListMenuItem);
        optionsMenu.add(hideCompletedTasksMenuItem);

        return optionsMenu;
    }

    private void createMenuBar() {
        menuBar = new JMenuBar();
        JMenu fileMenu = createFileMenu();
        JMenu optionsMenu = createOptionsMenu();
        menuBar.add(fileMenu);
        menuBar.add(optionsMenu);
    }

    private void doNewList() {
        checkUnsavedChanges();

        // get list name from the user
        String listName = (String) JOptionPane.showInputDialog(
                this,
                "Enter list title",
                "New List",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "untitled list");

        if (listName == null || listName.isEmpty()) {
            return;
        }

        // create the List object
        currentList = new List(listName);

        // remove the old ListPanel from the View (if there is one)
        if (currentListPanel != null) {
            remove(currentListPanel); // (Container.remove(Component))
        }

        currentListPanel = new ListPanel(currentList, completedTasksVisible);
        add(currentListPanel);

        updateMenuItems();

        revalidate();
        repaint();
    }

    private void updateMenuItems() {
        if (currentListPanel != null) {
            // there is a list on screen, so make the following options available
            renameListMenuItem.setEnabled(true);
            hideCompletedTasksMenuItem.setEnabled(true);
            saveListMenuItem.setEnabled(true);
        }
    }

    private void checkUnsavedChanges() {
        if (currentList == null) {
            return; // no list is open so there cannot be any unsaved changes
        }

        // there's already a list open, check to see if there have been any
        // changes to it
        if (!currentList.equals(savedList)) {
            // ask user if they want to save changes to list
            if (JOptionPane.showConfirmDialog(null, "Save changes to current list?", "WARNING - UNSAVED CHANGES",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                doSaveList();
            }
        }
    }

    private void doOpenList() {
        checkUnsavedChanges();

        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(this);
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            return; // user did not select a file to open
        }

        File file = fc.getSelectedFile();
        List newList;

        try {
            newList = List.fromXMLFile(file);
        } catch (Exception e) {
            System.out.println("something went wrong reading list from XML file");
            return;
        }

        currentList = newList; // only update the current list if the newList
        // was read successfully from the XML file
        editFile = file; // update the file that is being edited

        if (currentListPanel != null) {
            remove(currentListPanel); // remove the old list panel from the View (Container.remove(Component))
        }
        currentListPanel = new ListPanel(currentList, completedTasksVisible);
        add(currentListPanel);

        updateMenuItems();

        revalidate();
        repaint();
    }

    private void doSaveList() {
        if (currentList == null) {
            return; // no list to save
        }

        JFileChooser fileDialog = new JFileChooser();
        if (editFile != null) {
            fileDialog.setSelectedFile(editFile);
        }

        int returnVal = fileDialog.showSaveDialog(this);
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            return; // user did not select a file
        }

        File selectedFile = fileDialog.getSelectedFile();

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
            savedList = new List(currentList); // create a copy of the current list
            // at the time of save
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void doHideShowCompletedTasks() {
        if (currentList == null) { // no list open
            return;
        }

        if (completedTasksVisible) {
            completedTasksVisible = false;
            currentListPanel.setCompletedTasksVisible(false);
            hideCompletedTasksMenuItem.setText("show completed tasks");
        } else {
            completedTasksVisible = true;
            currentListPanel.setCompletedTasksVisible(true);
            hideCompletedTasksMenuItem.setText("Hide completed tasks");
        }
    }
}
