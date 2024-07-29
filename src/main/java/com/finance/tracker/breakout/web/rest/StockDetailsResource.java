package com.finance.tracker.breakout.web.rest;

import com.finance.tracker.breakout.repository.StockDetailsRepository;
import com.finance.tracker.breakout.service.StockDetailsService;
import com.finance.tracker.breakout.service.dto.StockDetailsDTO;
import com.finance.tracker.breakout.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.finance.tracker.breakout.domain.StockDetails}.
 */
@RestController
@RequestMapping("/api/stock-details")
public class StockDetailsResource {

    private static final Logger log = LoggerFactory.getLogger(StockDetailsResource.class);

    private static final String ENTITY_NAME = "stockDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StockDetailsService stockDetailsService;

    private final StockDetailsRepository stockDetailsRepository;

    public StockDetailsResource(StockDetailsService stockDetailsService, StockDetailsRepository stockDetailsRepository) {
        this.stockDetailsService = stockDetailsService;
        this.stockDetailsRepository = stockDetailsRepository;
    }

    /**
     * {@code POST  /stock-details} : Create a new stockDetails.
     *
     * @param stockDetailsDTO the stockDetailsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new stockDetailsDTO, or with status {@code 400 (Bad Request)} if the stockDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<StockDetailsDTO> createStockDetails(@RequestBody StockDetailsDTO stockDetailsDTO) throws URISyntaxException {
        log.debug("REST request to save StockDetails : {}", stockDetailsDTO);
        if (stockDetailsDTO.getId() != null) {
            throw new BadRequestAlertException("A new stockDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        stockDetailsDTO = stockDetailsService.save(stockDetailsDTO);
        return ResponseEntity.created(new URI("/api/stock-details/" + stockDetailsDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, stockDetailsDTO.getId().toString()))
            .body(stockDetailsDTO);
    }

    /**
     * {@code PUT  /stock-details/:id} : Updates an existing stockDetails.
     *
     * @param id the id of the stockDetailsDTO to save.
     * @param stockDetailsDTO the stockDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stockDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the stockDetailsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the stockDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StockDetailsDTO> updateStockDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StockDetailsDTO stockDetailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update StockDetails : {}, {}", id, stockDetailsDTO);
        if (stockDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stockDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stockDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        stockDetailsDTO = stockDetailsService.update(stockDetailsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stockDetailsDTO.getId().toString()))
            .body(stockDetailsDTO);
    }

    /**
     * {@code PATCH  /stock-details/:id} : Partial updates given fields of an existing stockDetails, field will ignore if it is null
     *
     * @param id the id of the stockDetailsDTO to save.
     * @param stockDetailsDTO the stockDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stockDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the stockDetailsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the stockDetailsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the stockDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StockDetailsDTO> partialUpdateStockDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StockDetailsDTO stockDetailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update StockDetails partially : {}, {}", id, stockDetailsDTO);
        if (stockDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stockDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stockDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StockDetailsDTO> result = stockDetailsService.partialUpdate(stockDetailsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stockDetailsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /stock-details} : get all the stockDetails.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of stockDetails in body.
     */
    @GetMapping("")
    public ResponseEntity<List<StockDetailsDTO>> getAllStockDetails(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of StockDetails");
        Page<StockDetailsDTO> page = stockDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /stock-details/:id} : get the "id" stockDetails.
     *
     * @param id the id of the stockDetailsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the stockDetailsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StockDetailsDTO> getStockDetails(@PathVariable("id") Long id) {
        log.debug("REST request to get StockDetails : {}", id);
        Optional<StockDetailsDTO> stockDetailsDTO = stockDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stockDetailsDTO);
    }

    /**
     * {@code DELETE  /stock-details/:id} : delete the "id" stockDetails.
     *
     * @param id the id of the stockDetailsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStockDetails(@PathVariable("id") Long id) {
        log.debug("REST request to delete StockDetails : {}", id);
        stockDetailsService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
