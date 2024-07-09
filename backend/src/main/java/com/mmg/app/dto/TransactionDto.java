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
    private boolean reviewed;
    private String type;
    private boolean isPlanned;
    private double plannedAmount;
    private boolean hasSplit;
    private double accountBalance;

    @Override
    public String toString() {
        return "TransactionDto{" +
                "id=" + id +
                ", accountId=" + accountId +
                ", userId=" + userId +
                ", amount=" + amount +
                ", splits=" + splits +
                ", categoryId=" + categoryId +
                ", description='" + description + '\'' +
                ", timeOfTransaction=" + timeOfTransaction +
                ", notes='" + notes + '\'' +
                ", merchant='" + merchant + '\'' +
                ", recurring=" + recurring +
                ", frequency='" + frequency + '\'' +
                ", included=" + included +
                ", reviewed=" + reviewed +
                ", type='" + type + '\'' +
                ", isPlanned=" + isPlanned +
                ", plannedAmount=" + plannedAmount +
                ", hasSplit=" + hasSplit +
                ", accountBalance=" + accountBalance +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
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

    public boolean isReviewed() {
        return reviewed;
    }

    public void setReviewed(boolean reviewed) {
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
        this.isPlanned = isPlanned;
    }

    public double getPlannedAmount() {
        return plannedAmount;
    }

    public void setPlannedAmount(double plannedAmount) {
        this.plannedAmount = plannedAmount;
    }

    public boolean isHasSplit() {
        return hasSplit;
    }

    public void setHasSplit(boolean hasSplit) {
        this.hasSplit = hasSplit;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }
}
