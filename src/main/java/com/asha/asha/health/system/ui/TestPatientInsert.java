package com.asha.asha.health.system.ui;

import com.asha.asha.health.system.db.PatientDAO;
import com.asha.asha.health.system.model.Patient;

public class TestPatientInsert {

    public static void main(String[] args) {

        Patient p = new Patient(
                "Anitha",
                28,
                "Calicut",
                "9876543210",
                1   // asha_worker_id (make sure 1 exists in users table)
        );

        boolean success = PatientDAO.addPatient(p);

        if (success) {
            System.out.println("Patient Inserted Successfully!");
        } else {
            System.out.println("Patient Insert Failed!");
        }
    }
}