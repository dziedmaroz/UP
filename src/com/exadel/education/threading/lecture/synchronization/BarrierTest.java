package com.exadel.education.threading.lecture.synchronization;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by Sergey Derugo
 * Date: 2/9/12
 * Time: 10:09 AM
 */
public class BarrierTest {
    private static final int N = 2;

    public static void main(String[] args) throws InterruptedException {
        CyclicBarrier barrier = new CyclicBarrier(N, new Runnable() {
            public void run() {
                // Действия, которые выполняются при достижении барьера всеми потоками
                System.out.println("Done");
            }
        });
        for (int i = 0; i < N; i++) {
            new Worker(barrier).start();
        }
    }
}

class Worker extends Thread {
    private CyclicBarrier barrier;

    public Worker(CyclicBarrier barrier) {
        this.barrier = barrier;
    }

    @Override
    public void run() {
        // Некоторое действие
        System.out.println("Doing hard job... " + this);

        try {
            barrier.await();   //Waiting until other threads finish
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }

        System.out.println(this + " is over");
    }
}


