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

public class View extends JPanel {

    private JMenuBar menuBar;
    private JMenuItem renameListMenuItem;
    private JMenuItem saveListMenuItem;
    private JMenuItem hideCompletedTasksMenuItem;
    private boolean completedTasksHidden = false;

    private List currentList; // the current list being edited
    
    private ListPanel currentListPanel; // graphical representation of current list
    private File editFile = null;  // The file that is being edited.  Set when user opens
    // or saves a file.  Value is null if no file is being edited. 

    public View() {
        setPreferredSize(new Dimension(500, 500));
        createMenuBar();
    }

    // the JFrame needs access to the menuBar
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
                if (currentList == null) { // no list open
                    return;
                }

                if (!completedTasksHidden) {
                    currentListPanel.showCompletedTasks(false);
                    hideCompletedTasksMenuItem.setText("show completed tasks");
                    completedTasksHidden = true;
                } else {
                    currentListPanel.showCompletedTasks(true);
                    hideCompletedTasksMenuItem.setText("Hide completed tasks");
                    completedTasksHidden = false;
                }
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
        // need to add functionality to ask user if they want to
        // save their changes to current list before creating a new list

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
            System.out.println("removing old list panel");
        }

        currentListPanel = new ListPanel(currentList);
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

    private void doOpenList() {
        // need to add functionality here to ask user if they want to
        // save their changes to current list before opening a new list
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(this);
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            System.out.println("user did not select a file to open");
            return;
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
            System.out.println("removing old list panel");
        }
        currentListPanel = new ListPanel(currentList);
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
            System.out.println("user cancelled saving file");
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
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
