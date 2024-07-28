package com.finance.tracker.breakout.web.rest;

import com.finance.tracker.breakout.repository.StockRecommendationRepository;
import com.finance.tracker.breakout.service.StockRecommendationService;
import com.finance.tracker.breakout.service.dto.StockRecommendationDTO;
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
 * REST controller for managing {@link com.finance.tracker.breakout.domain.StockRecommendation}.
 */
@RestController
@RequestMapping("/api/stock-recommendations")
public class StockRecommendationResource {

    private static final Logger log = LoggerFactory.getLogger(StockRecommendationResource.class);

    private static final String ENTITY_NAME = "stockRecommendation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StockRecommendationService stockRecommendationService;

    private final StockRecommendationRepository stockRecommendationRepository;

    public StockRecommendationResource(
        StockRecommendationService stockRecommendationService,
        StockRecommendationRepository stockRecommendationRepository
    ) {
        this.stockRecommendationService = stockRecommendationService;
        this.stockRecommendationRepository = stockRecommendationRepository;
    }

    /**
     * {@code POST  /stock-recommendations} : Create a new stockRecommendation.
     *
     * @param stockRecommendationDTO the stockRecommendationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new stockRecommendationDTO, or with status {@code 400 (Bad Request)} if the stockRecommendation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<StockRecommendationDTO> createStockRecommendation(@RequestBody StockRecommendationDTO stockRecommendationDTO)
        throws URISyntaxException {
        log.debug("REST request to save StockRecommendation : {}", stockRecommendationDTO);
        if (stockRecommendationDTO.getId() != null) {
            throw new BadRequestAlertException("A new stockRecommendation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        stockRecommendationDTO = stockRecommendationService.save(stockRecommendationDTO);
        return ResponseEntity.created(new URI("/api/stock-recommendations/" + stockRecommendationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, stockRecommendationDTO.getId().toString()))
            .body(stockRecommendationDTO);
    }

    /**
     * {@code PUT  /stock-recommendations/:id} : Updates an existing stockRecommendation.
     *
     * @param id the id of the stockRecommendationDTO to save.
     * @param stockRecommendationDTO the stockRecommendationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stockRecommendationDTO,
     * or with status {@code 400 (Bad Request)} if the stockRecommendationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the stockRecommendationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StockRecommendationDTO> updateStockRecommendation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StockRecommendationDTO stockRecommendationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update StockRecommendation : {}, {}", id, stockRecommendationDTO);
        if (stockRecommendationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stockRecommendationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stockRecommendationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        stockRecommendationDTO = stockRecommendationService.update(stockRecommendationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stockRecommendationDTO.getId().toString()))
            .body(stockRecommendationDTO);
    }

    /**
     * {@code PATCH  /stock-recommendations/:id} : Partial updates given fields of an existing stockRecommendation, field will ignore if it is null
     *
     * @param id the id of the stockRecommendationDTO to save.
     * @param stockRecommendationDTO the stockRecommendationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stockRecommendationDTO,
     * or with status {@code 400 (Bad Request)} if the stockRecommendationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the stockRecommendationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the stockRecommendationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StockRecommendationDTO> partialUpdateStockRecommendation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StockRecommendationDTO stockRecommendationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update StockRecommendation partially : {}, {}", id, stockRecommendationDTO);
        if (stockRecommendationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stockRecommendationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stockRecommendationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StockRecommendationDTO> result = stockRecommendationService.partialUpdate(stockRecommendationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stockRecommendationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /stock-recommendations} : get all the stockRecommendations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of stockRecommendations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<StockRecommendationDTO>> getAllStockRecommendations(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of StockRecommendations");
        Page<StockRecommendationDTO> page = stockRecommendationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /stock-recommendations/:id} : get the "id" stockRecommendation.
     *
     * @param id the id of the stockRecommendationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the stockRecommendationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StockRecommendationDTO> getStockRecommendation(@PathVariable("id") Long id) {
        log.debug("REST request to get StockRecommendation : {}", id);
        Optional<StockRecommendationDTO> stockRecommendationDTO = stockRecommendationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stockRecommendationDTO);
    }

    /**
     * {@code DELETE  /stock-recommendations/:id} : delete the "id" stockRecommendation.
     *
     * @param id the id of the stockRecommendationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStockRecommendation(@PathVariable("id") Long id) {
        log.debug("REST request to delete StockRecommendation : {}", id);
        stockRecommendationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
