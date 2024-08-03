package thread.volatile1;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class VolatileFlagMainV1 {

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
         * 기대 결과
         * 13:58:26.462 [     main] runFlag = true
         * 13:58:26.464 [     work] task 시작
         * 13:58:27.469 [     main] runFlag를 false로 변경
         * 13:58:27.469 [     main] task 종료
         * 13:58:27.469 [     main] runFlag = false
         * 13:58:27.469 [     main] main 스레드 종료
         */

        /*
         * 실제 결과
         * "task 종료"가 출력되지 않을 뿐만 아니라, 프로그램이 종료되지 않고 while문을 실행한다.
         * 13:58:26.462 [     main] runFlag = true
         * 13:58:26.464 [     work] task 시작
         * 13:58:27.469 [     main] runFlag를 false로 변경
         * 13:58:27.469 [     main] runFlag = false
         * 13:58:27.469 [     main] main 스레드 종료
         */

        // main 스레드와 work 스레드가 동일한 메모리 영역에 있는 runFlag 변수에 접근하고 있다.
        // main 스레드에서 runFlag를 false로 설정했기 때문에 work 스레드에서 접근하는 runFlag 또한 false가 되므로 while문이 종료될 것으로 예상할 수 있다.
        // 그러나 work 스레드가 접근하는 runFlag는 true인 상태이므로 while문이 종료되지 않는다.
        // 이를 "메모리 가시성" 문제라고 한다.
        // 메모리 가시성이란 멀티 스레드 환경에서 한 스레드가 변경한 값이 다른 스레드에서 언제 보이는지에 대한 문제를 의미한다.

        // main 스레드와 work 스레드가 동일한 메모리 영역에 존재하는 runFlag 값을 읽은 것은 맞다.
        // 사실 각 스레드는 CPU 자원을 효율적으로 사용하기 위해 runFlag 값을 각 스레드의 캐시 메모리에 저장한다.
        // 캐시 메모리에 저장하는 이유는 CPU와 메인 메모리의 속도 차이를 해결하기 위해서이다.
        // main 스레드와 work 스레드는 독립된 캐시 메모리를 갖고, 이 캐시 메모리에 runFlag = true 값을 저장한다.
        // main 스레드에서 runFlag 값을 변경하였을 때, 실제 메인 메모리에 위치한 runFlag 값을 변경하는 것이 아닌 캐시 메모리에 저장된 runFlag 값을 변경한다.
        // 이러한 이유로 인해서 main 스레드에서 runFlag 값을 변경했음에도, work 스레드의 캐시 메모리에는 runFlag 값이 변경되지 않았으므로 프로그램이 종료되지 않았던 것이다.

        // 이러한 문제를 해결하기 위해서는 main 스레드의 캐시 메모리에서 변경된 값을 실제 메인 메모리에 반영해야 한다.
        // 주로 "컨텍스트 스위칭"이 발생하는 시점에 캐시 메모리가 함께 갱신된다.
        // ex. 입출력이 발생하면 RUNNABLE 상태에서 WAITING 상태가 되고, 다시 RUNNABLE이 되는 과정에서 컨텍스트 스위칭이 발생한다.
        // 이는 "반드시" 캐시 메모리가 갱신됨을 "보장하지 않기 때문에" 근본적인 해결책이라고 할 수 없다.

    }

    static class MyTask implements Runnable {

        boolean runFlag = true;

        @Override
        public void run() {
            log("task 시작");
            while (runFlag) {
            }
            log("task 종료");
        }
    }

}
