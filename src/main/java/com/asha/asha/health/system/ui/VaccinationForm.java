package com.asha.asha.health.system.ui;

import com.asha.asha.health.system.db.DBConnection;
import com.asha.asha.health.system.db.VaccinationDAO;
import com.asha.asha.health.system.model.Vaccination;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import com.toedter.calendar.JDateChooser;

public class VaccinationForm extends JFrame {

    private int patientId;
    private JTextField txtVaccineName;
    private JDateChooser dateChooser;
    private JTable table;
    private DefaultTableModel model;

    public VaccinationForm(int patientId) {

    this.patientId = patientId;

    setTitle("Vaccinations for Patient ID: " + patientId);
    setSize(600, 400);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());

    // ===== Top Panel =====
    JPanel panel = new JPanel(new GridLayout(2,2,10,10));
    panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

    panel.add(new JLabel("Vaccine Name:"));
    txtVaccineName = new JTextField();
    panel.add(txtVaccineName);

    panel.add(new JLabel("Vaccination Date:"));
    dateChooser = new JDateChooser();
    dateChooser.setDateFormatString("yyyy-MM-dd");
    panel.add(dateChooser);

    add(panel, BorderLayout.NORTH);

    // ===== Table =====
    model = new DefaultTableModel();
    table = new JTable(model);
    table.setDefaultEditor(Object.class,null);

    model.addColumn("Vaccination ID");
    model.addColumn("Vaccine Name");
    model.addColumn("Date");

    add(new JScrollPane(table), BorderLayout.CENTER);

    // ===== Buttons =====
    JPanel btnPanel = new JPanel();
    JButton btnAdd = new JButton("Add Vaccination");
    JButton btnDelete = new JButton("Delete Vaccination");

    btnPanel.add(btnAdd);
    btnPanel.add(btnDelete);

    add(btnPanel, BorderLayout.SOUTH);

    loadVaccinations();

    btnAdd.addActionListener(e -> addVaccination());
    btnDelete.addActionListener(e -> deleteVaccination());

    setVisible(true);
}

    private void loadVaccinations() {

        model.setRowCount(0);

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(
                     "SELECT * FROM vaccinations WHERE patient_id=?")) {

            pst.setInt(1, patientId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("vaccination_id"),
                        rs.getString("vaccine_name"),
                        rs.getDate("vaccination_date")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addVaccination() {

    if (txtVaccineName.getText().isEmpty() || dateChooser.getDate() == null) {
        JOptionPane.showMessageDialog(this, "Fill all fields!");
        return;
    }

    try {

        java.util.Date utilDate = dateChooser.getDate();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

        Vaccination v = new Vaccination(
                patientId,
                txtVaccineName.getText(),
                sqlDate
        );

        if (VaccinationDAO.addVaccination(v)) {
            JOptionPane.showMessageDialog(this, "Vaccination Added!");
            loadVaccinations();
        }
        System.out.println("Opened VaccinationForm for patientId = " + patientId);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error adding vaccination!");
    }
}

    private void deleteVaccination() {

        int row = table.getSelectedRow();
        if (row == -1) return;

        int vaccinationId = (int) model.getValueAt(row, 0);

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(
                     "DELETE FROM vaccinations WHERE vaccination_id=?")) {

            pst.setInt(1, vaccinationId);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Vaccination Deleted!");
            loadVaccinations();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}