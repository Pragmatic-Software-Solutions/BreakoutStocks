package com.finance.tracker.breakout.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class StockDetailsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static StockDetails getStockDetailsSample1() {
        return new StockDetails().id(1L);
    }

    public static StockDetails getStockDetailsSample2() {
        return new StockDetails().id(2L);
    }

    public static StockDetails getStockDetailsRandomSampleGenerator() {
        return new StockDetails().id(longCount.incrementAndGet());
    }
}
