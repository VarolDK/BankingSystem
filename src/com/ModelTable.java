package com;

public class ModelTable {
    String senderName, receiverIBAN, amount, T_date;

    public ModelTable(String senderName, String receiverIBAN, String amount, String T_date) {
        this.senderName = senderName;
        this.receiverIBAN = receiverIBAN;
        this.amount = amount;
        this.T_date = T_date;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverIBAN() {
        return receiverIBAN;
    }

    public void setReceiverIBAN(String receiverIBAN) {
        this.receiverIBAN = receiverIBAN;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getT_date() {
        return T_date;
    }

    public void setT_date(String t_date) {
        T_date = t_date;
    }


}
