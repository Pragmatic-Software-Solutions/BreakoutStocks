package com.finance.tracker.breakout.domain;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "cons")
public class Cons implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "con")
    private String con;

    // Constructors, Getters, Setters
    public Cons() {}

    public Cons(String companyName, String con) {
        this.companyName = companyName;
        this.con = con;
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

    public String getCon() {
        return con;
    }

    public void setCon(String con) {
        this.con = con;
    }
}
