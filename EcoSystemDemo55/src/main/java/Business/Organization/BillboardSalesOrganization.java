// BillboardSalesOrganization.java
package Business.Organization;

import Business.Role.BillboardSalesManagerRole;
import Business.Role.BrandMarketingManagerRole;
import Business.Role.Role;
import java.util.ArrayList;

public class BillboardSalesOrganization extends Organization {

    public BillboardSalesOrganization() {
        super("Billboard Sales Organization");
    }

    @Override
public ArrayList<Role> getSupportedRole() {
    ArrayList<Role> roles = new ArrayList<>();
    roles.add(new BillboardSalesManagerRole());
    return roles;
}

}
