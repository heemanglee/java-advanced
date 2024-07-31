package thread.join;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class JoinMainV4 {

    public static void main(String[] args) throws InterruptedException {

        log("main 스레드 start");

        SumTask task1 = new SumTask(1, 50);
        Thread thread1 = new Thread(task1, "thread-1");

        thread1.start();

        log("main 스레드는 thread1 스레드의 종료까지 '1초만' 대기");
        thread1.join(1000);
        log("thread1의 연산이 1초가 지났거나 또는 종료된 상태라면 main 스레드는 wake-up");

        log("task1.result = " + task1.result);

        log("main 스레드 end");

        /*
         * 1212:16:05.525 [     main] main 스레드 start
         * 1212:16:05.527 [     main] main 스레드는 thread1 스레드의 종료까지 '1초만' 대기
         * 1212:16:05.527 [ thread-1] 작업 start
         * 1212:16:06.533 [     main] thread1의 연산이 1초가 지났거나 또는 종료된 상태라면 main 스레드는 wake-up
         * 1212:16:06.542 [     main] task1.result = 0 -> thread1의 연산은 2초가 걸리므로 main 스레드 입장에서는 result 값이 0이다.
         * 1212:16:06.543 [     main] main 스레드 end
         * 1212:16:07.534 [ thread-1] 작업 완료 result = 1275
         */

        // 만약 thread1.join(5000)일 때, thread1의 연산은 2초가 걸리므로 5초를 대기하는 것이 아닌 2초만에 대기 상태에서 실행 상태가 된다.
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
