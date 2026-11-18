package com.example.Deskservice.dto;

import com.example.Deskservice.Entity.*;

import jakarta.validation.constraints.NotNull;

public class CloseRecordRequest {

    @NotNull(message = "Status is required")
    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}