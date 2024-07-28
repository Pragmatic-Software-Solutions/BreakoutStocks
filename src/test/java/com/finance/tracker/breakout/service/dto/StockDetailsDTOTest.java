package com.finance.tracker.breakout.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.finance.tracker.breakout.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StockDetailsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockDetailsDTO.class);
        StockDetailsDTO stockDetailsDTO1 = new StockDetailsDTO();
        stockDetailsDTO1.setId(1L);
        StockDetailsDTO stockDetailsDTO2 = new StockDetailsDTO();
        assertThat(stockDetailsDTO1).isNotEqualTo(stockDetailsDTO2);
        stockDetailsDTO2.setId(stockDetailsDTO1.getId());
        assertThat(stockDetailsDTO1).isEqualTo(stockDetailsDTO2);
        stockDetailsDTO2.setId(2L);
        assertThat(stockDetailsDTO1).isNotEqualTo(stockDetailsDTO2);
        stockDetailsDTO1.setId(null);
        assertThat(stockDetailsDTO1).isNotEqualTo(stockDetailsDTO2);
    }
}
