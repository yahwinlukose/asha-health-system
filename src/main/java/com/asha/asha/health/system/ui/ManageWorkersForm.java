package com.asha.asha.health.system.ui;

import com.asha.asha.health.system.db.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ManageWorkersForm extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private JTextField txtUsername, txtPassword;
    private JButton btnAdd, btnDelete;

    public ManageWorkersForm() {

        setTitle("Manage ASHA Workers");
        setSize(600, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10,10));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // ===== TOP PANEL =====
        JPanel topPanel = new JPanel(new GridLayout(2,2,10,10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        topPanel.add(new JLabel("Username:"));
        txtUsername = new JTextField();
        topPanel.add(txtUsername);

        topPanel.add(new JLabel("Password:"));
        txtPassword = new JTextField();
        topPanel.add(txtPassword);

        add(topPanel, BorderLayout.NORTH);

        // ===== TABLE =====
        model = new DefaultTableModel();
        table = new JTable(model);
        model.addColumn("User ID");
        model.addColumn("Username");

        add(new JScrollPane(table), BorderLayout.CENTER);

        // ===== BUTTON PANEL =====
        JPanel btnPanel = new JPanel();

        btnAdd = new JButton("Add Worker");
        btnDelete = new JButton("Delete Worker");

        btnPanel.add(btnAdd);
        btnPanel.add(btnDelete);

        add(btnPanel, BorderLayout.SOUTH);

        loadWorkers();

        btnAdd.addActionListener(e -> addWorker());
        btnDelete.addActionListener(e -> deleteWorker());

        setVisible(true);
    }

    private void loadWorkers() {

        model.setRowCount(0);

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(
                     "SELECT * FROM users WHERE role='ASHA'")) {

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("user_id"),
                        rs.getString("username")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addWorker() {

        if (txtUsername.getText().isEmpty() ||
            txtPassword.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Fill all fields!");
            return;
        }

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(
                     "INSERT INTO users (username,password,role) VALUES (?,?,?)")) {

            pst.setString(1, txtUsername.getText());
            pst.setString(2, txtPassword.getText());
            pst.setString(3, "ASHA");

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Worker Added!");
            loadWorkers();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteWorker() {

        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a worker first!");
            return;
        }

        int userId = (int) model.getValueAt(row, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure?",
                "Delete Worker",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            try (Connection con = DBConnection.getConnection();
                 PreparedStatement pst = con.prepareStatement(
                         "DELETE FROM users WHERE user_id=?")) {

                pst.setInt(1, userId);
                pst.executeUpdate();

                JOptionPane.showMessageDialog(this, "Worker Deleted!");
                loadWorkers();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}