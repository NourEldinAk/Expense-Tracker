package com.noekatsu.expensetracker;

public class ExpenseModel {
    private String type;
    private int amount;
    private String date;

    public ExpenseModel(String type, int amount, String date) {
        this.type = type;
        this.amount = amount;
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
