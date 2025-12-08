package Business.Enterprise;

import java.util.ArrayList;

public class EnterpriseDirectory {

    private ArrayList<Enterprise> enterpriseList;

    public EnterpriseDirectory() {
        enterpriseList = new ArrayList<>();
    }

    public ArrayList<Enterprise> getEnterpriseList() {
        return enterpriseList;
    }

    public Enterprise createAndAddEnterprise(String name, EnterpriseType type) {
        Enterprise enterprise = null;
        switch (type) {
            case BILLBOARD_OPERATOR:
                enterprise = new SkyViewBillboardEnterprise(name);
                break;
            case AD_AGENCY:
                enterprise = new AdAgencyEnterprise(name);
                break;
            case CITY_SERVICES:
                enterprise = new CityServicesEnterprise(name);
                break;
            case POWER_UTILITY:
                enterprise = new PowerUtilityEnterprise(name);
                break;
        }
        enterpriseList.add(enterprise);
        return enterprise;
    }
}
