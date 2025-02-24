package com.mmg.app.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "transaction")
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private BankAccount accountId;

    private double amount;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<TransactionSplit> splits;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryParentChildRelations categoryId;

    private String description;

    @Column(name = "time_of_transaction")
    private Date timeOfTransaction;

    private String notes;
    private String merchant;
    private boolean recurring;
    private String frequency;
    private boolean included;
    private boolean reviewed;
    private String type;

    @Column(name = "is_planned")
    private boolean isPlanned;

    @Column(name = "planned_amount")
    private double plannedAmount;

    @Column(name = "has_split")
    private boolean hasSplit;

    @Column(name = "account_balance")
    private double accountBalance;

    @OneToOne
    private Expenses expense;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BankAccount getAccountId() {
        return accountId;
    }

    public void setAccountId(BankAccount accountId) {
        this.accountId = accountId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public List<TransactionSplit> getSplits() {
        return splits;
    }

    public void setSplits(List<TransactionSplit> splits) {
        this.splits = splits;
    }

    public CategoryParentChildRelations getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(CategoryParentChildRelations categoryId) {
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

//    public Expenses getExpense() {
//        return expense;
//    }

//    public void setExpense(Expenses expense) {
//        this.expense = expense;
//    }
}