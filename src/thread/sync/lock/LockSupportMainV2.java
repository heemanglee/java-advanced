package thread.sync.lock;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

import java.util.concurrent.locks.LockSupport;

public class LockSupportMainV2 {

    public static void main(String[] args) {
        Thread t1 = new Thread(new MyTask(), "thread");
        t1.start();

        sleep(100);
        log("t1 스레드 상태 : " + t1.getState());

        log("main 스레드에서 park()를 호출하여 t1 스레드가 깨어난다.");

        // LockSupport의 parkNanos(nanos)는 지정한 시간동안 대기 후, lock을 얻지 못하면 대기를 종료한다.
        /*
         * 00:53:21.164 [   thread] park 시작
         * 00:53:21.249 [     main] t1 스레드 상태 : TIMED_WAITING
         * 00:53:21.250 [     main] main 스레드에서 park()를 호출하여 t1 스레드가 깨어난다.
         * 00:53:23.172 [   thread] park 종료, 스레드 상태 : RUNNABLE
         */
    }

    static class MyTask implements Runnable {

        @Override
        public void run() {
            log("park 시작");
            LockSupport.parkNanos(2000_000000); // 2초(20억 나노초) 후에 TIMED_WAITING -> RUNNABLE 상태가 된다.

            log("park 종료, 스레드 상태 : " + Thread.currentThread().getState());
        }
    }
}
