package com.asha.asha.health.system.ui;

import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame {

    private JButton btnPatients;
    private JButton btnMedicine;
    private JButton btnLogout;

    public Dashboard() {

        setTitle("ASHA Worker Dashboard");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== Title (Shows Logged-in User) =====
        JLabel lblTitle = new JLabel("Welcome, " + Session.username, JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(lblTitle, BorderLayout.NORTH);

        // ===== Center Panel =====
        JPanel panel = new JPanel(new GridLayout(3, 1, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 80, 20, 80));

        btnPatients = new JButton("Manage Patients");
        btnMedicine = new JButton("Medicine Stock");
        btnLogout = new JButton("Logout");

        panel.add(btnPatients);
        panel.add(btnMedicine);
        panel.add(btnLogout);

        add(panel, BorderLayout.CENTER);

        // ===== Button Actions =====

        // Open Patient Management
        btnPatients.addActionListener(e -> new PatientForm());

        // Open Medicine Module
        btnMedicine.addActionListener(e -> new MedicineForm());

        // Logout with Session Clear
        btnLogout.addActionListener(e -> {

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to logout?",
                    "Logout Confirmation",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {

                Session.clear();   // 🔥 Clear session
                dispose();         // Close dashboard
                new LoginForm();   // Go back to login
            }
        });

        setVisible(true);
    }
}