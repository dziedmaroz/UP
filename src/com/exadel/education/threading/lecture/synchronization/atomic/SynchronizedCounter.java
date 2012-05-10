package com.exadel.education.threading.lecture.synchronization.atomic;

/**
 * Created by Sergey Derugo
 * Date: 2/21/12
 * Time: 3:41 AM
 */
public class SynchronizedCounter implements Counter {
    private int value;

    public synchronized int getValue() {
        return value;
    }

    public synchronized int increment() {
        return ++value;
    }
}
