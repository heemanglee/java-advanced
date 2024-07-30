package thread.start.problem;

import static util.MyLogger.log;

public class Problem3 {

    public static void main(String[] args) {
        Thread t1 = new Thread(new MyRunnable(1000, "A"));
        Thread t2 = new Thread(new MyRunnable(500, "B"));

        t1.start();
        t2.start();
    }

    static class MyRunnable implements Runnable {

        private final int sleepMs;
        private final String content;

        public MyRunnable(int sleepMs, String content) {
            this.sleepMs = sleepMs;
            this.content = content;
        }

        @Override
        public void run() {
            while (true) {
                log(String.format(content));
                try {
                    Thread.sleep(sleepMs);
                } catch (InterruptedException e) {
                    throw new RuntimeException();
                }
            }
        }
    }

}
