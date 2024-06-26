package com.mmg.app.model;

import jakarta.persistence.*;

@Entity
@Table(name = "shared_bill")
public class SharedBills {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bill_id")
    private Bills bill;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String name;

    @Column(name = "person_a_portion")
    private double personAPortion;

    @Column(name = "person_b_portion")
    private double personBPortion;

    @Column(name = "is_percentage")
    private boolean isPercentage;

    @Column(name = "percentage_split")
    private double percentageSplit;

    private String notes;
    private String reviewed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Bills getBill() {
        return bill;
    }

    public void setBill(Bills bill) {
        this.bill = bill;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPersonAPortion() {
        return personAPortion;
    }

    public void setPersonAPortion(double personAPortion) {
        this.personAPortion = personAPortion;
    }

    public double getPersonBPortion() {
        return personBPortion;
    }

    public void setPersonBPortion(double personBPortion) {
        this.personBPortion = personBPortion;
    }

    public boolean isPercentage() {
        return isPercentage;
    }

    public void setPercentage(boolean percentage) {
        isPercentage = percentage;
    }

    public double getPercentageSplit() {
        return percentageSplit;
    }

    public void setPercentageSplit(double percentageSplit) {
        this.percentageSplit = percentageSplit;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getReviewed() {
        return reviewed;
    }

    public void setReviewed(String reviewed) {
        this.reviewed = reviewed;
    }

    // Getters and Setters
}
