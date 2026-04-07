package com.cts.util;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

public class UIDGeneratorUtils {
    private static final AtomicLong counter = new AtomicLong();

    public static long uidGenerator() {
        long base = counter.incrementAndGet();
        long randomPart = ThreadLocalRandom.current().nextInt(100000, 999999);
        return (base % 10) * 1_000_000L + randomPart;
    }
}
