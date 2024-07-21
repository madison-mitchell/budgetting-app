package com.mmg.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "expense")
public class Expenses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private double amount;
    @Column(name = "total_budget")
    private double totalBudget;

//    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryParentChildRelations category;

    private String name;
    private String notes;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private BankAccount accountId;

    private String merchant;
    private boolean recurring;
    private String frequency;
    private boolean reviewed;
    private boolean paid;
    @Column(name = "payment_date")
    private Date paymentDate;

    @Column(name = "due_date")
    private Date dueDate;

    @Column(name = "other_party_share")
    private double otherPartyShare;

    @Column(name = "is_shared_expense")
    private boolean isSharedExpense;

    @Column(name = "my_share")
    private double myShare;

    @Column(name = "shared_party_name")
    private String sharedPartyName;

    @Column(name = "my_share_budget")
    private double myShareBudget;

    @OneToOne
    private Transactions transaction;


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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(double totalBudget) {
        this.totalBudget = totalBudget;
    }

    public CategoryParentChildRelations getCategory() {
        return category;
    }

    public void setCategory(CategoryParentChildRelations category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public BankAccount getAccountId() {
        return accountId;
    }

    public void setAccountId(BankAccount accountId) {
        this.accountId = accountId;
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

    public boolean getReviewed() {
        return reviewed;
    }

    public void setReviewed(boolean reviewed) {
        this.reviewed = reviewed;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public double getOtherPartyShare() {
        return otherPartyShare;
    }

    public void setOtherPartyShare(double otherPartyShare) {
        this.otherPartyShare = otherPartyShare;
    }

    public boolean isSharedExpense() {
        return isSharedExpense;
    }

    public void setSharedExpense(boolean sharedExpense) {
        isSharedExpense = sharedExpense;
    }

    public double getMyShare() {
        return myShare;
    }

    public void setMyShare(double myShare) {
        this.myShare = myShare;
    }

    public String getSharedPartyName() {
        return sharedPartyName;
    }

    public void setSharedPartyName(String sharedPartyName) {
        this.sharedPartyName = sharedPartyName;
    }

    public double getMyShareBudget() {
        return myShareBudget;
    }

    public void setMyShareBudget(double myShareBudget) {
        this.myShareBudget = myShareBudget;
    }

    public Transactions getTransaction() {
        return transaction;
    }

    public void setTransaction(Transactions transaction) {
        this.transaction = transaction;
    }
}
