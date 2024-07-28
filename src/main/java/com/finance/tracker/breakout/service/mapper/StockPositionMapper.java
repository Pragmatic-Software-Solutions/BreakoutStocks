package com.finance.tracker.breakout.service.mapper;

import com.finance.tracker.breakout.domain.Stock;
import com.finance.tracker.breakout.domain.StockPosition;
import com.finance.tracker.breakout.service.dto.StockDTO;
import com.finance.tracker.breakout.service.dto.StockPositionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StockPosition} and its DTO {@link StockPositionDTO}.
 */
@Mapper(componentModel = "spring")
public interface StockPositionMapper extends EntityMapper<StockPositionDTO, StockPosition> {
    @Mapping(target = "stock", source = "stock", qualifiedByName = "stockId")
    StockPositionDTO toDto(StockPosition s);

    @Named("stockId")
    //    @BeanMapping(ignoreByDefault = true)
    //    @Mapping(target = "id", source = "id")
    StockDTO toDtoStockId(Stock stock);
}
