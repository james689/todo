
package core;

public class Task {
    private String name; // name of the task
    boolean completed; // whether the task has been completed
    
    public Task(String name) {
        this.name = name;
        completed = false;
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
