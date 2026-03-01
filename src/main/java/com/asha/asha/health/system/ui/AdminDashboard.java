package com.asha.asha.health.system.ui;

import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {

    public AdminDashboard() {

        setTitle("Admin Dashboard");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("ADMIN DASHBOARD", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        add(lblTitle, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(3,1,15,15));
        panel.setBorder(BorderFactory.createEmptyBorder(20,80,20,80));

        JButton btnManageWorkers = new JButton("Manage ASHA Workers");
        JButton btnViewAllPatients = new JButton("View All Patients");
        JButton btnLogout = new JButton("Logout");

        panel.add(btnManageWorkers);
        panel.add(btnViewAllPatients);
        panel.add(btnLogout);

        add(panel, BorderLayout.CENTER);

        btnManageWorkers.addActionListener(e -> new ManageWorkersForm());
        btnViewAllPatients.addActionListener(e -> new AdminPatientView());

        btnLogout.addActionListener(e -> {
            Session.clear();
            dispose();
            new LoginForm();
        });

        setVisible(true);
    }
}