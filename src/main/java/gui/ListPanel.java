package gui;

import core.List;
import core.Task;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
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
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

/**
 * The ListPanel class represents a graphical representation of a List. 
 * It holds a reference to the underlying List object it represents.
 */
public class ListPanel extends JPanel {

    private List list; // the List this ListPanel represents
    private JLabel listNameLabel; 
    private JLabel numTasksLabel;
    private JPanel taskPanelContainer; 
    private Font largeFont = new Font("Serif", Font.BOLD, 25); 
    private boolean completedTasksVisible = true; // determines whether this
    // list panel will display completed tasks or not

    // constructor
    public ListPanel(List list, boolean completedTasksVisible) {
        this.list = list;
        this.completedTasksVisible = completedTasksVisible;
        //setBackground(Color.BLUE);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // stack components vertically

        listNameLabel = new JLabel(list.getName(), SwingConstants.LEFT);
        listNameLabel.setFont(largeFont);
        listNameLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        listNameLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // allow user to edit the list's name when they click mouse on listNameLabel 
                renameList();
            }
        });

        taskPanelContainer = new JPanel();
        taskPanelContainer.setLayout(new BoxLayout(taskPanelContainer, BoxLayout.Y_AXIS));
        for (Task task : list.getTasks()) {
            taskPanelContainer.add(new TaskPanel(this, task));
        }
        JScrollPane taskPanelContainerScroller = new JScrollPane(taskPanelContainer);
        taskPanelContainerScroller.setPreferredSize(new Dimension(200,200)); // must set a preferred size for scrolling to work properly

        JButton newTaskButton = new JButton("New task");
        newTaskButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addNewTask();
            }
        });
        
        numTasksLabel = new JLabel("");
        updateNumTasksLabel();
        
        // add the components to this ListPanel
        add(listNameLabel);
        add(numTasksLabel);
        add(taskPanelContainerScroller);
        add(newTaskButton);
    }

    public void renameList() {
        String newListName = (String) JOptionPane.showInputDialog(
                this,
                "Enter new list name",
                "Rename List",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                list.getName());

        // if user enters an empty string or cancels the dialog don't rename
        // the list. Could warn the user about the list not allowing non-empty name 
        // here. 
        if (newListName == null || newListName.isEmpty()) {
            return;
        }

        list.setName(newListName);
        listNameLabel.setText(list.getName());
    }

    // if b == true, show completed tasks, otherwise hide completed tasks.
    public void setCompletedTasksVisible(boolean b) {
        this.completedTasksVisible = b;
        
        for (Component component : taskPanelContainer.getComponents()) {
            TaskPanel taskPanel = (TaskPanel) component;
            taskPanel.update();
        }
    }
    
    public boolean isCompletedTasksVisible() {
        return completedTasksVisible;
    }

    public void removeTask(TaskPanel taskPanel) {
        // delete the task panel from the GUI
        taskPanelContainer.remove(taskPanel);  
        // delete the task from the underlying list
        list.removeTask(taskPanel.getTask());
        updateNumTasksLabel();
        revalidate();
        repaint();
    }
    
    public void addNewTask() {
        String taskName = (String) JOptionPane.showInputDialog(
                this,
                "Enter task name",
                "Add task",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                null);

        // don't create task if user enters an empty string or cancels
        // the dialog box. Could warn user here that the task must have a 
        // non-empty name
        if (taskName == null || taskName.isEmpty()) {
            return;
        }

        // create the task and add it to the underlying list
        Task theTask = new Task(taskName);
        list.addTask(theTask);
        
        // create a TaskPanel to visually represent the task on screen and
        // add it to the GUI
        taskPanelContainer.add(new TaskPanel(this, theTask));
        updateNumTasksLabel();
        
        revalidate();
        repaint();
    }
    
    private void updateNumTasksLabel() {
        int numTasksInList = list.getTasks().size();
        String plural = (numTasksInList == 1) ? "task" : "tasks";
        numTasksLabel.setText(numTasksInList + " " + plural);
    }
}
