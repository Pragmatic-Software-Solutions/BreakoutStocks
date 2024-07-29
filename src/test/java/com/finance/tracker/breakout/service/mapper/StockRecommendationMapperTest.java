package com.finance.tracker.breakout.service.mapper;

import static com.finance.tracker.breakout.domain.StockRecommendationAsserts.*;
import static com.finance.tracker.breakout.domain.StockRecommendationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StockRecommendationMapperTest {

    private StockRecommendationMapper stockRecommendationMapper;

    @BeforeEach
    void setUp() {
        stockRecommendationMapper = new StockRecommendationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getStockRecommendationSample1();
        var actual = stockRecommendationMapper.toEntity(stockRecommendationMapper.toDto(expected));
        assertStockRecommendationAllPropertiesEquals(expected, actual);
    }
}
