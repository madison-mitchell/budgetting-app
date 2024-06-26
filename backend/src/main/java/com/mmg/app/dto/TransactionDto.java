package com.mmg.app.dto;

import java.util.Date;
import java.util.List;

public class TransactionDto {
    private Long id;
    private Long accountId;
    private Long userId;
    private double amount;
    private List<TransactionSplitDto> splits;
    private Long categoryId;
    private String description;
    private Date timeOfTransaction;
    private String notes;
    private String merchant;
    private boolean recurring;
    private String frequency;
    private boolean included;
    private String reviewed;
    private String type;
    private boolean isPlanned;
    private double plannedAmount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBankAccountId() {
        return accountId;
    }

    public void setBankAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public List<TransactionSplitDto> getSplits() {
        return splits;
    }

    public void setSplits(List<TransactionSplitDto> splits) {
        this.splits = splits;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getTimeOfTransaction() {
        return timeOfTransaction;
    }

    public void setTimeOfTransaction(Date timeOfTransaction) {
        this.timeOfTransaction = timeOfTransaction;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public boolean isIncluded() {
        return included;
    }

    public void setIncluded(boolean included) {
        this.included = included;
    }

    public String getReviewed() {
        return reviewed;
    }

    public void setReviewed(String reviewed) {
        this.reviewed = reviewed;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isPlanned() {
        return isPlanned;
    }

    public void setIsPlanned(boolean isPlanned) {
        System.out.println("planned argument: " + isPlanned);
        this.isPlanned = isPlanned;
        System.out.println("this.isPlanned argument: " + this.isPlanned);
    }

    public double getPlannedAmount() {
        return plannedAmount;
    }

    public void setPlannedAmount(double plannedAmount) {
        this.plannedAmount = plannedAmount;
    }
}
