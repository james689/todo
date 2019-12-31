
package core;

import java.io.PrintWriter;

public class Main {
    public static void main(String[] args) {
        List newYearResolutions = new List("New Year Resolutions");
        newYearResolutions.addTask(new Task("get a computing job"));
        newYearResolutions.addTask(new Task("get a house"));
        newYearResolutions.addTask(new Task("start learning piano"));
        newYearResolutions.addTask(new Task("go gym and do more cardio"));
        
        List houseChores = new List("House Chores");
        houseChores.addTask(new Task("wash pots"));
        houseChores.addTask(new Task("hoover stairs"));
        Task cleanBathroom = new Task("clean bathroom");
        cleanBathroom.setCompleted(true);
        houseChores.addTask(cleanBathroom);
        
        PrintWriter out = new PrintWriter(System.out);
        out.println("<?xml version=\"1.0\"?>");
        out.println("<todo version=\"1.0\">");
        newYearResolutions.printAsXML(out);
        houseChores.printAsXML(out);
        out.println("</todo>");
        out.close();
    }
}
