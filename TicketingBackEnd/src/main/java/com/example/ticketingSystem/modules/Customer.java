package com.example.ticketingSystem.modules;

public class Customer {

    int customerID;
    int retrievalRate;

    public int getRetrievalRate() {
        return retrievalRate;
    }

    public void setRetrievalRate(int retrievalRate) {
        this.retrievalRate = retrievalRate;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }
}
