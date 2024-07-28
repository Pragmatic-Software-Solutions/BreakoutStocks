package com.finance.tracker.breakout.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A StockRecommendation.
 */
@Entity
@Table(name = "stock_recommendation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StockRecommendation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "entry")
    private Float entry;

    @Column(name = "stop_loss")
    private Float stopLoss;

    @Column(name = "target")
    private Float target;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "comments")
    private String comments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "stockRecommendations", "stockPositions", "stockDetails" }, allowSetters = true)
    private Stock stock;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StockRecommendation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getEntry() {
        return this.entry;
    }

    public StockRecommendation entry(Float entry) {
        this.setEntry(entry);
        return this;
    }

    public void setEntry(Float entry) {
        this.entry = entry;
    }

    public Float getStopLoss() {
        return this.stopLoss;
    }

    public StockRecommendation stopLoss(Float stopLoss) {
        this.setStopLoss(stopLoss);
        return this;
    }

    public void setStopLoss(Float stopLoss) {
        this.stopLoss = stopLoss;
    }

    public Float getTarget() {
        return this.target;
    }

    public StockRecommendation target(Float target) {
        this.setTarget(target);
        return this;
    }

    public void setTarget(Float target) {
        this.target = target;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public StockRecommendation quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getComments() {
        return this.comments;
    }

    public StockRecommendation comments(String comments) {
        this.setComments(comments);
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Stock getStock() {
        return this.stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public StockRecommendation stock(Stock stock) {
        this.setStock(stock);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StockRecommendation)) {
            return false;
        }
        return getId() != null && getId().equals(((StockRecommendation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StockRecommendation{" +
            "id=" + getId() +
            ", entry=" + getEntry() +
            ", stopLoss=" + getStopLoss() +
            ", target=" + getTarget() +
            ", quantity=" + getQuantity() +
            ", comments='" + getComments() + "'" +
            "}";
    }
}
