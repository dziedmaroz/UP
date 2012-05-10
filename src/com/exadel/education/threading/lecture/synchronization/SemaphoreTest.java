package com.exadel.education.threading.lecture.synchronization;

import java.util.concurrent.Semaphore;

/**
 * Created by Sergey Derugo
 * Date: 2/9/12
 * Time: 2:26 PM
 */
public class SemaphoreTest {
    public static void main(String[] args) throws InterruptedException {
        int permits = 2;
        Semaphore semaphore = new Semaphore(permits);
        semaphore.drainPermits();

        new SimpleThread("A", semaphore).start();
        new SimpleThread("B", semaphore).start();

        System.out.println("Release semaphore. Now threads can work.");
        semaphore.release(permits);
    }
}

class SimpleThread extends Thread {
    private final Semaphore semaphore;
    private final String name;

    public SimpleThread(String name, Semaphore semaphore) {
        this.semaphore = semaphore;
        this.name = name;
    }


    @Override
    public void run() {
        try {
            semaphore.acquire();
            //Some work here
            semaphore.release();

            System.out.println("Thread [" + name + "] has finished");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
