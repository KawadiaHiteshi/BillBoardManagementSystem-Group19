package ui.AgencyAccountRole;

import Business.EcoSystem;
import Business.Enterprise.Enterprise;
import Business.Enterprise.SkyViewBillboardEnterprise;
import Business.Network.Network;
import Business.Organization.AgencyClientServicesOrganization;
import Business.Organization.BillboardSalesOrganization;
import Business.Organization.Organization;
import Business.Role.BrandMarketingManagerRole;
import Business.UserAccount.UserAccount;
import Business.WorkQueue.CampaignBookingRequest;
import Business.WorkQueue.WorkRequest;
import Business.Billboard.Billboard; // Assuming this exists
import Business.Billboard.BillboardStatus; // Assuming this exists

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;

public class AgencyAccountWorkAreaJPanel extends JPanel {

    private JPanel userProcessContainer;
    private UserAccount account;
    private Organization organization;
    private Enterprise enterprise;
    private EcoSystem business;

    private JTable clientTable;
    private JTable billboardTable;
    private JTable bookingTable;
    private JComboBox<UserAccount> clientComboBox;

    // Professional color scheme
    private static final Color HEADER_BG = new Color(142, 68, 173); // Purple
    private static final Color BUTTON_BG = new Color(155, 89, 182);
    private static final Color SUCCESS_BG = new Color(46, 204, 113);
    private static final Color PANEL_BG = new Color(236, 240, 241);

    public AgencyAccountWorkAreaJPanel(JPanel userProcessContainer, UserAccount account,
            Organization organization, Enterprise enterprise,
            EcoSystem business) {
        this.userProcessContainer = userProcessContainer;
        this.account = account;
        this.organization = organization;
        this.enterprise = enterprise;
        this.business = business;

        initComponents();
        populateClients();
        populateBillboards();
        populateBookings();
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

        JLabel titleLabel = new JLabel("👔 Agency Account Manager");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        headerPanel.add(Box.createHorizontalGlue());

        JLabel welcomeLabel = new JLabel("Welcome, " + account.getUsername());
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        welcomeLabel.setForeground(Color.WHITE);
        headerPanel.add(welcomeLabel);

        add(headerPanel, BorderLayout.NORTH);

        // Main Tabbed Pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        tabbedPane.addTab("Manage Clients", createClientPanel());
        tabbedPane.addTab("New Campaign Booking", createBookingPanel());
        tabbedPane.addTab("Booking History", createHistoryPanel());

        add(tabbedPane, BorderLayout.CENTER);

        // Refresh Button at bottom
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(PANEL_BG);
        JButton refreshBtn = createStyledButton("🔄 Refresh All", new Color(149, 165, 166));
        refreshBtn.addActionListener(e -> {
            populateClients();
            populateBillboards();
            populateBookings();
        });
        footer.add(refreshBtn);
        add(footer, BorderLayout.SOUTH);
    }

    // --- TAB 1: Clients ---
    private JPanel createClientPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lbl = new JLabel("Brand Marketing Clients");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panel.add(lbl, BorderLayout.NORTH);

        String[] cols = { "Client Username", "Employee Name", "Role" };
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        clientTable = new JTable(model);
        styleTable(clientTable);

