package com.exadel.education.threading.lecture.pools;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Created by Sergey Derugo
 * Date: 2/10/12
 * Time: 5:20 AM
 */
public class ForkJoinTest {
    private static class FindMaximumRecursiveTask extends RecursiveTask<Integer> {
        private final List<Integer> input;

        public FindMaximumRecursiveTask(List<Integer> input) {
            this.input = input;
        }

        @Override
        protected Integer compute() {
            if (input.size() <= 3) {
                return Collections.max(input);
            } else {
                int mid = input.size() / 2;
                List<Integer> left = input.subList(0, mid);
                List<Integer> right = input.subList(mid, input.size());

                FindMaximumRecursiveTask leftTask = new FindMaximumRecursiveTask(left);
                leftTask.fork();

                FindMaximumRecursiveTask rightTask = new FindMaximumRecursiveTask(right);
                rightTask.fork();

                return Math.max(leftTask.join(), rightTask.join());
            }

        }

        public static void main(String[] args) {
            List<Integer> input = Arrays.asList(1, 2, 10, 3, 4, 5, 6, 7, 8);

            ForkJoinPool pool = new ForkJoinPool();
            Integer result = pool.invoke(new FindMaximumRecursiveTask(input));

            System.out.println("result = " + result);
        }
    }

}
