
package core;

public class Task {
    private String name;    // name of the task
    boolean completed;      // whether the task has been completed
    
    public Task(String name) {
        this(name, false);
    }
    
    public Task(String name, boolean completed) {
        this.name = name;
        this.completed = completed;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public boolean isCompleted() {
        return completed;
    }
    
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
