package com.asha.asha.health.system.ui;

import com.asha.asha.health.system.db.VisitDAO;
import com.asha.asha.health.system.model.Visit;
import java.sql.Date;

public class TestVisitInsert {

    public static void main(String[] args) {

        Visit visit = new Visit(
                3, // existing patient_id
                Date.valueOf("2026-02-28"),
                "Routine checkup"
        );

        boolean success = VisitDAO.addVisit(visit);

        if (success) {
            System.out.println("Visit inserted successfully!");
        } else {
            System.out.println("Insert failed!");
        }
    }
}