package com.billing.models;

import com.billing.models.*;
import com.billing.services.*;

import java.util.Date;

public class Order {
    private String customerName;
    private Phone phone;
    private Date orderDate;
    private boolean completed;

    public Order(String customerName, Phone phone) {
        this.customerName = customerName;
        this.phone = phone;
        this.orderDate = new Date(); 
        this.completed = false;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Phone getPhone() {
        return phone;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void completeOrder() {
        completed = true;
   
    }

    @Override
    public String toString() {
        return "Order for " + customerName + ": " + phone.getModel() + " - Status: " + (completed ? "Completed" : "Pending") + " - Date: " + orderDate;
    }
} 



