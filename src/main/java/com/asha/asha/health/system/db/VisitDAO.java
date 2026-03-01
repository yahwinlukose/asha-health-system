package com.asha.asha.health.system.db;

import com.asha.asha.health.system.model.Visit;
import java.sql.*;

public class VisitDAO {

    public static boolean addVisit(Visit visit) {

        String sql = "INSERT INTO visits (patient_id, visit_date, notes) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, visit.getPatientId());
            pst.setDate(2, visit.getVisitDate());
            pst.setString(3, visit.getNotes());

            int rows = pst.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}