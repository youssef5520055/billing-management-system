package com.billing.models;

import com.billing.models.*;
import com.billing.services.*;

public class Credit extends Payment {
    private String cardNumber;
    private String cardHolderName;
    private String expiryMonth;
    private String expiryYear;
    private String cvv;

    public Credit() {
        super();
    }

    public boolean validateCreditCard() {
        try {
            // Validate card number (16 digits)
            if (!isValidCardNumber(cardNumber)) {
                return false;
            }

            // Validate cardholder name
            if (!isValidCardHolderName(cardHolderName)) {
                return false;
            }

            // Validate expiry date
            if (!isValidExpiryDate(expiryMonth, expiryYear)) {
                return false;
            }

            // Validate CVV
            if (!isValidCVV(cvv)) {
                return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isValidCardNumber(String number) {
        return number != null && number.matches("\\d{16}");
    }

    private boolean isValidCardHolderName(String name) {
        return name != null && !name.trim().isEmpty();
    }

    private boolean isValidExpiryDate(String month, String year) {
        return month != null && month.matches("(0[1-9]|1[0-2])") &&
               year != null && year.matches("202[4-9]|203[0-4]");
    }

    private boolean isValidCVV(String cvv) {
        return cvv != null && cvv.matches("\\d{3}");
    }

    // Setters with validation
    public void setCardNumber(String cardNumber) {
        if (!isValidCardNumber(cardNumber)) {
            throw new IllegalArgumentException("Invalid card number - must be 16 digits");
        }
        this.cardNumber = cardNumber;
    }

    public void setCardHolderName(String cardHolderName) {
        if (!isValidCardHolderName(cardHolderName)) {
            throw new IllegalArgumentException("Invalid card holder name");
        }
        this.cardHolderName = cardHolderName;
    }

    public void setExpiryMonth(String expiryMonth) {
        if (expiryMonth == null || !expiryMonth.matches("(0[1-9]|1[0-2])")) {
            throw new IllegalArgumentException("Invalid expiry month");
        }
        this.expiryMonth = expiryMonth;
    }

    public void setExpiryYear(String expiryYear) {
        if (expiryYear == null || !expiryYear.matches("202[4-9]|203[0-4]")) {
            throw new IllegalArgumentException("Invalid expiry year");
        }
        this.expiryYear = expiryYear;
    }

    public void setCvv(String cvv) {
        if (!isValidCVV(cvv)) {
            throw new IllegalArgumentException("Invalid CVV - must be 3 digits");
        }
        this.cvv = cvv;
    }

    // Getters
    public String getCardNumber() { return cardNumber; }
    public String getCardHolderName() { return cardHolderName; }
    public String getExpiryMonth() { return expiryMonth; }
    public String getExpiryYear() { return expiryYear; }
    public String getCvv() { return cvv; }
}
