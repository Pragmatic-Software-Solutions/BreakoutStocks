package com.finance.tracker.breakout.domain;

import static com.finance.tracker.breakout.domain.StockDetailsTestSamples.*;
import static com.finance.tracker.breakout.domain.StockPositionTestSamples.*;
import static com.finance.tracker.breakout.domain.StockRecommendationTestSamples.*;
import static com.finance.tracker.breakout.domain.StockTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.finance.tracker.breakout.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class StockTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Stock.class);
        Stock stock1 = getStockSample1();
        Stock stock2 = new Stock();
        assertThat(stock1).isNotEqualTo(stock2);

        stock2.setId(stock1.getId());
        assertThat(stock1).isEqualTo(stock2);

        stock2 = getStockSample2();
        assertThat(stock1).isNotEqualTo(stock2);
    }

    @Test
    void stockRecommendationTest() {
        Stock stock = getStockRandomSampleGenerator();
        StockRecommendation stockRecommendationBack = getStockRecommendationRandomSampleGenerator();

        stock.addStockRecommendation(stockRecommendationBack);
        assertThat(stock.getStockRecommendations()).containsOnly(stockRecommendationBack);
        assertThat(stockRecommendationBack.getStock()).isEqualTo(stock);

        stock.removeStockRecommendation(stockRecommendationBack);
        assertThat(stock.getStockRecommendations()).doesNotContain(stockRecommendationBack);
        assertThat(stockRecommendationBack.getStock()).isNull();

        stock.stockRecommendations(new HashSet<>(Set.of(stockRecommendationBack)));
        assertThat(stock.getStockRecommendations()).containsOnly(stockRecommendationBack);
        assertThat(stockRecommendationBack.getStock()).isEqualTo(stock);

        stock.setStockRecommendations(new HashSet<>());
        assertThat(stock.getStockRecommendations()).doesNotContain(stockRecommendationBack);
        assertThat(stockRecommendationBack.getStock()).isNull();
    }

    @Test
    void stockPositionTest() {
        Stock stock = getStockRandomSampleGenerator();
        StockPosition stockPositionBack = getStockPositionRandomSampleGenerator();

        stock.addStockPosition(stockPositionBack);
        assertThat(stock.getStockPositions()).containsOnly(stockPositionBack);
        assertThat(stockPositionBack.getStock()).isEqualTo(stock);

        stock.removeStockPosition(stockPositionBack);
        assertThat(stock.getStockPositions()).doesNotContain(stockPositionBack);
        assertThat(stockPositionBack.getStock()).isNull();

        stock.stockPositions(new HashSet<>(Set.of(stockPositionBack)));
        assertThat(stock.getStockPositions()).containsOnly(stockPositionBack);
        assertThat(stockPositionBack.getStock()).isEqualTo(stock);

        stock.setStockPositions(new HashSet<>());
        assertThat(stock.getStockPositions()).doesNotContain(stockPositionBack);
        assertThat(stockPositionBack.getStock()).isNull();
    }

    @Test
    void stockDetailsTest() {
        Stock stock = getStockRandomSampleGenerator();
        StockDetails stockDetailsBack = getStockDetailsRandomSampleGenerator();

        stock.setStockDetails(stockDetailsBack);
        assertThat(stock.getStockDetails()).isEqualTo(stockDetailsBack);
        assertThat(stockDetailsBack.getStock()).isEqualTo(stock);

        stock.stockDetails(null);
        assertThat(stock.getStockDetails()).isNull();
        assertThat(stockDetailsBack.getStock()).isNull();
    }
}
