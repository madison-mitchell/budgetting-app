package com.mmg.app.model;

import jakarta.persistence.*;

@Entity
@Table(name = "categoryparentchildrelations")
public class CategoryParentChildRelations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parentcategoryid")
    private CategoryParent parentCategory;

    @ManyToOne
    @JoinColumn(name = "childcategoryid")
    private CategoryChild childCategory;

    private Double budget;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CategoryParent getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(CategoryParent parentCategory) {
        this.parentCategory = parentCategory;
    }

    public CategoryChild getChildCategory() {
        return childCategory;
    }

    public void setChildCategory(CategoryChild childCategory) {
        this.childCategory = childCategory;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }
}