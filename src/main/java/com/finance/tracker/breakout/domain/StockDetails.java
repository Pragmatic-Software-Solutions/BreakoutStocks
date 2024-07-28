package com.finance.tracker.breakout.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A StockDetails.
 */
@Entity
@Table(name = "stock_details")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StockDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "cur_price")
    private Float curPrice;

    @Column(name = "price_change")
    private Float priceChange;

    @Column(name = "change_per")
    private Float changePer;

    @JsonIgnoreProperties(value = { "stockRecommendations", "stockPositions", "stockDetails" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Stock stock;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StockDetails id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getCurPrice() {
        return this.curPrice;
    }

    public StockDetails curPrice(Float curPrice) {
        this.setCurPrice(curPrice);
        return this;
    }

    public void setCurPrice(Float curPrice) {
        this.curPrice = curPrice;
    }

    public Float getPriceChange() {
        return this.priceChange;
    }

    public StockDetails priceChange(Float priceChange) {
        this.setPriceChange(priceChange);
        return this;
    }

    public void setPriceChange(Float priceChange) {
        this.priceChange = priceChange;
    }

    public Float getChangePer() {
        return this.changePer;
    }

    public StockDetails changePer(Float changePer) {
        this.setChangePer(changePer);
        return this;
    }

    public void setChangePer(Float changePer) {
        this.changePer = changePer;
    }

    public Stock getStock() {
        return this.stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public StockDetails stock(Stock stock) {
        this.setStock(stock);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StockDetails)) {
            return false;
        }
        return getId() != null && getId().equals(((StockDetails) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StockDetails{" +
            "id=" + getId() +
            ", curPrice=" + getCurPrice() +
            ", priceChange=" + getPriceChange() +
            ", changePer=" + getChangePer() +
            "}";
    }
}
