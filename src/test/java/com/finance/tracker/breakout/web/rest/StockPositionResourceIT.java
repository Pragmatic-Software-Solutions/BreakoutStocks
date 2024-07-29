package com.finance.tracker.breakout.web.rest;

import static com.finance.tracker.breakout.domain.StockPositionAsserts.*;
import static com.finance.tracker.breakout.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.tracker.breakout.IntegrationTest;
import com.finance.tracker.breakout.domain.StockPosition;
import com.finance.tracker.breakout.repository.StockPositionRepository;
import com.finance.tracker.breakout.service.dto.StockPositionDTO;
import com.finance.tracker.breakout.service.mapper.StockPositionMapper;
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
 * Integration tests for the {@link StockPositionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StockPositionResourceIT {

    private static final Float DEFAULT_BUYING_PRICE = 1F;
    private static final Float UPDATED_BUYING_PRICE = 2F;

    private static final Float DEFAULT_EXIT_PRICE = 1F;
    private static final Float UPDATED_EXIT_PRICE = 2F;

    private static final Boolean DEFAULT_SOLD = false;
    private static final Boolean UPDATED_SOLD = true;

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/stock-positions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private StockPositionRepository stockPositionRepository;

    @Autowired
    private StockPositionMapper stockPositionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStockPositionMockMvc;

    private StockPosition stockPosition;

    private StockPosition insertedStockPosition;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StockPosition createEntity(EntityManager em) {
        StockPosition stockPosition = new StockPosition()
            .buyingPrice(DEFAULT_BUYING_PRICE)
            .exitPrice(DEFAULT_EXIT_PRICE)
            .sold(DEFAULT_SOLD)
            .quantity(DEFAULT_QUANTITY)
            .comments(DEFAULT_COMMENTS);
        return stockPosition;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StockPosition createUpdatedEntity(EntityManager em) {
        StockPosition stockPosition = new StockPosition()
            .buyingPrice(UPDATED_BUYING_PRICE)
            .exitPrice(UPDATED_EXIT_PRICE)
            .sold(UPDATED_SOLD)
            .quantity(UPDATED_QUANTITY)
            .comments(UPDATED_COMMENTS);
        return stockPosition;
    }

    @BeforeEach
    public void initTest() {
        stockPosition = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedStockPosition != null) {
            stockPositionRepository.delete(insertedStockPosition);
            insertedStockPosition = null;
        }
    }

    @Test
    @Transactional
    void createStockPosition() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the StockPosition
        StockPositionDTO stockPositionDTO = stockPositionMapper.toDto(stockPosition);
        var returnedStockPositionDTO = om.readValue(
            restStockPositionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stockPositionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            StockPositionDTO.class
        );

        // Validate the StockPosition in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedStockPosition = stockPositionMapper.toEntity(returnedStockPositionDTO);
        assertStockPositionUpdatableFieldsEquals(returnedStockPosition, getPersistedStockPosition(returnedStockPosition));

        insertedStockPosition = returnedStockPosition;
    }

    @Test
    @Transactional
    void createStockPositionWithExistingId() throws Exception {
        // Create the StockPosition with an existing ID
        stockPosition.setId(1L);
        StockPositionDTO stockPositionDTO = stockPositionMapper.toDto(stockPosition);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockPositionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stockPositionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StockPosition in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStockPositions() throws Exception {
        // Initialize the database
        insertedStockPosition = stockPositionRepository.saveAndFlush(stockPosition);

        // Get all the stockPositionList
        restStockPositionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockPosition.getId().intValue())))
            .andExpect(jsonPath("$.[*].buyingPrice").value(hasItem(DEFAULT_BUYING_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].exitPrice").value(hasItem(DEFAULT_EXIT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].sold").value(hasItem(DEFAULT_SOLD.booleanValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)));
    }

    @Test
    @Transactional
    void getStockPosition() throws Exception {
        // Initialize the database
        insertedStockPosition = stockPositionRepository.saveAndFlush(stockPosition);

        // Get the stockPosition
        restStockPositionMockMvc
            .perform(get(ENTITY_API_URL_ID, stockPosition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(stockPosition.getId().intValue()))
            .andExpect(jsonPath("$.buyingPrice").value(DEFAULT_BUYING_PRICE.doubleValue()))
            .andExpect(jsonPath("$.exitPrice").value(DEFAULT_EXIT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.sold").value(DEFAULT_SOLD.booleanValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS));
    }

    @Test
    @Transactional
    void getNonExistingStockPosition() throws Exception {
        // Get the stockPosition
        restStockPositionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStockPosition() throws Exception {
        // Initialize the database
        insertedStockPosition = stockPositionRepository.saveAndFlush(stockPosition);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the stockPosition
        StockPosition updatedStockPosition = stockPositionRepository.findById(stockPosition.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStockPosition are not directly saved in db
        em.detach(updatedStockPosition);
        updatedStockPosition
            .buyingPrice(UPDATED_BUYING_PRICE)
            .exitPrice(UPDATED_EXIT_PRICE)
            .sold(UPDATED_SOLD)
            .quantity(UPDATED_QUANTITY)
            .comments(UPDATED_COMMENTS);
        StockPositionDTO stockPositionDTO = stockPositionMapper.toDto(updatedStockPosition);

        restStockPositionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stockPositionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(stockPositionDTO))
            )
            .andExpect(status().isOk());

        // Validate the StockPosition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedStockPositionToMatchAllProperties(updatedStockPosition);
    }

    @Test
    @Transactional
    void putNonExistingStockPosition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stockPosition.setId(longCount.incrementAndGet());

        // Create the StockPosition
        StockPositionDTO stockPositionDTO = stockPositionMapper.toDto(stockPosition);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockPositionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stockPositionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(stockPositionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockPosition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStockPosition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stockPosition.setId(longCount.incrementAndGet());

        // Create the StockPosition
        StockPositionDTO stockPositionDTO = stockPositionMapper.toDto(stockPosition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockPositionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(stockPositionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockPosition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStockPosition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stockPosition.setId(longCount.incrementAndGet());

        // Create the StockPosition
        StockPositionDTO stockPositionDTO = stockPositionMapper.toDto(stockPosition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockPositionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stockPositionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StockPosition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStockPositionWithPatch() throws Exception {
        // Initialize the database
        insertedStockPosition = stockPositionRepository.saveAndFlush(stockPosition);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the stockPosition using partial update
        StockPosition partialUpdatedStockPosition = new StockPosition();
        partialUpdatedStockPosition.setId(stockPosition.getId());

        partialUpdatedStockPosition.buyingPrice(UPDATED_BUYING_PRICE).quantity(UPDATED_QUANTITY);

        restStockPositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStockPosition.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStockPosition))
            )
            .andExpect(status().isOk());

        // Validate the StockPosition in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStockPositionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedStockPosition, stockPosition),
            getPersistedStockPosition(stockPosition)
        );
    }

    @Test
    @Transactional
    void fullUpdateStockPositionWithPatch() throws Exception {
        // Initialize the database
        insertedStockPosition = stockPositionRepository.saveAndFlush(stockPosition);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the stockPosition using partial update
        StockPosition partialUpdatedStockPosition = new StockPosition();
        partialUpdatedStockPosition.setId(stockPosition.getId());

        partialUpdatedStockPosition
            .buyingPrice(UPDATED_BUYING_PRICE)
            .exitPrice(UPDATED_EXIT_PRICE)
            .sold(UPDATED_SOLD)
            .quantity(UPDATED_QUANTITY)
            .comments(UPDATED_COMMENTS);

        restStockPositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStockPosition.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStockPosition))
            )
            .andExpect(status().isOk());

        // Validate the StockPosition in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStockPositionUpdatableFieldsEquals(partialUpdatedStockPosition, getPersistedStockPosition(partialUpdatedStockPosition));
    }

    @Test
    @Transactional
    void patchNonExistingStockPosition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stockPosition.setId(longCount.incrementAndGet());

        // Create the StockPosition
        StockPositionDTO stockPositionDTO = stockPositionMapper.toDto(stockPosition);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockPositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, stockPositionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(stockPositionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockPosition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStockPosition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stockPosition.setId(longCount.incrementAndGet());

        // Create the StockPosition
        StockPositionDTO stockPositionDTO = stockPositionMapper.toDto(stockPosition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockPositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(stockPositionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockPosition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStockPosition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stockPosition.setId(longCount.incrementAndGet());

        // Create the StockPosition
        StockPositionDTO stockPositionDTO = stockPositionMapper.toDto(stockPosition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockPositionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(stockPositionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StockPosition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStockPosition() throws Exception {
        // Initialize the database
        insertedStockPosition = stockPositionRepository.saveAndFlush(stockPosition);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the stockPosition
        restStockPositionMockMvc
            .perform(delete(ENTITY_API_URL_ID, stockPosition.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return stockPositionRepository.count();
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

    protected StockPosition getPersistedStockPosition(StockPosition stockPosition) {
        return stockPositionRepository.findById(stockPosition.getId()).orElseThrow();
    }

    protected void assertPersistedStockPositionToMatchAllProperties(StockPosition expectedStockPosition) {
        assertStockPositionAllPropertiesEquals(expectedStockPosition, getPersistedStockPosition(expectedStockPosition));
    }

    protected void assertPersistedStockPositionToMatchUpdatableProperties(StockPosition expectedStockPosition) {
        assertStockPositionAllUpdatablePropertiesEquals(expectedStockPosition, getPersistedStockPosition(expectedStockPosition));
    }
}
