package com.billing.models;

import com.billing.models.*;
import com.billing.services.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Customer {
    private String name;
    private String phoneNumber;
    private String password;
    private String address;
    private List<Bill> bills;

    public Customer(String name, String phoneNumber, String address, String password) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.password = password;
        this.bills = new ArrayList<>();
    }

   
    public  String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

   
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
    
    
    public String getPassword() {
        return password; 
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void displayCustomerDetails() {
        System.out.println("Customer Details:");
        System.out.println("Name: " + name);
        System.out.println("Phone Number: " + phoneNumber);
        System.out.println("Address: " + address+"\n");
        System.out.println("***************************************" );
    }

   
    public void viewBills() {
        System.out.println("\n--- Your Bills ---");
        if (bills.isEmpty()) {
            System.out.println("No bills available.");
        } else {
            for (int i = 0; i < bills.size(); i++) {
                System.out.println((i + 1) + ". " + bills.get(i));
            }
        }
    }

 
    public void addBill(Bill bill) {
        bills.add(bill);
        System.out.println("Bill added successfully: " + bill);
    }

   
    public static Customer existingUserLogin(Scanner scanner, Store store) {
        System.out.print("Enter phone number: ");
        String phoneNumber = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        Customer customer = store.loginOrCreateCustomer(phoneNumber, password);
        if (customer != null) {
            System.out.println("Login successful. Welcome, " + customer.getName());
            return customer;
        } else {
            System.out.println("Login failed. Please check your credentials.");
            return null;
        }
    }

  
    public static Customer newUserRegistration(String name, String phoneNumber, String address, String password, Store store) {
        try {
            // Validate name
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Name cannot be empty");
            }

            // Validate phone number
            if (!isValidPhoneNumber(phoneNumber)) {
                throw new IllegalArgumentException("Invalid phone number. Please enter exactly 11 digits");
            }

            // Check if phone number already exists in store
            if (store != null && store.isPhoneNumberRegistered(phoneNumber)) {
                throw new IllegalArgumentException("This phone number is already registered");
            }

            // Validate address
            if (address == null || address.trim().isEmpty()) {
                throw new IllegalArgumentException("Address cannot be empty");
            }

            // Validate password
            if (password == null || password.trim().isEmpty()) {
                throw new IllegalArgumentException("Password cannot be empty");
            }

            // Create new customer with validated data
            return new Customer(name.trim(), phoneNumber.trim(), address.trim(), password.trim());

        } catch (Exception e) {
            throw new RuntimeException("Registration failed: " + e.getMessage(), e);
        }
    }

    private static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches("\\d{11}"); // Exactly 11 digits
    }

    public static Customer existingUserLogin(String phoneNumber, String password, Store store) {
        // Validate the input
        if (phoneNumber == null || phoneNumber.isEmpty() || password == null || password.isEmpty()) {
            System.out.println("Phone number and password must not be empty.");
            return null; // Invalid input
        }

        // Retrieve the list of customers from the store
        List<Customer> customers = store.getCustomers();

        // Iterate through the list to find a matching customer
        for (Customer customer : customers) {
            if (customer.getPhoneNumber().equals(phoneNumber) && customer.getPassword().equals(password)) {
                return customer; // Return the customer if credentials match
            }
        }

        // If no match is found, return null
        System.out.println("Invalid phone number or password.");
        return null;
    }

    // Add this method to validate phone number format
    public static boolean validatePhoneNumberFormat(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches("\\d{11}");
    }

}
