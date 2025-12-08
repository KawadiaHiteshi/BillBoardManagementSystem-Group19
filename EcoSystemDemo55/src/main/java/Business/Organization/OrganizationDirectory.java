package Business.Organization;

import java.util.ArrayList;

public class OrganizationDirectory {

    private ArrayList<Organization> organizationList;

    public OrganizationDirectory() {
        organizationList = new ArrayList<>();
    }

    public ArrayList<Organization> getOrganizationList() {
        return organizationList;
    }

    public Organization createOrganization(Organization.Type type) {
        Organization org = null;

        switch (type) {
            case BILLBOARD_SALES:
                org = new BillboardSalesOrganization();
                break;
            case BILLBOARD_OPERATIONS:
                org = new BillboardOperationsOrganization();
                break;
            case AGENCY_CLIENT_SERVICES:
                org = new AgencyClientServicesOrganization();
                break;
            case AGENCY_CAMPAIGN_PLANNING:
                org = new AgencyCampaignPlanningOrganization();
                break;
            case CITY_PERMITS:
                org = new CityPermitsOrganization();
                break;
            case COMPLIANCE_INSPECTION:
                org = new ComplianceInspectionOrganization();
                break;
            case POWERGRID_MAINTENANCE:
                org = new PowerGridMaintenanceOrganization();
                break;
            case FIELD_ENGINEERS:
                org = new FieldEngineersOrganization();
                break;
        }

        if (org != null) {
            organizationList.add(org);
        }

        return org;
    }
}
