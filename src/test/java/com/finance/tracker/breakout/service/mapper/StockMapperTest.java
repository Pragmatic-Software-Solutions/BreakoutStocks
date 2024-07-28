package com.finance.tracker.breakout.service.mapper;

import static com.finance.tracker.breakout.domain.StockAsserts.*;
import static com.finance.tracker.breakout.domain.StockTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StockMapperTest {

    private StockMapper stockMapper;

    @BeforeEach
    void setUp() {
        stockMapper = new StockMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getStockSample1();
        var actual = stockMapper.toEntity(stockMapper.toDto(expected));
        assertStockAllPropertiesEquals(expected, actual);
    }
}
