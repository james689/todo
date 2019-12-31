
package core;

public class Model {
    private List currentList; // the list the user currently has open (this is the
    // model's state)
    
    public Model() {
        currentList = null;
    }
    
    public void createNewList(String listName) {
        currentList = new List(listName);
    }
    
    // open existing list
    public void openList(String filename) {
        //currentList = // parse XML file and create list from it
    }
    
    // allows view to get access to model's state
    public List getCurrentList() {
        return currentList;
    }
    
    public void addTaskToList(Task task) {
        currentList.addTask(task);
        // notify observers of change in list
    }
    
    public void removeTaskFromList(Task task) {
        currentList.removeTask(task);
        // notify observers of change in list
    }
}
