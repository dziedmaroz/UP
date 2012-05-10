package com.exadel.education.threading.lecture.synchronization.atomic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Sergey Derugo
 * Date: 2/21/12
 * Time: 3:41 AM
 */
public class AtomicCounter implements Counter {
    private AtomicInteger value = new AtomicInteger();

    public int getValue() {
        return value.get();
    }

    public int increment() {
        return value.incrementAndGet();
    }

    // Alternative implementation as increment but just make the
    // implementation explicit
    public int incrementLongVersion() {
        int oldValue = value.get();
        while (!value.compareAndSet(oldValue, oldValue + 1)) {
            oldValue = value.get();
        }
        return oldValue + 1;
    }

}
