package com.finance.tracker.breakout.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class StockPositionAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertStockPositionAllPropertiesEquals(StockPosition expected, StockPosition actual) {
        assertStockPositionAutoGeneratedPropertiesEquals(expected, actual);
        assertStockPositionAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertStockPositionAllUpdatablePropertiesEquals(StockPosition expected, StockPosition actual) {
        assertStockPositionUpdatableFieldsEquals(expected, actual);
        assertStockPositionUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertStockPositionAutoGeneratedPropertiesEquals(StockPosition expected, StockPosition actual) {
        assertThat(expected)
            .as("Verify StockPosition auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertStockPositionUpdatableFieldsEquals(StockPosition expected, StockPosition actual) {
        assertThat(expected)
            .as("Verify StockPosition relevant properties")
            .satisfies(e -> assertThat(e.getBuyingPrice()).as("check buyingPrice").isEqualTo(actual.getBuyingPrice()))
            .satisfies(e -> assertThat(e.getExitPrice()).as("check exitPrice").isEqualTo(actual.getExitPrice()))
            .satisfies(e -> assertThat(e.getSold()).as("check sold").isEqualTo(actual.getSold()))
            .satisfies(e -> assertThat(e.getQuantity()).as("check quantity").isEqualTo(actual.getQuantity()))
            .satisfies(e -> assertThat(e.getComments()).as("check comments").isEqualTo(actual.getComments()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertStockPositionUpdatableRelationshipsEquals(StockPosition expected, StockPosition actual) {
        assertThat(expected)
            .as("Verify StockPosition relationships")
            .satisfies(e -> assertThat(e.getStock()).as("check stock").isEqualTo(actual.getStock()));
    }
}
