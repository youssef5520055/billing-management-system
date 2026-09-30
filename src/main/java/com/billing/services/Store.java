package com.billing.services;

import com.billing.models.*;
import com.billing.services.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Store {
    List<Phone> inventory;
    protected List<Bill> bills; 
    protected Admin admin;
    private List<Customer> customers;
    protected List<Order> orders; 

    public Store(Admin admin) {
        this.admin = admin;
        // Initialize file system
        Files.initializeFileSystem();
        // Load data from files
        this.inventory = Files.loadPhoneData();
        this.customers = Files.loadCustomerData();
        this.bills = Files.loadBillsData();
        this.orders = new ArrayList<>();
    }
    
    public List<Phone> getInventory() {
        return inventory; 
    }
    
    public List<Customer> getCustomers() {
        return customers;
    }
    

    public List<Phone> displayAvailablePhones() {
        System.out.println("Available Phones:");
        if (inventory.isEmpty()) {
            System.out.println("No phones available.");
        } else {
            for (int i = 0; i < inventory.size(); i++) {
                System.out.println(i + 1 + ": " + inventory.get(i));
            }
        }
		return null;
    }

    public void choosePhoneAndOrder(Customer customer) {
        if (inventory.isEmpty()) {
            System.out.println("No phones available to order.");
            return; 
        }

        displayAvailablePhones(); 
        Scanner input = new Scanner(System.in);
        int index = -1; 

        
        while (true) {
            System.out.print("Select the phone index to order (1 to " + inventory.size() + "): ");

            
            if (input.hasNextInt()) {
                index = input.nextInt();

               
                if (index > 0 && index <= inventory.size()) {
                    break; 
                } else {
                    System.out.println("Invalid index. Please enter a number between 1 and " + inventory.size() + ".");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                input.next(); 
            }
        }

        
        Phone selectedPhone = inventory.get(index - 1); 
        placeOrder(customer, selectedPhone);
    }

    public Customer loginOrCreateCustomer(String phoneNumber, String password) {
        for (Customer customer : customers) {
            if (customer.getPhoneNumber().equals(phoneNumber)) {
                if (customer.checkPassword(password)) {
                    System.out.println("Welcome back, " + customer.getName() + "!");
                    return customer; 
                } else {
                    System.out.println("Incorrect password.");
                    return null; 
                }
            }
        }

        Scanner input = new Scanner(System.in);
        System.out.print("Customer not found. Please enter your name: ");
        String name = input.nextLine();
        System.out.print("Please enter your address: ");
        String address = input.nextLine();
        
        Customer newCustomer = new Customer(name, phoneNumber, address, password);
        customers.add(newCustomer); 
        System.out.println("New customer created: " + newCustomer.getName());
        return newCustomer; 
    }

    public void addPhoneToInventory(Phone phone) {
        inventory.add(phone);
        Files.savePhoneData(inventory);
        System.out.println("Added to inventory: " + phone);
    }

    public String viewInventory() {
        System.out.println("Current Inventory:");
        if (inventory.isEmpty()) {
            System.out.println("No phones in inventory.");
        } else {
            for (Phone phone : inventory) {
                System.out.println(phone);
            }
        }
		return null;
    }

    public void addBill(Bill bill) {
        if (!isDuplicateBill(bill)) {
            bills.add(bill);
            Files.saveBillsData(bills);
            System.out.println("Bill added: " + bill);
        } else {
            System.out.println("Duplicate bill - not added");
        }
    }

    private boolean isDuplicateBill(Bill newBill) {
        return bills.stream().anyMatch(existingBill -> 
            existingBill.getCustomerName().equals(newBill.getCustomerName()) &&
            existingBill.getPhoneNumber().equals(newBill.getPhoneNumber()) &&
            Math.abs(existingBill.getAmount() - newBill.getAmount()) < 0.01 &&
            existingBill.getPaymentMethod() != null &&
            existingBill.getPaymentMethod().equals(newBill.getPaymentMethod())
        );
    }

    public String viewBills() {
        System.out.println("Current Bills:");
        if (bills.isEmpty()) {
            System.out.println("No bills available.");
        } else {
            for (Bill bill : bills) {
                System.out.println(bill);
            }
        }
		return null;
    }

    public void placeOrder(Customer customer, Phone phone) {
        if (phone.getStock() > 0) {
            Order order = new Order(customer.getName(), phone);
            orders.add(order);
            phone.setStock(phone.getStock() - 1); 
            
            Files.savePhoneData(inventory);
            System.out.println("Order placed: " + order);
        } else {
            System.out.println("Sorry, " + phone.getModel() + " is out of stock.");
        }
    }

    public void viewOrders() {
        System.out.println("Current Orders:");
        if (orders.isEmpty()) {
            System.out.println("No orders placed.");
        } else {
            for (Order order : orders) {
                System.out.println(order);
            }
        }
    }

    public void updatePhoneStock(List<Phone> inventory2, String model, int newStock) {
        for (Phone phone : inventory) {
            if (phone.getModel().equalsIgnoreCase(model)) {
                phone.setStock(newStock);
                System.out.println("Updated stock for " + model + " to " + newStock);
                return;
            }
        }
        System.out.println("Phone model not found in inventory.");
    }

    public void addCustomer(Customer customer) {
        if (customer != null && !isPhoneNumberRegistered(customer.getPhoneNumber())) {
            customers.add(customer);
            Files.saveCustomerData(customers);
            System.out.println("Customer added successfully: " + customer.getName());
        }
    }

    public boolean isPhoneNumberRegistered(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return false;
        }
        return customers.stream()
                .anyMatch(c -> phoneNumber.equals(c.getPhoneNumber()));
    }

    public List<Bill> getBills() {
        return bills;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void updateCustomer(Customer customer) {
        if (customer != null) {
            for (int i = 0; i < customers.size(); i++) {
                if (customers.get(i).getPhoneNumber().equals(customer.getPhoneNumber())) {
                    customers.set(i, customer);
                    Files.saveCustomerData(customers);
                    System.out.println("Customer updated: " + customer.getName());
                    return;
                }
            }
        }
    }

}
