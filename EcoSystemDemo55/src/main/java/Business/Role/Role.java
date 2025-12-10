package Business.Role;

import Business.Enterprise.Enterprise;
import Business.Organization.Organization;
import Business.UserAccount.UserAccount;
import Business.EcoSystem;
import javax.swing.JPanel;

public abstract class Role {

    public enum RoleType {
        BILLBOARD_SALES_MANAGER,
        BILLBOARD_OPERATIONS_MANAGER,
        AGENCY_MANAGER,
        CAMPAIGN_PLANNER,
        CLIENT,
        PERMIT_OFFICER,
        INSPECTOR,
        POWER_MANAGER,
        TECHNICIAN,
        SAFETY_OFFICER
    }

    public abstract JPanel createWorkArea(JPanel userProcessContainer,
                                          UserAccount account,
                                          Organization organization,
                                          Enterprise enterprise,
                                          EcoSystem business);

    @Override
    public String toString(){
        return this.getClass().getSimpleName();
    }
}
