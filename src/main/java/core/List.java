
package core;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class List {
    private String name;                // the name of this list
    private java.util.List<Task> tasks; // the tasks in this list
    
    public List(String name) {
        this.name = name;
        this.tasks = new java.util.ArrayList<Task>();
    }
    
    // copy constructor
    public List(List anotherList) {
        this.name = anotherList.name;
        this.tasks = new java.util.ArrayList<Task>();
        for (Task aTask : anotherList.getTasks()) {
            this.tasks.add(new Task(aTask));
        }
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
    
    // returns a List object parsed from an XML file
    public static List fromXMLFile(File file) throws ParserConfigurationException,
            SAXException, IOException {
        List list = null;
        
        DocumentBuilder docReader = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document xmlDoc = docReader.parse(file); // will throw an exception if cannot parse input
        
        Element rootElement = xmlDoc.getDocumentElement(); // root element is <todo>
        Element listElement = (Element) rootElement.getElementsByTagName("list").item(0);
        
        String listName = listElement.getAttribute("name");
        list = new List(listName);
        
        // iterate through the tasks and add them to the list
        NodeList nodeList = listElement.getElementsByTagName("task");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element taskElement = (Element) nodeList.item(i);
            Boolean completed = Boolean.valueOf(taskElement.getAttribute("completed"));
            String taskName = taskElement.getTextContent();
            list.addTask(new Task(taskName, completed));
        }
        
        return list;
    }
    
    // determines if two lists are equal
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        
        List otherList = (List) obj;

        if (!otherList.getName().equals(this.getName())) {
            return false; // different list name
        }
        
        java.util.List<Task> otherListTasks = otherList.getTasks();
        java.util.List<Task> thisListTasks = this.getTasks();
        if (otherListTasks.size() != thisListTasks.size()) {
            return false; // different number of tasks in list
        }
        
        for (int i = 0; i < otherListTasks.size(); i++) {
            Task otherListTask = otherListTasks.get(i);
            Task thisListTask = thisListTasks.get(i);
            if (!otherListTask.getName().equals(thisListTask.getName())) {
                return false;
            }
            if (otherListTask.isCompleted() != thisListTask.isCompleted()) {
                return false;
            }
        }
        
        return true;
    }
}
