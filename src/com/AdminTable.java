package com;

import java.util.Date;

public class AdminTable {
    String TC;
    double amount,amountInt;
    int creditMonth,paymentDate,creditID;
    Date creditDate;


    public AdminTable(String TC, double amount, double amountInt, int creditMonth, int paymentDate, Date creditDate,int creditID) {
        this.TC = TC;
        this.amount = amount;
        this.amountInt = amountInt;
        this.creditMonth = creditMonth;
        this.paymentDate = paymentDate;
        this.creditDate = creditDate;
        this.creditID = creditID;
    }

    public int getCreditID() {
        return creditID;
    }

    public void setCreditID(int creditID) {
        this.creditID = creditID;
    }

    public int getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(int paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getTC() {
        return TC;
    }

    public void setTC(String TC) {
        this.TC = TC;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmountInt() {
        return amountInt;
    }

    public void setAmountInt(double amountInt) {
        this.amountInt = amountInt;
    }

    public int getCreditMonth() {
        return creditMonth;
    }

    public void setCreditMonth(int creditMonth) {
        this.creditMonth = creditMonth;
    }

    public Date getCreditDate() {
        return creditDate;
    }

    public void setCreditDate(Date creditDate) {
        this.creditDate = creditDate;
    }
}
