package com.finance.tracker.breakout.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.finance.tracker.breakout.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StockPositionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockPositionDTO.class);
        StockPositionDTO stockPositionDTO1 = new StockPositionDTO();
        stockPositionDTO1.setId(1L);
        StockPositionDTO stockPositionDTO2 = new StockPositionDTO();
        assertThat(stockPositionDTO1).isNotEqualTo(stockPositionDTO2);
        stockPositionDTO2.setId(stockPositionDTO1.getId());
        assertThat(stockPositionDTO1).isEqualTo(stockPositionDTO2);
        stockPositionDTO2.setId(2L);
        assertThat(stockPositionDTO1).isNotEqualTo(stockPositionDTO2);
        stockPositionDTO1.setId(null);
        assertThat(stockPositionDTO1).isNotEqualTo(stockPositionDTO2);
    }
}
