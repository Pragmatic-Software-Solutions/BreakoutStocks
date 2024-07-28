package com.finance.tracker.breakout.service.mapper;

import com.finance.tracker.breakout.domain.Stock;
import com.finance.tracker.breakout.domain.StockRecommendation;
import com.finance.tracker.breakout.service.dto.StockDTO;
import com.finance.tracker.breakout.service.dto.StockRecommendationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StockRecommendation} and its DTO {@link StockRecommendationDTO}.
 */
@Mapper(componentModel = "spring")
public interface StockRecommendationMapper extends EntityMapper<StockRecommendationDTO, StockRecommendation> {
    @Mapping(target = "stock", source = "stock", qualifiedByName = "stockId")
    StockRecommendationDTO toDto(StockRecommendation s);

    @Named("stockId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StockDTO toDtoStockId(Stock stock);
}
