package Business.Enterprise;

import Business.Organization.OrganizationDirectory;
import Business.UserAccount.UserAccountDirectory;

public abstract class Enterprise {

    private String name;
    private EnterpriseType enterpriseType;
    private OrganizationDirectory organizationDirectory;
    private UserAccountDirectory userAccountDirectory;

    public Enterprise(String name, EnterpriseType type) {
        this.name = name;
        this.enterpriseType = type;
        this.organizationDirectory = new OrganizationDirectory();
        this.userAccountDirectory = new UserAccountDirectory();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EnterpriseType getEnterpriseType() {
        return enterpriseType;
    }

    public OrganizationDirectory getOrganizationDirectory() {
        return organizationDirectory;
    }

    public UserAccountDirectory getUserAccountDirectory() {
        return userAccountDirectory;
    }

    @Override
    public String toString() {
        return name;
    }
}
