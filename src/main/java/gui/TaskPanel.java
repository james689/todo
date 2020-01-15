package gui;

import core.Task;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

/**
 * The TaskPanel class represents a graphical representation of a Task. 
 * It holds a reference to the underlying Task object it represents.
 */
public class TaskPanel extends JPanel {

    private Task theTask;               // the Task this TaskPanel represents
    private ListPanel parentListPanel;  // the list panel this task panel is contained within
    private JLabel taskLabel;
    private JCheckBox checkBox;

    // constructor
    public TaskPanel(ListPanel parentListPanel, Task theTask) {
        this.parentListPanel = parentListPanel;
        this.theTask = theTask;

        //setBackground(Color.RED);
        Border lineBorder = BorderFactory.createLineBorder(Color.BLACK, 1);
        Border emptyBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        setBorder(new CompoundBorder(emptyBorder, lineBorder));

        checkBox = new JCheckBox();
        checkBox.setSelected(theTask.isCompleted());
        checkBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                theTask.setCompleted(checkBox.isSelected());
                update();
            }
        });

        taskLabel = new JLabel(theTask.getName());
        taskLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        taskLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                renameTask();
            }
        });

        JButton removeTaskButton = new JButton("Remove task");
        removeTaskButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parentListPanel.removeTask(TaskPanel.this);
            }
        });

        add(checkBox);
        add(taskLabel);
        add(removeTaskButton);
        
        update(); // update the task panel when it is first created to ensure
        // consistency
    }

    private void renameTask() {
        String newTaskName = (String) JOptionPane.showInputDialog(
                this,
                "Enter task name",
                "Rename Task",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                theTask.getName());

        if (newTaskName == null || newTaskName.isEmpty()) {
            return;
        }

        theTask.setName(newTaskName);
        update();
    }

    public void update() {
        if (theTask.isCompleted()) {
            // a completed task should have strike through text
            taskLabel.setText("<html><strike>" + theTask.getName() + "</strike></html>");
            // a completed task may also be invisible, check with the parent list panel
            this.setVisible(parentListPanel.isCompletedTasksVisible());
        } else {
            // no strike through text for an uncompleted task
            taskLabel.setText(theTask.getName());
            // an uncompleted task is always visible
            this.setVisible(true);
        }
    }

    public Task getTask() {
        return theTask;
    }
}
