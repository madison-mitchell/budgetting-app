package com.mmg.app.dto;

public class CategoryTotalDto {
    private Long parentId;
    private String parentName;
    private Long childId;
    private String childName;
    private double totalAmount;

    public CategoryTotalDto(Long parentId, String parentName, Long childId, String childName, double totalAmount) {
        this.parentId = parentId;
        this.parentName = parentName;
        this.childId = childId;
        this.childName = childName;
        this.totalAmount = totalAmount;
    }

    // Getters and Setters
    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Long getChildId() {
        return childId;
    }

    public void setChildId(Long childId) {
        this.childId = childId;
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
}
