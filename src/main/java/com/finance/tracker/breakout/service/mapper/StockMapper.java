package com.finance.tracker.breakout.service.mapper;

import com.finance.tracker.breakout.domain.Stock;
import com.finance.tracker.breakout.service.dto.StockDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Stock} and its DTO {@link StockDTO}.
 */
@Mapper(componentModel = "spring")
public interface StockMapper extends EntityMapper<StockDTO, Stock> {}
