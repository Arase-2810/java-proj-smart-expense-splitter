package com.example;

public class Expense {
    private User payer;
    private double amount;
    private String description;

    public Expense(User payer, double amount, String description) {
        this.payer = payer;
        this.amount = amount;
        this.description = description;
    }

    public User getPayer() {
        return payer;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }
}