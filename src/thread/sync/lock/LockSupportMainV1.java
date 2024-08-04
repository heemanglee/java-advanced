package thread.sync.lock;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

import java.util.concurrent.locks.LockSupport;

public class LockSupportMainV1 {

    public static void main(String[] args) {
        Thread t1 = new Thread(new MyTask(), "thread");
        t1.start();

        sleep(100);
        log("t1 스레드 상태 : " + t1.getState());

        log("main 스레드에서 park()를 호출하여 t1 스레드가 깨어난다.");
//        LockSupport.unpark(t1); // unpark()를 통해 스레드를 RUNNABLE 상태로 만든다.
        t1.interrupt(); // t1 스레드를 인터럽트를 발생시켜 RUNNABLE 상태로 변경한다.



        // LockSupport.unpark(t1) 호출
        /*
         * 00:48:56.290 [   thread] park 시작
         * 00:48:56.378 [     main] t1 스레드 상태 : WAITING
         * 00:48:56.378 [     main] main 스레드에서 park()를 호출하여 t1 스레드가 깨어난다.
         * 00:48:56.378 [   thread] park 종료, 스레드 상태 : RUNNABLE
         * 00:50:39.697 [   thread] 인터럽트 상태: false
         */

        // t1.interrupt() 호출
        // BLOCKED 상태와 달리, WAITING 상태의 스레드에 interrupt를 발생시킬 수 있다.
        /*
         * 00:50:39.610 [   thread] park 시작
         * 00:50:39.694 [     main] t1 스레드 상태 : WAITING
         * 00:50:39.694 [   thread] park 종료, 스레드 상태 : RUNNABLE
         * 00:50:39.697 [   thread] 인터럽트 상태: true
         */
    }

    static class MyTask implements Runnable {

        @Override
        public void run() {
            log("park 시작");
            LockSupport.park();

            log("park 종료, 스레드 상태 : " + Thread.currentThread().getState());
            log("인터럽트 상태: " + Thread.currentThread().isInterrupted());
        }
    }
}
