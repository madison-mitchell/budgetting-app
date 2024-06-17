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

    @Column(name = "name")
    private String name;

    @ManyToMany
    @JoinTable(
            name = "categoryparentchildrelations",
            joinColumns = @JoinColumn(name = "parentcategoryid"),
            inverseJoinColumns = @JoinColumn(name = "childcategoryid")
    )
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