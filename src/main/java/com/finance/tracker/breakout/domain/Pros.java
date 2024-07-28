package com.finance.tracker.breakout.domain;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "pros")
public class Pros implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "pro")
    private String pro;

    // Constructors, Getters, Setters
    public Pros() {}

    public Pros(String companyName, String pro) {
        this.companyName = companyName;
        this.pro = pro;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPro() {
        return pro;
    }

    public void setPro(String pro) {
        this.pro = pro;
    }
}
