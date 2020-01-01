package gui;

import core.List;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class View extends JPanel {

    private JMenuBar menuBar;
    private JMenuItem hideCompletedTasksMenuItem;
    private boolean completedTasksHidden = false;
    private ListPanel listPanel;
    private List currentList;
    private Font largeFont = new Font("Serif", Font.BOLD, 25); // font used for the list's name

    public View() {
        setPreferredSize(new Dimension(500, 500));
    }

    // visual representation of a List
    private class ListPanel extends JPanel {

        // should have a variable here containing the actual back-end list,
        // atm the back-end list variable only exists in the outer class
        private JLabel listNameLabel; // the name of the list
        private java.util.List<TaskPanel> taskPanels; // all task panel's contained in this ListPanel

        ListPanel() {
            taskPanels = new java.util.ArrayList<TaskPanel>();
            setBackground(Color.BLUE);
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            listNameLabel = new JLabel("My chores list", SwingConstants.LEFT);
            listNameLabel.setFont(largeFont);
            listNameLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // user clicked on the list's name, so show a dialog
                    // allowing user to edit the list's name
                    System.out.println("list name was clicked");
                    renameList();
                }
            });
            add(listNameLabel);

            TaskPanel taskPanel1 = new TaskPanel();
            add(taskPanel1);
            taskPanels.add(taskPanel1);
            TaskPanel taskPanel2 = new TaskPanel();
            add(taskPanel2);
            taskPanels.add(taskPanel2);
        }

        public void renameList() {
            String s = (String) JOptionPane.showInputDialog(
                    View.this,
                    "Rename list",
                    "Customized Dialog",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    listNameLabel.getText());

            if (s == null || s.isEmpty()) {
                System.out.println("null/empty string");
                return;
            }

            listNameLabel.setText(s);
        }
        
        public void hideCompletedTasks() {
            // iterate through all the TaskPanel's and get each one's underlying task
            // if the task is completed then make the task panel hidden
            for (TaskPanel taskPanel : taskPanels) {
                /*Task underlyingTask = taskPanel.getTask();
                if (underlyingTask.isCompleted) {
                    taskPanel.setVisible(false);
                }*/
                taskPanel.setVisible(false);
            }
        }
        
        public void showCompletedTasks() {
            // iterate through all the TaskPanel's and get each one's underlying task
            // if the task is completed then make the task panel hidden
            for (TaskPanel taskPanel : taskPanels) {
                /*Task underlyingTask = taskPanel.getTask();
                if (underlyingTask.isCompleted) {
                    taskPanel.setVisible(false);
                }*/
                taskPanel.setVisible(true);
            }
        }
    }

    private class TaskPanel extends JPanel {

        //private Task theTask;
        private JLabel taskLabel;
        private JCheckBox checkBox;

        TaskPanel() {
            setBackground(Color.RED);
            checkBox = new JCheckBox();
            checkBox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.out.println("check box ticked/unticked");
                    // update underlying task object's completed state
                }
            });
            taskLabel = new JLabel("clean bedroom");
            taskLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    System.out.println("task name was clicked");
                    renameTask();
                }
            });
            add(checkBox);
            add(taskLabel);
        }

        private void renameTask() {
            String s = (String) JOptionPane.showInputDialog(
                    View.this,
                    "Rename Task",
                    "Customized Dialog",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    taskLabel.getText());

            if (s == null || s.isEmpty()) {
                System.out.println("null/empty string");
                return;
            }

            taskLabel.setText(s);
            // change the actual back-end task object as well
        }
    }

    public JMenuBar createMenuBar() {
        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu optionsMenu = new JMenu("Options");

        JMenuItem openListMenuItem = new JMenuItem("Open list");
        openListMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("open list menu item clicked");
                JFileChooser fc = new JFileChooser();
                int returnVal = fc.showOpenDialog(View.this);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    //currentList = Model.openList(file);
                    if (listPanel != null) {
                        remove(listPanel); // remove the old list panel from the View (Container.remove(Component))
                        System.out.println("removing old list panel");
                    }
                    listPanel = new ListPanel();
                    add(listPanel);
                    revalidate();
                    repaint();
                }
            }
        });
        /*JMenuItem newList = new JMenuItem("New list");
        newList.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("new list menu item clicked");
                // create new list
            }
        });
        JMenuItem saveList = new JMenuItem("Save list");
        saveList.setEnabled(false);*/
        fileMenu.add(openListMenuItem);
        /* menu.add(newList);
        menu.add(saveList);*/
        menuBar.add(fileMenu);

        JMenuItem renameListMenuItem = new JMenuItem("Rename list");
        renameListMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listPanel.renameList();
            }
        });
        optionsMenu.add(renameListMenuItem);
        
        hideCompletedTasksMenuItem = new JMenuItem("Hide completed tasks");
        hideCompletedTasksMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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
        optionsMenu.add(hideCompletedTasksMenuItem);
        
        menuBar.add(optionsMenu);

        return menuBar;
    }
}
