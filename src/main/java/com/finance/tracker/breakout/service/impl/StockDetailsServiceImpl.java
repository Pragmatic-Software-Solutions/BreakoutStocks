package com.finance.tracker.breakout.service.impl;

import com.finance.tracker.breakout.domain.StockDetails;
import com.finance.tracker.breakout.repository.StockDetailsRepository;
import com.finance.tracker.breakout.service.StockDetailsService;
import com.finance.tracker.breakout.service.dto.StockDetailsDTO;
import com.finance.tracker.breakout.service.mapper.StockDetailsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.finance.tracker.breakout.domain.StockDetails}.
 */
@Service
@Transactional
public class StockDetailsServiceImpl implements StockDetailsService {

    private static final Logger log = LoggerFactory.getLogger(StockDetailsServiceImpl.class);

    private final StockDetailsRepository stockDetailsRepository;

    private final StockDetailsMapper stockDetailsMapper;

    public StockDetailsServiceImpl(StockDetailsRepository stockDetailsRepository, StockDetailsMapper stockDetailsMapper) {
        this.stockDetailsRepository = stockDetailsRepository;
        this.stockDetailsMapper = stockDetailsMapper;
    }

    @Override
    public StockDetailsDTO save(StockDetailsDTO stockDetailsDTO) {
        log.debug("Request to save StockDetails : {}", stockDetailsDTO);
        StockDetails stockDetails = stockDetailsMapper.toEntity(stockDetailsDTO);
        stockDetails = stockDetailsRepository.save(stockDetails);
        return stockDetailsMapper.toDto(stockDetails);
    }

    @Override
    public StockDetailsDTO update(StockDetailsDTO stockDetailsDTO) {
        log.debug("Request to update StockDetails : {}", stockDetailsDTO);
        StockDetails stockDetails = stockDetailsMapper.toEntity(stockDetailsDTO);
        stockDetails = stockDetailsRepository.save(stockDetails);
        return stockDetailsMapper.toDto(stockDetails);
    }

    @Override
    public Optional<StockDetailsDTO> partialUpdate(StockDetailsDTO stockDetailsDTO) {
        log.debug("Request to partially update StockDetails : {}", stockDetailsDTO);

        return stockDetailsRepository
            .findById(stockDetailsDTO.getId())
            .map(existingStockDetails -> {
                stockDetailsMapper.partialUpdate(existingStockDetails, stockDetailsDTO);

                return existingStockDetails;
            })
            .map(stockDetailsRepository::save)
            .map(stockDetailsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StockDetailsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StockDetails");
        return stockDetailsRepository.findAll(pageable).map(stockDetailsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StockDetailsDTO> findOne(Long id) {
        log.debug("Request to get StockDetails : {}", id);
        return stockDetailsRepository.findById(id).map(stockDetailsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete StockDetails : {}", id);
        stockDetailsRepository.deleteById(id);
    }
}
