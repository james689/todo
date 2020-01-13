
package core;

import gui.View;
import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        View view = new View();
        JFrame viewFrame = new JFrame("to-do list");
        viewFrame.setJMenuBar(view.getMenuBar());
        viewFrame.setContentPane(view);
        viewFrame.pack();
        viewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        viewFrame.setVisible(true);
        
        /*List list1 = new List("list 1");
        list1.addTask(new Task("clean bedroom"));
        List list2 = new List(list1);
        
        System.out.println("list 1 name: " + list1.getName());
        System.out.println("list 2 name: " + list2.getName());
        
        System.out.println("list 1 tasks");
        for (Task aTask : list1.getTasks()) {
            System.out.println(aTask.getName());
        }
        System.out.println("list 2 tasks");
        for (Task aTask : list2.getTasks()) {
            System.out.println(aTask.getName());
        }
        
        Task list1Task = list1.getTasks().get(0);
        list1Task.setName("do a poop");
        
        System.out.println("list 1 tasks");
        for (Task aTask : list1.getTasks()) {
            System.out.println(aTask.getName());
        }
        System.out.println("list 2 tasks");
        for (Task aTask : list2.getTasks()) {
            System.out.println(aTask.getName());
        }*/
        
    }
}