        panel.add(new JScrollPane(clientTable), BorderLayout.CENTER);
        return panel;
    }

    // --- TAB 2: Booking ---
    private JPanel createBookingPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Top: Inputs
        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        inputPanel.setBackground(Color.WHITE);

        clientComboBox = new JComboBox<>();
        JTextField campaignNameTxt = new JTextField();
        JTextField budgetTxt = new JTextField();
        JTextField contentTxt = new JTextField();

        inputPanel.add(new JLabel("Select Client (Brand):"));
        inputPanel.add(clientComboBox);

        inputPanel.add(new JLabel("Campaign Name:"));
        inputPanel.add(campaignNameTxt);

        inputPanel.add(new JLabel("Budget ($):"));
        inputPanel.add(budgetTxt);

        inputPanel.add(new JLabel("Creative Content / Message:"));
        inputPanel.add(contentTxt);

        panel.add(inputPanel, BorderLayout.NORTH);

        // Center: Billboard Selection
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(new TitledBorder("Available Billboards (Select One)"));

        String[] cols = { "ID", "Name/Loc", "Type", "Rate", "Status", "Object" };
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        billboardTable = new JTable(model);
        billboardTable.removeColumn(billboardTable.getColumnModel().getColumn(5)); // Hide Object
        styleTable(billboardTable);

        tablePanel.add(new JScrollPane(billboardTable), BorderLayout.CENTER);
        panel.add(tablePanel, BorderLayout.CENTER);

        // Bottom: Action
        JButton bookBtn = createStyledButton("🚀 Submit Booking Request", BUTTON_BG);
        bookBtn.addActionListener(e -> {
            int selectedRow = billboardTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Please select a billboard from the table.");
                return;
            }

            UserAccount selectedClient = (UserAccount) clientComboBox.getSelectedItem();
            if (selectedClient == null) {
                JOptionPane.showMessageDialog(this, "Please select a client.");
                return;
            }

            try {
                // Get Billboard
                int modelRow = billboardTable.convertRowIndexToModel(selectedRow);
                Billboard board = (Billboard) model.getValueAt(modelRow, 5);

                double budget = Double.parseDouble(budgetTxt.getText());
                String campaign = campaignNameTxt.getText();
                String content = contentTxt.getText();

                if (campaign.isEmpty() || content.isEmpty())
                    throw new Exception("Fields cannot be empty");

                // Create Request
                CampaignBookingRequest req = new CampaignBookingRequest();
                req.setSender(account); // Agency sends it
                req.setClientName(selectedClient.getUsername()); // Use unique username
                req.setCampaignName(campaign);
                req.setBoardId(board.getBoardId());
                req.setBudget(budget);
                req.setCreativeContent(content);
                req.setStartDate(new Date()); // Demo: Start Now
                req.setEndDate(new Date()); // Demo: End Today (needs date picker usually)
                req.setStatus("Sent to Sales");
                req.setMessage("New Booking for " + selectedClient.getUsername());

                // Find Sales Org
                SkyViewBillboardEnterprise skyView = findSkyViewEnterprise();
                if (skyView == null) {
                    JOptionPane.showMessageDialog(this, "Error: SkyView Billboard Enterprise not found!");
                    return;
                }

                BillboardSalesOrganization salesOrg = null;
                for (Organization org : skyView.getOrganizationDirectory().getOrganizationList()) {
                    if (org instanceof BillboardSalesOrganization) {
                        salesOrg = (BillboardSalesOrganization) org;
                        break;
                    }
                }

                if (salesOrg != null) {
                    salesOrg.getWorkQueue().getWorkRequestList().add(req);
                    account.getWorkQueue().getWorkRequestList().add(req);
                    JOptionPane.showMessageDialog(this, "Booking Request Sent Successfully!");
                    populateBookings();
                } else {
                    JOptionPane.showMessageDialog(this, "Error: Sales Organization not found.");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Budget.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(bookBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    // --- TAB 3: History & Incoming Requests ---
    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lbl = new JLabel("Booking Request History & Incoming Client Requests");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panel.add(lbl, BorderLayout.NORTH);

        String[] cols = { "Request ID", "Client", "Campaign", "Board ID", "Status", "Message", "Object" };
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        bookingTable = new JTable(model);
        bookingTable.removeColumn(bookingTable.getColumnModel().getColumn(6)); // Hide Object
        styleTable(bookingTable);

        panel.add(new JScrollPane(bookingTable), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(Color.WHITE);
        JButton processBtn = createStyledButton("➡️ Process & Forward to Sales", BUTTON_BG);
        processBtn.addActionListener(e -> handleProcessRequest());
        btnPanel.add(processBtn);

        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void handleProcessRequest() {
        int selectedRow = bookingTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Select a pending request first.");
            return;
        }

        DefaultTableModel model = (DefaultTableModel) bookingTable.getModel();
        int modelRow = bookingTable.convertRowIndexToModel(selectedRow);
        CampaignBookingRequest req = (CampaignBookingRequest) model.getValueAt(modelRow, 6);

        if (!req.getStatus().equals("Sent to Agency")) {
            JOptionPane.showMessageDialog(this, "Can only process requests with status 'Sent to Agency'.");
            return;
        }

        // Forward to Sales
        SkyViewBillboardEnterprise skyView = findSkyViewEnterprise();
        if (skyView == null) {
            JOptionPane.showMessageDialog(this, "Error: SkyView Billboard Enterprise not found!");
            return;
        }

        BillboardSalesOrganization salesOrg = null;
        for (Organization org : skyView.getOrganizationDirectory().getOrganizationList()) {
            if (org instanceof BillboardSalesOrganization) {
                salesOrg = (BillboardSalesOrganization) org;
                break;
            }
        }

        if (salesOrg != null) {
            salesOrg.getWorkQueue().getWorkRequestList().add(req);
            // Don't need to add to account work queue again, it's already there or in org
            // queue
            // But we should update status
            req.setStatus("Sent to Sales");
            req.setMessage("Forwarded by " + account.getUsername());
            req.setSender(account); // Now Agency is the sender to Sales

            JOptionPane.showMessageDialog(this, "Request Forwarded to Sales Organization!");
            populateBookings();
        } else {
            JOptionPane.showMessageDialog(this, "Error: Sales Organization not found.");
        }
    }

    // --- Helpers ---

    private void populateClients() {
        DefaultTableModel model = (DefaultTableModel) clientTable.getModel();
        model.setRowCount(0);
        clientComboBox.removeAllItems();

        // List user accounts in THIS organization that are Brand Managers
        for (UserAccount ua : organization.getUserAccountDirectory().getUserAccountList()) {
            if (ua.getRole() instanceof BrandMarketingManagerRole) {
                model.addRow(new Object[] { ua.getUsername(), ua.getEmployee().getName(), "Brand Manager" });
                clientComboBox.addItem(ua);
            }
        }
    }

    private void populateBillboards() {
        DefaultTableModel model = (DefaultTableModel) billboardTable.getModel();
        model.setRowCount(0);

        SkyViewBillboardEnterprise skyView = findSkyViewEnterprise();
        if (skyView != null) {
            for (Billboard b : skyView.getBillboardDirectory().getBillboards()) {
                // Show all or only available? Let's show all for selection
                Object[] row = {
                        b.getBoardId(),
                        b.getLocation(),
                        b.getSize(),
                        b.getPricePerDay(),
                        b.getStatus().toString(),
                        b
                };
                model.addRow(row);
            }
        }
    }

    private void populateBookings() {
        DefaultTableModel model = (DefaultTableModel) bookingTable.getModel();
        model.setRowCount(0);

        // Show requests from Organization Queue (Incoming from Brand) AND User Queue
        // (Sent by Agency)
        // Using a set to avoid duplicates if needed, or just list user's actions.
        // Actually, we want to see what is assigned to US (or our Org).

        // 1. Organization Queue (Incoming from Brand)
        for (WorkRequest wr : organization.getWorkQueue().getWorkRequestList()) {
            if (wr instanceof CampaignBookingRequest) {
                addToModel(model, (CampaignBookingRequest) wr);
            }
        }

        // 2. User Account Queue (Created by me)
        for (WorkRequest wr : account.getWorkQueue().getWorkRequestList()) {
            if (wr instanceof CampaignBookingRequest) {
                // Check if already added? Simple logic: if status is "Sent to Agency", it's in
                // Org queue.
                // If "Sent to Sales", it might be in Account queue.
                // For simplicity, let's just add all and rely on the UI to show them.
                // In production, we'd dedup.
                addToModel(model, (CampaignBookingRequest) wr);
            }
        }
    }

    private void addToModel(DefaultTableModel model, CampaignBookingRequest cbr) {
        // Dedup check
        for (int i = 0; i < model.getRowCount(); i++) {
            CampaignBookingRequest existing = (CampaignBookingRequest) model.getValueAt(i, 6);
            if (existing == cbr)
                return;
        }

        model.addRow(new Object[] {
                "REQ-" + cbr.hashCode(), // Request ID
                cbr.getClientName(),
                cbr.getCampaignName(),
                cbr.getBoardId(),
                cbr.getStatus(),
                cbr.getMessage(),
                cbr // Object
        });
    }

    private SkyViewBillboardEnterprise findSkyViewEnterprise() {
        // Search logically linked networks
        for (Network n : business.getNetworkList()) {
            // Check if current enterprise is in this network
            boolean inNetwork = false;
            for (Enterprise e : n.getEnterpriseDirectory().getEnterpriseList()) {
                if (e == enterprise) {
                    inNetwork = true;
                    break;
                }
            }

            if (inNetwork) {
                // Look for SkyView in THIS network
                for (Enterprise e : n.getEnterpriseDirectory().getEnterpriseList()) {
                    if (e instanceof SkyViewBillboardEnterprise) {
                        return (SkyViewBillboardEnterprise) e;
                    }
                }
            }
        }
        return null; // Not found
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(200, 40));
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

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(28);
        table.setSelectionBackground(new Color(142, 68, 173));
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(new Color(189, 195, 199));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(52, 73, 94));
        table.getTableHeader().setForeground(Color.WHITE);
    }
}
