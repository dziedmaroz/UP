package com.exadel.education.threading.lecture.synchronization.atomic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

/**
 * Created by Sergey Derugo
 * Date: 2/10/12
 * Time: 5:27 AM
 */
public class CounterBenchmark {
    private static final int NTHREDS = 20;

    public static void main(String[] args) {
        long start = System.nanoTime();


        //final Counter counter = new AtomicCounter();
        final Counter counter = new SynchronizedCounter();
        List<Future<Integer>> list = new ArrayList<Future<Integer>>();

        ExecutorService executor = Executors.newFixedThreadPool(NTHREDS);
        for (int i = 0; i < 500; i++) {
            Callable<Integer> worker = new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    int number = 0;
                    for (int j = 0; j < 2000; j++) {
                        number = counter.increment();
                    }
                    System.out.println(number);
                    return number;
                }
            };
            Future<Integer> submit = executor.submit(worker);
            list.add(submit);

        }


        // This will make the executor accept no new threads
        // and finish all existing threads in the queue
        executor.shutdown();
        // Wait until all threads are finish
        while (!executor.isTerminated()) {
        }
        Set<Integer> set = new HashSet<Integer>();
        for (Future<Integer> future : list) {
            try {
                set.add(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        if (list.size() != set.size()) {
            throw new RuntimeException("Double-entries!!!");
        }


        long end = System.nanoTime();

        System.out.println("Time = " + (end - start) / 1000000.0);
    }
}

