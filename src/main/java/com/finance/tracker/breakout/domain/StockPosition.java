package com.finance.tracker.breakout.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A StockPosition.
 */
@Entity
@Table(name = "stock_position")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StockPosition implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "buying_price")
    private Float buyingPrice;

    @Column(name = "exit_price")
    private Float exitPrice;

    @Column(name = "sold")
    private Boolean sold;

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

    public StockPosition id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getBuyingPrice() {
        return this.buyingPrice;
    }

    public StockPosition buyingPrice(Float buyingPrice) {
        this.setBuyingPrice(buyingPrice);
        return this;
    }

    public void setBuyingPrice(Float buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public Float getExitPrice() {
        return this.exitPrice;
    }

    public StockPosition exitPrice(Float exitPrice) {
        this.setExitPrice(exitPrice);
        return this;
    }

    public void setExitPrice(Float exitPrice) {
        this.exitPrice = exitPrice;
    }

    public Boolean getSold() {
        return this.sold;
    }

    public StockPosition sold(Boolean sold) {
        this.setSold(sold);
        return this;
    }

    public void setSold(Boolean sold) {
        this.sold = sold;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public StockPosition quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getComments() {
        return this.comments;
    }

    public StockPosition comments(String comments) {
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

    public StockPosition stock(Stock stock) {
        this.setStock(stock);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StockPosition)) {
            return false;
        }
        return getId() != null && getId().equals(((StockPosition) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StockPosition{" +
            "id=" + getId() +
            ", buyingPrice=" + getBuyingPrice() +
            ", exitPrice=" + getExitPrice() +
            ", sold='" + getSold() + "'" +
            ", quantity=" + getQuantity() +
            ", comments='" + getComments() + "'" +
            "}";
    }
}
