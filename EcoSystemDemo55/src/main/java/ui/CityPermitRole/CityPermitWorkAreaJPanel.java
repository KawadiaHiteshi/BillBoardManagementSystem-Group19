package ui.CityPermitRole;

import Business.EcoSystem;
import Business.Enterprise.Enterprise;
import Business.Organization.Organization;
import Business.UserAccount.UserAccount;
import Business.WorkQueue.PermitRequest;
import Business.WorkQueue.WorkRequest;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CityPermitWorkAreaJPanel extends JPanel {

    private JPanel userProcessContainer;
    private UserAccount account;
    private Organization organization;
    private Enterprise enterprise;
    private EcoSystem business;
    private JTable permitTable;
    private DefaultTableModel permitModel;
    private JTable historyTable;
    private DefaultTableModel historyModel;

    // Professional color scheme
    private static final Color HEADER_BG = new Color(52, 73, 94); // Dark Blue-Grey
    private static final Color TABLE_HEADER_BG = new Color(44, 62, 80);
    private static final Color APPROVE_BG = new Color(39, 174, 96); // Green
    private static final Color REJECT_BG = new Color(192, 57, 43); // Red
    private static final Color PANEL_BG = new Color(236, 240, 241);

    public CityPermitWorkAreaJPanel(JPanel userProcessContainer, UserAccount account,
            Organization organization, Enterprise enterprise,
            EcoSystem business) {
        this.userProcessContainer = userProcessContainer;
        this.account = account;
        this.organization = organization;
        this.enterprise = enterprise;
        this.business = business;

        initComponents();
        populatePermits();
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

        JLabel titleLabel = new JLabel("🏛 City Permit Officer");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        headerPanel.add(Box.createHorizontalGlue());

        JLabel welcomeLabel = new JLabel("Welcome, " + account.getUsername());
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        welcomeLabel.setForeground(Color.WHITE);
        headerPanel.add(welcomeLabel);

        add(headerPanel, BorderLayout.NORTH);

        // Tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        tabbedPane.addTab("Incoming Permit Requests", createIncomingPanel());
        tabbedPane.addTab("Permit History", createHistoryPanel());

        add(tabbedPane, BorderLayout.CENTER);

        // Refresh
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(PANEL_BG);
        JButton refreshBtn = createStyledButton("🔄 Refresh Tables", new Color(127, 140, 141));
        refreshBtn.addActionListener(e -> populatePermits());
        footer.add(refreshBtn);
        add(footer, BorderLayout.SOUTH);
    }

    private JPanel createIncomingPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        String[] cols = { "Request ID", "Board ID", "Requester", "Message", "Status" };
        permitModel = new DefaultTableModel(cols, 0);
        permitTable = new JTable(permitModel);
        styleTable(permitTable);

        panel.add(new JScrollPane(permitTable), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnPanel.setBackground(Color.WHITE);

        JButton approveBtn = createStyledButton("✓ Approve Permit", APPROVE_BG);
        JButton rejectBtn = createStyledButton("✗ Reject Permit", REJECT_BG);

        approveBtn.addActionListener(e -> handleProcess("Approved"));
        rejectBtn.addActionListener(e -> handleProcess("Rejected"));

        btnPanel.add(approveBtn);
        btnPanel.add(rejectBtn);

        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        String[] cols = { "Request ID", "Board ID", "Requester", "Status", "Resolution Date", "Officer" };
        historyModel = new DefaultTableModel(cols, 0);
        historyTable = new JTable(historyModel);
        styleTable(historyTable);

        panel.add(new JScrollPane(historyTable), BorderLayout.CENTER);

        return panel;
    }

    private void populatePermits() {
        permitModel.setRowCount(0);
        historyModel.setRowCount(0);

        for (WorkRequest wr : organization.getWorkQueue().getWorkRequestList()) {
            if (wr instanceof PermitRequest) {
                PermitRequest pr = (PermitRequest) wr;

                if (pr.getStatus().equals("Pending")) {
                    permitModel.addRow(new Object[] {
                            pr, // Store object
                            pr.getBoardId(),
                            pr.getRequestedBy(),
                            pr.getMessage(),
                            pr.getStatus()
                    });
                } else {
                    historyModel.addRow(new Object[] {
                            pr.getRequestId(),
                            pr.getBoardId(),
                            pr.getRequestedBy(),
                            pr.getStatus(),
                            pr.getResolveDate(),
                            pr.getReceiver() == null ? "N/A" : pr.getReceiver().getUsername()
                    });
                }
            }
        }
    }

    private void handleProcess(String decision) {
        int selectedRow = permitTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a request to process.");
            return;
        }

        PermitRequest pr = (PermitRequest) permitTable.getValueAt(selectedRow, 0);
        pr.setStatus(decision);
        pr.setReceiver(account);
        pr.setResolveDate(new java.util.Date());

        JOptionPane.showMessageDialog(this, "Permit " + decision + "!");
        populatePermits();
    }

    // UI Helpers
    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(28);
        table.setSelectionBackground(new Color(52, 152, 219));
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(new Color(189, 195, 199));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(TABLE_HEADER_BG);
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
