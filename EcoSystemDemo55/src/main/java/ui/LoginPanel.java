package ui;

import Business.EcoSystem;
import Business.Enterprise.Enterprise;
import Business.Network.Network;
import Business.Organization.Organization;
import Business.UserAccount.UserAccount;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPanel extends JPanel {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private EcoSystem system;
    private JPanel containerPanel;
    private JButton logoutButton;

    // Professional color scheme
    private static final Color PRIMARY_BG = new Color(41, 128, 185); // Professional Blue
    private static final Color BUTTON_BG = new Color(52, 152, 219);
    private static final Color SUCCESS_BG = new Color(46, 204, 113);
    private static final Color PANEL_BG = Color.WHITE;
    private static final Color CARD_BG = Color.WHITE;

    public LoginPanel(EcoSystem system, JPanel containerPanel, JButton logoutButton) {
        this.system = system;
        this.containerPanel = containerPanel;
        this.logoutButton = logoutButton;

        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        setBackground(PANEL_BG);

        // Main login card
        JPanel loginCard = new JPanel();
        loginCard.setLayout(new BoxLayout(loginCard, BoxLayout.Y_AXIS));
        loginCard.setBackground(CARD_BG);
        // Removed border for seamless look
        loginCard.setBorder(new EmptyBorder(40, 50, 40, 50));
        loginCard.setPreferredSize(new Dimension(550, 600));
        loginCard.setMaximumSize(new Dimension(550, 600));

        // Header with icon
        JLabel iconLabel = new JLabel("📊", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 72));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginCard.add(iconLabel);

        loginCard.add(Box.createVerticalStrut(20));

        // Title - using HTML for proper text wrapping
        JLabel titleLabel = new JLabel("<html><center>Billboard Management<br>System</center></html>");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(44, 62, 80));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginCard.add(titleLabel);

        loginCard.add(Box.createVerticalStrut(10));

        // Subtitle
        JLabel subtitleLabel = new JLabel("Sign in to your account");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(127, 140, 141));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginCard.add(subtitleLabel);

        loginCard.add(Box.createVerticalStrut(40));

        // Username field
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        usernameLabel.setForeground(new Color(44, 62, 80));
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginCard.add(usernameLabel);

        loginCard.add(Box.createVerticalStrut(8));

        usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setPreferredSize(new Dimension(450, 40));
        usernameField.setMaximumSize(new Dimension(450, 40));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(189, 195, 199), 1),
                new EmptyBorder(5, 10, 5, 10)));
        loginCard.add(usernameField);

        loginCard.add(Box.createVerticalStrut(20));

        // Password field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        passwordLabel.setForeground(new Color(44, 62, 80));
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginCard.add(passwordLabel);

        loginCard.add(Box.createVerticalStrut(8));

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(450, 40));
        passwordField.setMaximumSize(new Dimension(450, 40));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(189, 195, 199), 1),
                new EmptyBorder(5, 10, 5, 10)));

        // Add Enter key listener
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLogin();
                }
            }
        });

        loginCard.add(passwordField);

        loginCard.add(Box.createVerticalStrut(30));

        // Login button
        loginButton = createStyledButton("🔐 Sign In", BUTTON_BG);
        loginButton.setPreferredSize(new Dimension(450, 45));
        loginButton.setMaximumSize(new Dimension(450, 45));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.addActionListener(e -> handleLogin());
        loginCard.add(loginButton);

        loginCard.add(Box.createVerticalStrut(20));

        // Info text
        JLabel infoLabel = new JLabel(
                "<html><center>Use your assigned credentials to access<br>the Billboard Management System</center></html>");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        infoLabel.setForeground(new Color(127, 140, 141));
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginCard.add(infoLabel);

        add(loginCard);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }

            public void mouseExited(MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void handleLogin() {
        String userName = usernameField.getText();
        String password = String.valueOf(passwordField.getPassword());

        if (userName.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter both username and password.",
                    "Login Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Step 1: Check system admin user account directory
        UserAccount userAccount = system.getUserAccountDirectory().authenticateUser(userName, password);

        Enterprise inEnterprise = null;
        Organization inOrganization = null;

        if (userAccount == null) {
            // Step 2: Go inside each network and check each enterprise
            for (Network network : system.getNetworkList()) {
                // Step 2.a: check against each enterprise
                for (Enterprise enterprise : network.getEnterpriseDirectory().getEnterpriseList()) {
                    userAccount = enterprise.getUserAccountDirectory().authenticateUser(userName, password);
                    if (userAccount == null) {
                        // Step 3: check against each organization for each enterprise
                        for (Organization organization : enterprise.getOrganizationDirectory().getOrganizationList()) {
                            userAccount = organization.getUserAccountDirectory().authenticateUser(userName, password);
                            if (userAccount != null) {
                                inEnterprise = enterprise;
                                inOrganization = organization;
                                break;
                            }
                        }
                    } else {
                        inEnterprise = enterprise;
                        break;
                    }
                    if (inOrganization != null) {
                        break;
                    }
                }
                if (inEnterprise != null) {
                    break;
                }
            }
        }

        if (userAccount == null) {
            JOptionPane.showMessageDialog(this,
                    "Invalid credentials. Please try again.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
            return;
        }

        // Clear fields
        usernameField.setText("");
        passwordField.setText("");

        // Enable logout button
        logoutButton.setEnabled(true);

        // Show work area
        CardLayout layout = (CardLayout) containerPanel.getLayout();
        containerPanel.add("workArea", userAccount.getRole().createWorkArea(
                containerPanel, userAccount, inOrganization, inEnterprise, system));
        layout.next(containerPanel);

        JOptionPane.showMessageDialog(this,
                "Welcome, " + userName + "!",
                "Login Successful",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void reset() {
        usernameField.setText("");
        passwordField.setText("");
    }
}
