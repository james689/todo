
package gui;

import core.List;
import java.io.File;

public class Controller {
    private View view;
    //private Model model;
    
    public Controller() {
        view = new View(this);
        view.createView();
    }
    
    public void openList(File file) {
        //model.openList(file); // model will notify the view of the change in state
    }
    
    public void newList() {
        List newList = new List("default list");
        //model.setList(newList); // model will notify the view of the change in state
    }
}
