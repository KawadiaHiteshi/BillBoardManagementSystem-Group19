package ui.BillboardSalesRole;

import Business.EcoSystem;
import Business.Enterprise.Enterprise;
import Business.Organization.Organization;
import Business.UserAccount.UserAccount;
import Business.WorkQueue.CampaignBookingRequest;
import Business.WorkQueue.BoardSelectionRequest;
import Business.WorkQueue.WorkRequest;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BillboardSalesWorkAreaJPanel extends JPanel {

    private JPanel userProcessContainer;
    private UserAccount account;
    private Organization organization;
    private Enterprise enterprise;
    private EcoSystem business;
    private JTable workRequestTable;
    private DefaultTableModel tableModel;
    private JTabbedPane tabbedPane;
    private JTable inventoryTable;
    private DefaultTableModel inventoryModel;

    // Professional color scheme
    private static final Color HEADER_BG = new Color(41, 128, 185); // Professional blue
    private static final Color BUTTON_BG = new Color(52, 152, 219); // Lighter blue
    private static final Color APPROVE_BG = new Color(46, 204, 113); // Green
    private static final Color REJECT_BG = new Color(231, 76, 60); // Red
    private static final Color PANEL_BG = new Color(236, 240, 241); // Light gray

    public BillboardSalesWorkAreaJPanel(JPanel userProcessContainer, UserAccount account,
            Organization organization, Enterprise enterprise,
            EcoSystem business) {
        this.userProcessContainer = userProcessContainer;
        this.account = account;
        this.organization = organization;
        this.enterprise = enterprise;
        this.business = business;

        initComponents();
        populateWorkRequests();
        populateInventory();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(PANEL_BG);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(HEADER_BG);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));

        JLabel titleLabel = new JLabel("Billboard Sales Manager");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createHorizontalGlue());

        JLabel welcomeLabel = new JLabel("Welcome, " + account.getUsername());
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        welcomeLabel.setForeground(Color.WHITE);
        headerPanel.add(welcomeLabel);

        add(headerPanel, BorderLayout.NORTH);

        // Tabbed Pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Tab 1: Dashboard / Inventory
        JPanel dashboardPanel = createDashboardPanel();
        tabbedPane.addTab("Dashboard & Inventory", dashboardPanel);

        // Tab 2: Work Requests
        JPanel requestsPanel = createRequestsPanel();
        tabbedPane.addTab("Work Requests", requestsPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblInventory = new JLabel("Billboard Inventory Status");
        lblInventory.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.add(lblInventory, BorderLayout.NORTH);

        String[] columnNames = { "ID", "Location", "Type", "Lighted", "Status", "Price/Day" };
        inventoryModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        inventoryTable = new JTable(inventoryModel);
        styleTable(inventoryTable);

        JScrollPane scrollPane = new JScrollPane(inventoryTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton refreshButton = createStyledButton("Refresh Inventory", BUTTON_BG);
        refreshButton.addActionListener(e -> populateInventory());
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(refreshButton);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createRequestsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblRequests = new JLabel("Incoming Campaign Booking Requests");
        lblRequests.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.add(lblRequests, BorderLayout.NORTH);

        String[] columnNames = { "Request ID", "Board ID", "Type", "Sender", "Campaign", "Status", "Message" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        workRequestTable = new JTable(tableModel);
        styleTable(workRequestTable);

        JScrollPane scrollPane = new JScrollPane(workRequestTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(Color.WHITE);

        JButton approveButton = createStyledButton("✓ Approve Booking", APPROVE_BG);
        JButton rejectButton = createStyledButton("✗ Reject Booking", REJECT_BG);
        JButton createPermitButton = createStyledButton("Create Permit Request", BUTTON_BG);
        JButton refreshButton = createStyledButton("Refresh", new Color(149, 165, 166));

        approveButton.addActionListener(e -> handleApprove());
        rejectButton.addActionListener(e -> handleReject());
        createPermitButton.addActionListener(e -> handleCreatePermit());
        refreshButton.addActionListener(e -> populateWorkRequests());

        buttonPanel.add(approveButton);
        buttonPanel.add(rejectButton);
        buttonPanel.add(createPermitButton);
        buttonPanel.add(refreshButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(28);
        table.setSelectionBackground(new Color(52, 152, 219));
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(new Color(189, 195, 199));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(52, 73, 94));
        table.getTableHeader().setForeground(Color.WHITE);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(180, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void populateInventory() {
        inventoryModel.setRowCount(0);
        if (enterprise instanceof Business.Enterprise.SkyViewBillboardEnterprise) {
            Business.Enterprise.SkyViewBillboardEnterprise skyView = (Business.Enterprise.SkyViewBillboardEnterprise) enterprise;
            for (Business.Billboard.Billboard b : skyView.getBillboardDirectory().getBillboards()) {
                Object[] row = {
                        b.getBoardId(),
                        b.getLocation(),
                        b.getType(),
                        b.isLighted() ? "Yes" : "No",
                        b.getStatus().toString(),
                        String.format("$%.2f", b.getPricePerDay())
                };
                inventoryModel.addRow(row);
            }
        }
    }

    private void populateWorkRequests() {
        tableModel.setRowCount(0);
        for (WorkRequest request : organization.getWorkQueue().getWorkRequestList()) {
            if (request instanceof CampaignBookingRequest) {
                CampaignBookingRequest cbr = (CampaignBookingRequest) request;
                Object[] row = {
                        request, // Store object to retrieve later (now displays ID via toString())
                        cbr.getBoardId(),
                        "Campaign Booking",
                        request.getSender() != null ? request.getSender().getUsername() : "N/A",
                        cbr.getCampaignName() != null ? cbr.getCampaignName() : "N/A",
                        request.getStatus(),
                        request.getMessage()
                };
                tableModel.addRow(row);
            } else if (request instanceof BoardSelectionRequest) {
                BoardSelectionRequest bsr = (BoardSelectionRequest) request;
                String boardIds = bsr.getSelectedBoardIds().toString();
                Object[] row = {
                        request,
                        boardIds, // Shows list of IDs e.g. [1, 5]
                        "Board Proposal",
                        request.getSender() != null ? request.getSender().getUsername() : "N/A",
                        bsr.getCampaignName() != null ? bsr.getCampaignName() : "N/A",
                        request.getStatus(),
                        request.getMessage()
                };
                tableModel.addRow(row);
            }
        }
    }

    private void handleApprove() {
        int selectedRow = workRequestTable.getSelectedRow();
        if (selectedRow >= 0) {
            WorkRequest request = (WorkRequest) workRequestTable.getValueAt(selectedRow, 0);
            if (!request.getStatus().equals("Pending") && !request.getStatus().equals("Sent to Sales")) {
                JOptionPane.showMessageDialog(this, "Request is already processed.", "Warning",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Logic to update billboard status
            if (request instanceof CampaignBookingRequest) {
                CampaignBookingRequest cbr = (CampaignBookingRequest) request;

                if (enterprise instanceof Business.Enterprise.SkyViewBillboardEnterprise) {
                    Business.Enterprise.SkyViewBillboardEnterprise skyView = (Business.Enterprise.SkyViewBillboardEnterprise) enterprise;
                    Business.Billboard.Billboard b = skyView.getBillboardDirectory().getBillboardById(cbr.getBoardId());

                    if (b != null) {
                        // Check Availability
                        if (b.isAvailableForDates(cbr.getStartDate(), cbr.getEndDate())) {
                            // Available - Approve
                            b.setStatus(Business.Billboard.BillboardStatus.BOOKED);
                            b.addBooking(cbr.getStartDate(), cbr.getEndDate(), cbr.getClientName());

                            request.setStatus("Approved");
                            request.setResolveDate(new java.util.Date());
                            JOptionPane.showMessageDialog(this, "Booking Approved & Recorded!");
                        } else {
                            // Conflict - Waitlist
                            b.joinWaitlist(request.getSender(), "Booking Conflict for " + cbr.getClientName());

                            request.setStatus("Waitlisted");
                            request.setMessage("Board Occupied. Added to Waitlist.");
                            request.setResolveDate(new java.util.Date());

                            JOptionPane.showMessageDialog(this, "Board is Occupied! Request added to Waitlist.",
                                    "Waitlisted", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
            } else if (request instanceof BoardSelectionRequest) {
                request.setStatus("Approved");
                request.setResolveDate(new java.util.Date());
                JOptionPane.showMessageDialog(this,
                        "Proposal Approved. Please create formal bookings for these boards if needed.", "Info",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            // Assign receiver if null
            if (request.getReceiver() == null) {
                request.setReceiver(account);
            }

            populateWorkRequests();
            populateInventory();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a request to approve.", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleReject() {
        int selectedRow = workRequestTable.getSelectedRow();
        if (selectedRow >= 0) {
            WorkRequest request = (WorkRequest) workRequestTable.getValueAt(selectedRow, 0);
            if (!request.getStatus().equals("Pending") && !request.getStatus().equals("Sent to Sales")) {
                JOptionPane.showMessageDialog(this, "Request is already processed.", "Warning",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Assign receiver if null
            if (request.getReceiver() == null) {
                request.setReceiver(account);
            }

            request.setStatus("Rejected");
            request.setResolveDate(new java.util.Date());
            JOptionPane.showMessageDialog(this, "Request rejected.", "Rejected", JOptionPane.INFORMATION_MESSAGE);
            populateWorkRequests();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a request to reject.", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleCreatePermit() {
        int selectedRow = workRequestTable.getSelectedRow();
        if (selectedRow >= 0) {
            WorkRequest request = (WorkRequest) workRequestTable.getValueAt(selectedRow, 0);
            if (request instanceof CampaignBookingRequest) {
                CampaignBookingRequest cbr = (CampaignBookingRequest) request;

                // Find City Services Enterprise
                Enterprise cityEnt = null;
                for (Business.Network.Network network : business.getNetworkList()) {
                    for (Enterprise ent : network.getEnterpriseDirectory().getEnterpriseList()) {
                        if (ent.getEnterpriseType() == Business.Enterprise.EnterpriseType.CITY_SERVICES) {
                            cityEnt = ent;
                            break;
                        }
                    }
                }

                if (cityEnt != null) {
                    // Find City Permits Organization
                    Organization permitOrg = null;
                    for (Organization org : cityEnt.getOrganizationDirectory().getOrganizationList()) {
                        if (org instanceof Business.Organization.CityPermitsOrganization) {
                            permitOrg = org;
                            break;
                        }
                    }

                    if (permitOrg != null) {
                        Business.WorkQueue.PermitRequest permitReq = new Business.WorkQueue.PermitRequest();
                        permitReq.setSender(account);
                        permitReq.setBoardId(cbr.getBoardId());
                        permitReq.setRequestedBy(enterprise.getName());
                        permitReq.setMessage("Permit requested for Campaign: " + cbr.getCampaignName());
                        permitReq.setStatus("Pending");

                        permitOrg.getWorkQueue().getWorkRequestList().add(permitReq);
                        account.getWorkQueue().getWorkRequestList().add(permitReq);

                        JOptionPane.showMessageDialog(this, "Permit request sent to City Services!", "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "City Permits Organization not found!", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "City Services Enterprise not found!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Can only create permits for confirmed Bookings, not Proposals.",
                        "Info",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a booking request to create a permit for.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
}
