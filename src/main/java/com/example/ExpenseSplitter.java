package com.example;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExpenseSplitter {
    private static Session session;
    private static Scanner scanner = new Scanner(System.in);
    private static ExecutorService executor = Executors.newFixedThreadPool(2);

    public static void main(String[] args) {
        session = new Session();
        boolean running = true;

        while (running) {
            System.out.println("\nMenu:");
            System.out.println("1. Start Session");
            System.out.println("2. Add Expense");
            System.out.println("3. Split Expense");
            System.out.println("4. Who's Paid");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    startSession();
                    break;
                case 2:
                    addExpense();
                    break;
                case 3:
                    splitExpense();
                    break;
                case 4:
                    showPaid();
                    break;
                case 5:
                    running = false;
                    executor.shutdown();
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void startSession() {
        System.out.print("Enter number of users: ");
        int numUsers = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < numUsers; i++) {
            System.out.print("Enter user name: ");
            String name = scanner.nextLine();
            session.addUser(new User(name));
        }
        System.out.println("Session started with " + numUsers + " users.");
    }

    private static void addExpense() {
        if (session.getUsers().isEmpty()) {
            System.out.println("Start a session first.");
            return;
        }
        System.out.print("Enter payer name: ");
        String payerName = scanner.nextLine();
        User payer = findUser(payerName);
        if (payer == null) {
            System.out.println("User not found.");
            return;
        }
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter description: ");
        String desc = scanner.nextLine();

        executor.submit(() -> {
            session.addExpense(new Expense(payer, amount, desc));
            System.out.println("Expense added: " + payerName + " paid ₹" + amount + " for " + desc);
        });
    }

    private static void splitExpense() {
        if (session.getExpenses().isEmpty()) {
            System.out.println("No expenses to split.");
            return;
        }
        executor.submit(() -> {
            session.settle();
        });
    }

    private static void showPaid() {
        System.out.println("Expenses:");
        for (Expense exp : session.getExpenses()) {
            System.out.println(exp.getPayer().getName() + " paid ₹" + exp.getAmount() + " for " + exp.getDescription());
        }
    }

    private static User findUser(String name) {
        for (User user : session.getUsers()) {
            if (user.getName().equalsIgnoreCase(name)) {
                return user;
            }
        }
        return null;
    }
}