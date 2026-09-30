package com.billing.services;

import com.billing.models.*;
import com.billing.services.*;

import java.io.*;
import java.util.*;

public class Files {
    // Define base directory path - using a more generic path
    private static final String BASE_DIR = "data";
    
    // Define specific file paths
    private static final String USER_DATA_PATH = BASE_DIR + File.separator + "user_data.txt";
    private static final String PHONES_DATA_PATH = BASE_DIR + File.separator +  "phones.txt";
    private static final String BILLS_DATA_PATH = BASE_DIR + File.separator + "bills.txt";

    // Initialize the file system
    public static void initializeFileSystem() {
        try {
            // Create directory if it doesn't exist
            File directory = new File(BASE_DIR);
            if (!directory.exists()) {
                if (directory.mkdirs()) {
                    System.out.println("Created directory: " + BASE_DIR);
                } else {
                    throw new IOException("Failed to create directory: " + BASE_DIR);
                }
            }

            // Create files if they don't exist
            createFileIfNotExists(USER_DATA_PATH);
            createFileIfNotExists(PHONES_DATA_PATH);
            createFileIfNotExists(BILLS_DATA_PATH);
            
            System.out.println("File system initialized successfully");
        } catch (IOException e) {
            System.err.println("Error initializing file system: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void createFileIfNotExists(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            if (file.createNewFile()) {
                System.out.println("Created file: " + filePath);
            } else {
                throw new IOException("Failed to create file: " + filePath);
            }
        }
    }

    // Save customer data to file
    public static void saveCustomerData(List<Customer> customers) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_DATA_PATH))) {
            // Write number of customers first
            writer.write(String.valueOf(customers.size()));
            writer.newLine();
            
            // Write each customer's data
            for (Customer customer : customers) {
                // Ensure data is not null
                String name = customer.getName() != null ? customer.getName() : "";
                String phone = customer.getPhoneNumber() != null ? customer.getPhoneNumber() : "";
                String address = customer.getAddress() != null ? customer.getAddress() : "";
                String password = customer.getPassword() != null ? customer.getPassword() : "";
                
                // Write data in specific format
                writer.write(String.format("%s;%s;%s;%s",
                    escapeSpecialChars(name),
                    escapeSpecialChars(phone),
                    escapeSpecialChars(address),
                    escapeSpecialChars(password)
                ));
                writer.newLine();
            }
            System.out.println("Customer data saved successfully: " + customers.size() + " customers");
        } catch (IOException e) {
            System.err.println("Error saving customer data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Load customer data from file
    public static List<Customer> loadCustomerData() {
        List<Customer> customers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_DATA_PATH))) {
            // Read number of customers
            String countLine = reader.readLine();
            if (countLine == null) return customers;
            
            int count = Integer.parseInt(countLine.trim());
            
            // Read each customer's data
            String line;
            while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
                try {
                    String[] data = line.split(";");
                    if (data.length == 4) {
                        Customer customer = new Customer(
                            unescapeSpecialChars(data[0]), // name
                            unescapeSpecialChars(data[1]), // phone
                            unescapeSpecialChars(data[2]), // address
                            unescapeSpecialChars(data[3])  // password
                        );
                        customers.add(customer);
                    }
                } catch (Exception e) {
                    System.err.println("Error reading customer data: " + line);
                    e.printStackTrace();
                }
            }
            System.out.println("Customer data loaded successfully: " + customers.size() + " customers");
        } catch (IOException e) {
            System.err.println("Error loading customer data: " + e.getMessage());
            e.printStackTrace();
        }
        return customers;
    }

    // Save phone data to file
    public static void savePhoneData(List<Phone> phones) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PHONES_DATA_PATH))) {
            for (Phone phone : phones) {
                writer.write(String.format("%s,%s,%.2f,%d%n",
                    escapeCommas(phone.getBrand()),
                    escapeCommas(phone.getModel()),
                    phone.getPrice(),
                    phone.getStock()
                ));
            }
            System.out.println("Phone data saved successfully");
        } catch (IOException e) {
            System.err.println("Error saving phone data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Load phone data from file
    public static List<Phone> loadPhoneData() {
        List<Phone> phones = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PHONES_DATA_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                    if (data.length == 4) {
                        try {
                            Phone phone = new Phone(
                                unescapeCommas(data[0]),
                                unescapeCommas(data[1]),
                                Double.parseDouble(data[2]),
                                Integer.parseInt(data[3])
                            );
                            phones.add(phone);
                        } catch (NumberFormatException e) {
                            System.err.println("Error parsing number in line: " + line);
                        }
                    }
                }
            }
            System.out.println("Phone data loaded successfully");
        } catch (IOException e) {
            System.err.println("Error loading phone data: " + e.getMessage());
            e.printStackTrace();
        }
        return phones;
    }

    // Save bills data to file
    public static void saveBillsData(List<Bill> bills) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BILLS_DATA_PATH))) {
            for (Bill bill : bills) {
                writer.write(String.format("%s,%s,%s,%.2f,%b,%s%n",
                    escapeCommas(bill.getCustomerName()),
                    bill.getPhoneNumber(),
                    escapeCommas(bill.getAddress()),
                    bill.getAmount(),
                    bill.isPaid(),
                    bill.getPaymentMethod() != null ? escapeCommas(bill.getPaymentMethod()) : "N/A"
                ));
            }
            System.out.println("Bills data saved successfully");
        } catch (IOException e) {
            System.err.println("Error saving bills data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Load bills data from file
    public static List<Bill> loadBillsData() {
        List<Bill> bills = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(BILLS_DATA_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                    if (data.length == 6) {
                        try {
                            Bill bill = new Bill(
                                unescapeCommas(data[0]),
                                data[1],
                                unescapeCommas(data[2]),
                                Double.parseDouble(data[3]),
                                data[5].equals("N/A") ? null : unescapeCommas(data[5])
                            );
                            if (Boolean.parseBoolean(data[4])) {
                                bill.payBill();
                            }
                            bills.add(bill);
                        } catch (NumberFormatException e) {
                            System.err.println("Error parsing number in line: " + line);
                        }
                    }
                }
            }
            System.out.println("Bills data loaded successfully");
        } catch (IOException e) {
            System.err.println("Error loading bills data: " + e.getMessage());
            e.printStackTrace();
        }
        return bills;
    }

    // Helper methods for handling commas in text
    private static String escapeCommas(String text) {
        if (text == null) return "";
        if (text.contains(",")) {
            return "\"" + text.replace("\"", "\"\"") + "\"";
        }
        return text;
    }

    private static String unescapeCommas(String text) {
        if (text == null) return "";
        if (text.startsWith("\"") && text.endsWith("\"")) {
            return text.substring(1, text.length() - 1).replace("\"\"", "\"");
        }
        return text;
    }

    // Helper functions for handling special characters
    private static String escapeSpecialChars(String text) {
        if (text == null) return "";
        return text.replace(";", "\\;")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r");
    }

    private static String unescapeSpecialChars(String text) {
        if (text == null) return "";
        return text.replace("\\;", ";")
                   .replace("\\n", "\n")
                   .replace("\\r", "\r");
    }
}
