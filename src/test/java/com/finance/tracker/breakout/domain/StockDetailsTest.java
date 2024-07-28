package com.finance.tracker.breakout.domain;

import static com.finance.tracker.breakout.domain.StockDetailsTestSamples.*;
import static com.finance.tracker.breakout.domain.StockTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.finance.tracker.breakout.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StockDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockDetails.class);
        StockDetails stockDetails1 = getStockDetailsSample1();
        StockDetails stockDetails2 = new StockDetails();
        assertThat(stockDetails1).isNotEqualTo(stockDetails2);

        stockDetails2.setId(stockDetails1.getId());
        assertThat(stockDetails1).isEqualTo(stockDetails2);

        stockDetails2 = getStockDetailsSample2();
        assertThat(stockDetails1).isNotEqualTo(stockDetails2);
    }

    @Test
    void stockTest() {
        StockDetails stockDetails = getStockDetailsRandomSampleGenerator();
        Stock stockBack = getStockRandomSampleGenerator();

        stockDetails.setStock(stockBack);
        assertThat(stockDetails.getStock()).isEqualTo(stockBack);

        stockDetails.stock(null);
        assertThat(stockDetails.getStock()).isNull();
    }
}
