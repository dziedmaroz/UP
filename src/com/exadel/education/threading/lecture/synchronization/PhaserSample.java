package com.exadel.education.threading.lecture.synchronization;

import java.util.concurrent.Phaser;

/**
 * Created by Sergey Derugo
 * Date: 2/9/12
 * Time: 8:05 AM
 */
class MyThread implements Runnable {
    private final Phaser phaser;
    private final String name;

    public MyThread(String name, Phaser phaser) {
        this.phaser = phaser;
        this.name = name;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Thread [" + name + "] is about to arrive, phaser = " + phaser);
        phaser.arrive();
        System.out.println("Thread [" + name + "] has arrived, phaser = " + phaser);
    }
}


public class PhaserSample {
    public static void main(String[] args) {
        Phaser phaser = new Phaser(2);
        System.out.println("phaser = " + phaser);
        phaser.register();
        System.out.println("New party is added, phaser = " + phaser);

        new Thread(new MyThread("A", phaser)).start();
        new Thread(new MyThread("B", phaser)).start();

        System.out.println("Wating, phaser = " + phaser);
        phaser.arriveAndAwaitAdvance();
        System.out.println("Main thread arrived, phaser= " + phaser);
    }
}

