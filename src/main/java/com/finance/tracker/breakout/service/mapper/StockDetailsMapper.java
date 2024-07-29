package com.finance.tracker.breakout.service.mapper;

import com.finance.tracker.breakout.domain.Stock;
import com.finance.tracker.breakout.domain.StockDetails;
import com.finance.tracker.breakout.service.dto.StockDTO;
import com.finance.tracker.breakout.service.dto.StockDetailsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StockDetails} and its DTO {@link StockDetailsDTO}.
 */
@Mapper(componentModel = "spring")
public interface StockDetailsMapper extends EntityMapper<StockDetailsDTO, StockDetails> {
    @Mapping(target = "stock", source = "stock", qualifiedByName = "stockId")
    StockDetailsDTO toDto(StockDetails s);

    @Named("stockId")
    //    @BeanMapping(ignoreByDefault = true)
    //    @Mapping(target = "id", source = "id")
    StockDTO toDtoStockId(Stock stock);
}
