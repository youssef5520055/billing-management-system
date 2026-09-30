
package com.billing.models;

import com.billing.models.*;
import com.billing.services.*;


public class Phone {
    private static int nextId = 1;
    private int id;
    private String brand;
    private String model;
    private double price;
    private int stock;

    public Phone(String brand, String model, double price, int stock) {
        this.id = nextId++;
        this.brand = brand;
        this.model = model;
        this.price = price;
        this.stock = stock;
    }

    public int getId() { return id; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    
    public void setStock(int stock) { this.stock = stock; }
    
    public String getFormattedPrice() {
        return String.format("$%.2f", price);
    }

    public String getDetails() {
        return String.format("ID: %d\nBrand: %s\nModel: %s\nPrice: %s\nStock: %d\n",
            id, brand, model, getFormattedPrice(), stock);
    }

    @Override
    public String toString() {
        return String.format("%s %s - %s (Stock: %d)", brand, model, getFormattedPrice(), stock);
    }
}
