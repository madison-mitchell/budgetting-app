package com.mmg.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "categoryparentchildrelations")
public class CategoryParentChildRelations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fullname")
    private String fullName;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "parentcategoryid")
    private CategoryParent parentCategory;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "childcategoryid")
    private CategoryChild childCategory;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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
