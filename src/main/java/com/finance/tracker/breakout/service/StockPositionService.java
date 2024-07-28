package com.finance.tracker.breakout.service;

import com.finance.tracker.breakout.service.dto.StockPositionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.finance.tracker.breakout.domain.StockPosition}.
 */
public interface StockPositionService {
    /**
     * Save a stockPosition.
     *
     * @param stockPositionDTO the entity to save.
     * @return the persisted entity.
     */
    StockPositionDTO save(StockPositionDTO stockPositionDTO);

    /**
     * Updates a stockPosition.
     *
     * @param stockPositionDTO the entity to update.
     * @return the persisted entity.
     */
    StockPositionDTO update(StockPositionDTO stockPositionDTO);

    /**
     * Partially updates a stockPosition.
     *
     * @param stockPositionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StockPositionDTO> partialUpdate(StockPositionDTO stockPositionDTO);

    /**
     * Get all the stockPositions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StockPositionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" stockPosition.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StockPositionDTO> findOne(Long id);

    /**
     * Delete the "id" stockPosition.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
