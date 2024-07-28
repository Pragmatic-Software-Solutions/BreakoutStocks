package com.finance.tracker.breakout.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class StockPositionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static StockPosition getStockPositionSample1() {
        return new StockPosition().id(1L).quantity(1).comments("comments1");
    }

    public static StockPosition getStockPositionSample2() {
        return new StockPosition().id(2L).quantity(2).comments("comments2");
    }

    public static StockPosition getStockPositionRandomSampleGenerator() {
        return new StockPosition()
            .id(longCount.incrementAndGet())
            .quantity(intCount.incrementAndGet())
            .comments(UUID.randomUUID().toString());
    }
}
