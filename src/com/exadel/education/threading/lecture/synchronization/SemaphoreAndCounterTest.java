package com.exadel.education.threading.lecture.synchronization;

import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Sergey Derugo
 * Date: 2/8/12
 * Time: 5:04 AM
 */
public class SemaphoreAndCounterTest {

    private static class MyThread implements Runnable {
        private final Phaser phaser;
        private final Semaphore semaphore;
        private AtomicInteger counter;
        private String name;

        private MyThread(String name, Phaser phaser, AtomicInteger counter, Semaphore semaphore) {
            this.phaser = phaser;
            this.counter = counter;
            this.name = name;
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            try {
                semaphore.acquire();
                //phaser.register();

                Random random = new Random();

                for (int i = 0; i < 10; i++) {
                    int time = Math.abs(random.nextInt()) % 100;
                    Thread.sleep(time);
                    int count = counter.incrementAndGet();
                    System.out.println(name + ": count = " + count);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int arrive = phaser.arrive();
            System.out.println("Thread [" + name + "] arrived: " + arrive);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final int permits = 2;

        Phaser phaser = new Phaser(2);
        //int register = phaser.register();
        //System.out.println("register = " + register);


        Semaphore semaphore = new Semaphore(permits);
        semaphore.acquire(permits);

        AtomicInteger counter = new AtomicInteger();

        new Thread(new MyThread("A", phaser, counter, semaphore)).start();
        new Thread(new MyThread("B", phaser, counter, semaphore)).start();

        System.out.println("Allow threads to work...");
        semaphore.release(permits);

        //phaser.awaitAdvance(phaser.getPhase());
        //phaser.arriveAndAwaitAdvance();
        phaser.awaitAdvance(0);
        System.out.println("Done! Counter = " + counter);

    }
}
