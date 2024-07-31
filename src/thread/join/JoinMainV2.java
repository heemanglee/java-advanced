package thread.join;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

import java.lang.Thread.State;

public class JoinMainV2 {

    public static void main(String[] args) throws InterruptedException {

        log("main 스레드 start");

        SumTask task1 = new SumTask(1, 50);
        SumTask task2 = new SumTask(51, 100);
        Thread thread1 = new Thread(task1, "thread-1");
        Thread thread2 = new Thread(task2, "thread-2");

        thread1.start();
        thread2.start();

        log("main 스레드 sleep");
        Thread.sleep(3000); // thread1, thread2의 연산 시간을 2초로 가정하였기 때문에 main 스레드가 깨어나기 전에 모든 연산이 완료된다.
        log("main 스레드 wake-up");

        log("task1.result = " + task1.result);
        log("task2.result = " + task2.result);

        int sum = task1.result + task2.result;

        log("task1 + task2 = " + sum);
        log("main 스레드 end");

        /*
         * 1212:01:24.766 [     main] main 스레드 start
         * 1212:01:24.768 [ thread-1] 작업 start
         * 1212:01:24.768 [ thread-2] 작업 start
         * 1212:01:24.768 [     main] main 스레드 sleep -> 2초간 대기하면서 thread1, thread2의 연산을 대기한다.
         * 1212:01:26.773 [ thread-2] 작업 완료 result = 3775
         * 1212:01:26.773 [ thread-1] 작업 완료 result = 1275
         * 1212:01:27.773 [     main] main 스레드 wake-up -> 2초 후에 대기하던 상태에서 깨어난다.
         * 1212:01:27.774 [     main] task1.result = 1275
         * 1212:01:27.775 [     main] task2.result = 3775
         * 1212:01:27.775 [     main] task1 + task2 = 5050
         * 1212:01:27.775 [     main] main 스레드 end
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
