package ui.FieldEngineerRole;

import Business.EcoSystem;
import Business.Enterprise.Enterprise;
import Business.Organization.Organization;
import Business.UserAccount.UserAccount;
import Business.WorkQueue.FieldEngineerAssignmentRequest;
import Business.WorkQueue.WorkRequest;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FieldEngineerWorkAreaJPanel extends JPanel {

    private JPanel userProcessContainer;
    private UserAccount account;
    private Organization organization;
    private Enterprise enterprise;
    private EcoSystem business;
    private JTable taskTable;
    private DefaultTableModel tableModel;

    // Professional color scheme
    private static final Color HEADER_BG = new Color(52, 73, 94); // Dark Blue-Gray
    private static final Color BUTTON_BG = new Color(52, 152, 219);
    private static final Color COMPLETE_BG = new Color(46, 204, 113);
    private static final Color PANEL_BG = new Color(236, 240, 241);

    public FieldEngineerWorkAreaJPanel(JPanel userProcessContainer, UserAccount account,
            Organization organization, Enterprise enterprise,
            EcoSystem business) {
        this.userProcessContainer = userProcessContainer;
        this.account = account;
        this.organization = organization;
        this.enterprise = enterprise;
        this.business = business;

        initComponents();
        populateTasks();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(PANEL_BG);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(HEADER_BG);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("👷 Field Maintenance Engineer - Work Area");
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

        JLabel tableTitle = new JLabel("🔧 Assigned Tasks");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tableTitle.setForeground(new Color(44, 62, 80));
        centerPanel.add(tableTitle, BorderLayout.NORTH);

        // Task Table
        String[] columnNames = { "Task ID", "Engineer ID", "Description", "Status", "Request Object" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        taskTable = new JTable(tableModel);
        taskTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        taskTable.setRowHeight(28);
        taskTable.setSelectionBackground(new Color(52, 152, 219));
        taskTable.setSelectionForeground(Color.WHITE);
        taskTable.setGridColor(new Color(189, 195, 199));
        taskTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        taskTable.getTableHeader().setBackground(new Color(52, 73, 94));
        taskTable.getTableHeader().setForeground(Color.WHITE);

        // Hide object column
        taskTable.removeColumn(taskTable.getColumnModel().getColumn(4));

        JScrollPane scrollPane = new JScrollPane(taskTable);
        scrollPane.setBorder(new LineBorder(new Color(189, 195, 199), 1));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(PANEL_BG);

        JButton startButton = createStyledButton("▶️ Start Task", BUTTON_BG);
        JButton completeButton = createStyledButton("✓ Complete Task", COMPLETE_BG);
        JButton refreshButton = createStyledButton("🔄 Refresh", new Color(149, 165, 166));

        startButton.addActionListener(e -> handleStart());
        completeButton.addActionListener(e -> handleComplete());
        refreshButton.addActionListener(e -> populateTasks());

        buttonPanel.add(startButton);
        buttonPanel.add(completeButton);
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
        button.setPreferredSize(new Dimension(160, 40));
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

    private void handleStart() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = taskTable.convertRowIndexToModel(selectedRow);
            WorkRequest request = (WorkRequest) tableModel.getValueAt(modelRow, 4);

            request.setStatus("In Progress");
            JOptionPane.showMessageDialog(this, "▶️ Task started!", "Success", JOptionPane.INFORMATION_MESSAGE);
            populateTasks();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a task.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleComplete() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = taskTable.convertRowIndexToModel(selectedRow);
            WorkRequest request = (WorkRequest) tableModel.getValueAt(modelRow, 4);

            if (request.getStatus().equalsIgnoreCase("Completed")) {
                JOptionPane.showMessageDialog(this, "Task already completed.", "Info", JOptionPane.WARNING_MESSAGE);
                return;
            }

            request.setStatus("Completed");

            // Status Synchronization
            if (request instanceof FieldEngineerAssignmentRequest) {
                FieldEngineerAssignmentRequest fear = (FieldEngineerAssignmentRequest) request;

                // Sync status to the original PowerIssueRequest
                boolean originalRequestFound = false;
                Business.WorkQueue.PowerIssueRequest pReq = fear.getPowerRequest(); // Might be null if not directly
                                                                                    // linked

                // 1. Try to find via ID in the Enterprise Queues (Robust)
                if (enterprise != null) {
                    for (Organization org : enterprise.getOrganizationDirectory().getOrganizationList()) {
                        for (WorkRequest r : org.getWorkQueue().getWorkRequestList()) {
                            if (r.getRequestId() == fear.getPowerRequestId()) {
                                r.setStatus("Resolved by " + account.getEmployee().getName());
                                r.setResolveDate(new java.util.Date());
                                if (r instanceof Business.WorkQueue.PowerIssueRequest) {
                                    pReq = (Business.WorkQueue.PowerIssueRequest) r;
                                }
                                originalRequestFound = true;
                                break;
                            }
                        }
                        if (originalRequestFound)
                            break;
                    }
                }

                // 2. If not found via ID, check direct link
                if (!originalRequestFound && pReq != null) {
                    pReq.setStatus("Resolved by " + account.getEmployee().getName());
                    pReq.setResolveDate(new java.util.Date());
                    originalRequestFound = true;
                }

                // 3. Restore Billboard Status if we have the PowerRequest
                if (originalRequestFound && pReq != null) {
                    // Find Billboard in SkyView Enterprise
                    Business.Network.Network myNetwork = null;
                    for (Business.Network.Network n : business.getNetworkList()) {
                        for (Enterprise e : n.getEnterpriseDirectory().getEnterpriseList()) {
                            if (e == enterprise) {
                                myNetwork = n;
                                break;
                            }
                        }
                    }

                    if (myNetwork != null) {
                        for (Enterprise e : myNetwork.getEnterpriseDirectory().getEnterpriseList()) {
                            if (e instanceof Business.Enterprise.SkyViewBillboardEnterprise) {
                                Business.Enterprise.SkyViewBillboardEnterprise skyView = (Business.Enterprise.SkyViewBillboardEnterprise) e;
                                Business.Billboard.Billboard b = skyView.getBillboardDirectory()
                                        .getBillboardById(pReq.getBoardId());
                                if (b != null) {
                                    b.setStatus(Business.Billboard.BillboardStatus.AVAILABLE);
                                    b.setLighted(true);
                                    JOptionPane.showMessageDialog(this, "System Update: Billboard " + b.getBoardId()
                                            + " status restored to AVAILABLE.");
                                }
                            }
                        }
                    }
                }
            }

            JOptionPane.showMessageDialog(this, "✓ Task completed!", "Success", JOptionPane.INFORMATION_MESSAGE);
            populateTasks();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a task.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void populateTasks() {
        tableModel.setRowCount(0);

        for (WorkRequest request : organization.getWorkQueue().getWorkRequestList()) {
            if (request instanceof FieldEngineerAssignmentRequest) {
                FieldEngineerAssignmentRequest fear = (FieldEngineerAssignmentRequest) request;
                Object[] row = {
                        "TASK-" + System.identityHashCode(request),
                        fear.getEngineerId(),
                        fear.getTaskDescription() != null ? fear.getTaskDescription() : "Maintenance Task",
                        request.getStatus(),
                        request // Store object
                };
                tableModel.addRow(row);
            }
        }
    }
}
