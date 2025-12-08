package Business.Employee;

public class Employee {

    private int id;
    private String name;
    private static int count = 1;

    public Employee(String name) {
        this.name = name;
        id = count++;
    }

    public String getName() { return name; }
    public int getId() { return id; }
}
