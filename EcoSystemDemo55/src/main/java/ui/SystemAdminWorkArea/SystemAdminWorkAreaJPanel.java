/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.SystemAdminWorkArea;

import Business.EcoSystem;
import Business.Enterprise.Enterprise;
import Business.Network.Network;
import Business.Organization.Organization;
import java.awt.CardLayout;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author MyPC1
 */
public class SystemAdminWorkAreaJPanel extends javax.swing.JPanel {

    /**
     * Creates new form SystemAdminWorkAreaJPanel
     */
    JPanel userProcessContainer;
    EcoSystem ecosystem;

    // Professional color scheme
    private static final Color ADMIN_PRIMARY = new Color(142, 68, 173); // Purple for admin
    private static final Color BUTTON_BG = new Color(155, 89, 182);
    private static final Color HEADER_BG = new Color(127, 140, 141);
    private static final Color PANEL_BG = new Color(236, 240, 241);

    public SystemAdminWorkAreaJPanel(JPanel userProcessContainer, EcoSystem ecosystem) {
        initComponents();
        this.userProcessContainer = userProcessContainer;
        this.ecosystem = ecosystem;
        populateTree();
        applyProfessionalStyling();
    }

    private void applyProfessionalStyling() {
        // ============================================================
        // STEP 1: Style the main panel background
        // ============================================================
        // This sets the background color for the entire admin panel
        setBackground(PANEL_BG);

        // ============================================================
        // STEP 2: Hide unnecessary UI elements
        // ============================================================
        // We hide the tree view (left panel) because it's not needed for a clean admin
        // UI
        jPanel1.setVisible(false);

        // Remove the divider between left and right panels
        jSplitPane.setDividerSize(0);
        jSplitPane.setDividerLocation(0);

        // Hide the "Selected Node" label and its value
        jLabel1.setVisible(false);
        lblSelectedNode.setVisible(false);

        // ============================================================
        // STEP 3: Configure the main content panel for centering
        // ============================================================
        // Set background color for the right panel
        jPanel2.setBackground(PANEL_BG);

        // IMPORTANT: Use GridBagLayout to enable centering of content
        // GridBagLayout is the most flexible layout manager for positioning components
        jPanel2.setLayout(new java.awt.GridBagLayout());

        // ============================================================
        // STEP 4: Create a container for buttons with vertical stacking
        // ============================================================
        // This container will hold all three buttons stacked vertically
        javax.swing.JPanel buttonContainer = new javax.swing.JPanel();
        buttonContainer.setLayout(new javax.swing.BoxLayout(buttonContainer, javax.swing.BoxLayout.Y_AXIS));
        buttonContainer.setBackground(PANEL_BG);

        // ============================================================
        // STEP 5: Create and style the header panel
        // ============================================================
        // Create a panel for the header with purple background
        javax.swing.JPanel headerPanel = new javax.swing.JPanel();
        headerPanel.setBackground(ADMIN_PRIMARY);
        headerPanel.setBorder(new javax.swing.border.EmptyBorder(20, 20, 20, 20));

        // IMPORTANT: Use GridBagLayout for the header to center the title
        headerPanel.setLayout(new java.awt.GridBagLayout());

        // Create the title label with icon and text
        javax.swing.JLabel titleLabel = new javax.swing.JLabel("System Administration");
        titleLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        // LEARNING POINT: GridBagConstraints control how components are positioned
        // in a GridBagLayout. Here we center the title horizontally.
        java.awt.GridBagConstraints headerGbc = new java.awt.GridBagConstraints();
        headerGbc.gridx = 0; // Column 0
        headerGbc.gridy = 0; // Row 0
        headerGbc.anchor = java.awt.GridBagConstraints.CENTER; // Center the component
        headerPanel.add(titleLabel, headerGbc);

        // Add the header panel to the top of the main panel
        // BorderLayout.NORTH means it will be at the top
        add(headerPanel, java.awt.BorderLayout.NORTH);

        // ============================================================
        // STEP 6: Style the buttons with icons and colors
        // ============================================================
        styleButton(btnManageNetwork, "🌐 Manage Network", BUTTON_BG);
        styleButton(btnManageEnterprise, "🏢 Manage Enterprise", BUTTON_BG);
        styleButton(btnManageAdmin, "👤 Manage Enterprise Admin", BUTTON_BG);

        // ============================================================
        // STEP 7: Center-align buttons within their container
        // ============================================================
        // setAlignmentX centers each button horizontally in the BoxLayout
        btnManageNetwork.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        btnManageEnterprise.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        btnManageAdmin.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

        // ============================================================
        // STEP 8: Add buttons to container with vertical spacing
        // ============================================================
        // createVerticalStrut adds empty space between components
        buttonContainer.add(javax.swing.Box.createVerticalStrut(20)); // Top padding
        buttonContainer.add(btnManageNetwork);
        buttonContainer.add(javax.swing.Box.createVerticalStrut(15)); // Space between buttons
        buttonContainer.add(btnManageEnterprise);
        buttonContainer.add(javax.swing.Box.createVerticalStrut(15)); // Space between buttons
        buttonContainer.add(btnManageAdmin);
        buttonContainer.add(javax.swing.Box.createVerticalStrut(20)); // Bottom padding

        // ============================================================
        // STEP 9: Add button container to center of main panel
        // ============================================================
        // LEARNING POINT: This is the KEY to centering!
        // GridBagConstraints with CENTER anchor places the container
        // in the exact center of jPanel2 (both horizontally and vertically)
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 0; // Column position
        gbc.gridy = 0; // Row position
        gbc.anchor = java.awt.GridBagConstraints.CENTER; // CENTER is the magic!
        jPanel2.add(buttonContainer, gbc);
    }

