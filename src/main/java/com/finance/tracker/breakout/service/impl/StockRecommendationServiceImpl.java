package com.finance.tracker.breakout.service.impl;

import com.finance.tracker.breakout.domain.StockRecommendation;
import com.finance.tracker.breakout.repository.StockRecommendationRepository;
import com.finance.tracker.breakout.service.StockRecommendationService;
import com.finance.tracker.breakout.service.dto.StockRecommendationDTO;
import com.finance.tracker.breakout.service.mapper.StockRecommendationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.finance.tracker.breakout.domain.StockRecommendation}.
 */
@Service
@Transactional
public class StockRecommendationServiceImpl implements StockRecommendationService {

    private static final Logger log = LoggerFactory.getLogger(StockRecommendationServiceImpl.class);

    private final StockRecommendationRepository stockRecommendationRepository;

    private final StockRecommendationMapper stockRecommendationMapper;

    public StockRecommendationServiceImpl(
        StockRecommendationRepository stockRecommendationRepository,
        StockRecommendationMapper stockRecommendationMapper
    ) {
        this.stockRecommendationRepository = stockRecommendationRepository;
        this.stockRecommendationMapper = stockRecommendationMapper;
    }

    @Override
    public StockRecommendationDTO save(StockRecommendationDTO stockRecommendationDTO) {
        log.debug("Request to save StockRecommendation : {}", stockRecommendationDTO);
        StockRecommendation stockRecommendation = stockRecommendationMapper.toEntity(stockRecommendationDTO);
        stockRecommendation = stockRecommendationRepository.save(stockRecommendation);
        return stockRecommendationMapper.toDto(stockRecommendation);
    }

    @Override
    public StockRecommendationDTO update(StockRecommendationDTO stockRecommendationDTO) {
        log.debug("Request to update StockRecommendation : {}", stockRecommendationDTO);
        StockRecommendation stockRecommendation = stockRecommendationMapper.toEntity(stockRecommendationDTO);
        stockRecommendation = stockRecommendationRepository.save(stockRecommendation);
        return stockRecommendationMapper.toDto(stockRecommendation);
    }

    @Override
    public Optional<StockRecommendationDTO> partialUpdate(StockRecommendationDTO stockRecommendationDTO) {
        log.debug("Request to partially update StockRecommendation : {}", stockRecommendationDTO);

        return stockRecommendationRepository
            .findById(stockRecommendationDTO.getId())
            .map(existingStockRecommendation -> {
                stockRecommendationMapper.partialUpdate(existingStockRecommendation, stockRecommendationDTO);

                return existingStockRecommendation;
            })
            .map(stockRecommendationRepository::save)
            .map(stockRecommendationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StockRecommendationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StockRecommendations");
        return stockRecommendationRepository.findAll(pageable).map(stockRecommendationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StockRecommendationDTO> findOne(Long id) {
        log.debug("Request to get StockRecommendation : {}", id);
        return stockRecommendationRepository.findById(id).map(stockRecommendationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete StockRecommendation : {}", id);
        stockRecommendationRepository.deleteById(id);
    }
}
