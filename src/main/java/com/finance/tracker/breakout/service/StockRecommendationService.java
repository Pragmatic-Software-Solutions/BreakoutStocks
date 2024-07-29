package com.finance.tracker.breakout.service;

import com.finance.tracker.breakout.service.dto.StockRecommendationDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.finance.tracker.breakout.domain.StockRecommendation}.
 */
public interface StockRecommendationService {
    /**
     * Save a stockRecommendation.
     *
     * @param stockRecommendationDTO the entity to save.
     * @return the persisted entity.
     */
    StockRecommendationDTO save(StockRecommendationDTO stockRecommendationDTO);

    /**
     * Updates a stockRecommendation.
     *
     * @param stockRecommendationDTO the entity to update.
     * @return the persisted entity.
     */
    StockRecommendationDTO update(StockRecommendationDTO stockRecommendationDTO);

    /**
     * Partially updates a stockRecommendation.
     *
     * @param stockRecommendationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StockRecommendationDTO> partialUpdate(StockRecommendationDTO stockRecommendationDTO);

    /**
     * Get all the stockRecommendations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StockRecommendationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" stockRecommendation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StockRecommendationDTO> findOne(Long id);

    /**
     * Delete the "id" stockRecommendation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
