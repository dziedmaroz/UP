package com.exadel.education.threading.lecture.basics;

/**
 * Created by Sergey Derugo
 * Date: 2/9/12
 * Time: 1:17 PM
 */
public class DemoThread extends Thread {
    @Override
    public void run() {
        System.out.println("Hello world from other thread");
    }

    public static void main(String[] args) {
        DemoThread thread = new DemoThread();
        thread.start();

        System.out.println("Hello from the main thread");
    }
}
