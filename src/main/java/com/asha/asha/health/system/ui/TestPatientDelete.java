package com.asha.asha.health.system.ui;

import com.asha.asha.health.system.db.PatientDAO;

public class TestPatientDelete {

    public static void main(String[] args) {

        boolean success = PatientDAO.deletePatient(1);

        if (success) {
            System.out.println("Patient Deleted Successfully!");
        } else {
            System.out.println("Delete Failed!");
        }
    }
}