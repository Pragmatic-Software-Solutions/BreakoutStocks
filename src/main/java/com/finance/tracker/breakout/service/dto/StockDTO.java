package com.finance.tracker.breakout.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.finance.tracker.breakout.domain.Stock} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StockDTO implements Serializable {

    private Long id;

    private String symbol;

    private String exchange;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StockDTO)) {
            return false;
        }

        StockDTO stockDTO = (StockDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, stockDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StockDTO{" +
            "id=" + getId() +
            ", symbol='" + getSymbol() + "'" +
            ", exchange='" + getExchange() + "'" +
            "}";
    }
}
