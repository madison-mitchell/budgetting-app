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
    private CategoryParent parent;

    @ManyToOne
    @JoinColumn(name = "childcategoryid")
    private CategoryChild child;

    private Double budget;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CategoryParent getParent() {
        return parent;
    }

    public void setParent(CategoryParent parent) {
        this.parent = parent;
    }

    public CategoryChild getChild() {
        return child;
    }

    public void setChild(CategoryChild child) {
        this.child = child;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }
}
