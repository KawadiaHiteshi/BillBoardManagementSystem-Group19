package Business.Organization;

import Business.Employee.EmployeeDirectory;
import Business.UserAccount.UserAccountDirectory;
import Business.WorkQueue.WorkQueue;
import Business.Role.Role;
import java.util.ArrayList;

public abstract class Organization {

    private String name;
    private WorkQueue workQueue;
    private EmployeeDirectory employeeDirectory;
    private UserAccountDirectory userAccountDirectory;
    private int organizationID;
    private static int counter = 1;

    public enum Type {
        BILLBOARD_SALES("Billboard Sales Organization"),
        BILLBOARD_OPERATIONS("Billboard Operations Organization"),
        AGENCY_CLIENT_SERVICES("Agency Client Services Organization"),
        AGENCY_CAMPAIGN_PLANNING("Agency Campaign Planning Organization"),
        CITY_PERMITS("City Permits Organization"),
        COMPLIANCE_INSPECTION("Compliance Inspection Organization"),
        POWERGRID_MAINTENANCE("PowerGrid Maintenance Organization"),
        FIELD_ENGINEERS("Field Engineers Organization");

        private String value;

        private Type(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public Organization(String name) {
        this.name = name;
        workQueue = new WorkQueue();
        employeeDirectory = new EmployeeDirectory();
        userAccountDirectory = new UserAccountDirectory();
        organizationID = counter;
        ++counter;
    }

    public abstract ArrayList<Role> getSupportedRole();

    // Getters and setters
    public String getName() {
        return name;
    }

    public WorkQueue getWorkQueue() {
        return workQueue;
    }

    public EmployeeDirectory getEmployeeDirectory() {
        return employeeDirectory;
    }

    public UserAccountDirectory getUserAccountDirectory() {
        return userAccountDirectory;
    }

    public int getOrganizationID() {
        return organizationID;
    }

    @Override
    public String toString() {
        return name;
    }
}
