package com.finance.tracker.breakout.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.finance.tracker.breakout.domain.StockDetails} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StockDetailsDTO implements Serializable {

    private Long id;

    private Float curPrice;

    private Float priceChange;

    private Float changePer;

    private StockDTO stock;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getCurPrice() {
        return curPrice;
    }

    public void setCurPrice(Float curPrice) {
        this.curPrice = curPrice;
    }

    public Float getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(Float priceChange) {
        this.priceChange = priceChange;
    }

    public Float getChangePer() {
        return changePer;
    }

    public void setChangePer(Float changePer) {
        this.changePer = changePer;
    }

    public StockDTO getStock() {
        return stock;
    }

    public void setStock(StockDTO stock) {
        this.stock = stock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StockDetailsDTO)) {
            return false;
        }

        StockDetailsDTO stockDetailsDTO = (StockDetailsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, stockDetailsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StockDetailsDTO{" +
            "id=" + getId() +
            ", curPrice=" + getCurPrice() +
            ", priceChange=" + getPriceChange() +
            ", changePer=" + getChangePer() +
            ", stock=" + getStock() +
            "}";
    }
}
