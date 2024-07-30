package thread.start.problem;

import static util.MyLogger.log;

public class Problem1 {

    public static void main(String[] args) {
        CounterThread thread = new CounterThread();
        thread.start();
    }

    static class CounterThread extends Thread {

        private static final int NUM = 5;

        @Override
        public void run() {
            for(int i = 1; i <= NUM; i++) {
                log(String.format("value: %d", i));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException();
                }
            }
        }
    }

}
