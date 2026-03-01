package com.asha.asha.health.system.ui;

import com.asha.asha.health.system.db.DBConnection;
import com.asha.asha.health.system.db.VisitDAO;
import com.asha.asha.health.system.model.Visit;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class VisitForm extends JFrame {

    private int patientId;
    private JTextField txtDate, txtNotes;
    private JTable table;
    private DefaultTableModel model;

    public VisitForm(int patientId) {

        this.patientId = patientId;

        setTitle("Visits for Patient ID: " + patientId);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ===== Top Panel =====
        JPanel panel = new JPanel(new GridLayout(2,2,10,10));
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        panel.add(new JLabel("Visit Date (YYYY-MM-DD):"));
        txtDate = new JTextField();
        panel.add(txtDate);

        panel.add(new JLabel("Notes:"));
        txtNotes = new JTextField();
        panel.add(txtNotes);

        add(panel, BorderLayout.NORTH);

        // ===== Table =====
        model = new DefaultTableModel();
        table = new JTable(model);
        table.setDefaultEditor(Object.class,null);

        model.addColumn("Visit ID");
        model.addColumn("Date");
        model.addColumn("Notes");

        add(new JScrollPane(table), BorderLayout.CENTER);

        // ===== Buttons =====
        JPanel btnPanel = new JPanel();
        JButton btnAdd = new JButton("Add Visit");
        JButton btnDelete = new JButton("Delete Visit");

        btnPanel.add(btnAdd);
        btnPanel.add(btnDelete);

        add(btnPanel, BorderLayout.SOUTH);

        loadVisits();

        btnAdd.addActionListener(e -> addVisit());
        btnDelete.addActionListener(e -> deleteVisit());

        setVisible(true);
    }

    private void loadVisits() {

        model.setRowCount(0);

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(
                     "SELECT * FROM visits WHERE patient_id=?")) {

            pst.setInt(1, patientId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("visit_id"),
                        rs.getDate("visit_date"),
                        rs.getString("notes")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addVisit() {

        try {
            Date date = Date.valueOf(txtDate.getText());

            Visit visit = new Visit(
                    patientId,
                    date,
                    txtNotes.getText()
            );

            if (VisitDAO.addVisit(visit)) {
                JOptionPane.showMessageDialog(this, "Visit Added!");
                loadVisits();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid Date Format!");
        }
    }

    private void deleteVisit() {

        int row = table.getSelectedRow();
        if (row == -1) return;

        int visitId = (int) model.getValueAt(row, 0);

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(
                     "DELETE FROM visits WHERE visit_id=?")) {

            pst.setInt(1, visitId);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Visit Deleted!");
            loadVisits();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}