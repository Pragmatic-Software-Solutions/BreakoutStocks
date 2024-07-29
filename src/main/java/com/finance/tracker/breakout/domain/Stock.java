package com.finance.tracker.breakout.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Stock.
 */
@Entity
@Table(name = "stock")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Stock implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "exchange")
    private String exchange;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "stock")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "stock" }, allowSetters = true)
    private Set<StockRecommendation> stockRecommendations = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "stock")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "stock" }, allowSetters = true)
    private Set<StockPosition> stockPositions = new HashSet<>();

    @JsonIgnoreProperties(value = { "stock" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "stock")
    private StockDetails stockDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Stock id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public Stock symbol(String symbol) {
        this.setSymbol(symbol);
        return this;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getExchange() {
        return this.exchange;
    }

    public Stock exchange(String exchange) {
        this.setExchange(exchange);
        return this;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public Set<StockRecommendation> getStockRecommendations() {
        return this.stockRecommendations;
    }

    public void setStockRecommendations(Set<StockRecommendation> stockRecommendations) {
        if (this.stockRecommendations != null) {
            this.stockRecommendations.forEach(i -> i.setStock(null));
        }
        if (stockRecommendations != null) {
            stockRecommendations.forEach(i -> i.setStock(this));
        }
        this.stockRecommendations = stockRecommendations;
    }

    public Stock stockRecommendations(Set<StockRecommendation> stockRecommendations) {
        this.setStockRecommendations(stockRecommendations);
        return this;
    }

    public Stock addStockRecommendation(StockRecommendation stockRecommendation) {
        this.stockRecommendations.add(stockRecommendation);
        stockRecommendation.setStock(this);
        return this;
    }

    public Stock removeStockRecommendation(StockRecommendation stockRecommendation) {
        this.stockRecommendations.remove(stockRecommendation);
        stockRecommendation.setStock(null);
        return this;
    }

    public Set<StockPosition> getStockPositions() {
        return this.stockPositions;
    }

    public void setStockPositions(Set<StockPosition> stockPositions) {
        if (this.stockPositions != null) {
            this.stockPositions.forEach(i -> i.setStock(null));
        }
        if (stockPositions != null) {
            stockPositions.forEach(i -> i.setStock(this));
        }
        this.stockPositions = stockPositions;
    }

    public Stock stockPositions(Set<StockPosition> stockPositions) {
        this.setStockPositions(stockPositions);
        return this;
    }

    public Stock addStockPosition(StockPosition stockPosition) {
        this.stockPositions.add(stockPosition);
        stockPosition.setStock(this);
        return this;
    }

    public Stock removeStockPosition(StockPosition stockPosition) {
        this.stockPositions.remove(stockPosition);
        stockPosition.setStock(null);
        return this;
    }

    public StockDetails getStockDetails() {
        return this.stockDetails;
    }

    public void setStockDetails(StockDetails stockDetails) {
        if (this.stockDetails != null) {
            this.stockDetails.setStock(null);
        }
        if (stockDetails != null) {
            stockDetails.setStock(this);
        }
        this.stockDetails = stockDetails;
    }

    public Stock stockDetails(StockDetails stockDetails) {
        this.setStockDetails(stockDetails);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Stock)) {
            return false;
        }
        return getId() != null && getId().equals(((Stock) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Stock{" +
            "id=" + getId() +
            ", symbol='" + getSymbol() + "'" +
            ", exchange='" + getExchange() + "'" +
            "}";
    }
}
