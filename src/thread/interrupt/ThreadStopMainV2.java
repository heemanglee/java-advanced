package thread.interrupt;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class ThreadStopMainV2 {

    public static void main(String[] args) {
        MyTask task = new MyTask();
        Thread thread = new Thread(task, "work");
        thread.start();

        sleep(4000);
        log("작업 중단 지시 thread.interrupt()");
        // / interrupt()는 InterruptedException 예외를 발생시키는 코드에만 적용된다.
        // interrupt된 스레드는 TIMED_WAITING 상태에서 RUNNABLE 상태가 된다.
        thread.interrupt(); // 인터럽트 발생
        log("work 스레드 인터럽스 상태1 = " + thread.isInterrupted()); // true
        log("work 스레드 인터럽스 상태3 = " + thread.isInterrupted()); // false

        /*
         * 33:46:05.465 [     work] 작업 중
         * 33:46:08.473 [     work] 작업 중
         * 33:46:09.450 [     main] 작업 중단 지시 thread.interrupt()
         * 33:46:09.459 [     main] work 스레드 인터럽스 상태1 = true
         * 33:46:09.459 [     work] work 스레드 인터럽트 상태2 = false
         * 33:46:09.460 [     main] work 스레드 인터럽스 상태3 = false
         * 33:46:09.460 [     work] interrupt message = sleep interrupted
         * 33:46:09.460 [     work] state = RUNNABLE
         * 33:46:09.461 [     work] 자원 정리
         * 33:46:09.461 [     work] 작업 종료
         */

        // 주의깊게 봐야할 점은 WAITING 상태에서 RUNNABLE 상태가 된 스레드는 isInterrupted()를 호출하였을 때 true가 아닌 false를 받는다는 점이다.
        // main에서 interrupt()를 호출할 시점에는 스레드가 아직 TIMED_WAITING 상태이기 때문에 true를 반환하고,
        // TIMED_WAITING 상태에서 interrupt가 발생하여 RUNNABLE 상태가 된 상태에서 isInterrupted()를 호출하면 false를 반환한다.
    }

    static class MyTask implements Runnable {

        @Override
        public void run() {
            try {
                while(true) {
                    log("작업 중");
                    // 강제로 wake-up 되면 InterruptedException 예외가 발생한다.
                    // TIMED_WAITING -> RUNNABLE
                    Thread.sleep(3000);
                }
            }
            catch (InterruptedException e) {
                log("work 스레드 인터럽트 상태2 = " + Thread.currentThread().isInterrupted()); // false
                log("interrupt message = " + e.getMessage());
                log("state = " + Thread.currentThread().getState());
            }
            log("자원 정리");
            log("작업 종료");
        }
    }
}
