package com.asha.asha.health.system.db;

import com.asha.asha.health.system.model.Medicine;
import java.sql.*;

public class MedicineDAO {

    public static boolean addMedicine(Medicine m) {

        String sql = "INSERT INTO medicines (medicine_name, quantity, expiry_date) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, m.getMedicineName());
            pst.setInt(2, m.getQuantity());
            pst.setDate(3, m.getExpiryDate());

            return pst.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteMedicine(int id) {

        String sql = "DELETE FROM medicines WHERE medicine_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, id);
            return pst.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}