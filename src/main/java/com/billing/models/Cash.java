package com.billing.models;

import com.billing.models.*;
import com.billing.services.*;

public class Cash extends Payment {
    public void proceedPayment(Customer customer) {
        this.success = true; 
        System.out.println("Cash payment processed successfully.");
        System.out.println("The order will be delivered to the ADDRESS: " + customer.getAddress());
    }
}