    private void styleButton(javax.swing.JButton button, String text, Color bgColor) {
        button.setText(text);
        button.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new java.awt.Dimension(250, 45));
        button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
    }

    public void populateTree() {
        DefaultTreeModel model = (DefaultTreeModel) jTree.getModel();
        ArrayList<Network> networkList = ecosystem.getNetworkList();
        ArrayList<Enterprise> enterpriseList;
        ArrayList<Organization> organizationList;

        Network network;
        Enterprise enterprise;
        Organization organization;

        DefaultMutableTreeNode networks = new DefaultMutableTreeNode("Networks");
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        root.removeAllChildren();
        root.insert(networks, 0);

        DefaultMutableTreeNode networkNode;
        DefaultMutableTreeNode enterpriseNode;
        DefaultMutableTreeNode organizationNode;

        for (int i = 0; i < networkList.size(); i++) {
            network = networkList.get(i);
            networkNode = new DefaultMutableTreeNode(network.getName());
            networks.insert(networkNode, i);

            enterpriseList = network.getEnterpriseDirectory().getEnterpriseList();
            for (int j = 0; j < enterpriseList.size(); j++) {
                enterprise = enterpriseList.get(j);
                enterpriseNode = new DefaultMutableTreeNode(enterprise.getName());
                networkNode.insert(enterpriseNode, j);

                organizationList = enterprise.getOrganizationDirectory().getOrganizationList();
                for (int k = 0; k < organizationList.size(); k++) {
                    organization = organizationList.get(k); // <-- use k here
                    organizationNode = new DefaultMutableTreeNode(organization.getName());
                    enterpriseNode.insert(organizationNode, k);
                }
            }
        }
        model.reload();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTree = new javax.swing.JTree();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblSelectedNode = new javax.swing.JLabel();
        btnManageNetwork = new javax.swing.JButton();
        btnManageEnterprise = new javax.swing.JButton();
        btnManageAdmin = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jTreeValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jTree);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE));

        jSplitPane.setLeftComponent(jPanel1);

        jLabel1.setText("Selected Node:");

        lblSelectedNode.setText("<View_selected_node>");

        btnManageNetwork.setText("Manage Network");
        btnManageNetwork.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnManageNetworkActionPerformed(evt);
            }
        });

        btnManageEnterprise.setText("Manage Enterprise");
        btnManageEnterprise.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnManageEnterpriseActionPerformed(evt);
            }
        });

        btnManageAdmin.setText("Manage Enterprise Admin");
        btnManageAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnManageAdminActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGap(37, 37, 37)
                                                .addComponent(jLabel1)
                                                .addGap(18, 18, 18)
                                                .addComponent(lblSelectedNode))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGap(91, 91, 91)
                                                .addGroup(jPanel2Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(btnManageAdmin)
                                                        .addGroup(jPanel2Layout
                                                                .createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING,
                                                                        false)
                                                                .addComponent(btnManageEnterprise,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        Short.MAX_VALUE)
                                                                .addComponent(btnManageNetwork,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        Short.MAX_VALUE)))))
                                .addContainerGap(239, Short.MAX_VALUE)));

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL,
                new java.awt.Component[] { btnManageAdmin, btnManageEnterprise, btnManageNetwork });

        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(lblSelectedNode))
                                .addGap(54, 54, 54)
                                .addComponent(btnManageNetwork)
                                .addGap(18, 18, 18)
                                .addComponent(btnManageEnterprise)
                                .addGap(18, 18, 18)
                                .addComponent(btnManageAdmin)
                                .addContainerGap(175, Short.MAX_VALUE)));

        jSplitPane.setRightComponent(jPanel2);

        add(jSplitPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnManageNetworkActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnManageNetworkActionPerformed
        ManageNetworkJPanel manageNetworkJPanel = new ManageNetworkJPanel(userProcessContainer, ecosystem);
        userProcessContainer.add("manageNetworkJPanel", manageNetworkJPanel);
        CardLayout layout = (CardLayout) userProcessContainer.getLayout();
        layout.next(userProcessContainer);
    }// GEN-LAST:event_btnManageNetworkActionPerformed

    private void btnManageEnterpriseActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnManageEnterpriseActionPerformed
        ManageEnterpriseJPanel manageEnterpriseJPanel = new ManageEnterpriseJPanel(userProcessContainer, ecosystem);
        userProcessContainer.add("manageEnterpriseJPanel", manageEnterpriseJPanel);
        CardLayout layout = (CardLayout) userProcessContainer.getLayout();
        layout.next(userProcessContainer);
    }// GEN-LAST:event_btnManageEnterpriseActionPerformed

    private void btnManageAdminActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnManageAdminActionPerformed
        ManageEnterpriseAdminJPanel manageEnterpriseAdminJPanel = new ManageEnterpriseAdminJPanel(userProcessContainer,
                ecosystem);
        userProcessContainer.add("manageEnterpriseAdminJPanel", manageEnterpriseAdminJPanel);
        CardLayout layout = (CardLayout) userProcessContainer.getLayout();
        layout.next(userProcessContainer);
    }// GEN-LAST:event_btnManageAdminActionPerformed

    private void jTreeValueChanged(javax.swing.event.TreeSelectionEvent evt) {// GEN-FIRST:event_jTreeValueChanged

        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();
        if (selectedNode != null) {
            lblSelectedNode.setText(selectedNode.toString());
        }
    }// GEN-LAST:event_jTreeValueChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnManageAdmin;
    private javax.swing.JButton btnManageEnterprise;
    private javax.swing.JButton btnManageNetwork;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane;
    private javax.swing.JTree jTree;
    private javax.swing.JLabel lblSelectedNode;
    // End of variables declaration//GEN-END:variables
}
