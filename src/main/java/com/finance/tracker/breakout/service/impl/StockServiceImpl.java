package com.finance.tracker.breakout.service.impl;

import com.finance.tracker.breakout.domain.Stock;
import com.finance.tracker.breakout.repository.StockRepository;
import com.finance.tracker.breakout.service.StockService;
import com.finance.tracker.breakout.service.dto.StockDTO;
import com.finance.tracker.breakout.service.mapper.StockMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.finance.tracker.breakout.domain.Stock}.
 */
@Service
@Transactional
public class StockServiceImpl implements StockService {

    private static final Logger log = LoggerFactory.getLogger(StockServiceImpl.class);

    private final StockRepository stockRepository;

    private final StockMapper stockMapper;

    public StockServiceImpl(StockRepository stockRepository, StockMapper stockMapper) {
        this.stockRepository = stockRepository;
        this.stockMapper = stockMapper;
    }

    @Override
    public StockDTO save(StockDTO stockDTO) {
        log.debug("Request to save Stock : {}", stockDTO);
        Stock stock = stockMapper.toEntity(stockDTO);
        stock = stockRepository.save(stock);
        return stockMapper.toDto(stock);
    }

    @Override
    public StockDTO update(StockDTO stockDTO) {
        log.debug("Request to update Stock : {}", stockDTO);
        Stock stock = stockMapper.toEntity(stockDTO);
        stock = stockRepository.save(stock);
        return stockMapper.toDto(stock);
    }

    @Override
    public Optional<StockDTO> partialUpdate(StockDTO stockDTO) {
        log.debug("Request to partially update Stock : {}", stockDTO);

        return stockRepository
            .findById(stockDTO.getId())
            .map(existingStock -> {
                stockMapper.partialUpdate(existingStock, stockDTO);

                return existingStock;
            })
            .map(stockRepository::save)
            .map(stockMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StockDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Stocks");
        return stockRepository.findAll(pageable).map(stockMapper::toDto);
    }

    /**
     *  Get all the stocks where StockDetails is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<StockDTO> findAllWhereStockDetailsIsNull() {
        log.debug("Request to get all stocks where StockDetails is null");
        return StreamSupport.stream(stockRepository.findAll().spliterator(), false)
            .filter(stock -> stock.getStockDetails() == null)
            .map(stockMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StockDTO> findOne(Long id) {
        log.debug("Request to get Stock : {}", id);
        return stockRepository.findById(id).map(stockMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Stock : {}", id);
        stockRepository.deleteById(id);
    }
}
