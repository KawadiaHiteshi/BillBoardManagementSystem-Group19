package ui.BrandMarketingRole;

import Business.EcoSystem;
import Business.Enterprise.Enterprise;
import Business.Network.Network;
import Business.Organization.Organization;
import Business.UserAccount.UserAccount;
import Business.Billboard.Billboard;
import Business.Billboard.Billboard.BookingRecord;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class BrandMarketingWorkAreaJPanel extends JPanel {

    private JPanel userProcessContainer;
    private UserAccount account;
    private Organization organization;
    private Enterprise enterprise;
    private EcoSystem business;
    private JTable billboardTable;
    private DefaultTableModel billboardModel;
    private JTable campaignTable;
    private DefaultTableModel campaignModel;

    // Professional color scheme
    private static final Color HEADER_BG = new Color(211, 84, 0); // Burnt Orange
    private static final Color BUTTON_BG = new Color(230, 126, 34); // Carrot Orange
    private static final Color WAITLIST_BG = new Color(41, 128, 185); // Blue
    private static final Color PANEL_BG = new Color(236, 240, 241);

    public BrandMarketingWorkAreaJPanel(JPanel userProcessContainer, UserAccount account,
            Organization organization, Enterprise enterprise,
            EcoSystem business) {
        this.userProcessContainer = userProcessContainer;
        this.account = account;
        this.organization = organization;
        this.enterprise = enterprise;
        this.business = business;

        initComponents();
        populateAvailableBillboards();
        populateMyCampaigns();
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

        JLabel titleLabel = new JLabel("🎨 Brand Marketing Manager");
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
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        tabbedPane.addTab("Available Billboards & Waitlist", createBillboardPanel());
        tabbedPane.addTab("My Active Campaigns", createCampaignPanel());

        add(tabbedPane, BorderLayout.CENTER);

        // Footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(PANEL_BG);
        JButton refreshBtn = createStyledButton("🔄 Refresh", new Color(149, 165, 166));
        refreshBtn.addActionListener(e -> {
            populateAvailableBillboards();
            populateMyCampaigns();
        });
        footer.add(refreshBtn);
        add(footer, BorderLayout.SOUTH);
    }

    private JPanel createBillboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lbl = new JLabel("Browse High-Traffic Billboards");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.add(lbl, BorderLayout.NORTH);

        String[] cols = { "Board ID", "Location", "Type", "Status", "Price/Day", "Object" };
        billboardModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        billboardTable = new JTable(billboardModel);
        styleTable(billboardTable);
        billboardTable.removeColumn(billboardTable.getColumnModel().getColumn(5));

        panel.add(new JScrollPane(billboardTable), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setBackground(Color.WHITE);
        JButton waitlistBtn = createStyledButton("Join Waitlist", WAITLIST_BG);
        waitlistBtn.addActionListener(e -> handleJoinWaitlist());
        btnPanel.add(waitlistBtn);

        panel.add(btnPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createCampaignPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lbl = new JLabel("Track Your Campaign Performance");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.add(lbl, BorderLayout.NORTH);

        String[] cols = { "Campaign Name", "Board Location", "Start Date", "End Date", "Status" };
        campaignModel = new DefaultTableModel(cols, 0);
        campaignTable = new JTable(campaignModel);
        styleTable(campaignTable);

        panel.add(new JScrollPane(campaignTable), BorderLayout.CENTER);

        return panel;
    }

    // Logic
    private void populateAvailableBillboards() {
        billboardModel.setRowCount(0);
        for (Network network : business.getNetworkList()) {
            for (Enterprise ent : network.getEnterpriseDirectory().getEnterpriseList()) {
                if (ent instanceof Business.Enterprise.SkyViewBillboardEnterprise) {
                    Business.Enterprise.SkyViewBillboardEnterprise skyView = (Business.Enterprise.SkyViewBillboardEnterprise) ent;
                    if (skyView.getBillboardDirectory() != null) {
                        for (Billboard b : skyView.getBillboardDirectory().getBillboards()) {
                            billboardModel.addRow(new Object[] {
                                    b.getBoardId(),
                                    b.getLocation(),
                                    b.getType(),
                                    b.getStatus(),
                                    "$" + b.getPricePerDay(),
                                    b
                            });
                        }
                    }
                }
            }
        }
    }

    private void populateMyCampaigns() {
        campaignModel.setRowCount(0);
        // In a real app, we'd query the BookingRecords.
        // For now, we iterate all billboards and check if our username matches any
        // booking record client name,
        // OR simply display requests we sent (which is easier if we tracked requests
        // properly).
        // Let's iterate billboards to find bookings for "Brand1" etc.
        // NOTE: The 'clientName' in BookingRecord might be the company name or the user
        // name.
        // Let's assume user.getName() or user.getUsername().

        String myName = account.getUsername(); // or employee name

        for (Network network : business.getNetworkList()) {
            for (Enterprise ent : network.getEnterpriseDirectory().getEnterpriseList()) {
                if (ent instanceof Business.Enterprise.SkyViewBillboardEnterprise) {
                    Business.Enterprise.SkyViewBillboardEnterprise skyView = (Business.Enterprise.SkyViewBillboardEnterprise) ent;
                    if (skyView.getBillboardDirectory() != null) {
                        for (Billboard b : skyView.getBillboardDirectory().getBillboards()) {
                            for (BookingRecord record : b.getBookingHistory()) {
                                // Check matching username (standardized) or employee name (legacy support)
                                boolean match = record.getClientName().equalsIgnoreCase(account.getUsername());

                                if (!match && account.getEmployee() != null) {
                                    match = record.getClientName().equalsIgnoreCase(account.getEmployee().getName());
                                }

                                if (match) {
                                    campaignModel.addRow(new Object[] {
                                            "Brand Campaign",
                                            b.getLocation(),
                                            record.getStartDate(),
                                            record.getEndDate(),
                                            "Active"
                                    });
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void handleJoinWaitlist() {
        int selectedRow = billboardTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Select a billboard first.");
            return;
        }

        Billboard b = (Billboard) billboardModel.getValueAt(billboardTable.convertRowIndexToModel(selectedRow), 5);
        if (b.getStatus() == Business.Billboard.BillboardStatus.AVAILABLE) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "This billboard is Available! Do you want to request a booking now?", "Available",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                handleRequestBooking(b);
            }
            return;
        }

        String msg = JOptionPane.showInputDialog(this, "Enter waitlist message (e.g. Desired dates):");
        if (msg != null && !msg.trim().isEmpty()) {
            b.joinWaitlist(account, msg);
            JOptionPane.showMessageDialog(this, "You have been added to the Waitlist!");
        }
    }

    private void handleRequestBooking(Billboard b) {
        // Simple input for demo
        String campaign = JOptionPane.showInputDialog(this, "Enter Campaign Name:");
        if (campaign == null || campaign.trim().isEmpty())
            return;

        String budgetStr = JOptionPane.showInputDialog(this, "Enter Budget ($):");
        double budget = 0;
        try {
            budget = Double.parseDouble(budgetStr);
        } catch (Exception e) {
        }

        String content = JOptionPane.showInputDialog(this, "Enter Creative Content:");

        // Create Request to Agency
        // using CampaignBookingRequest but with status "Sent to Agency"
        Business.WorkQueue.CampaignBookingRequest req = new Business.WorkQueue.CampaignBookingRequest();
        req.setSender(account);
        req.setClientName(account.getUsername());
        req.setCampaignName(campaign);
        req.setBoardId(b.getBoardId());
        req.setBudget(budget);
        req.setCreativeContent(content);
        req.setStartDate(new java.util.Date());
        req.setEndDate(new java.util.Date()); // Demo: 1 day default
        req.setStatus("Sent to Agency");
        req.setMessage("Please book this for me.");

        // Find Agency Org
        boolean sent = false;
        // Search in same enterprise/network for Agency Org
        // Assuming Brand and Agency in same Enterprise (AdSpark)
        for (Organization org : enterprise.getOrganizationDirectory().getOrganizationList()) {
            if (org instanceof Business.Organization.AgencyClientServicesOrganization) { // Account Managers are here?
                // Actually AccountManagerRole belongs to AgencyClientServicesOrganization
                // usually
                org.getWorkQueue().getWorkRequestList().add(req);
                account.getWorkQueue().getWorkRequestList().add(req);
                sent = true;
                break;
            }
        }

        if (sent) {
            JOptionPane.showMessageDialog(this, "Booking Request sent to your Agency Account Manager!");
        } else {
            JOptionPane.showMessageDialog(this, "Error: Agency Organization not found.");
        }
    }

    // UI Helpers
    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(28);
        table.setSelectionBackground(new Color(230, 126, 34));
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(new Color(189, 195, 199));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(211, 84, 0));
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
}
