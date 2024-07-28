package com.finance.tracker.breakout.service;

import com.finance.tracker.breakout.service.dto.StockDetailsDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.finance.tracker.breakout.domain.StockDetails}.
 */
public interface StockDetailsService {
    /**
     * Save a stockDetails.
     *
     * @param stockDetailsDTO the entity to save.
     * @return the persisted entity.
     */
    StockDetailsDTO save(StockDetailsDTO stockDetailsDTO);

    /**
     * Updates a stockDetails.
     *
     * @param stockDetailsDTO the entity to update.
     * @return the persisted entity.
     */
    StockDetailsDTO update(StockDetailsDTO stockDetailsDTO);

    /**
     * Partially updates a stockDetails.
     *
     * @param stockDetailsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StockDetailsDTO> partialUpdate(StockDetailsDTO stockDetailsDTO);

    /**
     * Get all the stockDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StockDetailsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" stockDetails.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StockDetailsDTO> findOne(Long id);

    /**
     * Delete the "id" stockDetails.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
