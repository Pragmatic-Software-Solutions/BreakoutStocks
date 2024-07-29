package com.finance.tracker.breakout.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class StockTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Stock getStockSample1() {
        return new Stock().id(1L).symbol("symbol1").exchange("exchange1");
    }

    public static Stock getStockSample2() {
        return new Stock().id(2L).symbol("symbol2").exchange("exchange2");
    }

    public static Stock getStockRandomSampleGenerator() {
        return new Stock().id(longCount.incrementAndGet()).symbol(UUID.randomUUID().toString()).exchange(UUID.randomUUID().toString());
    }
}
