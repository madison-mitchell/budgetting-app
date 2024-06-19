package com.mmg.app.dto;

public class CategoryTotalDto {
    private Long relationId;
    private Long parentCategoryId;
    private String parentName;
    private Long childCategoryId;
    private String childName;
    private double totalAmount;
    private double budget;

    // Constructor
    public CategoryTotalDto(Long relationId, Long parentCategoryId, String parentName, Long childCategoryId, String childName, double totalAmount, double budget) {
        this.relationId = relationId;
        this.parentCategoryId = parentCategoryId;
        this.parentName = parentName;
        this.childCategoryId = childCategoryId;
        this.childName = childName;
        this.totalAmount = totalAmount;
        this.budget = budget;
    }

    // Getters and Setters
    public Long getRelationId() {
        return relationId;
    }

    public void setRelationId(Long relationId) {
        this.relationId = relationId;
    }

    public Long getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(Long parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Long getChildCategoryId() {
        return childCategoryId;
    }

    public void setChildCategoryId(Long childCategoryId) {
        this.childCategoryId = childCategoryId;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }
}
