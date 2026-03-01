package com.asha.asha.health.system.model;

import java.sql.Date;

public class Medicine {

    private String medicineName;
    private int quantity;
    private Date expiryDate;

    public Medicine(String medicineName, int quantity, Date expiryDate) {
        this.medicineName = medicineName;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public int getQuantity() {
        return quantity;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }
}