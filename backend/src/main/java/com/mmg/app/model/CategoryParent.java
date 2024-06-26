package com.mmg.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "category_parent")
public class CategoryParent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany
    @JoinTable(name = "category_parent_child_relation", joinColumns = @JoinColumn(name = "parent_category_id"), inverseJoinColumns = @JoinColumn(name = "child_category_id"))
    private List<CategoryChild> childCategories;

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

    public List<CategoryChild> getChildCategories() {
        return childCategories;
    }

    public void setChildCategories(List<CategoryChild> childCategories) {
        this.childCategories = childCategories;
    }
}