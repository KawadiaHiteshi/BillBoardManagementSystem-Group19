/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Business.Organization;

import Business.Role.PowerGridCoordinatorRole;
import Business.Role.Role;
import java.util.ArrayList;
/**
 *
 * @author prekshapraveen
 */
public class PowerGridMaintenanceOrganization extends Organization {

    public PowerGridMaintenanceOrganization() {
        super(Type.POWERGRID_MAINTENANCE.getValue());
    }

    @Override
    public ArrayList<Role> getSupportedRole() {
        ArrayList<Role> roles = new ArrayList<>();
        roles.add(new PowerGridCoordinatorRole());
        return roles;
    }
}
