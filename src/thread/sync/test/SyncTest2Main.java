package thread.sync.test;

import static util.MyLogger.log;

public class SyncTest2Main {

    public static void main(String[] args) throws InterruptedException {
        MyCounter myCounter = new MyCounter();
        Runnable task = new Runnable() {
            @Override
            public void run() {
                myCounter.count();
            }
        };
        Thread thread1 = new Thread(task, "Thread-1");
        Thread thread2 = new Thread(task, "Thread-2");
        thread1.start();
        thread2.start();

        /*
         * 16:08:38.766 [ Thread-1] 결과: 1000
         * 16:08:38.766 [ Thread-2] 결과: 1000
         */
    }

    static class MyCounter {

        /*
         * 각 스레드는 "독립된 스택 영역"을 갖는다.
         * 스택 영역에는 메서드 호출과 관계되는 지역 변수와 매개 변수로 저장되는 영역이다.
         * 따라서 여러 스레드가 지역 변수에 동시에 접근하더라도 동시성 문제가 발생하지 않는다.
         */
        public void count() {
            int localValue = 0;
            for (int i = 0; i < 1000; i++) {
                localValue = localValue + 1;
            }
            log("결과: " + localValue);
        }
    }
}