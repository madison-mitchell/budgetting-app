package com.mmg.app.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "category_budget", uniqueConstraints = {@UniqueConstraint(columnNames = {"category_id", "year", "month"})})
public class CategoryBudget {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_budget_id_seq")
    @SequenceGenerator(name = "category_budget_id_seq", sequenceName = "category_budget_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryParentChildRelations category;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private int year;
    private int month;
    private double budgetAmount;
    private double amountSpent;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CategoryParentChildRelations getCategory() {
        return category;
    }

    public void setCategory(CategoryParentChildRelations category) {
        this.category = category;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public double getBudgetAmount() {
        return budgetAmount;
    }

    public void setBudgetAmount(double budgetAmount) {
        this.budgetAmount = budgetAmount;
    }

    public double getAmountSpent() {
        return amountSpent;
    }

    public void setAmountSpent(double amountSpent) {
        this.amountSpent = amountSpent;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
