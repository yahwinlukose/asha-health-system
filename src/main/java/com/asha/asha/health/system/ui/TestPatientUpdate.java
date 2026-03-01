package com.asha.asha.health.system.ui;

import com.asha.asha.health.system.db.PatientDAO;

public class TestPatientUpdate {

    public static void main(String[] args) {

        boolean success = PatientDAO.updatePatient(
                2,
                "Anitha Updated",
                30,
                "Kozhikode",
                "9999999999"
        );

        if (success) {
            System.out.println("Patient Updated Successfully!");
        } else {
            System.out.println("Update Failed!");
        }
    }
}