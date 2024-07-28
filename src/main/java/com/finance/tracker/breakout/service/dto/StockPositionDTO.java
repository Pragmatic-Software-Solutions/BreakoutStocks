package com.finance.tracker.breakout.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.finance.tracker.breakout.domain.StockPosition} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StockPositionDTO implements Serializable {

    private Long id;

    private Float buyingPrice;

    private Float exitPrice;

    private Boolean sold;

    private Integer quantity;

    private String comments;

    private StockDTO stock;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(Float buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public Float getExitPrice() {
        return exitPrice;
    }

    public void setExitPrice(Float exitPrice) {
        this.exitPrice = exitPrice;
    }

    public Boolean getSold() {
        return sold;
    }

    public void setSold(Boolean sold) {
        this.sold = sold;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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
        if (!(o instanceof StockPositionDTO)) {
            return false;
        }

        StockPositionDTO stockPositionDTO = (StockPositionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, stockPositionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StockPositionDTO{" +
            "id=" + getId() +
            ", buyingPrice=" + getBuyingPrice() +
            ", exitPrice=" + getExitPrice() +
            ", sold='" + getSold() + "'" +
            ", quantity=" + getQuantity() +
            ", comments='" + getComments() + "'" +
            ", stock=" + getStock() +
            "}";
    }
}
