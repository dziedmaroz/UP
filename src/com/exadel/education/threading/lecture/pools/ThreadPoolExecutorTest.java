package com.exadel.education.threading.lecture.pools;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Sergey Derugo
 * Date: 2/10/12
 * Time: 2:45 PM
 */
public class ThreadPoolExecutorTest {
    private static class HelloTask implements Callable<String> {
        private final String arg;

        private HelloTask(String arg) {
            this.arg = arg;
        }

        @Override
        public String call() throws Exception {
            return "Hello, " + arg;
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        List<Future<String>> futures = new ArrayList<>();
        futures.add(executorService.submit(new HelloTask("Java")));
        futures.add(executorService.submit(new HelloTask("world")));

        try {
            for (Future<String> future : futures) {
                String result = future.get();
                System.out.println(result);
            }
        } finally {
            executorService.shutdown();
        }

    }
}
