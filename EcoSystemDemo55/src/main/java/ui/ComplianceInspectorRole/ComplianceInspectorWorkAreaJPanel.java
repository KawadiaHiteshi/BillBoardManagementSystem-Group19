package ui.ComplianceInspectorRole;

import Business.EcoSystem;
import Business.Enterprise.Enterprise;
import Business.Organization.Organization;
import Business.UserAccount.UserAccount;
import Business.WorkQueue.ComplianceInspectionRequest;
import Business.WorkQueue.WorkRequest;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ComplianceInspectorWorkAreaJPanel extends JPanel {

    private JPanel userProcessContainer;
    private UserAccount account;
    private Organization organization;
    private Enterprise enterprise;
    private EcoSystem business;
    private JTable inspectionTable;
    private DefaultTableModel tableModel;

    // Professional color scheme
    private static final Color HEADER_BG = new Color(192, 57, 43); // Dark Red
    private static final Color BUTTON_BG = new Color(231, 76, 60);
    private static final Color SUCCESS_BG = new Color(46, 204, 113);
    private static final Color WARNING_BG = new Color(243, 156, 18);
    private static final Color PANEL_BG = new Color(236, 240, 241);

    public ComplianceInspectorWorkAreaJPanel(JPanel userProcessContainer, UserAccount account,
            Organization organization, Enterprise enterprise,
            EcoSystem business) {
        this.userProcessContainer = userProcessContainer;
        this.account = account;
        this.organization = organization;
        this.enterprise = enterprise;
        this.business = business;

        initComponents();
        populateInspections();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(PANEL_BG);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(HEADER_BG);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("🔍 Compliance Inspector - Work Area");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        JLabel welcomeLabel = new JLabel("Welcome, " + account.getUsername());
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        welcomeLabel.setForeground(Color.WHITE);
        headerPanel.add(Box.createHorizontalStrut(30));
        headerPanel.add(welcomeLabel);

        add(headerPanel, BorderLayout.NORTH);

        // Center panel with table
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(189, 195, 199), 1),
                new EmptyBorder(15, 15, 15, 15)));

        JLabel tableTitle = new JLabel("📋 Pending Inspections");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tableTitle.setForeground(new Color(44, 62, 80));
        centerPanel.add(tableTitle, BorderLayout.NORTH);

        // Inspection Table
        String[] columnNames = { "Request ID", "Billboard ID", "Type", "Status" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        inspectionTable = new JTable(tableModel);
        inspectionTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        inspectionTable.setRowHeight(28);
        inspectionTable.setSelectionBackground(new Color(231, 76, 60));
        inspectionTable.setSelectionForeground(Color.WHITE);
        inspectionTable.setGridColor(new Color(189, 195, 199));
        inspectionTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        inspectionTable.getTableHeader().setBackground(new Color(52, 73, 94));
        inspectionTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(inspectionTable);
        scrollPane.setBorder(new LineBorder(new Color(189, 195, 199), 1));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(PANEL_BG);

        JButton passButton = createStyledButton("✓ Pass Inspection", SUCCESS_BG);
        JButton failButton = createStyledButton("✗ Fail Inspection", BUTTON_BG);
        JButton recordButton = createStyledButton("📝 Record Results", WARNING_BG);
        JButton refreshButton = createStyledButton("🔄 Refresh", new Color(149, 165, 166));

        passButton.addActionListener(e -> handlePass());
        failButton.addActionListener(e -> handleFail());
        recordButton.addActionListener(e -> handleRecord());
        refreshButton.addActionListener(e -> populateInspections());

        buttonPanel.add(passButton);
        buttonPanel.add(failButton);
        buttonPanel.add(recordButton);
        buttonPanel.add(refreshButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(170, 40));
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

    private void handlePass() {
        int selectedRow = inspectionTable.getSelectedRow();
        if (selectedRow >= 0) {
            // Get actual object from table model (assuming we store it in column 0 or
            // similar, but here we iterate list.
            // Better to store object in table or use ID. Let's rely on list index matching
            // for now or robustify this.)
            // NOTE: The previous code relied on
            // organization.getWorkQueue().getWorkRequestList().get(selectedRow) which is
            // risky if filtered.
            // Let's get the request from the table model if we stored it there.
            // We need to update populateInspections to store the request object.

            // ... updating populateInspections first ...
            WorkRequest request = (WorkRequest) tableModel.getValueAt(selectedRow, 0);

            if (!request.getStatus().equals("Pending")) {
                JOptionPane.showMessageDialog(this, "Request already processed.");
                return;
            }

            request.setStatus("Passed");
            request.setResolveDate(new java.util.Date());
            request.setReceiver(account);

            JOptionPane.showMessageDialog(this, "✓ Inspection passed!", "Success", JOptionPane.INFORMATION_MESSAGE);
            populateInspections();
        } else {
            JOptionPane.showMessageDialog(this, "Please select an inspection.", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleFail() {
        int selectedRow = inspectionTable.getSelectedRow();
        if (selectedRow >= 0) {
            WorkRequest request = (WorkRequest) tableModel.getValueAt(selectedRow, 0);

            if (!request.getStatus().equals("Pending")) {
                JOptionPane.showMessageDialog(this, "Request already processed.");
                return;
            }

            ComplianceInspectionRequest cir = (ComplianceInspectionRequest) request;

            request.setStatus("Failed");
            request.setResolveDate(new java.util.Date());
            request.setReceiver(account);

            // Raise Maintenance Request
            Business.WorkQueue.MaintenanceRequest maintReq = new Business.WorkQueue.MaintenanceRequest();
            maintReq.setBoardId(cir.getBoardId());
            maintReq.setIssueDescription("Compliance Check Failed: " + cir.getInspectionType());
            maintReq.setUrgencyLevel("High");
            maintReq.setSender(account);
            maintReq.setStatus("Pending");

            // Find Billboard Ops Org
            Organization opsOrg = null;
            // Iterate networks/enterprises to find BillboardOps
            for (Business.Network.Network network : business.getNetworkList()) {
                for (Enterprise ent : network.getEnterpriseDirectory().getEnterpriseList()) {
                    // specific check or just iterate orgs
                    for (Organization org : ent.getOrganizationDirectory().getOrganizationList()) {
                        if (org instanceof Business.Organization.BillboardOperationsOrganization) {
                            opsOrg = org;
                            break;
                        }
                    }
                }
            }

            if (opsOrg != null) {
                opsOrg.getWorkQueue().getWorkRequestList().add(maintReq);
                account.getWorkQueue().getWorkRequestList().add(maintReq);
                JOptionPane.showMessageDialog(this, "Failed. Maintenance Request raised to Ops!", "Failure Processed",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed, but could not find Billboard Ops Organization!", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

            populateInspections();
        } else {
            JOptionPane.showMessageDialog(this, "Please select an inspection.", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleRecord() {
        JOptionPane.showMessageDialog(this, "Results recorded! (Placeholder for detailed report)", "Info",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void populateInspections() {
        tableModel.setRowCount(0);

        for (WorkRequest request : organization.getWorkQueue().getWorkRequestList()) {
            if (request instanceof ComplianceInspectionRequest) {
                ComplianceInspectionRequest cir = (ComplianceInspectionRequest) request;
                Object[] row = {
                        cir, // Store object
                        cir.getBoardId(),
                        cir.getInspectionType() != null ? cir.getInspectionType() : "General",
                        request.getStatus()
                };
                tableModel.addRow(row);
            }
        }
    }
}
