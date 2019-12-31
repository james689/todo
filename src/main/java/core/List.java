
package core;

import java.io.PrintWriter;
import java.util.Collections;

public class List {
    private String name; // the name of this list
    private java.util.List<Task> tasks; // the tasks of this list
    
    public List(String name) {
        this.name = name;
        tasks = new java.util.ArrayList<Task>();
    }
    
    public String getName() { 
        return name; 
    }
    
    public void setName(String name) { 
        this.name = name;
    }
    
    public void addTask(Task task) {
        tasks.add(task);
    }
    
    public boolean removeTask(Task task) {
        return tasks.remove(task);
    }
    
    // see https://stackoverflow.com/questions/23607881/returning-a-private-collection-using-a-getter-method-in-java
    public java.util.List<Task> getTasks() {
        return Collections.unmodifiableList(tasks);
    }
    
    // Save this list in XML format
    public void printAsXML(PrintWriter out) {
        try {
            out.println("<list name=\"" + this.getName() + "\">");
            for (Task task : tasks) {
                out.println("<task completed=\"" + task.isCompleted() + "\">" + task.getName() + "</task>");
            }
            out.println("</list>");
            out.flush();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}
