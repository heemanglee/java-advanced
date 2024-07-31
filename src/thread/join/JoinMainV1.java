package thread.join;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class JoinMainV1 {

    public static void main(String[] args) {

        log("main 스레드 start");

        SumTask task1 = new SumTask(1, 50);
        SumTask task2 = new SumTask(51, 100);
        Thread thread1 = new Thread(task1, "thread-1");
        Thread thread2 = new Thread(task2, "thread-2");

        thread1.start();
        thread2.start();

        log("task1.result = " + task1.result);
        log("task2.result = " + task2.result);

        int sum = task1.result + task2.result;

        log("task1 + task2 = " + sum);
        log("main 스레드 end");

        /*
         * 1111:52:00.608 [     main] main 스레드 start
         * 1111:52:00.610 [ thread-1] 작업 start
         * 1111:52:00.610 [ thread-2] 작업 start
         * 1111:52:00.616 [     main] task1.result = 0 -> thread1의 연산 도중에 main 스레드가 먼저 종료되었기 때문에 0이 출력된다.
         * 1111:52:00.616 [     main] task2.result = 0 -> thread2의 연산 도중에 main 스레드가 먼저 종료되었기 때문에 0이 출력된다.
         * 1111:52:00.616 [     main] task1 + task2 = 0
         * 1111:52:00.616 [     main] main 스레드 end
         * 1111:52:02.613 [ thread-1] 작업 완료 result = 1275 -> main 스레드가 먼저 종료되고, 2초 후에 작업이 완료된다.
         * 1111:52:02.616 [ thread-2] 작업 완료 result = 3775
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
