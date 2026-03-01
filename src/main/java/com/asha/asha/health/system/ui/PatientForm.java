package com.asha.asha.health.system.ui;

import com.asha.asha.health.system.db.PatientDAO;
import com.asha.asha.health.system.model.Patient;
import com.asha.asha.health.system.db.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;

public class PatientForm extends JFrame {

    private JTextField txtName, txtAge, txtAddress, txtPhone;
    private JButton btnAdd, btnUpdate, btnDelete;
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtSearch;
    private JButton btnSearch;

    public PatientForm() {

    setTitle("Manage Patients");
    setSize(750, 550);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLayout(new BorderLayout(10,10));

    // ===== TITLE =====
    JLabel lblTitle = new JLabel("Manage Patients", JLabel.CENTER);
    lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
    add(lblTitle, BorderLayout.NORTH);

    // ===== MAIN PANEL =====
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout(10,10));
    mainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

    // ===== SEARCH PANEL =====
    JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    searchPanel.add(new JLabel("Search by Name:"));
    txtSearch = new JTextField(15);
    btnSearch = new JButton("Search");
    searchPanel.add(txtSearch);
    searchPanel.add(btnSearch);

    mainPanel.add(searchPanel, BorderLayout.NORTH);

    // ===== FORM PANEL =====
    JPanel formPanel = new JPanel(new GridLayout(4,2,10,10));

    formPanel.add(new JLabel("Name:"));
    txtName = new JTextField();
    formPanel.add(txtName);

    formPanel.add(new JLabel("Age:"));
    txtAge = new JTextField();
    formPanel.add(txtAge);

    formPanel.add(new JLabel("Address:"));
    txtAddress = new JTextField();
    formPanel.add(txtAddress);

    formPanel.add(new JLabel("Phone:"));
    txtPhone = new JTextField();
    formPanel.add(txtPhone);

    mainPanel.add(formPanel, BorderLayout.CENTER);

    // ===== BUTTON PANEL =====
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,20,10));
    btnAdd = new JButton("Add");
    btnUpdate = new JButton("Update");
    btnDelete = new JButton("Delete");

    JButton btnViewVisits = new JButton("View Visits");

    buttonPanel.add(btnAdd);
    buttonPanel.add(btnUpdate);
    buttonPanel.add(btnDelete);
    buttonPanel.add(btnViewVisits);
    JButton btnViewVaccinations = new JButton("View Vaccinations");
    buttonPanel.add(btnViewVaccinations);
    

    mainPanel.add(buttonPanel, BorderLayout.SOUTH);

    add(mainPanel, BorderLayout.WEST);

    // ===== TABLE =====
    model = new DefaultTableModel();
    table = new JTable(model);
    table.setAutoCreateRowSorter(true);
    table.setDefaultEditor(Object.class,null);

    model.addColumn("ID");
    model.addColumn("Name");
    model.addColumn("Age");
    model.addColumn("Address");
    model.addColumn("Phone");

    JScrollPane scrollPane = new JScrollPane(table);
    add(scrollPane, BorderLayout.CENTER);

    loadPatients();

    // ===== BUTTON ACTIONS =====
    btnAdd.addActionListener(e -> addPatient());
    btnUpdate.addActionListener(e -> updatePatient());
    btnDelete.addActionListener(e -> deletePatient());
    btnSearch.addActionListener(e -> searchPatient());
    btnViewVisits.addActionListener(e -> openVisitForm());
    btnViewVaccinations.addActionListener(e -> openVaccinationForm());

    // ===== TABLE CLICK =====
    table.addMouseListener(new MouseAdapter() {
    public void mouseClicked(MouseEvent e) {

        int selectedRow = table.getSelectedRow();

        if (selectedRow != -1) {

            int modelRow = table.convertRowIndexToModel(selectedRow);

            txtName.setText(model.getValueAt(modelRow, 1).toString());
            txtAge.setText(model.getValueAt(modelRow, 2).toString());
            txtAddress.setText(model.getValueAt(modelRow, 3).toString());
            txtPhone.setText(model.getValueAt(modelRow, 4).toString());
        }
    }
});

    setVisible(true);
}
    // ================= LOAD DATA =================
  // ================= LOAD DATA =================
