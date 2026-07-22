package com.example.pingstack.messaging.service;

import com.example.pingstack.messaging.enums.SubscriberStatus;
import com.opencsv.bean.CsvBindByName;

public class AudienceCSVRepresentation {

    @CsvBindByName(column = "name")
    private String name;
    @CsvBindByName(column = "destinationNumber")
    private String dNumber;
    @CsvBindByName(column = "status")
    private SubscriberStatus status;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getdNumber() {
        return dNumber;
    }

    public void setdNumber(String dNumber) {
        this.dNumber = dNumber;
    }

    public SubscriberStatus getStatus() {
        return status;
    }

    public void setStatus(SubscriberStatus status) {
        this.status = status;
    }
}
