package com.asha.asha.health.system.model;

import java.sql.Date;

public class Vaccination {

    private int patientId;
    private String vaccineName;
    private Date vaccinationDate;

    public Vaccination(int patientId, String vaccineName, Date vaccinationDate) {
        this.patientId = patientId;
        this.vaccineName = vaccineName;
        this.vaccinationDate = vaccinationDate;
    }

    public int getPatientId() {
        return patientId;
    }

    public String getVaccineName() {
        return vaccineName;
    }

    public Date getVaccinationDate() {
        return vaccinationDate;
    }
}