package com.finance.tracker.breakout.service.mapper;

import static com.finance.tracker.breakout.domain.StockDetailsAsserts.*;
import static com.finance.tracker.breakout.domain.StockDetailsTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StockDetailsMapperTest {

    private StockDetailsMapper stockDetailsMapper;

    @BeforeEach
    void setUp() {
        stockDetailsMapper = new StockDetailsMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getStockDetailsSample1();
        var actual = stockDetailsMapper.toEntity(stockDetailsMapper.toDto(expected));
        assertStockDetailsAllPropertiesEquals(expected, actual);
    }
}
