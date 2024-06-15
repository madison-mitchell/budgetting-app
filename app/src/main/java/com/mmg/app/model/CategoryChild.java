package com.mmg.app.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "categorychild")
public class CategoryChild {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "childCategory")
    @Column(name = "parentcategories")
    private List<CategoryParentChildRelations> parentCategories;

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

    public List<CategoryParentChildRelations> getParentCategories() {
        return parentCategories;
    }

    public void setParentCategories(List<CategoryParentChildRelations> parentCategories) {
        this.parentCategories = parentCategories;
    }

    // Getters and Setters
}
