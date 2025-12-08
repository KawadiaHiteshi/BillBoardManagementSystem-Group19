package Business.UserAccount;

import Business.Employee.Employee;
import Business.Role.Role;

public class UserAccount {

    private String username;
    private String password;
    private Employee employee;
    private Role role;
    private boolean isActive;
    private Business.WorkQueue.WorkQueue workQueue;

    public UserAccount(String username, String password, Employee employee, Role role) {
        this.username = username;
        this.password = password;
        this.employee = employee;
        this.role = role;
        this.isActive = true;
        this.workQueue = new Business.WorkQueue.WorkQueue();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Business.WorkQueue.WorkQueue getWorkQueue() {
        return workQueue;
    }

    @Override
    public String toString() {
        return username + " (" + role.toString() + ")";
    }
}
