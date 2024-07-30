package thread.start.problem;

import static util.MyLogger.log;

public class Problem4 {

    private static final int NUM = 5;

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            for (int i = 1;; i++) {
                log(String.format("A"));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException();
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 1;; i++) {
                log(String.format("B"));
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException();
                }
            }
        });

        thread1.start();
        thread2.start();
    }
}
