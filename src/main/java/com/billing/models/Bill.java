package com.billing.models;

import com.billing.models.*;
import com.billing.services.*;

public class Bill {
    private String customerName;
    private String phoneNumber;
    private String address;
    private double amount;
    private boolean paid;
    private String paymentMethod; 

    // Constructor
    public Bill(String customerName, String phoneNumber, String address, double amount, String paymentMethod) {
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.amount = amount;
        this.paid = false; // Default to unpaid
        this.paymentMethod = paymentMethod;
    }

   
    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

   
    public String getCustomerName() {
        return customerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public double getAmount() {
        return amount;
    }

    public boolean isPaid() {
        return paid;
    }

    
    public void payBill() {
        if (!paid) {
            paid = true;
            System.out.println("Bill for " + customerName + " of amount $" + amount + " has been successfully paid.");
        } else {
            System.out.println("This bill has already been paid.");
        }
    }

    
    @Override
    public String toString() {
        return "Bill [Customer: " + customerName + 
               ", Phone Number: " + phoneNumber + 
               ", Address: " + address + 
               ", Amount: $" + amount + 
               ", Status: " + (paid ? "Paid" : "Unpaid") + 
               ", Payment Method: " + (paymentMethod != null ? paymentMethod : "Not specified") + "]";
    }

}
