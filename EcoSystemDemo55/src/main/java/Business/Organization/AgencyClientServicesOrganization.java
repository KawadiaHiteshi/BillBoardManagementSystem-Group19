/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Business.Organization;


import Business.Role.AgencyAccountManagerRole;
import Business.Role.BrandMarketingManagerRole;
import Business.Role.Role;
import java.util.ArrayList;
/**
 *
 * @author prekshapraveen
 */
public class AgencyClientServicesOrganization extends Organization{
    public AgencyClientServicesOrganization() {
        super(Type.AGENCY_CLIENT_SERVICES.getValue());
    }

    @Override
    public ArrayList<Role> getSupportedRole() {
        ArrayList<Role> roles = new ArrayList<>();
        roles.add(new AgencyAccountManagerRole());
        roles.add(new BrandMarketingManagerRole()); // brand-side login, if needed
        return roles;
    }
}
