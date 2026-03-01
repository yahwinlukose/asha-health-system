package com.asha.asha.health.system.model;

import java.sql.Date;

public class Visit {

    private int patientId;
    private Date visitDate;
    private String notes;

    public Visit(int patientId, Date visitDate, String notes) {
        this.patientId = patientId;
        this.visitDate = visitDate;
        this.notes = notes;
    }

    public int getPatientId() {
        return patientId;
    }

    public Date getVisitDate() {
        return visitDate;
    }

    public String getNotes() {
        return notes;
    }
}