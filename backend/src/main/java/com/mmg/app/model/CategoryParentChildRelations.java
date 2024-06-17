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
    private CategoryParent parentCategoryId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "childcategoryid")
    private CategoryChild childCategoryId;

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
        return parentCategoryId;
    }

    public void setParentCategory(CategoryParent parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public CategoryChild getChildCategory() {
        return childCategoryId;
    }

    public void setChildCategory(CategoryChild childCategoryId) {
        this.childCategoryId = childCategoryId;
    }
}
