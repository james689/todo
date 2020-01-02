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

class TaskPanel extends JPanel {

    private Task theTask; // the Task this TaskPanel represents
    private ListPanel parentListPanel; // the list panel this task panel belongs to
    private JLabel taskLabel;
    private JCheckBox checkBox;

    TaskPanel(ListPanel parentListPanel, Task theTask) {
        this.parentListPanel = parentListPanel;
        this.theTask = theTask;
        
        setBackground(Color.RED);
        Border lineBorder = BorderFactory.createLineBorder(Color.BLACK, 1);
        Border emptyBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        setBorder(new CompoundBorder(emptyBorder, lineBorder));
        
        checkBox = new JCheckBox();
        checkBox.setSelected(theTask.isCompleted());
        checkBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("check box ticked/unticked");
                // update underlying task object's completed state
                theTask.setCompleted(checkBox.isSelected());
            }
        });
        
        taskLabel = new JLabel(theTask.getName());
        taskLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        taskLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("task name was clicked");
                renameTask();
            }
        });

        JButton removeTaskButton = new JButton("Remove task");
        removeTaskButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("remove task button clicked");
                parentListPanel.removeTask(TaskPanel.this);
            }
        });
        
        add(checkBox);
        add(taskLabel);
        add(removeTaskButton);
    }

    private void renameTask() {
        String s = (String) JOptionPane.showInputDialog(
                this,
                "Enter task name",
                "Rename Task",
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
    
    public Task getTask() {
        return theTask;
    }
}
