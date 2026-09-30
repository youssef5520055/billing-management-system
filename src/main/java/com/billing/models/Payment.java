package com.billing.models;

import com.billing.models.*;
import com.billing.services.*;

public class Payment {
    protected int amount;
    protected String method;
    protected boolean success;

    public Payment() {
        amount = 0;
        method = "";
        success = false;
    }

    public Payment(int amount, String method, boolean success) {
        this.amount = amount;
        this.method = method;
        this.success = success;
    }

    public void setMethod(String paymentMethod) {
        this.method = paymentMethod;
    }

    public String getMethod() {
        return method;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}



