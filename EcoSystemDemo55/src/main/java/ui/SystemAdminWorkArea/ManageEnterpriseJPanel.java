/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.SystemAdminWorkArea;

import Business.EcoSystem;
import Business.Enterprise.Enterprise;
import Business.Enterprise.EnterpriseType;
import Business.Network.Network;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author raunak
 */
public class ManageEnterpriseJPanel extends javax.swing.JPanel {

        private JPanel userProcessContainer;
        private EcoSystem system;

        // Professional color scheme
        private static final Color ADMIN_PRIMARY = new Color(142, 68, 173);
        private static final Color BUTTON_BG = new Color(155, 89, 182);
        private static final Color PANEL_BG = new Color(236, 240, 241);
        private static final Color SUCCESS_BG = new Color(46, 204, 113);

        /**
         * Creates new form ManageEnterpriseJPanel
         */
        public ManageEnterpriseJPanel(JPanel userProcessContainer, EcoSystem system) {
                initComponents();
                this.userProcessContainer = userProcessContainer;
                this.system = system;

                populateTable();
                populateComboBox();
                applyProfessionalStyling();
        }

        private void applyProfessionalStyling() {
                setBackground(PANEL_BG);

                // Style table
                enterpriseJTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                enterpriseJTable.setRowHeight(30);
                enterpriseJTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
                enterpriseJTable.getTableHeader().setBackground(ADMIN_PRIMARY);
                enterpriseJTable.getTableHeader().setForeground(Color.WHITE);
                jScrollPane1.setBorder(new javax.swing.border.LineBorder(new Color(189, 195, 199), 1));

                // Style labels
                jLabel1.setFont(new Font("Segoe UI", Font.BOLD, 14));
                jLabel1.setForeground(new Color(44, 62, 80));
                jLabel2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                jLabel2.setForeground(new Color(44, 62, 80));

                // Style text field
                nameJTextField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                nameJTextField.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                                new javax.swing.border.LineBorder(new Color(189, 195, 199), 1),
                                new javax.swing.border.EmptyBorder(5, 10, 5, 10)));

                // Style combo box
                networkJComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));

                // Style buttons
                styleButton(submitJButton, "✓ Submit", SUCCESS_BG);
                styleButton(backJButton, "← Back", BUTTON_BG);
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

        private void populateTable() {
                DefaultTableModel model = (DefaultTableModel) enterpriseJTable.getModel();

                model.setRowCount(0);
                for (Network network : system.getNetworkList()) {
                        for (Enterprise enterprise : network.getEnterpriseDirectory().getEnterpriseList()) {
                                Object[] row = new Object[5];
                                row[0] = enterprise.getName();
                                row[1] = network.getName();
                                row[2] = enterprise.getEnterpriseType().getValue();
                                row[3] = "Edit"; // Edit button text
                                row[4] = "Delete"; // Delete button text

                                model.addRow(row);
                        }
                }
        }

        private void populateComboBox() {
                networkJComboBox.removeAllItems();
                enterpriseTypeJComboBox.removeAllItems();

                for (Network network : system.getNetworkList()) {
                        networkJComboBox.addItem(network);
                }

                for (EnterpriseType type : EnterpriseType.values()) {
                        enterpriseTypeJComboBox.addItem(type);
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
                enterpriseJTable = new javax.swing.JTable();
                jLabel1 = new javax.swing.JLabel();
                networkJComboBox = new javax.swing.JComboBox();
                jLabel2 = new javax.swing.JLabel();
                nameJTextField = new javax.swing.JTextField();
                jLabel3 = new javax.swing.JLabel();
                enterpriseTypeJComboBox = new javax.swing.JComboBox();
                submitJButton = new javax.swing.JButton();
                backJButton = new javax.swing.JButton();

                enterpriseJTable.setModel(new javax.swing.table.DefaultTableModel(
                                new Object[][] {
                                                { null, null, null, null, null },
                                                { null, null, null, null, null },
                                                { null, null, null, null, null },
                                                { null, null, null, null, null }
                                },
                                new String[] {
                                                "Enterprise Name", "Network", "Type", "Edit", "Delete"
                                }) {
                        boolean[] canEdit = new boolean[] {
                                        false, false, false, false, false
                        };

                        public boolean isCellEditable(int rowIndex, int columnIndex) {
                                return canEdit[columnIndex];
                        }
                });

                // Add mouse listener for edit/delete button clicks
                enterpriseJTable.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                int row = enterpriseJTable.rowAtPoint(evt.getPoint());
                                int col = enterpriseJTable.columnAtPoint(evt.getPoint());

                                if (row >= 0) {
                                        String enterpriseName = (String) enterpriseJTable.getValueAt(row, 0);
                                        String networkName = (String) enterpriseJTable.getValueAt(row, 1);

                                        // Find the enterprise
                                        Network targetNetwork = null;
                                        Enterprise targetEnterprise = null;

                                        for (Network network : system.getNetworkList()) {
                                                if (network.getName().equals(networkName)) {
                                                        targetNetwork = network;
                                                        for (Enterprise enterprise : network.getEnterpriseDirectory()
                                                                        .getEnterpriseList()) {
                                                                if (enterprise.getName().equals(enterpriseName)) {
                                                                        targetEnterprise = enterprise;
                                                                        break;
                                                                }
                                                        }
                                                        break;
                                                }
                                        }

                                        if (targetEnterprise != null && targetNetwork != null) {
                                                if (col == 3) { // Edit column
                                                        String newName = javax.swing.JOptionPane.showInputDialog(
                                                                        ManageEnterpriseJPanel.this,
                                                                        "Enter new enterprise name:",
                                                                        enterpriseName);

                                                        if (newName != null && !newName.trim().isEmpty()) {
                                                                // Check for duplicates in the same network
                                                                boolean exists = false;
                                                                for (Enterprise e : targetNetwork
                                                                                .getEnterpriseDirectory()
                                                                                .getEnterpriseList()) {
                                                                        if (e != targetEnterprise && e.getName()
                                                                                        .equalsIgnoreCase(newName
                                                                                                        .trim())) {
                                                                                exists = true;
                                                                                break;
                                                                        }
                                                                }

                                                                if (exists) {
                                                                        javax.swing.JOptionPane.showMessageDialog(
                                                                                        ManageEnterpriseJPanel.this,
                                                                                        "Enterprise name already exists in this network!",
                                                                                        "Error",
                                                                                        javax.swing.JOptionPane.ERROR_MESSAGE);
                                                                } else {
                                                                        targetEnterprise.setName(newName.trim());
                                                                        populateTable();
                                                                        javax.swing.JOptionPane.showMessageDialog(
                                                                                        ManageEnterpriseJPanel.this,
                                                                                        "Enterprise updated successfully!",
                                                                                        "Success",
                                                                                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
                                                                }
                                                        }
                                                } else if (col == 4) { // Delete column
                                                        int confirm = javax.swing.JOptionPane.showConfirmDialog(
                                                                        ManageEnterpriseJPanel.this,
                                                                        "Are you sure you want to delete enterprise: "
                                                                                        + enterpriseName + "?",
                                                                        "Confirm Delete",
                                                                        javax.swing.JOptionPane.YES_NO_OPTION,
                                                                        javax.swing.JOptionPane.WARNING_MESSAGE);

                                                        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                                                                targetNetwork.getEnterpriseDirectory()
                                                                                .getEnterpriseList()
                                                                                .remove(targetEnterprise);
                                                                populateTable();
                                                                javax.swing.JOptionPane.showMessageDialog(
                                                                                ManageEnterpriseJPanel.this,
                                                                                "Enterprise deleted successfully!",
                                                                                "Success",
                                                                                javax.swing.JOptionPane.INFORMATION_MESSAGE);
                                                        }
                                                }
                                        }
                                }
                        }
                });
                jScrollPane1.setViewportView(enterpriseJTable);

                jLabel1.setText("Network");

                networkJComboBox.setModel(
                                new javax.swing.DefaultComboBoxModel(
                                                new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

                jLabel2.setText("Name");

                jLabel3.setText("Enterprise Type");

                enterpriseTypeJComboBox.setModel(
                                new javax.swing.DefaultComboBoxModel(
                                                new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

                submitJButton.setText("Submit");
                submitJButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                submitJButtonActionPerformed(evt);
                        }
                });

                backJButton.setText("<< Back");
                backJButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                backJButtonActionPerformed(evt);
                        }
                });

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
                this.setLayout(layout);
                layout.setHorizontalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                false)
                                                                                .addGroup(layout.createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                .addGroup(layout.createSequentialGroup()
                                                                                                                .addGap(88, 88, 88)
                                                                                                                .addGroup(layout
                                                                                                                                .createParallelGroup(
                                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                                .addComponent(jLabel1)
                                                                                                                                .addComponent(jLabel2)
                                                                                                                                .addComponent(jLabel3))
                                                                                                                .addGap(52, 52, 52)
                                                                                                                .addGroup(layout
                                                                                                                                .createParallelGroup(
                                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                                                                false)
                                                                                                                                .addComponent(networkJComboBox,
                                                                                                                                                0,
                                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                                Short.MAX_VALUE)
                                                                                                                                .addComponent(enterpriseTypeJComboBox,
                                                                                                                                                0,
                                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                                Short.MAX_VALUE)
                                                                                                                                .addComponent(nameJTextField,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                136,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                                                .addGroup(layout.createSequentialGroup()
                                                                                                                .addGap(39, 39, 39)
                                                                                                                .addComponent(jScrollPane1,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                523,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                                .addGroup(layout.createSequentialGroup()
                                                                                                .addGap(61, 61, 61)
                                                                                                .addComponent(backJButton)
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                Short.MAX_VALUE)
                                                                                                .addComponent(submitJButton)))
                                                                .addContainerGap(22, Short.MAX_VALUE)));
                layout.setVerticalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                                .addGap(58, 58, 58)
                                                                .addComponent(jScrollPane1,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                95,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(45, 45, 45)
                                                                .addGroup(layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(jLabel1)
                                                                                .addComponent(networkJComboBox,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(31, 31, 31)
                                                                .addGroup(layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(jLabel3)
                                                                                .addComponent(enterpriseTypeJComboBox,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(18, 18, 18)
                                                                .addGroup(layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(jLabel2)
                                                                                .addComponent(nameJTextField,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(18, 18, 18)
                                                                .addGroup(layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(submitJButton)
                                                                                .addComponent(backJButton))
                                                                .addContainerGap(57, Short.MAX_VALUE)));
        }// </editor-fold>//GEN-END:initComponents

        private void submitJButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_submitJButtonActionPerformed

                Network network = (Network) networkJComboBox.getSelectedItem();
                EnterpriseType type = (EnterpriseType) enterpriseTypeJComboBox.getSelectedItem();

                if (network == null || type == null) {
                        JOptionPane.showMessageDialog(null, "Invalid Input!");
                        return;
                }

                String name = nameJTextField.getText();

                Enterprise enterprise = network.getEnterpriseDirectory().createAndAddEnterprise(name, type);

                populateTable();

        }// GEN-LAST:event_submitJButtonActionPerformed

        private void backJButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_backJButtonActionPerformed
                userProcessContainer.remove(this);
                Component[] componentArray = userProcessContainer.getComponents();
                Component component = componentArray[componentArray.length - 1];
                SystemAdminWorkAreaJPanel sysAdminwjp = (SystemAdminWorkAreaJPanel) component;
                sysAdminwjp.populateTree();

                CardLayout layout = (CardLayout) userProcessContainer.getLayout();
                layout.previous(userProcessContainer);
        }// GEN-LAST:event_backJButtonActionPerformed

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JButton backJButton;
        private javax.swing.JTable enterpriseJTable;
        private javax.swing.JComboBox enterpriseTypeJComboBox;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JLabel jLabel2;
        private javax.swing.JLabel jLabel3;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JTextField nameJTextField;
        private javax.swing.JComboBox networkJComboBox;
        private javax.swing.JButton submitJButton;
        // End of variables declaration//GEN-END:variables
}
