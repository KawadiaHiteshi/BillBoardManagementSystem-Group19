package ui.PowerGridRole;

import Business.EcoSystem;
import Business.Enterprise.Enterprise;
import Business.Organization.FieldEngineersOrganization;
import Business.Organization.Organization;
import Business.UserAccount.UserAccount;
import Business.WorkQueue.FieldEngineerAssignmentRequest;
import Business.WorkQueue.PowerIssueRequest;
import Business.WorkQueue.WorkRequest;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PowerGridWorkAreaJPanel extends JPanel {

    private JPanel userProcessContainer;
    private UserAccount account;
    private Organization organization;
    private Enterprise enterprise;
    private EcoSystem business;
    private JTable powerIssueTable;
    private DefaultTableModel tableModel;

    // Professional color scheme
    private static final Color HEADER_BG = new Color(241, 196, 15); // Yellow/Gold
    private static final Color BUTTON_BG = new Color(243, 156, 18);
    private static final Color ASSIGN_BG = new Color(52, 152, 219);
    private static final Color PANEL_BG = new Color(236, 240, 241);

    public PowerGridWorkAreaJPanel(JPanel userProcessContainer, UserAccount account,
            Organization organization, Enterprise enterprise,
            EcoSystem business) {
        this.userProcessContainer = userProcessContainer;
        this.account = account;
        this.organization = organization;
        this.enterprise = enterprise;
        this.business = business;

        initComponents();
        populatePowerIssues();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(PANEL_BG);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(HEADER_BG);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("⚡ Power Grid Coordinator - Work Area");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(44, 62, 80));
        headerPanel.add(titleLabel);

        JLabel welcomeLabel = new JLabel("Welcome, " + account.getUsername());
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        welcomeLabel.setForeground(new Color(44, 62, 80));
        headerPanel.add(Box.createHorizontalStrut(30));
        headerPanel.add(welcomeLabel);

        add(headerPanel, BorderLayout.NORTH);

        // Center panel with table
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(189, 195, 199), 1),
                new EmptyBorder(15, 15, 15, 15)));

        JLabel tableTitle = new JLabel("⚡ Power Issue Requests");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tableTitle.setForeground(new Color(44, 62, 80));
        centerPanel.add(tableTitle, BorderLayout.NORTH);

        // Power Issue Table
        String[] columnNames = { "Request ID", "Billboard ID", "Severity", "Status", "Request Object" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        powerIssueTable = new JTable(tableModel);
        powerIssueTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        powerIssueTable.setRowHeight(28);
        powerIssueTable.setSelectionBackground(new Color(243, 156, 18));
        powerIssueTable.setSelectionForeground(Color.WHITE);
        powerIssueTable.setGridColor(new Color(189, 195, 199));
        powerIssueTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        powerIssueTable.getTableHeader().setBackground(new Color(52, 73, 94));
        powerIssueTable.getTableHeader().setForeground(Color.WHITE);

        // Hide object column
        powerIssueTable.removeColumn(powerIssueTable.getColumnModel().getColumn(4));

        JScrollPane scrollPane = new JScrollPane(powerIssueTable);
        scrollPane.setBorder(new LineBorder(new Color(189, 195, 199), 1));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(PANEL_BG);

        JButton assignButton = createStyledButton("👷 Assign Engineer", ASSIGN_BG);
        JButton resolveButton = createStyledButton("✓ Mark Resolved", new Color(46, 204, 113));
        JButton refreshButton = createStyledButton("🔄 Refresh", new Color(149, 165, 166));

        assignButton.addActionListener(e -> handleAssign());
        resolveButton.addActionListener(e -> handleResolve());
        refreshButton.addActionListener(e -> populatePowerIssues());

        buttonPanel.add(assignButton);
        buttonPanel.add(resolveButton);
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

    private void handleAssign() {
        try {
            // DEBUG
            // JOptionPane.showMessageDialog(this, "Debug: handleAssign called");

            int selectedRow = powerIssueTable.getSelectedRow();
            if (selectedRow >= 0) {
                // Determine model index to be safe (though removed column shouldn't affect row
                // mapping unless sorted)
                int modelRow = powerIssueTable.convertRowIndexToModel(selectedRow);
                PowerIssueRequest request = (PowerIssueRequest) tableModel.getValueAt(modelRow, 4);

                // Debug request
                if (request == null) {
                    JOptionPane.showMessageDialog(this, "Error: Request object is null.");
                    return;
                }

                String status = request.getStatus();
                if (status != null && (status.equalsIgnoreCase("Completed") || status.equalsIgnoreCase("Resolved"))) {
                    JOptionPane.showMessageDialog(this, "Request is already resolved.", "Warning",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (enterprise == null) {
                    JOptionPane.showMessageDialog(this, "Error: Enterprise is null. Cannot find organizations.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Find Field Engineers Organization
                FieldEngineersOrganization fieldOrg = null;
                for (Organization org : enterprise.getOrganizationDirectory().getOrganizationList()) {
                    if (org instanceof FieldEngineersOrganization) {
                        fieldOrg = (FieldEngineersOrganization) org;
                        break;
                    }
                }

                if (fieldOrg == null) {
                    JOptionPane.showMessageDialog(this,
                            "Field Engineers Organization not found in " + enterprise.getName(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Get Engineers
                java.util.List<UserAccount> engineers = fieldOrg.getUserAccountDirectory().getUserAccountList();

                if (engineers.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No engineers found in directory.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                UserAccount[] engineerArray = engineers.toArray(new UserAccount[0]);
                UserAccount selectedEngineer = (UserAccount) JOptionPane.showInputDialog(this,
                        "Select Field Engineer:",
                        "Assign Engineer",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        engineerArray,
                        engineerArray[0]);

                if (selectedEngineer != null) {
                    // Create Assignment Request
                    FieldEngineerAssignmentRequest assignmentReq = new FieldEngineerAssignmentRequest();
                    assignmentReq.setSender(account);
                    assignmentReq.setReceiver(selectedEngineer);
                    assignmentReq.setMessage("Fix Power Issue for Billboard #" + request.getBoardId());
                    assignmentReq.setTaskDescription("Reported Power Issue: " + request.getMessage());
                    assignmentReq.setEngineerId(selectedEngineer.getEmployee().getId());
                    assignmentReq.setStatus("Assigned");
                    assignmentReq.setPowerRequest(request); // Link parent request object (legacy)
                    assignmentReq.setPowerRequestId(request.getRequestId()); // Link via ID (robust)
                    // JOptionPane.showMessageDialog(this, "Debug: Linked Power Request ID: " +
                    // request.getRequestId());
                    // JOptionPane.showMessageDialog(this, "Debug: Linked Power Request REQ-" +
                    // System.identityHashCode(request));

                    // Add to queues
                    selectedEngineer.getWorkQueue().getWorkRequestList().add(assignmentReq);
                    fieldOrg.getWorkQueue().getWorkRequestList().add(assignmentReq);

                    // Update original request
                    request.setStatus("Assigned to " + selectedEngineer.getEmployee().getName());

                    JOptionPane.showMessageDialog(this, "Engineer assigned successfully!", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    populatePowerIssues();
                }

            } else {
                JOptionPane.showMessageDialog(this, "Please select a power issue row.", "No Selection",
                        JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error assigning engineer: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleResolve() {
        int selectedRow = powerIssueTable.getSelectedRow();
        if (selectedRow >= 0) {
            WorkRequest request = (WorkRequest) tableModel.getValueAt(selectedRow, 4);
            request.setStatus("Resolved");
            request.setResolveDate(new java.util.Date());
            JOptionPane.showMessageDialog(this, "✓ Issue marked as resolved!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            populatePowerIssues();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a power issue.", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void populatePowerIssues() {
        tableModel.setRowCount(0);

        for (WorkRequest request : organization.getWorkQueue().getWorkRequestList()) {
            if (request instanceof PowerIssueRequest) {
                PowerIssueRequest pir = (PowerIssueRequest) request;
                Object[] row = {
                        "REQ-" + System.identityHashCode(request),
                        pir.getBoardId(),
                        pir.getSeverity() != null ? pir.getSeverity() : "N/A",
                        request.getStatus(),
                        pir // Store object
                };
                tableModel.addRow(row);
            }
        }
    }
}
