package com.example;

import java.util.*;

public class Session {
    private List<User> users;
    private List<Expense> expenses;

    public Session() {
        this.users = new ArrayList<>();
        this.expenses = new ArrayList<>();
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void settle() {
        // Calculate total expenses
        double total = 0;
        for (Expense exp : expenses) {
            total += exp.getAmount();
        }

        int numUsers = users.size();
        double eachShare = total / numUsers;

        // Calculate balances
        Map<User, Double> balances = new HashMap<>();
        for (User user : users) {
            balances.put(user, 0.0);
        }

        for (Expense exp : expenses) {
            balances.put(exp.getPayer(), balances.get(exp.getPayer()) + exp.getAmount());
        }

        for (User user : users) {
            balances.put(user, balances.get(user) - eachShare);
        }

        // Print results
        System.out.println("Total = ₹" + String.format("%.0f", total));
        System.out.println("Each should pay = ₹" + String.format("%.0f", eachShare));
        System.out.println();
        System.out.println("Final Calculation");
        System.out.println("Person\tPaid\tShould Pay\tBalance");
        for (User user : users) {
            double paid = 0;
            for (Expense exp : expenses) {
                if (exp.getPayer().equals(user)) {
                    paid += exp.getAmount();
                }
            }
            System.out.println(user.getName() + "\t" + String.format("%.0f", paid) + "\t" + String.format("%.0f", eachShare) + "\t" + String.format("%.0f", balances.get(user)));
        }
        System.out.println();

        // Now, separate creditors and debtors
        List<User> creditors = new ArrayList<>();
        List<User> debtors = new ArrayList<>();

        for (Map.Entry<User, Double> entry : balances.entrySet()) {
            if (entry.getValue() > 0) {
                creditors.add(entry.getKey());
            } else if (entry.getValue() < 0) {
                debtors.add(entry.getKey());
            }
        }

        // Sort creditors by balance desc, debtors by balance asc (most negative first)
        creditors.sort((a, b) -> Double.compare(balances.get(b), balances.get(a)));
        debtors.sort((a, b) -> Double.compare(balances.get(a), balances.get(b))); // asc, since negative, smaller (more negative) first

        // Now, settle
        List<String> settlements = new ArrayList<>();
        int i = 0, j = 0;
        while (i < debtors.size() && j < creditors.size()) {
            User debtor = debtors.get(i);
            User creditor = creditors.get(j);

            double debt = -balances.get(debtor);
            double credit = balances.get(creditor);

            double amount = Math.min(debt, credit);

            settlements.add(debtor.getName() + " pays ₹" + String.format("%.0f", amount) + " to " + creditor.getName());

            balances.put(debtor, balances.get(debtor) + amount);
            balances.put(creditor, balances.get(creditor) - amount);

            if (Math.abs(balances.get(debtor)) < 0.01) {
                i++;
            }
            if (Math.abs(balances.get(creditor)) < 0.01) {
                j++;
            }
        }

        System.out.println("Final Result");
        for (String s : settlements) {
            System.out.println("👉 " + s);
        }
    }
}