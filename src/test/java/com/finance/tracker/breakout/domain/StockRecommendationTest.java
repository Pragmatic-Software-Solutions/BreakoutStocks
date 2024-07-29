package com.finance.tracker.breakout.domain;

import static com.finance.tracker.breakout.domain.StockRecommendationTestSamples.*;
import static com.finance.tracker.breakout.domain.StockTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.finance.tracker.breakout.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StockRecommendationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockRecommendation.class);
        StockRecommendation stockRecommendation1 = getStockRecommendationSample1();
        StockRecommendation stockRecommendation2 = new StockRecommendation();
        assertThat(stockRecommendation1).isNotEqualTo(stockRecommendation2);

        stockRecommendation2.setId(stockRecommendation1.getId());
        assertThat(stockRecommendation1).isEqualTo(stockRecommendation2);

        stockRecommendation2 = getStockRecommendationSample2();
        assertThat(stockRecommendation1).isNotEqualTo(stockRecommendation2);
    }

    @Test
    void stockTest() {
        StockRecommendation stockRecommendation = getStockRecommendationRandomSampleGenerator();
        Stock stockBack = getStockRandomSampleGenerator();

        stockRecommendation.setStock(stockBack);
        assertThat(stockRecommendation.getStock()).isEqualTo(stockBack);

        stockRecommendation.stock(null);
        assertThat(stockRecommendation.getStock()).isNull();
    }
}
