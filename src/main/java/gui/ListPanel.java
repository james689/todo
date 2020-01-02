package gui;

import core.List;
import core.Task;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

// a ListPanel is a visual representation of a List. It holds a reference
// to the underlying List object it represents.
class ListPanel extends JPanel {

    private List list; // the List this ListPanel wraps/represents
    private JLabel listNameLabel; 
    //private java.util.List<TaskPanel> taskPanels; // all task panel's contained in this ListPanel
    private JPanel taskPanelContainer;
    private Font largeFont = new Font("Serif", Font.BOLD, 25); // font used for the list's name

    ListPanel(List list) {
        this.list = list;
        //taskPanels = new java.util.ArrayList<TaskPanel>();
        setBackground(Color.BLUE);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        listNameLabel = new JLabel(list.getName(), SwingConstants.LEFT);
        listNameLabel.setFont(largeFont);
        listNameLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        listNameLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // user clicked on the list's name, so show a dialog
                // allowing user to edit the list's name
                System.out.println("list name was clicked");
                renameList();
            }
        });

        taskPanelContainer = new JPanel();
        taskPanelContainer.setLayout(new BoxLayout(taskPanelContainer, BoxLayout.Y_AXIS));
        for (Task task : list.getTasks()) {
            taskPanelContainer.add(new TaskPanel(this, task));
        }

        JButton addNewTaskButton = new JButton("New task");
        addNewTaskButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("add task button clicked");
                addNewTask();
            }
        });
        
        // add the components to this ListPanel
        add(listNameLabel);
        add(taskPanelContainer);
        add(addNewTaskButton);
    }

    public void renameList() {
        String s = (String) JOptionPane.showInputDialog(
                this,
                "Enter new list name",
                "Rename List",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                list.getName());

        // if user enters an empty string or cancels the dialog don't rename
        // the list. Could warn the user about the list having to have a non-empty
        // name here if you want.
        if (s == null || s.isEmpty()) {
            return;
        }

        list.setName(s);
        listNameLabel.setText(list.getName());
    }

    public void hideCompletedTasks() {
        // iterate through all the TaskPanel's and get each one's underlying task
        // if the task is completed then make the task panel hidden
        for (Component component : taskPanelContainer.getComponents()) {
            TaskPanel taskPanel = (TaskPanel) component;
            if (taskPanel.getTask().isCompleted()) {
                taskPanel.setVisible(false);
            }
        }
    }

    public void showCompletedTasks() {
        // iterate through all the TaskPanel's and get each one's underlying task
        // if the task is completed then make the task panel hidden
        for (Component component : taskPanelContainer.getComponents()) {
            TaskPanel taskPanel = (TaskPanel) component;
            if (taskPanel.getTask().isCompleted()) {
                taskPanel.setVisible(true);
            }
        }
    }

    public void removeTask(TaskPanel taskPanel) {
        // delete the task panel from the GUI
        //taskPanels.remove(taskPanel); // remove from the internal list we are keeping ourselves
        taskPanelContainer.remove(taskPanel); // remove from the JPanel container 
        
        // delete the task from the model
        list.removeTask(taskPanel.getTask());
        revalidate();
        repaint();
    }
    
    public void addNewTask() {
        // get user input
        String taskName = (String) JOptionPane.showInputDialog(
                this,
                "Enter task name",
                "Add task",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                listNameLabel.getText());

        // don't create task if user enters an empty string or cancels
        // the dialog box. Could warn user here that the task must have a 
        // non-empty name
        if (taskName == null || taskName.isEmpty()) {
            return;
        }

        // create the task and add it to the underlying model
        Task theTask = new Task(taskName);
        list.addTask(theTask);
        
        // create a TaskPanel to visually represent the task on screen and
        // add it to the GUI
        TaskPanel taskPanel = new TaskPanel(this, theTask);
        taskPanelContainer.add(taskPanel);
        //taskPanels.add(taskPanel);
        revalidate();
        repaint();
    }
}
