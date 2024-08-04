package thread.sync.test;

public class SyncTest1Main {

    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();

        Runnable task = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10000; i++) {
                    counter.increment();
                }
            }
        };

        Thread thread1 = new Thread(task);
        Thread thread2 = new Thread(task);
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println("결과: " + counter.getCount());
    }

    static class Counter {

        private int count = 0;

        /**
         * 3번의 계산이 발생한다.
         * 1. count 값을 읽는다.
         * 2. 읽은 count 값에 1을 더한다.
         * 3. count에 1을 더한 값을 저장한다.
         * 임계 영역으로 설정하지 않으면 thread1이 2번을 수행하기 직전에, thread2가 1번을 진행하여
         * 두 스레드가 동일한 count 값을 읽고 나머지 2~3번의 계산을 진행하게 된다.
         */
        public synchronized void increment() {
            count = count + 1;
        }

        public int getCount() {
            return count;
        }
    }
}
