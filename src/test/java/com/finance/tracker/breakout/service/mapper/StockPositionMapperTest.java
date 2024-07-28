package com.finance.tracker.breakout.service.mapper;

import static com.finance.tracker.breakout.domain.StockPositionAsserts.*;
import static com.finance.tracker.breakout.domain.StockPositionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StockPositionMapperTest {

    private StockPositionMapper stockPositionMapper;

    @BeforeEach
    void setUp() {
        stockPositionMapper = new StockPositionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getStockPositionSample1();
        var actual = stockPositionMapper.toEntity(stockPositionMapper.toDto(expected));
        assertStockPositionAllPropertiesEquals(expected, actual);
    }
}
