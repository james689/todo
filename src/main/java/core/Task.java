package core;

/**
 * The Task class represents an item in a list. A task has a name, for example
 * "hoover living room" and a completion status.
 */
public class Task {

    private String name;            // name of the task
    private boolean completed;      // whether the task has been completed

    public Task(String name) {
        this(name, false);
    }

    public Task(String name, boolean completed) {
        if (name == null) {
            name = "untitled task";
        }
        this.name = name;
        this.completed = completed;
    }

    // copy constructor
    public Task(Task anotherTask) {
        this.name = anotherTask.name;
        this.completed = anotherTask.completed;
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

    // determines if two tasks are equal
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        Task otherTask = (Task) obj;

        if (!otherTask.getName().equals(this.getName())) {
            return false; // not equal, different task name
        }
        if (otherTask.isCompleted() != this.isCompleted()) {
            return false; // not equal, different completed status
        }
        
        return true;
    }
}
