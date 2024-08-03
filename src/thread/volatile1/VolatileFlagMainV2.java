package thread.volatile1;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class VolatileFlagMainV2 {

    public static void main(String[] args) {

        MyTask task = new MyTask();
        Thread thread = new Thread(task, "work");
        log("runFlag = " + task.runFlag);
        thread.start();

        sleep(1000);
        log("runFlag를 false로 변경");
        task.runFlag = false;
        log("runFlag = " + task.runFlag);
        log("main 스레드 종료");

        /*
         * 14:32:32.701 [     main] runFlag = true
         * 14:32:32.703 [     work] task 시작
         * 14:32:33.706 [     main] runFlag를 false로 변경
         * 14:32:33.706 [     work] task 종료  -> "volatile" 키워드를 사용하기 때문에 work 스레드 또한 "메인 메모리"에 저장된 runFlag 값을 읽게 되어 정상적으로 종료된다.
         * 14:32:33.706 [     main] runFlag = false
         * 14:32:33.706 [     main] main 스레드 종료
         */

        // runFlag에 "volatile" 키워드를 추가하면 해당 변수에 대해서 "캐시 메모리" 값을 사용하지 않고, 값을 읽고 쓸 때 반드시 "메인 메모리"에 직접 접근한다.
        // 즉, main 스레드에서 "task.runFlag = false;" 호출한 경우, main 스레드의 캐시 메모리에 저장된 runFlag에 값을 쓰는 것이 아닌, 메인 메모리에 저장된 runFlag에 값을 쓴다.
        // 또한 work 스레드가 "while(runFlag)"를 통해 runFlag 값을 읽을 때, work 스레드의 캐시 메모리에 저장된 runFlag 값을 읽는 것이 아닌, 메인 메모리에 저장된 runFlag 값을 읽는다.

    }

    static class MyTask implements Runnable {

        volatile boolean runFlag = true; // volatile 키워드 추가

        @Override
        public void run() {
            log("task 시작");
            while (runFlag) {
            }
            log("task 종료");
        }
    }

}
