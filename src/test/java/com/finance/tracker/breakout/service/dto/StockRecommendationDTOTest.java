package com.finance.tracker.breakout.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.finance.tracker.breakout.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StockRecommendationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockRecommendationDTO.class);
        StockRecommendationDTO stockRecommendationDTO1 = new StockRecommendationDTO();
        stockRecommendationDTO1.setId(1L);
        StockRecommendationDTO stockRecommendationDTO2 = new StockRecommendationDTO();
        assertThat(stockRecommendationDTO1).isNotEqualTo(stockRecommendationDTO2);
        stockRecommendationDTO2.setId(stockRecommendationDTO1.getId());
        assertThat(stockRecommendationDTO1).isEqualTo(stockRecommendationDTO2);
        stockRecommendationDTO2.setId(2L);
        assertThat(stockRecommendationDTO1).isNotEqualTo(stockRecommendationDTO2);
        stockRecommendationDTO1.setId(null);
        assertThat(stockRecommendationDTO1).isNotEqualTo(stockRecommendationDTO2);
    }
}
