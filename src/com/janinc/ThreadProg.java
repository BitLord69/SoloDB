package com.janinc;


public class ThreadProg {
    public ThreadProg() {
        Thread a  = new Thread(this::loop, "A");
        Thread b  = new Thread(this::loop, "B");

        a.setPriority(10);
        b.setPriority(1);

        a.start();
        b.start();
    }

    private void loop() {
        for (int i = 0; i < 10000; i++) {
            System.out.println(Thread.currentThread() + " " + i);
        }
    }

    public static void main(String[] args) {
        new ThreadProg();
    }
}
