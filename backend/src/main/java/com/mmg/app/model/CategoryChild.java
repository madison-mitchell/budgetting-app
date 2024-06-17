package com.mmg.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "categorychild")
public class CategoryChild {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

//    @ManyToMany(mappedBy = "childCategories")
//    private List<CategoryParent> parentCategories;

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

//    public List<CategoryParent> getParentCategories() {
//        return parentCategories;
//    }
//
//    public void setParentCategories(List<CategoryParent> parentCategories) {
//        this.parentCategories = parentCategories;
//    }
}
