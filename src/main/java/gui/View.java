package gui;

import core.List;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
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
    private ListPanel listPanel;

    public View() {
        setPreferredSize(new Dimension(500, 500));
    }

    public JMenuBar createMenuBar() {
        menuBar = new JMenuBar();
        
        // file menu
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

        /*JMenuItem saveList = new JMenuItem("Save list");
        saveList.setEnabled(false);*/
        fileMenu.add(openListMenuItem);
        fileMenu.add(newList);
        //fileMenu.add(saveList);

        // options menu 
        JMenu optionsMenu = new JMenu("Options");
        
        JMenuItem renameListMenuItem = new JMenuItem("Rename list");
        renameListMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (listPanel != null) {
                    listPanel.renameList();
                }
            }
        });

        hideCompletedTasksMenuItem = new JMenuItem("Hide completed tasks");
        hideCompletedTasksMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (listPanel == null) { // no list open
                    return;
                }
                
                if (!completedTasksHidden) {
                    listPanel.hideCompletedTasks();
                    hideCompletedTasksMenuItem.setText("show completed tasks");
                    completedTasksHidden = true;
                } else {
                    listPanel.showCompletedTasks();
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

        List list = new List(s); // create the underlying list object

        if (listPanel != null) {
            // remove the old list panel
            remove(listPanel); // remove the old list panel from the View (Container.remove(Component))
            System.out.println("removing old list panel");
        }

        listPanel = new ListPanel(list);
        add(listPanel);
        revalidate();
        repaint();
    }

    private void doOpenList() {
        // need to add functionality to ask user if they want to
        // save their changes to current list before opening a new list
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(View.this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            List list = new List("opened list"); // replace this with Model.openList(file)
            if (listPanel != null) {
                remove(listPanel); // remove the old list panel from the View (Container.remove(Component))
                System.out.println("removing old list panel");
            }
            listPanel = new ListPanel(list);
            add(listPanel);
            revalidate();
            repaint();
        }
    }
}
