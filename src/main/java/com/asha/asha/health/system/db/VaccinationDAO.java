package com.asha.asha.health.system.db;

import com.asha.asha.health.system.model.Vaccination;
import java.sql.*;

public class VaccinationDAO {

    public static boolean addVaccination(Vaccination v) {

        String sql = "INSERT INTO vaccinations (patient_id, vaccine_name, vaccination_date) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, v.getPatientId());
            pst.setString(2, v.getVaccineName());
            pst.setDate(3, v.getVaccinationDate());

            return pst.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}