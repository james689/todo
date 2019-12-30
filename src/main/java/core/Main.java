
package core;

public class Main {
    public static void main(String[] args) {
        List chores = new List("chores");
        System.out.println("chores.getName() = " + chores.getName());
        chores.setName("shopping");
        System.out.println("chores.getName() = " + chores.getName());
        chores.setName("biscuits");
        System.out.println("chores.getName() = " + chores.getName());
    }
}
