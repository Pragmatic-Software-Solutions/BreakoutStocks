package com.finance.tracker.breakout.web.rest;

import static com.finance.tracker.breakout.domain.StockRecommendationAsserts.*;
import static com.finance.tracker.breakout.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.tracker.breakout.IntegrationTest;
import com.finance.tracker.breakout.domain.StockRecommendation;
import com.finance.tracker.breakout.repository.StockRecommendationRepository;
import com.finance.tracker.breakout.service.dto.StockRecommendationDTO;
import com.finance.tracker.breakout.service.mapper.StockRecommendationMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link StockRecommendationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StockRecommendationResourceIT {

    private static final Float DEFAULT_ENTRY = 1F;
    private static final Float UPDATED_ENTRY = 2F;

    private static final Float DEFAULT_STOP_LOSS = 1F;
    private static final Float UPDATED_STOP_LOSS = 2F;

    private static final Float DEFAULT_TARGET = 1F;
    private static final Float UPDATED_TARGET = 2F;

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/stock-recommendations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private StockRecommendationRepository stockRecommendationRepository;

    @Autowired
    private StockRecommendationMapper stockRecommendationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStockRecommendationMockMvc;

    private StockRecommendation stockRecommendation;

    private StockRecommendation insertedStockRecommendation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StockRecommendation createEntity(EntityManager em) {
        StockRecommendation stockRecommendation = new StockRecommendation()
            .entry(DEFAULT_ENTRY)
            .stopLoss(DEFAULT_STOP_LOSS)
            .target(DEFAULT_TARGET)
            .quantity(DEFAULT_QUANTITY)
            .comments(DEFAULT_COMMENTS);
        return stockRecommendation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StockRecommendation createUpdatedEntity(EntityManager em) {
        StockRecommendation stockRecommendation = new StockRecommendation()
            .entry(UPDATED_ENTRY)
            .stopLoss(UPDATED_STOP_LOSS)
            .target(UPDATED_TARGET)
            .quantity(UPDATED_QUANTITY)
            .comments(UPDATED_COMMENTS);
        return stockRecommendation;
    }

    @BeforeEach
    public void initTest() {
        stockRecommendation = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedStockRecommendation != null) {
            stockRecommendationRepository.delete(insertedStockRecommendation);
            insertedStockRecommendation = null;
        }
    }

    @Test
    @Transactional
    void createStockRecommendation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the StockRecommendation
        StockRecommendationDTO stockRecommendationDTO = stockRecommendationMapper.toDto(stockRecommendation);
        var returnedStockRecommendationDTO = om.readValue(
            restStockRecommendationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stockRecommendationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            StockRecommendationDTO.class
        );

        // Validate the StockRecommendation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedStockRecommendation = stockRecommendationMapper.toEntity(returnedStockRecommendationDTO);
        assertStockRecommendationUpdatableFieldsEquals(
            returnedStockRecommendation,
            getPersistedStockRecommendation(returnedStockRecommendation)
        );

        insertedStockRecommendation = returnedStockRecommendation;
    }

    @Test
    @Transactional
    void createStockRecommendationWithExistingId() throws Exception {
        // Create the StockRecommendation with an existing ID
        stockRecommendation.setId(1L);
        StockRecommendationDTO stockRecommendationDTO = stockRecommendationMapper.toDto(stockRecommendation);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockRecommendationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stockRecommendationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StockRecommendation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStockRecommendations() throws Exception {
        // Initialize the database
        insertedStockRecommendation = stockRecommendationRepository.saveAndFlush(stockRecommendation);

        // Get all the stockRecommendationList
        restStockRecommendationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockRecommendation.getId().intValue())))
            .andExpect(jsonPath("$.[*].entry").value(hasItem(DEFAULT_ENTRY.doubleValue())))
            .andExpect(jsonPath("$.[*].stopLoss").value(hasItem(DEFAULT_STOP_LOSS.doubleValue())))
            .andExpect(jsonPath("$.[*].target").value(hasItem(DEFAULT_TARGET.doubleValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)));
    }

    @Test
    @Transactional
    void getStockRecommendation() throws Exception {
        // Initialize the database
        insertedStockRecommendation = stockRecommendationRepository.saveAndFlush(stockRecommendation);

        // Get the stockRecommendation
        restStockRecommendationMockMvc
            .perform(get(ENTITY_API_URL_ID, stockRecommendation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(stockRecommendation.getId().intValue()))
            .andExpect(jsonPath("$.entry").value(DEFAULT_ENTRY.doubleValue()))
            .andExpect(jsonPath("$.stopLoss").value(DEFAULT_STOP_LOSS.doubleValue()))
            .andExpect(jsonPath("$.target").value(DEFAULT_TARGET.doubleValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS));
    }

    @Test
    @Transactional
    void getNonExistingStockRecommendation() throws Exception {
        // Get the stockRecommendation
        restStockRecommendationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStockRecommendation() throws Exception {
        // Initialize the database
        insertedStockRecommendation = stockRecommendationRepository.saveAndFlush(stockRecommendation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the stockRecommendation
        StockRecommendation updatedStockRecommendation = stockRecommendationRepository.findById(stockRecommendation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStockRecommendation are not directly saved in db
        em.detach(updatedStockRecommendation);
        updatedStockRecommendation
            .entry(UPDATED_ENTRY)
            .stopLoss(UPDATED_STOP_LOSS)
            .target(UPDATED_TARGET)
            .quantity(UPDATED_QUANTITY)
            .comments(UPDATED_COMMENTS);
        StockRecommendationDTO stockRecommendationDTO = stockRecommendationMapper.toDto(updatedStockRecommendation);

        restStockRecommendationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stockRecommendationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(stockRecommendationDTO))
            )
            .andExpect(status().isOk());

        // Validate the StockRecommendation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedStockRecommendationToMatchAllProperties(updatedStockRecommendation);
    }

    @Test
    @Transactional
    void putNonExistingStockRecommendation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stockRecommendation.setId(longCount.incrementAndGet());

        // Create the StockRecommendation
        StockRecommendationDTO stockRecommendationDTO = stockRecommendationMapper.toDto(stockRecommendation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockRecommendationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stockRecommendationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(stockRecommendationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockRecommendation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStockRecommendation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stockRecommendation.setId(longCount.incrementAndGet());

        // Create the StockRecommendation
        StockRecommendationDTO stockRecommendationDTO = stockRecommendationMapper.toDto(stockRecommendation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockRecommendationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(stockRecommendationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockRecommendation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStockRecommendation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stockRecommendation.setId(longCount.incrementAndGet());

        // Create the StockRecommendation
        StockRecommendationDTO stockRecommendationDTO = stockRecommendationMapper.toDto(stockRecommendation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockRecommendationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stockRecommendationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StockRecommendation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStockRecommendationWithPatch() throws Exception {
        // Initialize the database
        insertedStockRecommendation = stockRecommendationRepository.saveAndFlush(stockRecommendation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the stockRecommendation using partial update
        StockRecommendation partialUpdatedStockRecommendation = new StockRecommendation();
        partialUpdatedStockRecommendation.setId(stockRecommendation.getId());

        partialUpdatedStockRecommendation
            .entry(UPDATED_ENTRY)
            .stopLoss(UPDATED_STOP_LOSS)
            .quantity(UPDATED_QUANTITY)
            .comments(UPDATED_COMMENTS);

        restStockRecommendationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStockRecommendation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStockRecommendation))
            )
            .andExpect(status().isOk());

        // Validate the StockRecommendation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStockRecommendationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedStockRecommendation, stockRecommendation),
            getPersistedStockRecommendation(stockRecommendation)
        );
    }

    @Test
    @Transactional
    void fullUpdateStockRecommendationWithPatch() throws Exception {
        // Initialize the database
        insertedStockRecommendation = stockRecommendationRepository.saveAndFlush(stockRecommendation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the stockRecommendation using partial update
        StockRecommendation partialUpdatedStockRecommendation = new StockRecommendation();
        partialUpdatedStockRecommendation.setId(stockRecommendation.getId());

        partialUpdatedStockRecommendation
            .entry(UPDATED_ENTRY)
            .stopLoss(UPDATED_STOP_LOSS)
            .target(UPDATED_TARGET)
            .quantity(UPDATED_QUANTITY)
            .comments(UPDATED_COMMENTS);

        restStockRecommendationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStockRecommendation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStockRecommendation))
            )
            .andExpect(status().isOk());

        // Validate the StockRecommendation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStockRecommendationUpdatableFieldsEquals(
            partialUpdatedStockRecommendation,
            getPersistedStockRecommendation(partialUpdatedStockRecommendation)
        );
    }

    @Test
    @Transactional
    void patchNonExistingStockRecommendation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stockRecommendation.setId(longCount.incrementAndGet());

        // Create the StockRecommendation
        StockRecommendationDTO stockRecommendationDTO = stockRecommendationMapper.toDto(stockRecommendation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockRecommendationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, stockRecommendationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(stockRecommendationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockRecommendation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStockRecommendation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stockRecommendation.setId(longCount.incrementAndGet());

        // Create the StockRecommendation
        StockRecommendationDTO stockRecommendationDTO = stockRecommendationMapper.toDto(stockRecommendation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockRecommendationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(stockRecommendationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockRecommendation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStockRecommendation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stockRecommendation.setId(longCount.incrementAndGet());

        // Create the StockRecommendation
        StockRecommendationDTO stockRecommendationDTO = stockRecommendationMapper.toDto(stockRecommendation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockRecommendationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(stockRecommendationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StockRecommendation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStockRecommendation() throws Exception {
        // Initialize the database
        insertedStockRecommendation = stockRecommendationRepository.saveAndFlush(stockRecommendation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the stockRecommendation
        restStockRecommendationMockMvc
            .perform(delete(ENTITY_API_URL_ID, stockRecommendation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return stockRecommendationRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected StockRecommendation getPersistedStockRecommendation(StockRecommendation stockRecommendation) {
        return stockRecommendationRepository.findById(stockRecommendation.getId()).orElseThrow();
    }

    protected void assertPersistedStockRecommendationToMatchAllProperties(StockRecommendation expectedStockRecommendation) {
        assertStockRecommendationAllPropertiesEquals(
            expectedStockRecommendation,
            getPersistedStockRecommendation(expectedStockRecommendation)
        );
    }

    protected void assertPersistedStockRecommendationToMatchUpdatableProperties(StockRecommendation expectedStockRecommendation) {
        assertStockRecommendationAllUpdatablePropertiesEquals(
            expectedStockRecommendation,
            getPersistedStockRecommendation(expectedStockRecommendation)
        );
    }
}
