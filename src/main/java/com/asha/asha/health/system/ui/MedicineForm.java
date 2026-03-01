package com.asha.asha.health.system.ui;

import com.asha.asha.health.system.db.DBConnection;
import com.asha.asha.health.system.db.MedicineDAO;
import com.asha.asha.health.system.model.Medicine;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class MedicineForm extends JFrame {

    private JTextField txtName, txtQuantity;
    private JDateChooser dateChooser;
    private JTable table;
    private DefaultTableModel model;

    public MedicineForm() {

        setTitle("Medicine Stock");
        setSize(650, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ===== Top Panel =====
        JPanel panel = new JPanel(new GridLayout(3,2,10,10));
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        panel.add(new JLabel("Medicine Name:"));
        txtName = new JTextField();
        panel.add(txtName);

        panel.add(new JLabel("Quantity:"));
        txtQuantity = new JTextField();
        panel.add(txtQuantity);

        panel.add(new JLabel("Expiry Date:"));
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");
        panel.add(dateChooser);

        add(panel, BorderLayout.NORTH);

        // ===== Table =====
        model = new DefaultTableModel();
        table = new JTable(model);
        table.setDefaultEditor(Object.class,null);
        table.setAutoCreateRowSorter(true);

        model.addColumn("ID");
        model.addColumn("Medicine Name");
        model.addColumn("Quantity");
        model.addColumn("Expiry Date");

        add(new JScrollPane(table), BorderLayout.CENTER);

        // ===== Buttons =====
        JPanel btnPanel = new JPanel();
        JButton btnAdd = new JButton("Add");
        JButton btnDelete = new JButton("Delete");

        btnPanel.add(btnAdd);
        btnPanel.add(btnDelete);

        add(btnPanel, BorderLayout.SOUTH);

        loadMedicines();

        btnAdd.addActionListener(e -> addMedicine());
        btnDelete.addActionListener(e -> deleteMedicine());

        setVisible(true);
    }

    private void loadMedicines() {

        model.setRowCount(0);

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement("SELECT * FROM medicines");
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("medicine_id"),
                        rs.getString("medicine_name"),
                        rs.getInt("quantity"),
                        rs.getDate("expiry_date")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addMedicine() {

        if (txtName.getText().isEmpty() ||
            txtQuantity.getText().isEmpty() ||
            dateChooser.getDate() == null) {

            JOptionPane.showMessageDialog(this, "Fill all fields!");
            return;
        }

        try {
            int quantity = Integer.parseInt(txtQuantity.getText());
            java.sql.Date date = new java.sql.Date(dateChooser.getDate().getTime());

            Medicine m = new Medicine(
                    txtName.getText(),
                    quantity,
                    date
            );

            if (MedicineDAO.addMedicine(m)) {
                JOptionPane.showMessageDialog(this, "Medicine Added!");
                loadMedicines();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid Quantity!");
        }
    }

    private void deleteMedicine() {

        int row = table.getSelectedRow();
        if (row == -1) return;

        int id = (int) model.getValueAt(row, 0);

        if (MedicineDAO.deleteMedicine(id)) {
            JOptionPane.showMessageDialog(this, "Medicine Deleted!");
            loadMedicines();
        }
    }
}