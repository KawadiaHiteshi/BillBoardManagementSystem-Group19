package ui.CampaignPlannerRole;

import Business.EcoSystem;
import Business.Enterprise.Enterprise;
import Business.Enterprise.SkyViewBillboardEnterprise;
import Business.Network.Network;
import Business.Organization.BillboardSalesOrganization;
import Business.Organization.Organization;
import Business.UserAccount.UserAccount;
import Business.WorkQueue.BoardSelectionRequest;
import Business.WorkQueue.WorkRequest;
import Business.Billboard.Billboard;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CampaignPlannerWorkAreaJPanel extends JPanel {

    private JPanel userProcessContainer;
    private UserAccount account;
    private Organization organization;
    private Enterprise enterprise;
    private EcoSystem business;

    private JTable billboardTable;
    private JTable requestTable;
    private DefaultTableModel billboardModel;
    private TableRowSorter<DefaultTableModel> sorter;

    // Filters
    private JComboBox<String> locationFilter;
    private JComboBox<String> typeFilter;

    // Professional color scheme
    private static final Color HEADER_BG = new Color(230, 126, 34); // Pumpkin Orange
    private static final Color BUTTON_BG = new Color(211, 84, 0);
    private static final Color PANEL_BG = new Color(236, 240, 241);

    public CampaignPlannerWorkAreaJPanel(JPanel userProcessContainer, UserAccount account,
            Organization organization, Enterprise enterprise,
            EcoSystem business) {
        this.userProcessContainer = userProcessContainer;
        this.account = account;
        this.organization = organization;
        this.enterprise = enterprise;
        this.business = business;

        initComponents();
        populateBillboards();
        populateRequests();
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

        JLabel titleLabel = new JLabel("🎨 Campaign Planner");
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

        tabbedPane.addTab("Browse & Select Billboards", createBrowsePanel());
        tabbedPane.addTab("My Plans & Requests", createRequestPanel());

        add(tabbedPane, BorderLayout.CENTER);

        // Refresh
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(PANEL_BG);
        JButton refreshBtn = createStyledButton("🔄 Refresh", new Color(149, 165, 166));
        refreshBtn.addActionListener(e -> {
            populateBillboards();
            populateRequests();
        });
        footer.add(refreshBtn);
        add(footer, BorderLayout.SOUTH);
    }

    // --- TAB 1: Browse ---
    private JPanel createBrowsePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Top: Filter Bar
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(new TitledBorder("Filters"));

        locationFilter = new JComboBox<>();
        locationFilter.addItem("All Locations");

        typeFilter = new JComboBox<>();
        typeFilter.addItem("All Types");
        typeFilter.addItem("Digital LED");
        typeFilter.addItem("Static");

        JButton applyFilter = new JButton("Apply Filters");
        applyFilter.addActionListener(e -> applyFilters());

        filterPanel.add(new JLabel("Location:"));
        filterPanel.add(locationFilter);
        filterPanel.add(new JLabel("Type:"));
        filterPanel.add(typeFilter);
        filterPanel.add(applyFilter);

        panel.add(filterPanel, BorderLayout.NORTH);

        // Center: Table
        String[] cols = { "ID", "Location", "Type", "Size", "Price/Day ($)", "Select", "Object" };
        billboardModel = new DefaultTableModel(cols, 0) {
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 5)
                    return Boolean.class; // Checkbox for selection
                return super.getColumnClass(columnIndex);
            }

            public boolean isCellEditable(int row, int col) {
                return col == 5; // Only checkbox editable
            }
        };

        billboardTable = new JTable(billboardModel);
        sorter = new TableRowSorter<>(billboardModel);
        billboardTable.setRowSorter(sorter);
        billboardTable.removeColumn(billboardTable.getColumnModel().getColumn(6)); // Hide Object

        styleTable(billboardTable);

        panel.add(new JScrollPane(billboardTable), BorderLayout.CENTER);

        // Bottom: Action
        JPanel actionPanel = new JPanel();
        actionPanel.setBackground(Color.WHITE);
        JButton planBtn = createStyledButton("📝 Create Plan from Selection", BUTTON_BG);

        planBtn.addActionListener(e -> handleCreatePlan());

        actionPanel.add(planBtn);
        panel.add(actionPanel, BorderLayout.SOUTH);

        return panel;
    }

    // --- TAB 2: Requests ---
    private JPanel createRequestPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        String[] cols = { "Request ID", "Campaign Name", "Boards Selected", "Status", "Receiver" };
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        requestTable = new JTable(model);
        styleTable(requestTable);

        panel.add(new JScrollPane(requestTable), BorderLayout.CENTER);

        return panel;
    }

    // --- Business Logic ---

    private void populateBillboards() {
        billboardModel.setRowCount(0);

        // Keep selected location if any
        Object selectedLoc = locationFilter.getSelectedItem();

        locationFilter.removeAllItems();
        locationFilter.addItem("All Locations");
        ArrayList<String> locs = new ArrayList<>();

        SkyViewBillboardEnterprise skyView = findSkyViewEnterprise();
        if (skyView != null) {
            for (Billboard b : skyView.getBillboardDirectory().getBillboards()) {
                // Populate Filter
                String loc = b.getLocation().split(" - ")[0]; // Simplified location
                if (!locs.contains(loc)) {
                    locs.add(loc);
                    locationFilter.addItem(loc);
                }

                Object[] row = {
                        b.getBoardId(),
                        b.getLocation(),
                        b.getType(),
                        b.getSize(),
                        b.getPricePerDay(),
                        false, // Selection box
                        b
                };
                billboardModel.addRow(row);
            }
        }

        if (selectedLoc != null)
            locationFilter.setSelectedItem(selectedLoc);
    }

    private void applyFilters() {
        RowFilter<DefaultTableModel, Object> rf = null;
        List<RowFilter<Object, Object>> filters = new ArrayList<>();

        try {
            // Location Filter
            String loc = (String) locationFilter.getSelectedItem();
            if (loc != null && !loc.equals("All Locations")) {
                filters.add(RowFilter.regexFilter(loc, 1));
            }

            // Type Filter
            String type = (String) typeFilter.getSelectedItem();
            if (type != null && !type.equals("All Types")) {
                filters.add(RowFilter.regexFilter(type, 2));
            }

            // If filters list is empty, rf remains null (no filter)
            if (!filters.isEmpty()) {
                rf = RowFilter.andFilter(filters);
            }

        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }

    private void handleCreatePlan() {
        List<Billboard> selectedBoards = new ArrayList<>();

        for (int i = 0; i < billboardModel.getRowCount(); i++) {
            boolean isSelected = (Boolean) billboardModel.getValueAt(i, 5);
            if (isSelected) {
                selectedBoards.add((Billboard) billboardModel.getValueAt(i, 6));
            }
        }

        if (selectedBoards.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least one billboard.");
            return;
        }

        String campaignName = JOptionPane.showInputDialog(this, "Enter Campaign Name for this Plan:");
        if (campaignName != null && !campaignName.trim().isEmpty()) {
            // Create Request
            BoardSelectionRequest req = new BoardSelectionRequest();
            req.setSender(account);
            req.setCampaignName(campaignName);
            req.setStatus("Sent to Sales");
            req.setMessage("Proposal for " + selectedBoards.size() + " billboards");

            for (Billboard b : selectedBoards) {
                req.addBoardId(b.getBoardId());
            }

            // Send to Sales Org
            SkyViewBillboardEnterprise skyView = findSkyViewEnterprise();
            if (skyView == null)
                return;

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
                JOptionPane.showMessageDialog(this, "Plan sent to Billboard Sales!");
                // Clear selection
                for (int i = 0; i < billboardModel.getRowCount(); i++)
                    billboardModel.setValueAt(false, i, 5);
                populateRequests();
            } else {
                JOptionPane.showMessageDialog(this, "Billboard Sales Organization not found.");
            }
        }
    }

    private void populateRequests() {
        DefaultTableModel model = (DefaultTableModel) requestTable.getModel();
        model.setRowCount(0);

        for (WorkRequest wr : account.getWorkQueue().getWorkRequestList()) {
            if (wr instanceof BoardSelectionRequest) {
                BoardSelectionRequest bsr = (BoardSelectionRequest) wr;

                String receiver = "Billboard Sales (Queue)";
                if (bsr.getReceiver() != null) {
                    receiver = bsr.getReceiver().getUsername();
                }

                model.addRow(new Object[] {
                        "REQ-" + bsr.getRequestId(),
                        bsr.getCampaignName(),
                        bsr.getSelectedBoardIds().size(),
                        bsr.getStatus(),
                        receiver
                });
            }
        }
    }

    private SkyViewBillboardEnterprise findSkyViewEnterprise() {
        for (Network n : business.getNetworkList()) {
            boolean inNetwork = false;
            for (Enterprise e : n.getEnterpriseDirectory().getEnterpriseList()) {
                if (e == enterprise) {
                    inNetwork = true;
                    break;
                }
            }
            if (inNetwork) {
                for (Enterprise e : n.getEnterpriseDirectory().getEnterpriseList()) {
                    if (e instanceof SkyViewBillboardEnterprise) {
                        return (SkyViewBillboardEnterprise) e;
                    }
                }
            }
        }
        return null;
    }

    // --- Styles ---
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(220, 40));
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
        table.setSelectionBackground(new Color(230, 126, 34));
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(new Color(189, 195, 199));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(52, 73, 94));
        table.getTableHeader().setForeground(Color.WHITE);
    }
}
