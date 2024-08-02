package thread.interrupt;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class ThreadStopMainV3 {

    public static void main(String[] args) {
        MyTask task = new MyTask();
        Thread thread = new Thread(task, "work");
        thread.start();

        sleep(100);
        log("작업 중단 지시 thread.interrupt()");
        // / interrupt()는 InterruptedException 예외를 발생시키는 코드에만 적용된다.
        // interrupt된 스레드는 TIMED_WAITING 상태에서 RUNNABLE 상태가 된다.
        thread.interrupt(); // 인터럽트 발생
        log("work 스레드 인터럽스 상태1 = " + thread.isInterrupted()); // true

        /*
         * 44:03:01.555 [     work] 작업 중
         * 44:03:01.555 [     work] 작업 중
         * 44:03:01.555 [     main] 작업 중단 지시 thread.interrupt()
         * 44:03:01.555 [     work] 작업 중
         * 44:03:01.558 [     main] work 스레드 인터럽스 상태1 = true
         * 44:03:01.558 [     work] work 스레드 인터럽트 상태2 = true
         * 44:03:01.558 [     work] 자원 정리
         * 44:03:01.559 [     work] 자원 정리 실패 - 자원 정리 중 인터럽트 발생
         * 44:03:01.559 [     work] work 스레드 인터럽트 상태3 = false
         * 44:03:01.559 [     work] 작업 종료
         */

        // V2에서는 인터럽트 상태2에서 false를 반환했으나, V3에서는 true를 반환한다.
        // 자바는 interrupt가 발생하여 InterruptedException 예외를 처리한 경우 스레드의 인터럽트 상태를 false로 변경한다.
        // V3에서는 main에서 work 스레드에 인터럽트를 발생시켰을 때, 별도의 InterruptedException을 처리하지 않았으므로 인터럽트 상태가 true로 유지된다.\
        // 인러럽트 상태2 이후에 추가로 sleep()이 발생하였고, 이때 인터럽트 상태는 true이므로 바로 wake-up이 되어 InterruptedException 예외를 처리하게 된다.
        // 예외를 처리한 인터럽트 상태3에서는 인터럽트 상태가 false가 된다.

        // while문을 탈출하기 위해 인터럽트를 발생시켰는데 이후에 인터럽트 상태가 여전히 true가 나머지 코드에 영향을 미치게 된다.

    }

    static class MyTask implements Runnable {

        @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()) {
                log("작업 중");
            }
            log("work 스레드 인터럽트 상태2 = " + Thread.currentThread().isInterrupted()); // true

            try {
                log("자원 정리");
                Thread.sleep(1000);
                log("작업 종료");
            } catch(InterruptedException e) {
                log("자원 정리 실패 - 자원 정리 중 인터럽트 발생");
                log("work 스레드 인터럽트 상태3 = " + Thread.currentThread().isInterrupted()); // false
            }
            log("작업 종료");
        }
    }
}
