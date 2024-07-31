package thread.join;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class JoinMainV3 {

    public static void main(String[] args) throws InterruptedException {

        log("main 스레드 start");

        SumTask task1 = new SumTask(1, 50);
        SumTask task2 = new SumTask(51, 100);
        Thread thread1 = new Thread(task1, "thread-1");
        Thread thread2 = new Thread(task2, "thread-2");

        thread1.start();
        thread2.start();

        log("join() -> main 스레드는 thread1, thread2가 종료될 때까지 대기 상태로 있는다.");
        thread1.join();
        thread2.join();
        log("thread1, thread2의 작업이 완료됨에 따라 main 스레드는 wake-up 상태가 된다.");

        /*
         * 아래는 thread1과 thread2의 상태가 종료(TERMINATED)인지 확인하는 과정을 반복하기 떄문에 CPU 연산을 반복하여 수행한다.
         * 좋은 방법이 아니다.
        while(thread1.getState() != State.TERMINATED && thread2.getState() != State.TERMINATED) {

        }
         */

        log("task1.result = " + task1.result);
        log("task2.result = " + task2.result);

        int sum = task1.result + task2.result;

        log("task1 + task2 = " + sum);
        log("main 스레드 end");

        /*
         * 1212:12:11.611 [     main] main 스레드 start
         * 1212:12:11.613 [ thread-1] 작업 start
         * 1212:12:11.613 [     main] join() -> main 스레드는 thread1, thread2가 종료될 때까지 대기 상태로 있는다.
         * 1212:12:11.613 [ thread-2] 작업 start
         * 1212:12:13.628 [ thread-1] 작업 완료 result = 1275 -> thread1 스레드 작업 종료
         * 1212:12:13.628 [ thread-2] 작업 완료 result = 3775 -> thread2 스레드 작업 종료
         * 1212:12:13.629 [     main] thread1, thread2의 작업이 완료됨에 따라 main 스레드는 wake-up 상태가 된다.
         * 1212:12:13.630 [     main] task1.result = 1275
         * 1212:12:13.630 [     main] task2.result = 3775
         * 1212:12:13.630 [     main] task1 + task2 = 5050
         * 1212:12:13.631 [     main] main 스레드 end
         */
    }

    static class SumTask implements Runnable {

        int startValue;
        int endValue;
        int result = 0;

        public SumTask(int startValue, int endValue) {
            this.startValue = startValue;
            this.endValue = endValue;
        }

        @Override
        public void run() {
            log("작업 start");
            sleep(2000); // 덧셈 작업에 2초가 걸린다고 가정한다.
            int sum = 0;
            for(int i = startValue; i <= endValue; i++) {
                sum += i;
            }
            this.result = sum;
            log("작업 완료 result = " + result);
        }
    }
}
