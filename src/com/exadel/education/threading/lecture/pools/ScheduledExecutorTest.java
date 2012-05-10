package com.exadel.education.threading.lecture.pools;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Sergey Derugo
 * Date: 2/10/12
 * Time: 2:23 PM
 */
public class ScheduledExecutorTest {
    private static class MyRunnable implements Runnable {
        @Override
        public void run() {
            System.out.println("Task is called at " + new Date());

        }
    }

    public static void main(String[] args) {
        System.out.println("Application started at " + new Date());
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.schedule(new MyRunnable(), 1, TimeUnit.SECONDS);

        executorService.shutdown();
    }
}
