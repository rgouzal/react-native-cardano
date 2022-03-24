package com.rgouzal.reactnativecardano.models;


public class WalletBalance {
    private String amount;
    private String unit;

    public WalletBalance(String amount, String unit) {
        this.amount = amount;
        this.unit = unit;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
