package com.example.Deskservice.dto;

import com.example.Deskservice.Entity.Status;
import java.math.BigDecimal;

public class RecordResponse {

    private Long id;

    private String title;

    private String description;

    private BigDecimal amount;

    private Status status;

    private Long categoryId;

    private String categoryName;

    // Optimistic locking version
    private Long version;

    public RecordResponse() {
    }

    public RecordResponse(Long id, String title,
            String description, BigDecimal amount,
            Status status) {

        this.id = id;
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.status = status;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}