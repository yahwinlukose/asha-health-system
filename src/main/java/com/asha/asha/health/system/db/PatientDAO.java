package com.asha.asha.health.system.db;

import com.asha.asha.health.system.model.Patient;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.*;

public class PatientDAO {

    // Insert Patient
    public static boolean addPatient(Patient patient) {

        String sql = "INSERT INTO patients (name, age, address, phone, asha_worker_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, patient.getName());
            pst.setInt(2, patient.getAge());
            pst.setString(3, patient.getAddress());
            pst.setString(4, patient.getPhone());
            pst.setInt(5, patient.getAshaWorkerId());

            int rows = pst.executeUpdate();

            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    // View All Patients
public static void viewAllPatients() {

    String sql = "SELECT * FROM patients";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement pst = con.prepareStatement(sql);
         ResultSet rs = pst.executeQuery()) {

        while (rs.next()) {
            System.out.println(
                    rs.getInt("patient_id") + " | " +
                    rs.getString("name") + " | " +
                    rs.getInt("age") + " | " +
                    rs.getString("address") + " | " +
                    rs.getString("phone")
            );
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}
// Delete Patient by ID
public static boolean deletePatient(int patientId) {

    String sql = "DELETE FROM patients WHERE patient_id = ?";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement pst = con.prepareStatement(sql)) {

        pst.setInt(1, patientId);

        int rows = pst.executeUpdate();

        return rows > 0;

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
// Update Patient by ID
public static boolean updatePatient(int patientId, String name, int age,
                                    String address, String phone) {

    String sql = "UPDATE patients SET name=?, age=?, address=?, phone=? WHERE patient_id=?";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement pst = con.prepareStatement(sql)) {

        pst.setString(1, name);
        pst.setInt(2, age);
        pst.setString(3, address);
        pst.setString(4, phone);
        pst.setInt(5, patientId);

        int rows = pst.executeUpdate();

        return rows > 0;

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
    
}