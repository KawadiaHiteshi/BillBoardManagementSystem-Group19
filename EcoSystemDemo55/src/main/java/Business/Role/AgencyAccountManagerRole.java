/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Business.Role;

import Business.EcoSystem;
import Business.Enterprise.Enterprise;
import Business.Organization.Organization;
import Business.UserAccount.UserAccount;
import ui.AgencyAccountRole.AgencyAccountWorkAreaJPanel;
import javax.swing.JPanel;

/**
 *
 * @author prekshapraveen
 */
public class AgencyAccountManagerRole extends Role {

    @Override
    public JPanel createWorkArea(JPanel container, UserAccount account, Organization org, Enterprise enterprise,
            EcoSystem system) {
        return new AgencyAccountWorkAreaJPanel(container, account, org, enterprise, system);
    }

}
