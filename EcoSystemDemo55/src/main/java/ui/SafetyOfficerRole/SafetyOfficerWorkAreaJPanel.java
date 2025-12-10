package ui.SafetyOfficerRole;

import Business.EcoSystem;
import Business.Enterprise.Enterprise;
import Business.Network.Network;
import Business.Organization.Organization;
import Business.UserAccount.UserAccount;
import Business.Billboard.Billboard;
import Business.Billboard.BillboardDirectory;
import Business.WorkQueue.ComplianceInspectionRequest;
import Business.WorkQueue.MaintenanceRequest;
import Business.WorkQueue.WorkRequest;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class SafetyOfficerWorkAreaJPanel extends JPanel {

    private JPanel userProcessContainer;
    private UserAccount account;
    private Organization organization;
    private Enterprise enterprise;
    private EcoSystem business;
    private JTable billboardTable;
    private DefaultTableModel billboardModel;
    private JTable historyTable;
    private DefaultTableModel historyModel;

    // Professional color scheme
    private static final Color HEADER_BG = new Color(192, 57, 43); // Safety Red
    private static final Color INSPECT_BG = new Color(52, 152, 219); // Blue
    private static final Color HAZARD_BG = new Color(231, 76, 60); // Red
    private static final Color PANEL_BG = new Color(236, 240, 241);

    public SafetyOfficerWorkAreaJPanel(JPanel userProcessContainer, UserAccount account,
            Organization organization, Enterprise enterprise,
            EcoSystem business) {
        this.userProcessContainer = userProcessContainer;
        this.account = account;
        this.organization = organization;
        this.enterprise = enterprise;
        this.business = business;

        initComponents();
        populateBillboards();
        populateHistory();
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

        JLabel titleLabel = new JLabel("🛡️ Safety Check Officer");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        headerPanel.add(Box.createHorizontalGlue());

        JLabel welcomeLabel = new JLabel("Acc: " + account.getUsername());
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        welcomeLabel.setForeground(Color.WHITE);
        headerPanel.add(welcomeLabel);

        add(headerPanel, BorderLayout.NORTH);

        // Tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        tabbedPane.addTab("Perform Safety Audits", createAuditPanel());
        tabbedPane.addTab("Report History", createHistoryPanel());

        add(tabbedPane, BorderLayout.CENTER);

        // Refresh Button
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(PANEL_BG);
        JButton refreshBtn = createStyledButton("🔄 Refresh", new Color(149, 165, 166));
        refreshBtn.addActionListener(e -> {
            populateBillboards();
            populateHistory();
        });
        footer.add(refreshBtn);
        add(footer, BorderLayout.SOUTH);
    }

    private JPanel createAuditPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lbl = new JLabel("City Billboard Network Status");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.add(lbl, BorderLayout.NORTH);

        String[] cols = { "Board ID", "Location", "Type", "Status", "Object" };
        billboardModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        billboardTable = new JTable(billboardModel);
        styleTable(billboardTable);
        billboardTable.removeColumn(billboardTable.getColumnModel().getColumn(4)); // Hide object

        panel.add(new JScrollPane(billboardTable), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnPanel.setBackground(Color.WHITE);

        JButton inspectBtn = createStyledButton("Request Inspection", INSPECT_BG);
        JButton hazardBtn = createStyledButton("Report Critical Hazard", HAZARD_BG);

        inspectBtn.addActionListener(e -> handleInspectionRequest());
        hazardBtn.addActionListener(e -> handleHazardReport());

        btnPanel.add(inspectBtn);
        btnPanel.add(hazardBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        String[] cols = { "Request ID", "Type", "Board ID", "Receiver", "Status", "Resolution Date" };
        historyModel = new DefaultTableModel(cols, 0);
        historyTable = new JTable(historyModel);
        styleTable(historyTable);

        panel.add(new JScrollPane(historyTable), BorderLayout.CENTER);

        return panel;
    }

    private void populateBillboards() {
        billboardModel.setRowCount(0);
        // Find Billboard Enterprise
        // Assuming single network for demo, but iterating to be safe
        for (Network network : business.getNetworkList()) {
            for (Enterprise ent : network.getEnterpriseDirectory().getEnterpriseList()) {
                if (ent instanceof Business.Enterprise.SkyViewBillboardEnterprise) { // Check type
                    Business.Enterprise.SkyViewBillboardEnterprise skyView = (Business.Enterprise.SkyViewBillboardEnterprise) ent;
                    if (skyView.getBillboardDirectory() != null) {
                        for (Billboard b : skyView.getBillboardDirectory().getBillboards()) {
                            billboardModel.addRow(new Object[] {
                                    b.getBoardId(),
                                    b.getLocation(),
                                    b.getType(),
                                    b.getStatus(),
                                    b // Store object
                            });
                        }
                    }
                }
            }
        }
    }

    private void populateHistory() {
        historyModel.setRowCount(0);
        for (WorkRequest wr : account.getWorkQueue().getWorkRequestList()) {
            String type = "Unknown";
            int boardId = -1;

            if (wr instanceof ComplianceInspectionRequest) {
                type = "Inspection";
                boardId = ((ComplianceInspectionRequest) wr).getBoardId();
            } else if (wr instanceof MaintenanceRequest) {
                type = "Hazard Report";
                boardId = ((MaintenanceRequest) wr).getBoardId();
            }

            historyModel.addRow(new Object[] {
                    "REQ-" + wr.hashCode(),
                    type,
                    boardId,
                    wr.getReceiver() != null ? wr.getReceiver().getUsername() : "Org Queue",
                    wr.getStatus(),
                    wr.getResolveDate()
            });
        }
    }

    private void handleInspectionRequest() {
        int selectedRow = billboardTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Select a billboard first.");
            return;
        }

        Billboard b = (Billboard) billboardModel.getValueAt(billboardTable.convertRowIndexToModel(selectedRow), 4);

        ComplianceInspectionRequest req = new ComplianceInspectionRequest();
        req.setBoardId(b.getBoardId());
        req.setSender(account);
        req.setInspectionType("Routine Safety Audit");
        req.setStatus("Pending");
        req.setMessage("Safety Audit requested by " + account.getUsername());

        // Send to Compliance Organization (own enterprise)
        boolean sent = false;
        for (Organization org : enterprise.getOrganizationDirectory().getOrganizationList()) {
            if (org instanceof Business.Organization.ComplianceInspectionOrganization) {
                org.getWorkQueue().getWorkRequestList().add(req);
                account.getWorkQueue().getWorkRequestList().add(req);
                sent = true;
                break;
            }
        }

        if (sent) {
            JOptionPane.showMessageDialog(this, "Inspection Request Sent!");
            populateHistory();
        } else {
            JOptionPane.showMessageDialog(this, "Error: Could not find Compliance Organization.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleHazardReport() {
        int selectedRow = billboardTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Select a billboard first.");
            return;
        }

        Billboard b = (Billboard) billboardModel.getValueAt(billboardTable.convertRowIndexToModel(selectedRow), 4);

        String hazard = JOptionPane.showInputDialog(this, "Describe the hazard:", "Report Hazard",
                JOptionPane.WARNING_MESSAGE);
        if (hazard == null || hazard.trim().isEmpty())
            return;

        MaintenanceRequest req = new MaintenanceRequest();
        req.setBoardId(b.getBoardId());
        req.setSender(account);
        req.setIssueDescription("HAZARD: " + hazard);
        req.setUrgencyLevel("Critical");
        req.setStatus("Pending");
        req.setMessage("Critical Safety Hazard Reported");

        // Send to Billboard Operations Organization (Cross Enterprise)
        boolean sent = false;
        for (Network n : business.getNetworkList()) {
            for (Enterprise e : n.getEnterpriseDirectory().getEnterpriseList()) {
                for (Organization org : e.getOrganizationDirectory().getOrganizationList()) {
                    if (org instanceof Business.Organization.BillboardOperationsOrganization) {
                        org.getWorkQueue().getWorkRequestList().add(req);
                        account.getWorkQueue().getWorkRequestList().add(req);
                        sent = true;
                        break;
                    }
                }
                if (sent)
                    break;
            }
            if (sent)
                break;
        }

        if (sent) {
            JOptionPane.showMessageDialog(this, "Hazard Reported to Operations!", "Hazard Sent",
                    JOptionPane.WARNING_MESSAGE);
            populateHistory();
        } else {
            JOptionPane.showMessageDialog(this, "Error: Could not find Billboard Operations Organization.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // UI Helpers
    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(28);
        table.setSelectionBackground(new Color(52, 152, 219));
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(new Color(189, 195, 199));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(44, 62, 80));
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
