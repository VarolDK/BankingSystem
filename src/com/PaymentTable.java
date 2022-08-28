package com;

public class PaymentTable {
    String IBAN, currency;
    double amount;

    public PaymentTable(String IBAN, String currency, double amount) {
        this.IBAN = IBAN;
        this.currency = currency;
        this.amount = amount;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
