package com.finance.tracker.breakout.service;

import com.finance.tracker.breakout.service.dto.StockDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.finance.tracker.breakout.domain.Stock}.
 */
public interface StockService {
    /**
     * Save a stock.
     *
     * @param stockDTO the entity to save.
     * @return the persisted entity.
     */
    StockDTO save(StockDTO stockDTO);

    /**
     * Updates a stock.
     *
     * @param stockDTO the entity to update.
     * @return the persisted entity.
     */
    StockDTO update(StockDTO stockDTO);

    /**
     * Partially updates a stock.
     *
     * @param stockDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StockDTO> partialUpdate(StockDTO stockDTO);

    /**
     * Get all the stocks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StockDTO> findAll(Pageable pageable);

    /**
     * Get all the StockDTO where StockDetails is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<StockDTO> findAllWhereStockDetailsIsNull();

    /**
     * Get the "id" stock.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StockDTO> findOne(Long id);

    /**
     * Delete the "id" stock.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
