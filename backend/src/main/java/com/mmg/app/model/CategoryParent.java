package com.mmg.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "categoryparent")
public class CategoryParent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "parentCategoryId")
    @Column(name = "childcategories")
    private List<CategoryParentChildRelations> childCategories;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CategoryParentChildRelations> getChildCategories() {
        return childCategories;
    }

    public void setChildCategories(List<CategoryParentChildRelations> childCategories) {
        this.childCategories = childCategories;
    }

    // Getters and Setters
}
