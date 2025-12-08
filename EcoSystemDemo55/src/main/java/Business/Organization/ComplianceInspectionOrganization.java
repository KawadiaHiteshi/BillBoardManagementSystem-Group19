/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Business.Organization;

import Business.Role.CityComplianceInspectorRole;
import Business.Role.SafetyCheckOfficerRole;
import Business.Role.Role;
import java.util.ArrayList;
/**
 *
 * @author prekshapraveen
 */
public class ComplianceInspectionOrganization extends Organization {
    
    public ComplianceInspectionOrganization() {
        super(Type.COMPLIANCE_INSPECTION.getValue());
    }

    @Override
    public ArrayList<Role> getSupportedRole() {
        ArrayList<Role> roles = new ArrayList<>();
        roles.add(new CityComplianceInspectorRole());
        roles.add(new SafetyCheckOfficerRole());
        return roles;
    }
}
