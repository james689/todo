
package core;

import java.util.Collections;

public class List {
    private String name;                // the name of this list
    private java.util.List<Task> tasks; // the tasks in this list
    
    public List(String name) {
        this.name = name;
        this.tasks = new java.util.ArrayList<Task>();
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
    
    // return an XML representation of this List
    public String toXMLString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<list name=\"" + getName() + "\">" + "\n");
        for (Task task : getTasks()) {
            sb.append("<task completed=\"" + task.isCompleted() + "\">" + task.getName() + "</task>" + "\n");
        }
        sb.append("</list>");
        return sb.toString();
    }
}
