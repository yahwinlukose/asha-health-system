package com.asha.asha.health.system.ui;

import com.asha.asha.health.system.db.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginForm extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public LoginForm() {

        // Window settings
        setTitle("ASHA Worker Login");
        setSize(400, 250);
        setLocationRelativeTo(null); //To make the window open at the centre of the entire screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== Title =====
        JLabel lblTitle = new JLabel("ASHA Worker Health System", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTitle, BorderLayout.NORTH);

        // ===== Center Panel =====
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Username:"));
        txtUsername = new JTextField();
        panel.add(txtUsername);

        panel.add(new JLabel("Password:"));
        txtPassword = new JPasswordField();
        panel.add(txtPassword);

        btnLogin = new JButton("Login");
        panel.add(new JLabel()); // Empty space
        panel.add(btnLogin);

        add(panel, BorderLayout.CENTER);

        // Login button event
        btnLogin.addActionListener(e -> loginUser());

        setVisible(true);
    }

    // ================= LOGIN METHOD =================
    private void loginUser() {

        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username and password!");
            return;
        }

        try (Connection con = DBConnection.getConnection()) {

            String sql = "SELECT * FROM users WHERE username=? AND password=?";
            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, username);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {

                Session.userId = rs.getInt("user_id");
                Session.username = rs.getString("username");
                Session.role = rs.getString("role");

                JOptionPane.showMessageDialog(this, 
                    "Login Successful as " + Session.role);

                dispose();

    // 🔥 ROLE BASED REDIRECT
                if (Session.role.equals("ADMIN")) {
                    new AdminDashboard();
            } else {
                new Dashboard(); // ASHA dashboard
            }

        } else {
            JOptionPane.showMessageDialog(this, 
                "Invalid Username or Password!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error!");
        }
    }

    // Main method (Entry Point)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginForm::new);//always create UI inside Event Dispatch Thread for guaranteed safety
        
    }
}