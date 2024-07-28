package com.finance.tracker.breakout.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class StockRecommendationAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertStockRecommendationAllPropertiesEquals(StockRecommendation expected, StockRecommendation actual) {
        assertStockRecommendationAutoGeneratedPropertiesEquals(expected, actual);
        assertStockRecommendationAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertStockRecommendationAllUpdatablePropertiesEquals(StockRecommendation expected, StockRecommendation actual) {
        assertStockRecommendationUpdatableFieldsEquals(expected, actual);
        assertStockRecommendationUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertStockRecommendationAutoGeneratedPropertiesEquals(StockRecommendation expected, StockRecommendation actual) {
        assertThat(expected)
            .as("Verify StockRecommendation auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertStockRecommendationUpdatableFieldsEquals(StockRecommendation expected, StockRecommendation actual) {
        assertThat(expected)
            .as("Verify StockRecommendation relevant properties")
            .satisfies(e -> assertThat(e.getEntry()).as("check entry").isEqualTo(actual.getEntry()))
            .satisfies(e -> assertThat(e.getStopLoss()).as("check stopLoss").isEqualTo(actual.getStopLoss()))
            .satisfies(e -> assertThat(e.getTarget()).as("check target").isEqualTo(actual.getTarget()))
            .satisfies(e -> assertThat(e.getQuantity()).as("check quantity").isEqualTo(actual.getQuantity()))
            .satisfies(e -> assertThat(e.getComments()).as("check comments").isEqualTo(actual.getComments()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertStockRecommendationUpdatableRelationshipsEquals(StockRecommendation expected, StockRecommendation actual) {
        assertThat(expected)
            .as("Verify StockRecommendation relationships")
            .satisfies(e -> assertThat(e.getStock()).as("check stock").isEqualTo(actual.getStock()));
    }
}
