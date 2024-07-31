package thread.join;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class JoinMainV0 {

    public static void main(String[] args) {
        log("main 스레드 start");

        Job job = new Job();
        Thread thread1 = new Thread(job, "thread-1");
        Thread thread2 = new Thread(job, "thread-2");

        thread1.start();
        thread2.start();

        log("end"); // main 스레드는 thread1, thread2를 기다리지 않고 바로 종료된다.

        /*
         * 1111:44:07.346 [     main] main 스레드 start
         * 1111:44:07.348 [     main] end
         * 1111:44:07.349 [ thread-1] thread-1 스레드 작업 start
         * 1111:44:07.349 [ thread-2] thread-2 스레드 작업 start
         * 1111:44:09.351 [ thread-1] thread-1 스레드 작업 end
         * 1111:44:09.351 [ thread-2] thread-2 스레드 작업 end
         */
    }

    static class Job implements Runnable {

        @Override
        public void run() {
            log(Thread.currentThread().getName() + " 스레드 작업 start");
            sleep(2000);
            log(Thread.currentThread().getName() +  " 스레드 작업 end");
        }
    }

}
