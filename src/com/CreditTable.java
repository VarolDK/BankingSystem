package com;

import java.util.Date;

public class CreditTable {
    double amount, lateFee;
    Date date;

    public CreditTable(double amount, double lateFee, Date date) {
        this.amount = amount;
        this.lateFee = lateFee;
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getLateFee() {
        return lateFee;
    }

    public void setLateFee(double lateFee) {
        this.lateFee = lateFee;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
