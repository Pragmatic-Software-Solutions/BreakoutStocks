package com.finance.tracker.breakout.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.finance.tracker.breakout.domain.StockRecommendation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StockRecommendationDTO implements Serializable {

    private Long id;

    private Float entry;

    private Float stopLoss;

    private Float target;

    private Integer quantity;

    private String comments;

    private StockDTO stock;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getEntry() {
        return entry;
    }

    public void setEntry(Float entry) {
        this.entry = entry;
    }

    public Float getStopLoss() {
        return stopLoss;
    }

    public void setStopLoss(Float stopLoss) {
        this.stopLoss = stopLoss;
    }

    public Float getTarget() {
        return target;
    }

    public void setTarget(Float target) {
        this.target = target;
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
        if (!(o instanceof StockRecommendationDTO)) {
            return false;
        }

        StockRecommendationDTO stockRecommendationDTO = (StockRecommendationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, stockRecommendationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StockRecommendationDTO{" +
            "id=" + getId() +
            ", entry=" + getEntry() +
            ", stopLoss=" + getStopLoss() +
            ", target=" + getTarget() +
            ", quantity=" + getQuantity() +
            ", comments='" + getComments() + "'" +
            ", stock=" + getStock() +
            "}";
    }
}
