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

    @Column(name = "fullname")
    private String fullName;

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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
