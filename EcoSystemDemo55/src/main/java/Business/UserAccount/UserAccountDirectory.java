package Business.UserAccount;

import Business.Employee.Employee;
import Business.Role.Role;
import java.util.ArrayList;

public class UserAccountDirectory {

    private ArrayList<UserAccount> userAccountList;

    public UserAccountDirectory() {
        userAccountList = new ArrayList<>();
    }

    public UserAccount authenticateUser(String username, String password) {
        if (username == null || password == null) {
            return null;
        }

        for (UserAccount ua : userAccountList) {
            if (ua.getUsername().equals(username) && ua.getPassword().equals(password)) {
                if (ua.isActive()) {
                    return ua;
                }
            }
        }
        return null;
    }

    public UserAccount createUserAccount(String username, String password, Employee employee, Role role) {
        // Check for duplicate username
        if (checkIfUsernameExists(username)) {
            return null; // Username already exists
        }

        UserAccount ua = new UserAccount(username, password, employee, role);
        userAccountList.add(ua);
        return ua;
    }

    public boolean checkIfUsernameExists(String username) {
        for (UserAccount ua : userAccountList) {
            if (ua.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    public UserAccount getUserAccountByUsername(String username) {
        for (UserAccount ua : userAccountList) {
            if (ua.getUsername().equals(username)) {
                return ua;
            }
        }
        return null;
    }

    public ArrayList<UserAccount> getUserAccountList() {
        return userAccountList;
    }

    public int getTotalUsers() {
        return userAccountList.size();
    }

    public int getActiveUsers() {
        int count = 0;
        for (UserAccount ua : userAccountList) {
            if (ua.isActive()) {
                count++;
            }
        }
        return count;
    }
}