private void loadPatients() {

    model.setRowCount(0);

    try (Connection con = DBConnection.getConnection();
         PreparedStatement pst = con.prepareStatement(
                 "SELECT * FROM patients WHERE asha_worker_id = ?")) {

        pst.setInt(1, Session.userId);

        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            model.addRow(new Object[]{
                    rs.getInt("patient_id"),
                    rs.getString("name"),
                    rs.getInt("age"),
                    rs.getString("address"),
                    rs.getString("phone")
            });
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}
    private void searchPatient() {

    String keyword = txtSearch.getText().trim();

    TableRowSorter<DefaultTableModel> sorter =
            new TableRowSorter<>(model);

    table.setRowSorter(sorter);

    if (keyword.isEmpty()) {
        sorter.setRowFilter(null);
        return;
    }

    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + keyword, 1));

    if (table.getRowCount() == 0) {
        JOptionPane.showMessageDialog(this, "Patient not found!");
        sorter.setRowFilter(null); // reset filter
    }
}

    // ================= ADD =================
    private void addPatient() {

        // Empty validation
        if (txtName.getText().isEmpty() ||
            txtAge.getText().isEmpty() ||
            txtAddress.getText().isEmpty() ||
            txtPhone.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        // Age numeric validation
        int age;
        try {
            age = Integer.parseInt(txtAge.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Age must be a number!");
            return;
        }

       Patient p = new Patient(
            txtName.getText(),
            age,
            txtAddress.getText(),
            txtPhone.getText(),
            Session.userId
        );

        if (PatientDAO.addPatient(p)) {
            JOptionPane.showMessageDialog(this, "Patient Added!");
            loadPatients();
            clearFields();
        }
    }

    // ================= UPDATE =================
  private void updatePatient() {

    int selectedRow = table.getSelectedRow();

    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a patient to update!");
        return;
    }

    int modelRow = table.convertRowIndexToModel(selectedRow);
    int id = (int) model.getValueAt(modelRow, 0);

    if (txtName.getText().isEmpty() ||
        txtAge.getText().isEmpty() ||
        txtAddress.getText().isEmpty() ||
        txtPhone.getText().isEmpty()) {

        JOptionPane.showMessageDialog(this, "Please fill all fields!");
        return;
    }

    int age;
    try {
        age = Integer.parseInt(txtAge.getText());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Age must be a number!");
        return;
    }

    boolean success = PatientDAO.updatePatient(
            id,
            txtName.getText(),
            age,
            txtAddress.getText(),
            txtPhone.getText()
    );

    if (success) {
        JOptionPane.showMessageDialog(this, "Patient Updated!");
        loadPatients();
        clearFields();
    }
}

    // ================= DELETE =================
    private void deletePatient() {

    int selectedRow = table.getSelectedRow();

    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a patient to delete!");
        return;
    }

    int modelRow = table.convertRowIndexToModel(selectedRow);
    int id = (int) model.getValueAt(modelRow, 0);

    int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
    );

    if (confirm == JOptionPane.YES_OPTION) {

        if (PatientDAO.deletePatient(id)) {
            JOptionPane.showMessageDialog(this, "Patient Deleted!");
            loadPatients();
            clearFields();
        }
    }
}

    private void clearFields() {
        txtName.setText("");
        txtAge.setText("");
        txtAddress.setText("");
        txtPhone.setText("");
    }
    private void openVisitForm() {

    int selectedRow = table.getSelectedRow();

    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a patient first!");
        return;
    }

    int patientId = (int) model.getValueAt(selectedRow, 0);

    new VisitForm(patientId);
    }
    private void openVaccinationForm() {

    int selectedRow = table.getSelectedRow();

    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a patient first!");
        return;
    }

    int patientId = (int) model.getValueAt(selectedRow, 0);

    new VaccinationForm(patientId);
}
}