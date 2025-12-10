/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.SystemAdminWorkArea;

import Business.EcoSystem;
import Business.Network.Network;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author raunak
 */
public class ManageNetworkJPanel extends javax.swing.JPanel {

        private JPanel userProcessContainer;
        private EcoSystem system;

        // Professional color scheme matching admin panel
        private static final Color ADMIN_PRIMARY = new Color(142, 68, 173);
        private static final Color BUTTON_BG = new Color(155, 89, 182);
        private static final Color PANEL_BG = new Color(236, 240, 241);
        private static final Color SUCCESS_BG = new Color(46, 204, 113);

        /**
         *
         * Creates new form ManageNetworkJPanel
         */
        public ManageNetworkJPanel(JPanel userProcessContainer, EcoSystem system) {
                initComponents();

                this.userProcessContainer = userProcessContainer;
                this.system = system;

                populateNetworkTable();
                applyProfessionalStyling();
        }

        private void applyProfessionalStyling() {
                // Set entire panel background to white
                setBackground(Color.WHITE);

                // Style table with 5 visible rows (smaller)
                networkJTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                networkJTable.setRowHeight(30);
                networkJTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
                networkJTable.getTableHeader().setBackground(ADMIN_PRIMARY);
                networkJTable.getTableHeader().setForeground(Color.WHITE);
                jScrollPane1.setBorder(new javax.swing.border.LineBorder(new Color(189, 195, 199), 1));

                // Set table to show exactly 5 rows (smaller table)
                int visibleRows = 5;
                int rowHeight = 30;
                int headerHeight = 25;
                int tableHeight = (visibleRows * rowHeight) + headerHeight;
                jScrollPane1.setPreferredSize(new java.awt.Dimension(700, tableHeight));
                jScrollPane1.setMaximumSize(new java.awt.Dimension(700, tableHeight));

                // Style labels
                lblName.setText("Network Name:");
                lblName.setFont(new Font("Segoe UI", Font.BOLD, 14));
                lblName.setForeground(new Color(44, 62, 80));

                // Style text field - make it larger
                txtNetworkName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                txtNetworkName.setPreferredSize(new java.awt.Dimension(400, 40));
                txtNetworkName.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                                new javax.swing.border.LineBorder(new Color(189, 195, 199), 1),
                                new javax.swing.border.EmptyBorder(5, 10, 5, 10)));

                // Style buttons
                btnSubmit.setPreferredSize(new java.awt.Dimension(200, 45));
                styleButton(btnSubmit, "✓ Create Network", SUCCESS_BG);
                styleButton(btnBack, "← Back", BUTTON_BG);

                // Set up main layout directly on the panel
                setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
                setBorder(new javax.swing.border.EmptyBorder(30, 40, 30, 40));

                // Add back button at top
                btnBack.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
                add(btnBack);
                add(javax.swing.Box.createVerticalStrut(25));

                // Add table section heading
                javax.swing.JLabel tableHeading = new javax.swing.JLabel("Existing Networks");
                tableHeading.setFont(new Font("Segoe UI", Font.BOLD, 16));
                tableHeading.setForeground(new Color(44, 62, 80));
                tableHeading.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

                add(tableHeading);
                add(javax.swing.Box.createVerticalStrut(15));

                // Add table
                jScrollPane1.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
                add(jScrollPane1);
                add(javax.swing.Box.createVerticalStrut(40));

                // Add divider line
                javax.swing.JSeparator separator = new javax.swing.JSeparator();
                separator.setMaximumSize(new java.awt.Dimension(700, 1));
                separator.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
                add(separator);
                add(javax.swing.Box.createVerticalStrut(40));

                // Add form section heading
                javax.swing.JLabel createHeading = new javax.swing.JLabel("Create New Network");
                createHeading.setFont(new Font("Segoe UI", Font.BOLD, 20));
                createHeading.setForeground(ADMIN_PRIMARY);
                createHeading.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

                add(createHeading);
                add(javax.swing.Box.createVerticalStrut(25));

                // Create form panel with left alignment
                javax.swing.JPanel formPanel = new javax.swing.JPanel();
                formPanel.setBackground(Color.WHITE);
                formPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 15));
                formPanel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

                // Add label and text field
                formPanel.add(lblName);
                formPanel.add(txtNetworkName);

                add(formPanel);
                add(javax.swing.Box.createVerticalStrut(20));

                // Add submit button (left-aligned)
                btnSubmit.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
                add(btnSubmit);
        }

        private void styleButton(javax.swing.JButton button, String text, Color bgColor) {
                button.setText(text);
                button.setFont(new Font("Segoe UI", Font.BOLD, 13));
                button.setBackground(bgColor);
                button.setForeground(Color.WHITE);
                button.setFocusPainted(false);
                button.setBorderPainted(false);
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

        private void populateNetworkTable() {
                DefaultTableModel model = (DefaultTableModel) networkJTable.getModel();

                model.setRowCount(0);
                for (Network network : system.getNetworkList()) {
                        Object[] row = new Object[3];
                        row[0] = network.getName();
                        row[1] = "Edit"; // Edit button text
                        row[2] = "Delete"; // Delete button text
                        model.addRow(row);
                }
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

                jScrollPane1 = new javax.swing.JScrollPane();
                networkJTable = new javax.swing.JTable();
                lblName = new javax.swing.JLabel();
                btnSubmit = new javax.swing.JButton();
                txtNetworkName = new javax.swing.JTextField();
                btnBack = new javax.swing.JButton();

                networkJTable.setModel(new javax.swing.table.DefaultTableModel(
                                new Object[][] {
                                },
                                new String[] {
                                                "Name", "Edit", "Delete"
                                }) {
                        Class[] types = new Class[] {
                                        java.lang.String.class, java.lang.String.class, java.lang.String.class
                        };
                        boolean[] canEdit = new boolean[] {
                                        false, false, false
                        };

                        public Class getColumnClass(int columnIndex) {
                                return types[columnIndex];
                        }

                        public boolean isCellEditable(int rowIndex, int columnIndex) {
                                return canEdit[columnIndex];
                        }
                });

                // Add mouse listener for edit/delete button clicks
                networkJTable.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                int row = networkJTable.rowAtPoint(evt.getPoint());
                                int col = networkJTable.columnAtPoint(evt.getPoint());

                                if (row >= 0) {
                                        String networkName = (String) networkJTable.getValueAt(row, 0);
                                        Network networkToEdit = null;
                                        for (Network network : system.getNetworkList()) {
                                                if (network.getName().equals(networkName)) {
                                                        networkToEdit = network;
                                                        break;
                                                }
                                        }

                                        if (col == 1 && networkToEdit != null) { // Edit column
                                                String newName = javax.swing.JOptionPane.showInputDialog(
                                                                ManageNetworkJPanel.this,
                                                                "Enter new network name:",
                                                                networkName);

                                                if (newName != null && !newName.trim().isEmpty()) {
                                                        // Check for duplicates
                                                        boolean exists = false;
                                                        for (Network n : system.getNetworkList()) {
                                                                if (n != networkToEdit && n.getName()
                                                                                .equalsIgnoreCase(newName.trim())) {
                                                                        exists = true;
                                                                        break;
                                                                }
                                                        }

                                                        if (exists) {
                                                                javax.swing.JOptionPane.showMessageDialog(
                                                                                ManageNetworkJPanel.this,
                                                                                "Network name already exists!",
                                                                                "Error",
                                                                                javax.swing.JOptionPane.ERROR_MESSAGE);
                                                        } else {
                                                                networkToEdit.setName(newName.trim());
                                                                populateNetworkTable();
                                                                javax.swing.JOptionPane.showMessageDialog(
                                                                                ManageNetworkJPanel.this,
                                                                                "Network updated successfully!",
                                                                                "Success",
                                                                                javax.swing.JOptionPane.INFORMATION_MESSAGE);
                                                        }
                                                }
                                        } else if (col == 2 && networkToEdit != null) { // Delete column
                                                int confirm = javax.swing.JOptionPane.showConfirmDialog(
                                                                ManageNetworkJPanel.this,
                                                                "Are you sure you want to delete network: "
                                                                                + networkName + "?",
                                                                "Confirm Delete",
                                                                javax.swing.JOptionPane.YES_NO_OPTION,
                                                                javax.swing.JOptionPane.WARNING_MESSAGE);

                                                if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                                                        system.getNetworkList().remove(networkToEdit);
                                                        populateNetworkTable();
                                                        javax.swing.JOptionPane.showMessageDialog(
                                                                        ManageNetworkJPanel.this,
                                                                        "Network deleted successfully!",
                                                                        "Success",
                                                                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
                                                }
                                        }
                                }
                        }
                });
                jScrollPane1.setViewportView(networkJTable);
                if (networkJTable.getColumnModel().getColumnCount() > 0) {
                        networkJTable.getColumnModel().getColumn(0).setResizable(false);
                        networkJTable.getColumnModel().getColumn(1).setResizable(false);
                        networkJTable.getColumnModel().getColumn(1).setPreferredWidth(60);
                        networkJTable.getColumnModel().getColumn(1).setMaxWidth(60);
                        networkJTable.getColumnModel().getColumn(2).setResizable(false);
                        networkJTable.getColumnModel().getColumn(2).setPreferredWidth(70);
                        networkJTable.getColumnModel().getColumn(2).setMaxWidth(70);
                }

                lblName.setText("Name:");

                btnSubmit.setText("Submit");
                btnSubmit.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnSubmitActionPerformed(evt);
                        }
                });

                btnBack.setText("<< Back");
                btnBack.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnBackActionPerformed(evt);
                        }
                });

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
                this.setLayout(layout);
                layout.setHorizontalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addGroup(layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(btnBack)
                                                                                .addGroup(layout
                                                                                                .createParallelGroup(
                                                                                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                                false)
                                                                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                                layout
                                                                                                                                .createSequentialGroup()
                                                                                                                                .addGap(28, 28, 28)
                                                                                                                                .addComponent(lblName)
                                                                                                                                .addGap(18, 18, 18)
                                                                                                                                .addComponent(txtNetworkName,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                181,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                                .addPreferredGap(
                                                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                                Short.MAX_VALUE)
                                                                                                                                .addComponent(btnSubmit))
                                                                                                .addComponent(jScrollPane1,
                                                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                700,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                .addContainerGap(232, Short.MAX_VALUE)));
                layout.setVerticalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(btnBack)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(jScrollPane1,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                325,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(27, 27, 27)
                                                                .addGroup(layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(btnSubmit)
                                                                                .addComponent(txtNetworkName,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(lblName))
                                                                .addContainerGap(258, Short.MAX_VALUE)));
        }// </editor-fold>//GEN-END:initComponents

        private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnSubmitActionPerformed

                String name = txtNetworkName.getText();

                system.createAndAddNetwork(name);

                populateNetworkTable();
        }// GEN-LAST:event_btnSubmitActionPerformed

        private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnBackActionPerformed
                userProcessContainer.remove(this);
                Component[] componentArray = userProcessContainer.getComponents();
                Component component = componentArray[componentArray.length - 1];
                SystemAdminWorkAreaJPanel sysAdminwjp = (SystemAdminWorkAreaJPanel) component;
                sysAdminwjp.populateTree();
                CardLayout layout = (CardLayout) userProcessContainer.getLayout();
                layout.previous(userProcessContainer);
        }// GEN-LAST:event_btnBackActionPerformed

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JButton btnBack;
        private javax.swing.JButton btnSubmit;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JLabel lblName;
        private javax.swing.JTable networkJTable;
        private javax.swing.JTextField txtNetworkName;
        // End of variables declaration//GEN-END:variables
}
