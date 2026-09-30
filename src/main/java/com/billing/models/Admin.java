package com.billing.models;

import com.billing.models.*;
import com.billing.services.*;

import java.util.List;
import java.util.Scanner;
import java.io.IOException;

public class Admin {
    private static String username = "youssef";
    private static String password = "1234";
    private int adminCode;

    public Admin(String username, String password, int adminCode) {
        Admin.username = username;
        Admin.password = password;
        this.adminCode = adminCode;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        Admin.username = username;
    }

    public int getAdminCode() {
        return adminCode;
    }

    public void setAdminCode(int adminCode) {
        this.adminCode = adminCode;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        Admin.password = password;
    }

    public static boolean login(Scanner scanner) {
        System.out.print("Enter admin username: ");
        String inputUsername = scanner.nextLine();
        System.out.print("Enter admin password: ");
        String inputPassword = scanner.nextLine();

        if (inputUsername.equals(username) && inputPassword.equals(password)) {
            System.out.println("Login successful!");
            return true;
        } else {
            System.out.println("Authentication failed. Incorrect username or password.");
            return false;
        }
    }

    public String viewCustomerReports(List<Customer> customers) {
        StringBuilder report = new StringBuilder("\n--- Customer Reports ---\n");
        List<Customer> loadedCustomers = Files.loadCustomerData();
        if (loadedCustomers.isEmpty()) {
            report.append("No customers available.\n");
        } else {
            for (Customer customer : loadedCustomers) {
                report.append(String.format("Name: %s\n", customer.getName()));
                report.append(String.format("Phone Number: %s\n", customer.getPhoneNumber()));
                report.append(String.format("Address: %s\n", customer.getAddress()));
                report.append("***************************************\n");
            }
        }
        System.out.println(report.toString());
        return report.toString();
    }

    public void addPhoneToInventory(Scanner scanner, Store store) {
        System.out.print("Enter Phone Brand: ");
        String brand = scanner.nextLine();

        System.out.print("Enter Phone Model: ");
        String model = scanner.nextLine();

        double price;
        do {
            System.out.print("Enter Phone Price: ");
            String priceInput = scanner.nextLine();
            if (isValidDouble(priceInput)) {
                price = Double.parseDouble(priceInput);
                break;
            } else {
                System.out.println("Invalid price. Please enter a valid numeric value.");
            }
        } while (true);

        int stock;
        do {
            System.out.print("Enter Stock Quantity: ");
            String stockInput = scanner.nextLine();
            if (isValidInteger(stockInput)) {
                stock = Integer.parseInt(stockInput);
                break;
            } else {
                System.out.println("Invalid stock quantity. Please enter a valid integer value.");
            }
        } while (true);

        Phone newPhone = new Phone(brand, model, price, stock);
        store.addPhoneToInventory(newPhone);
        
        // Save the updated inventory to file
        List<Phone> updatedInventory = store.getInventory();
        Files.savePhoneData(updatedInventory);
        System.out.println("Phone added successfully and saved to file!");
    }

    public void updatePhoneStock(Scanner scanner, Store store) {
        System.out.print("Enter Phone Model to Update: ");
        String model = scanner.nextLine();

        int newStock;
        do {
            System.out.print("Enter New Stock Quantity: ");
            String stockInput = scanner.nextLine();
            if (isValidInteger(stockInput)) {
                newStock = Integer.parseInt(stockInput);
                break;
            } else {
                System.out.println("Invalid stock quantity. Please enter a valid integer value.");
            }
        } while (true);

        List<Phone> inventory = store.getInventory();
        for (Phone phone : inventory) {
            if (phone.getModel().equalsIgnoreCase(model)) {
                phone.setStock(newStock);
                
                // Save the updated inventory to file
                Files.savePhoneData(inventory);
                System.out.println("Stock updated successfully and saved to file!");
                return;
            }
        }
        System.out.println("Phone model not found.");
    }

    public void viewInventory(Store store) {
        System.out.println("\n--- Inventory ---");
        List<Phone> inventory = Files.loadPhoneData();
        if (inventory.isEmpty()) {
            System.out.println("No phones in inventory.");
        } else {
            for (Phone phone : inventory) {
                System.out.println(phone);
            }
        }
    }

    private boolean isValidDouble(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean login(String inputUsername, String inputPassword) {
        return username.equals(inputUsername) && password.equals(inputPassword);
    }
}
