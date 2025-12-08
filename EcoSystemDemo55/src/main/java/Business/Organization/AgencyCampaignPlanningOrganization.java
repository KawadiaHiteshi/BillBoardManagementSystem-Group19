/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Business.Organization;

import Business.Role.CampaignPlannerRole;
import Business.Role.Role;
import java.util.ArrayList;
/**
 *
 * @author prekshapraveen
 */
public class AgencyCampaignPlanningOrganization extends Organization{
    public AgencyCampaignPlanningOrganization() {
        super(Type.AGENCY_CAMPAIGN_PLANNING.getValue());
    }

    @Override
    public ArrayList<Role> getSupportedRole() {
        ArrayList<Role> roles = new ArrayList<>();
        roles.add(new CampaignPlannerRole());
        return roles;
    }
}
