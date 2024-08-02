package thread.interrupt;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class ThreadStopMainV4 {

    public static void main(String[] args) {
        MyTask task = new MyTask();
        Thread thread = new Thread(task, "work");
        thread.start();

        sleep(100);
        log("작업 중단 지시 thread.interrupt()");
        thread.interrupt(); // 인터럽트 발생
        log("work 스레드 인터럽스 상태1 = " + thread.isInterrupted()); // true

        /*
         * 44:29:33.286 [     work] 작업 중
         * 44:29:33.286 [     work] 작업 중
         * 44:29:33.286 [     main] 작업 중단 지시 thread.interrupt()
         * 44:29:33.286 [     work] 작업 중
         * 44:29:33.289 [     main] work 스레드 인터럽스 상태1 = true
         * 44:29:33.290 [     work] work 스레드 인터럽트 상태2 = false
         * 44:29:33.290 [     work] 자원 정리
         * 44:29:34.295 [     work] 작업 종료
         * 44:29:34.295 [     work] 작업 종료
         */

        // V2에서 InterruptedException 예외를 처리할 경우에 자바가 스레드의 인터럽트 상태를 true -> false로 변경해주었다.
        // V3에서 예외를 처리하지 않고, 단순히 isInterrupted()를 통해 스레드의 인터럽트 상태에 따라 wake-up된 경우 스레드의 인터럽트 상태를 변경하지 않는다.
        // V4에서 예외를 처리하지 않고, interrupted()를 통해 스레드의 인터럽트 상태에 따라 wake-up된 경우 스레드의 인터럽트 상태를 true -> false로 변경한다.
    }

    static class MyTask implements Runnable {

        @Override
        public void run() {
            // isInterrupted() : 스레드의 인터럽트 상태를 "단순히 조회"하는 메서드
            // interrupted() : 스레드의 인터럽트 상태 확인 + 인터럽트 상태 변경
            while(!Thread.interrupted()) {
                log("작업 중");
            }
            // V3에서 isInterrupted()로 while문을 나올 경우 true를 출력
            // V4에서는 interrupted()로 while문을 빠져 나오면서 스레드의 인터럽트 상태를 변경한다. -> false 출력
            log("work 스레드 인터럽트 상태2 = " + Thread.currentThread().isInterrupted()); // false

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
