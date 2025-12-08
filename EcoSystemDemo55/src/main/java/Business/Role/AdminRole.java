package Business.Role;

import Business.EcoSystem;
import Business.Enterprise.Enterprise;
import Business.Organization.Organization;
import Business.UserAccount.UserAccount;
import javax.swing.JPanel;

/**
 * AdminRole for enterprise administrators
 */
public class AdminRole extends Role {

    @Override
    public JPanel createWorkArea(JPanel container, UserAccount account, Organization org, Enterprise enterprise,
            EcoSystem system) {
        return new ui.AdministrativeRole.AdminWorkAreaJPanel(container, enterprise);
    }
}
