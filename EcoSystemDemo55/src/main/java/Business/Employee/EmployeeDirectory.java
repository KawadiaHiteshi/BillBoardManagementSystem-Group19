package Business.Employee;

import java.util.ArrayList;

public class EmployeeDirectory {

    private ArrayList<Employee> employeeList;

    public EmployeeDirectory() {
        employeeList = new ArrayList<>();
    }

    public Employee createEmployee(String name) {
        Employee e = new Employee(name);
        employeeList.add(e);
        return e;
    }

    public ArrayList<Employee> getEmployeeList() {
        return employeeList;
    }
}
