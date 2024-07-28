package com.finance.tracker.breakout.domain;

import static com.finance.tracker.breakout.domain.StockPositionTestSamples.*;
import static com.finance.tracker.breakout.domain.StockTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.finance.tracker.breakout.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StockPositionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockPosition.class);
        StockPosition stockPosition1 = getStockPositionSample1();
        StockPosition stockPosition2 = new StockPosition();
        assertThat(stockPosition1).isNotEqualTo(stockPosition2);

        stockPosition2.setId(stockPosition1.getId());
        assertThat(stockPosition1).isEqualTo(stockPosition2);

        stockPosition2 = getStockPositionSample2();
        assertThat(stockPosition1).isNotEqualTo(stockPosition2);
    }

    @Test
    void stockTest() {
        StockPosition stockPosition = getStockPositionRandomSampleGenerator();
        Stock stockBack = getStockRandomSampleGenerator();

        stockPosition.setStock(stockBack);
        assertThat(stockPosition.getStock()).isEqualTo(stockBack);

        stockPosition.stock(null);
        assertThat(stockPosition.getStock()).isNull();
    }
}
