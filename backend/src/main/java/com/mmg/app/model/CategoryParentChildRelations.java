package com.mmg.app.model;

import jakarta.persistence.*;

@Entity
@Table(name = "category_parent_child_relation")
public class CategoryParentChildRelations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_category_id")
    private CategoryParent parentCategory;

    @ManyToOne
    @JoinColumn(name = "child_category_id")
    private CategoryChild childCategory;




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
}