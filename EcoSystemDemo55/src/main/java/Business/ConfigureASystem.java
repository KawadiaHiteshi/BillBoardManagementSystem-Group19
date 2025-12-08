package Business;

import Business.Billboard.Billboard;
import Business.Billboard.BillboardDirectory;
import Business.Billboard.BillboardStatus;
import Business.Employee.Employee;
import Business.Enterprise.*;
import Business.Network.Network;
import Business.Organization.*;
import Business.Role.*;
import Business.UserAccount.UserAccount;
import Business.WorkQueue.*;
import com.github.javafaker.Faker;

import java.util.Calendar;
import java.util.Date;

public class ConfigureASystem {

        private static Faker faker = new Faker();

        public static EcoSystem initialize() {

                EcoSystem system = EcoSystem.getInstance();

                // Create network
                Network bostonNetwork = system.createAndAddNetwork("Boston Billboard Network");

                // ========== ENTERPRISE 1: SkyView Billboards (Billboard Operator) ==========
                SkyViewBillboardEnterprise skyViewEnterprise = (SkyViewBillboardEnterprise) bostonNetwork
                                .getEnterpriseDirectory().createAndAddEnterprise(
                                                "SkyView Billboards", EnterpriseType.BILLBOARD_OPERATOR);

                // Organization 1: Billboard Sales
                BillboardSalesOrganization salesOrg = new BillboardSalesOrganization();
                skyViewEnterprise.getOrganizationDirectory().getOrganizationList().add(salesOrg);

                // Organization 2: Billboard Operations
                BillboardOperationsOrganization opsOrg = new BillboardOperationsOrganization();
                skyViewEnterprise.getOrganizationDirectory().getOrganizationList().add(opsOrg);

                // ========== ENTERPRISE 2: AdSpark Agency (Ad Agency) ==========
                AdAgencyEnterprise adAgencyEnterprise = (AdAgencyEnterprise) bostonNetwork.getEnterpriseDirectory()
                                .createAndAddEnterprise(
                                                "AdSpark Agency", EnterpriseType.AD_AGENCY);

                // Organization 3: Agency Client Services
                AgencyClientServicesOrganization clientServicesOrg = new AgencyClientServicesOrganization();
                adAgencyEnterprise.getOrganizationDirectory().getOrganizationList().add(clientServicesOrg);

                // Organization 4: Agency Campaign Planning
                AgencyCampaignPlanningOrganization campaignPlanningOrg = new AgencyCampaignPlanningOrganization();
                adAgencyEnterprise.getOrganizationDirectory().getOrganizationList().add(campaignPlanningOrg);

                // ========== ENTERPRISE 3: Boston City Services (City Services) ==========
                CityServicesEnterprise cityEnterprise = (CityServicesEnterprise) bostonNetwork.getEnterpriseDirectory()
                                .createAndAddEnterprise(
                                                "Boston City Services", EnterpriseType.CITY_SERVICES);

                // Organization 5: City Permits
                CityPermitsOrganization permitsOrg = new CityPermitsOrganization();
                cityEnterprise.getOrganizationDirectory().getOrganizationList().add(permitsOrg);

                // Organization 6: Compliance Inspection
                ComplianceInspectionOrganization complianceOrg = new ComplianceInspectionOrganization();
                cityEnterprise.getOrganizationDirectory().getOrganizationList().add(complianceOrg);

                // ========== ENTERPRISE 4: MassPower Utility (Power Utility) ==========
                PowerUtilityEnterprise powerEnterprise = (PowerUtilityEnterprise) bostonNetwork.getEnterpriseDirectory()
                                .createAndAddEnterprise(
                                                "MassPower Utility", EnterpriseType.POWER_UTILITY);

                // Organization 7: PowerGrid Maintenance
                PowerGridMaintenanceOrganization powerGridOrg = new PowerGridMaintenanceOrganization();
                powerEnterprise.getOrganizationDirectory().getOrganizationList().add(powerGridOrg);

                // Organization 8: Field Engineers
                FieldEngineersOrganization fieldEngOrg = new FieldEngineersOrganization();
                powerEnterprise.getOrganizationDirectory().getOrganizationList().add(fieldEngOrg);

                // ========== CREATE BILLBOARD INVENTORY ==========
                BillboardDirectory billboardDirectory = new BillboardDirectory();

                // Create 20+ billboards with Faker data
                String[] locations = {
                                "Downtown Boston - Tremont St", "Back Bay - Boylston St", "Fenway - Lansdowne St",
                                "South End - Washington St", "North End - Hanover St", "Seaport - Summer St",
                                "Cambridge - Mass Ave", "Brookline - Beacon St", "Somerville - Highland Ave",
                                "Allston - Harvard Ave", "Jamaica Plain - Centre St", "Roxbury - Warren St",
                                "Charlestown - Main St", "Dorchester - Dot Ave", "South Boston - Broadway",
                                "East Boston - Meridian St", "West Roxbury - Centre St", "Hyde Park - River St",
                                "Mattapan - Blue Hill Ave", "Roslindale - Washington St"
                };

                String[] types = { "Digital LED", "Static", "Digital LCD", "Billboard", "LED Screen" };
                String[] sizes = { "14x48", "10x30", "20x60", "12x24", "48x14" };

                for (int i = 0; i < 20; i++) {
                        String location = locations[i];
                        String type = types[faker.number().numberBetween(0, types.length)];
                        String size = sizes[faker.number().numberBetween(0, sizes.length)];
                        double price = faker.number().randomDouble(2, 200, 2000);
                        String description = "Premium " + type + " billboard in " + location.split(" - ")[0];

                        boolean isLighted = type.contains("Digital") || type.contains("LED");
                        Billboard billboard = billboardDirectory.createBillboard(location, size, type, price,
                                        description, isLighted);

                        // Set some billboards to different statuses
                        if (i % 5 == 0 && i != 0) {
                                billboard.setStatus(BillboardStatus.BOOKED);
                        } else if (i % 7 == 0) {
                                billboard.setStatus(BillboardStatus.MAINTENANCE);
                        }
                }

                skyViewEnterprise.setBillboardDirectory(billboardDirectory);

                // ========== CREATE EMPLOYEES USING FAKER ==========

                // SkyView Employees
                Employee salesManager1 = new Employee(faker.name().fullName());
                Employee salesManager2 = new Employee(faker.name().fullName());
                Employee opsManager1 = new Employee(faker.name().fullName());
                Employee opsManager2 = new Employee(faker.name().fullName());

                salesOrg.getEmployeeDirectory().getEmployeeList().add(salesManager1);
                salesOrg.getEmployeeDirectory().getEmployeeList().add(salesManager2);
                opsOrg.getEmployeeDirectory().getEmployeeList().add(opsManager1);
                opsOrg.getEmployeeDirectory().getEmployeeList().add(opsManager2);

                // AdSpark Employees
                Employee accountManager1 = new Employee(faker.name().fullName());
                Employee accountManager2 = new Employee(faker.name().fullName());
                Employee campaignPlanner1 = new Employee(faker.name().fullName());
                Employee campaignPlanner2 = new Employee(faker.name().fullName());
                Employee brandManager1 = new Employee(faker.name().fullName());

                clientServicesOrg.getEmployeeDirectory().getEmployeeList().add(accountManager1);
                clientServicesOrg.getEmployeeDirectory().getEmployeeList().add(accountManager2);
                clientServicesOrg.getEmployeeDirectory().getEmployeeList().add(brandManager1);
                campaignPlanningOrg.getEmployeeDirectory().getEmployeeList().add(campaignPlanner1);
                campaignPlanningOrg.getEmployeeDirectory().getEmployeeList().add(campaignPlanner2);

                // City Services Employees
                Employee permitOfficer1 = new Employee(faker.name().fullName());
                Employee permitOfficer2 = new Employee(faker.name().fullName());
                Employee inspector1 = new Employee(faker.name().fullName());
                Employee inspector2 = new Employee(faker.name().fullName());
                Employee safetyOfficer1 = new Employee(faker.name().fullName());

                permitsOrg.getEmployeeDirectory().getEmployeeList().add(permitOfficer1);
                permitsOrg.getEmployeeDirectory().getEmployeeList().add(permitOfficer2);
                complianceOrg.getEmployeeDirectory().getEmployeeList().add(inspector1);
                complianceOrg.getEmployeeDirectory().getEmployeeList().add(inspector2);
                complianceOrg.getEmployeeDirectory().getEmployeeList().add(safetyOfficer1);

                // Power Utility Employees
                Employee powerCoordinator1 = new Employee(faker.name().fullName());
                Employee powerCoordinator2 = new Employee(faker.name().fullName());
                Employee fieldEngineer1 = new Employee(faker.name().fullName());
                Employee fieldEngineer2 = new Employee(faker.name().fullName());
                Employee fieldEngineer3 = new Employee(faker.name().fullName());

                powerGridOrg.getEmployeeDirectory().getEmployeeList().add(powerCoordinator1);
                powerGridOrg.getEmployeeDirectory().getEmployeeList().add(powerCoordinator2);
                fieldEngOrg.getEmployeeDirectory().getEmployeeList().add(fieldEngineer1);
                fieldEngOrg.getEmployeeDirectory().getEmployeeList().add(fieldEngineer2);
                fieldEngOrg.getEmployeeDirectory().getEmployeeList().add(fieldEngineer3);

                // ========== CREATE USER ACCOUNTS FOR ALL ROLES ==========

                // System Admin
                Employee sysAdminEmp = new Employee("System Administrator");
                system.getUserAccountDirectory().createUserAccount("sysadmin", "admin", sysAdminEmp,
                                new SystemAdminRole());

                // Enterprise Admins
                Employee skyViewAdmin = new Employee("SkyView Admin");
                skyViewEnterprise.getUserAccountDirectory().createUserAccount("billboardadmin", "admin", skyViewAdmin,
                                new AdminRole());

                Employee adSparkAdmin = new Employee("AdSpark Admin");
                adAgencyEnterprise.getUserAccountDirectory().createUserAccount("agencyadmin", "admin", adSparkAdmin,
                                new AdminRole());

                Employee cityAdmin = new Employee("City Services Admin");
                cityEnterprise.getUserAccountDirectory().createUserAccount("cityadmin", "admin", cityAdmin,
                                new AdminRole());

                Employee powerAdmin = new Employee("MassPower Admin");
                powerEnterprise.getUserAccountDirectory().createUserAccount("poweradmin", "admin", powerAdmin,
                                new AdminRole());

                // SkyView Accounts
                salesOrg.getUserAccountDirectory().createUserAccount("sales1", "Sales123", salesManager1,
                                new BillboardSalesManagerRole());
                salesOrg.getUserAccountDirectory().createUserAccount("sales2", "Sales123", salesManager2,
                                new BillboardSalesManagerRole());
                opsOrg.getUserAccountDirectory().createUserAccount("ops1", "Ops123", opsManager1,
                                new BillboardOperationsManagerRole());
                opsOrg.getUserAccountDirectory().createUserAccount("ops2", "Ops123", opsManager2,
                                new BillboardOperationsManagerRole());

                // AdSpark Accounts
                clientServicesOrg.getUserAccountDirectory().createUserAccount("account1", "Account123", accountManager1,
                                new AgencyAccountManagerRole());
                clientServicesOrg.getUserAccountDirectory().createUserAccount("account2", "Account123", accountManager2,
                                new AgencyAccountManagerRole());
                clientServicesOrg.getUserAccountDirectory().createUserAccount("brand1", "Brand123", brandManager1,
                                new BrandMarketingManagerRole());
                campaignPlanningOrg.getUserAccountDirectory().createUserAccount("planner1", "Planner123",
                                campaignPlanner1, new CampaignPlannerRole());
                campaignPlanningOrg.getUserAccountDirectory().createUserAccount("planner2", "Planner123",
                                campaignPlanner2, new CampaignPlannerRole());

                // City Services Accounts
                permitsOrg.getUserAccountDirectory().createUserAccount("permit1", "Permit123", permitOfficer1,
                                new CityPermitOfficerRole());
                permitsOrg.getUserAccountDirectory().createUserAccount("permit2", "Permit123", permitOfficer2,
                                new CityPermitOfficerRole());
                complianceOrg.getUserAccountDirectory().createUserAccount("inspector1", "Inspector123", inspector1,
                                new CityComplianceInspectorRole());
                complianceOrg.getUserAccountDirectory().createUserAccount("inspector2", "Inspector123", inspector2,
                                new CityComplianceInspectorRole());
                complianceOrg.getUserAccountDirectory().createUserAccount("safety1", "Safety123", safetyOfficer1,
                                new SafetyCheckOfficerRole());

                // Power Utility Accounts
                powerGridOrg.getUserAccountDirectory().createUserAccount("power1", "Power123", powerCoordinator1,
                                new PowerGridCoordinatorRole());
                powerGridOrg.getUserAccountDirectory().createUserAccount("power2", "Power123", powerCoordinator2,
                                new PowerGridCoordinatorRole());
                fieldEngOrg.getUserAccountDirectory().createUserAccount("engineer1", "Engineer123", fieldEngineer1,
                                new FieldMaintenanceEngineerRole());
                fieldEngOrg.getUserAccountDirectory().createUserAccount("engineer2", "Engineer123", fieldEngineer2,
                                new FieldMaintenanceEngineerRole());
                fieldEngOrg.getUserAccountDirectory().createUserAccount("engineer3", "Engineer123", fieldEngineer3,
                                new FieldMaintenanceEngineerRole());

                // ========== CREATE SAMPLE WORK REQUESTS ==========

                // Get user accounts for work requestsci
                UserAccount accountMgr = clientServicesOrg.getUserAccountDirectory()
                                .getUserAccountByUsername("account1");
                UserAccount planner = campaignPlanningOrg.getUserAccountDirectory()
                                .getUserAccountByUsername("planner1");
                UserAccount salesMgr = salesOrg.getUserAccountDirectory().getUserAccountByUsername("sales1");
                UserAccount permitOfficer = permitsOrg.getUserAccountDirectory().getUserAccountByUsername("permit1");
                UserAccount inspector = complianceOrg.getUserAccountDirectory().getUserAccountByUsername("inspector1");
                UserAccount opsMgr = opsOrg.getUserAccountDirectory().getUserAccountByUsername("ops1");
                UserAccount powerCoord = powerGridOrg.getUserAccountDirectory().getUserAccountByUsername("power1");
                UserAccount engineer = fieldEngOrg.getUserAccountDirectory().getUserAccountByUsername("engineer1");

                // Request 1: Cross-Organization (Agency Client Services → Campaign Planning)
                BoardSelectionRequest boardSelectionReq = new BoardSelectionRequest();
                boardSelectionReq.setSender(accountMgr);
                boardSelectionReq.setReceiver(planner);
                boardSelectionReq.setMessage("Please select billboards for Nike Summer Campaign");
                boardSelectionReq.setCampaignName("Nike Summer 2024");
                boardSelectionReq.addBoardId(1000);
                boardSelectionReq.addBoardId(1005);
                boardSelectionReq.setStatus("Pending");
                campaignPlanningOrg.getWorkQueue().getWorkRequestList().add(boardSelectionReq);

                // Request 2: Cross-Enterprise (AdSpark → SkyView Billboards)
                CampaignBookingRequest bookingReq = new CampaignBookingRequest();
                bookingReq.setSender(planner);
                bookingReq.setReceiver(salesMgr);
                bookingReq.setMessage("Book billboard for Nike campaign");
                bookingReq.setCampaignName("Nike Summer 2024");
                bookingReq.setClientName("Nike Inc.");
                bookingReq.setBoardId(1000);
                bookingReq.setStartDate(getDateDaysFromNow(10));
                bookingReq.setEndDate(getDateDaysFromNow(40));
                bookingReq.setBudget(15000.00);
                bookingReq.setStatus("Pending");
                salesOrg.getWorkQueue().getWorkRequestList().add(bookingReq);

                // Request 3: Cross-Enterprise (SkyView → City Services)
                PermitRequest permitReq = new PermitRequest();
                permitReq.setSender(salesMgr);
                permitReq.setReceiver(permitOfficer);
                permitReq.setMessage("Permit request for Nike campaign billboard");
                permitReq.setBoardId(1000);
                permitReq.setRequestedBy("SkyView Billboards");
                permitReq.setValidUntil(getDateDaysFromNow(60));
                permitReq.setStatus("Pending");
                permitsOrg.getWorkQueue().getWorkRequestList().add(permitReq);

                // Request 4: Cross-Enterprise (City → SkyView)
                ComplianceInspectionRequest inspectionReq = new ComplianceInspectionRequest();
                inspectionReq.setSender(permitOfficer);
                inspectionReq.setReceiver(inspector);
                inspectionReq.setMessage("Safety inspection required for billboard #1005");
                inspectionReq.setBoardId(1005);
                inspectionReq.setInspectionType("Safety");
                inspectionReq.setStatus("Pending");
                complianceOrg.getWorkQueue().getWorkRequestList().add(inspectionReq);

                // Request 5: Cross-Organization (SkyView Operations → Sales)
                MaintenanceRequest maintenanceReq = new MaintenanceRequest();
                maintenanceReq.setSender(opsMgr);
                maintenanceReq.setReceiver(salesMgr);
                maintenanceReq.setMessage("Billboard #1010 requires urgent maintenance");
                maintenanceReq.setBoardId(1010);
                maintenanceReq.setIssueDescription("LED panel flickering");
                maintenanceReq.setUrgencyLevel("High");
                maintenanceReq.setStatus("Pending");
                salesOrg.getWorkQueue().getWorkRequestList().add(maintenanceReq);

                // Request 6: Cross-Enterprise (SkyView → MassPower)
                PowerIssueRequest powerReq = new PowerIssueRequest();
                powerReq.setSender(opsMgr);
                powerReq.setReceiver(powerCoord);
                powerReq.setMessage("Power outage at billboard #1015");
                powerReq.setBoardId(1015);
                powerReq.setSeverity("Critical");
                powerReq.setOutageType("Complete");
                powerReq.setStatus("Pending");
                powerGridOrg.getWorkQueue().getWorkRequestList().add(powerReq);

                // Request 8: Incoming Maintenance Request (Sales -> Operations) linked to board
                // logic
                MaintenanceRequest incomingMaintReq = new MaintenanceRequest();
                incomingMaintReq.setSender(salesMgr);
                // No specific receiver, just to the organization
                incomingMaintReq.setMessage("Panel damage reported by client");
                incomingMaintReq.setBoardId(1003);
                incomingMaintReq.setIssueDescription("Physical damage to frame");
                incomingMaintReq.setUrgencyLevel("Medium");
                incomingMaintReq.setStatus("Sent");
                opsOrg.getWorkQueue().getWorkRequestList().add(incomingMaintReq);

                // Request 7: Within Power Utility (PowerGrid → Field Engineers)
                FieldEngineerAssignmentRequest fieldReq = new FieldEngineerAssignmentRequest();
                fieldReq.setSender(powerCoord);
                fieldReq.setReceiver(engineer);
                fieldReq.setMessage("Assign engineer to fix power issue at billboard #1015");
                fieldReq.setEngineerId(engineer.getEmployee().getId());
                fieldReq.setTaskDescription("Restore power to billboard #1015 - Downtown Boston");
                fieldReq.setStatus("Pending");
                fieldEngOrg.getWorkQueue().getWorkRequestList().add(fieldReq);

                return system;
        }

        private static Date getDateDaysFromNow(int days) {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_MONTH, days);
                return cal.getTime();
        }
}
