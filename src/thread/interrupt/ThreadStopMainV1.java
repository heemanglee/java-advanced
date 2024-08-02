package thread.interrupt;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class ThreadStopMainV1 {

    public static void main(String[] args) {
        MyTask task = new MyTask();
        Thread thread = new Thread(task, "task");
        thread.start();

        sleep(4000);
        log("작업 중단 지시 runFlag = false");
        task.runFlag = false;

        /*
         * 33:02:25.343 [     task] 작업중
         * 33:02:28.350 [     task] 작업중
         * 33:02:29.324 [     main] 작업 중단 지시 runFlag = false
         * 33:02:31.352 [     task] 자원 정리 -> main스레드가 29초에 작업 중단 지시를 했음에도 work 스레드가 바료 중단되지 않는다.
         * 33:02:31.353 [     task] 작업 종료
         */

    }

    static class MyTask implements Runnable {

        volatile boolean runFlag = true;

        @Override
        public void run() {
            while(runFlag) {
                log("작업중");
                sleep(3000);
            }
            log("자원 정리");
            log("작업 종료");
        }
    }
}
