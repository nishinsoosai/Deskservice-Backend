package com.example.Deskservice.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.*;

public class WriteRecord {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Amount is required")
    @PositiveOrZero(message = "Amount cannot be negative")
    @Digits(integer = 10, fraction = 2,
            message = "Amount must have maximum 2 decimal places")
    private BigDecimal amount;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    // Version for optimistic locking
    private Long version;

    // GETTERS AND SETTERS

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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}