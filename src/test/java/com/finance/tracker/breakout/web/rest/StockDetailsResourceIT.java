package com.finance.tracker.breakout.web.rest;

import static com.finance.tracker.breakout.domain.StockDetailsAsserts.*;
import static com.finance.tracker.breakout.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.tracker.breakout.IntegrationTest;
import com.finance.tracker.breakout.domain.StockDetails;
import com.finance.tracker.breakout.repository.StockDetailsRepository;
import com.finance.tracker.breakout.service.dto.StockDetailsDTO;
import com.finance.tracker.breakout.service.mapper.StockDetailsMapper;
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
 * Integration tests for the {@link StockDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StockDetailsResourceIT {

    private static final Float DEFAULT_CUR_PRICE = 1F;
    private static final Float UPDATED_CUR_PRICE = 2F;

    private static final Float DEFAULT_PRICE_CHANGE = 1F;
    private static final Float UPDATED_PRICE_CHANGE = 2F;

    private static final Float DEFAULT_CHANGE_PER = 1F;
    private static final Float UPDATED_CHANGE_PER = 2F;

    private static final String ENTITY_API_URL = "/api/stock-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private StockDetailsRepository stockDetailsRepository;

    @Autowired
    private StockDetailsMapper stockDetailsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStockDetailsMockMvc;

    private StockDetails stockDetails;

    private StockDetails insertedStockDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StockDetails createEntity(EntityManager em) {
        StockDetails stockDetails = new StockDetails()
            .curPrice(DEFAULT_CUR_PRICE)
            .priceChange(DEFAULT_PRICE_CHANGE)
            .changePer(DEFAULT_CHANGE_PER);
        return stockDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StockDetails createUpdatedEntity(EntityManager em) {
        StockDetails stockDetails = new StockDetails()
            .curPrice(UPDATED_CUR_PRICE)
            .priceChange(UPDATED_PRICE_CHANGE)
            .changePer(UPDATED_CHANGE_PER);
        return stockDetails;
    }

    @BeforeEach
    public void initTest() {
        stockDetails = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedStockDetails != null) {
            stockDetailsRepository.delete(insertedStockDetails);
            insertedStockDetails = null;
        }
    }

    @Test
    @Transactional
    void createStockDetails() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the StockDetails
        StockDetailsDTO stockDetailsDTO = stockDetailsMapper.toDto(stockDetails);
        var returnedStockDetailsDTO = om.readValue(
            restStockDetailsMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stockDetailsDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            StockDetailsDTO.class
        );

        // Validate the StockDetails in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedStockDetails = stockDetailsMapper.toEntity(returnedStockDetailsDTO);
        assertStockDetailsUpdatableFieldsEquals(returnedStockDetails, getPersistedStockDetails(returnedStockDetails));

        insertedStockDetails = returnedStockDetails;
    }

    @Test
    @Transactional
    void createStockDetailsWithExistingId() throws Exception {
        // Create the StockDetails with an existing ID
        stockDetails.setId(1L);
        StockDetailsDTO stockDetailsDTO = stockDetailsMapper.toDto(stockDetails);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stockDetailsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StockDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStockDetails() throws Exception {
        // Initialize the database
        insertedStockDetails = stockDetailsRepository.saveAndFlush(stockDetails);

        // Get all the stockDetailsList
        restStockDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].curPrice").value(hasItem(DEFAULT_CUR_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].priceChange").value(hasItem(DEFAULT_PRICE_CHANGE.doubleValue())))
            .andExpect(jsonPath("$.[*].changePer").value(hasItem(DEFAULT_CHANGE_PER.doubleValue())));
    }

    @Test
    @Transactional
    void getStockDetails() throws Exception {
        // Initialize the database
        insertedStockDetails = stockDetailsRepository.saveAndFlush(stockDetails);

        // Get the stockDetails
        restStockDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, stockDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(stockDetails.getId().intValue()))
            .andExpect(jsonPath("$.curPrice").value(DEFAULT_CUR_PRICE.doubleValue()))
            .andExpect(jsonPath("$.priceChange").value(DEFAULT_PRICE_CHANGE.doubleValue()))
            .andExpect(jsonPath("$.changePer").value(DEFAULT_CHANGE_PER.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingStockDetails() throws Exception {
        // Get the stockDetails
        restStockDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStockDetails() throws Exception {
        // Initialize the database
        insertedStockDetails = stockDetailsRepository.saveAndFlush(stockDetails);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the stockDetails
        StockDetails updatedStockDetails = stockDetailsRepository.findById(stockDetails.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStockDetails are not directly saved in db
        em.detach(updatedStockDetails);
        updatedStockDetails.curPrice(UPDATED_CUR_PRICE).priceChange(UPDATED_PRICE_CHANGE).changePer(UPDATED_CHANGE_PER);
        StockDetailsDTO stockDetailsDTO = stockDetailsMapper.toDto(updatedStockDetails);

        restStockDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stockDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(stockDetailsDTO))
            )
            .andExpect(status().isOk());

        // Validate the StockDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedStockDetailsToMatchAllProperties(updatedStockDetails);
    }

    @Test
    @Transactional
    void putNonExistingStockDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stockDetails.setId(longCount.incrementAndGet());

        // Create the StockDetails
        StockDetailsDTO stockDetailsDTO = stockDetailsMapper.toDto(stockDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stockDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(stockDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStockDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stockDetails.setId(longCount.incrementAndGet());

        // Create the StockDetails
        StockDetailsDTO stockDetailsDTO = stockDetailsMapper.toDto(stockDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(stockDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStockDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stockDetails.setId(longCount.incrementAndGet());

        // Create the StockDetails
        StockDetailsDTO stockDetailsDTO = stockDetailsMapper.toDto(stockDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockDetailsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stockDetailsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StockDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStockDetailsWithPatch() throws Exception {
        // Initialize the database
        insertedStockDetails = stockDetailsRepository.saveAndFlush(stockDetails);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the stockDetails using partial update
        StockDetails partialUpdatedStockDetails = new StockDetails();
        partialUpdatedStockDetails.setId(stockDetails.getId());

        partialUpdatedStockDetails.priceChange(UPDATED_PRICE_CHANGE).changePer(UPDATED_CHANGE_PER);

        restStockDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStockDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStockDetails))
            )
            .andExpect(status().isOk());

        // Validate the StockDetails in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStockDetailsUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedStockDetails, stockDetails),
            getPersistedStockDetails(stockDetails)
        );
    }

    @Test
    @Transactional
    void fullUpdateStockDetailsWithPatch() throws Exception {
        // Initialize the database
        insertedStockDetails = stockDetailsRepository.saveAndFlush(stockDetails);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the stockDetails using partial update
        StockDetails partialUpdatedStockDetails = new StockDetails();
        partialUpdatedStockDetails.setId(stockDetails.getId());

        partialUpdatedStockDetails.curPrice(UPDATED_CUR_PRICE).priceChange(UPDATED_PRICE_CHANGE).changePer(UPDATED_CHANGE_PER);

        restStockDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStockDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStockDetails))
            )
            .andExpect(status().isOk());

        // Validate the StockDetails in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStockDetailsUpdatableFieldsEquals(partialUpdatedStockDetails, getPersistedStockDetails(partialUpdatedStockDetails));
    }

    @Test
    @Transactional
    void patchNonExistingStockDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stockDetails.setId(longCount.incrementAndGet());

        // Create the StockDetails
        StockDetailsDTO stockDetailsDTO = stockDetailsMapper.toDto(stockDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, stockDetailsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(stockDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStockDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stockDetails.setId(longCount.incrementAndGet());

        // Create the StockDetails
        StockDetailsDTO stockDetailsDTO = stockDetailsMapper.toDto(stockDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(stockDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStockDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stockDetails.setId(longCount.incrementAndGet());

        // Create the StockDetails
        StockDetailsDTO stockDetailsDTO = stockDetailsMapper.toDto(stockDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockDetailsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(stockDetailsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StockDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStockDetails() throws Exception {
        // Initialize the database
        insertedStockDetails = stockDetailsRepository.saveAndFlush(stockDetails);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the stockDetails
        restStockDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, stockDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return stockDetailsRepository.count();
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

    protected StockDetails getPersistedStockDetails(StockDetails stockDetails) {
        return stockDetailsRepository.findById(stockDetails.getId()).orElseThrow();
    }

    protected void assertPersistedStockDetailsToMatchAllProperties(StockDetails expectedStockDetails) {
        assertStockDetailsAllPropertiesEquals(expectedStockDetails, getPersistedStockDetails(expectedStockDetails));
    }

    protected void assertPersistedStockDetailsToMatchUpdatableProperties(StockDetails expectedStockDetails) {
        assertStockDetailsAllUpdatablePropertiesEquals(expectedStockDetails, getPersistedStockDetails(expectedStockDetails));
    }
}
