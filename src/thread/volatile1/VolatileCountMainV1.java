package thread.volatile1;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class VolatileCountMainV1 {

    public static void main(String[] args) {

        MyTask task = new MyTask();
        Thread thread = new Thread(task, "work");
        thread.start();

        sleep(1000);

        task.flag = false;
        log("flag = " + task.flag + ", count = " + task.count + ", in main()");

        /*
         * 기대 결과
         * 14:57:16.284 [     work] flag = true, count = 100000000, in while()
         * 14:57:16.381 [     work] flag = true, count = 200000000, in while()
         * 14:57:16.473 [     work] flag = true, count = 300000000, in while()
         * 14:57:16.561 [     work] flag = true, count = 400000000, in while()
         * 14:57:16.648 [     work] flag = true, count = 500000000, in while()
         * 14:57:16.735 [     work] flag = true, count = 600000000, in while()
         * 14:57:16.822 [     work] flag = true, count = 700000000, in while()
         * 14:57:16.909 [     work] flag = true, count = 800000000, in while()
         * 14:57:16.998 [     work] flag = true, count = 900000000, in while()
         * 14:57:17.084 [     work] flag = true, count = 1000000000, in while()
         * 14:57:17.171 [     work] flag = true, count = 1100000000, in while()
         * 14:57:17.186 [     main] flag = false, count = 1116986736, in main()
         * 14:57:17.258 [     work] flag = false, count = 1116986736, 종료 -> main 스레드에서 flag 값을 변경하였으므로 while(flag = false)됨에 따라 종료된다.
         */

        /*
         * 실제 결과
         * 14:57:16.284 [     work] flag = true, count = 100000000, in while()
         * 14:57:16.381 [     work] flag = true, count = 200000000, in while()
         * 14:57:16.473 [     work] flag = true, count = 300000000, in while()
         * 14:57:16.561 [     work] flag = true, count = 400000000, in while()
         * 14:57:16.648 [     work] flag = true, count = 500000000, in while()
         * 14:57:16.735 [     work] flag = true, count = 600000000, in while()
         * 14:57:16.822 [     work] flag = true, count = 700000000, in while()
         * 14:57:16.909 [     work] flag = true, count = 800000000, in while()
         * 14:57:16.998 [     work] flag = true, count = 900000000, in while()
         * 14:57:17.084 [     work] flag = true, count = 1000000000, in while()
         * 14:57:17.171 [     work] flag = true, count = 1100000000, in while()
         * 14:57:17.186 [     main] flag = false, count = 1116986736, in main()
         * 14:57:17.258 [     work] flag = true, count = 1200000000, in while() -> 실제로 main 스레드에서 flag 값을 false로 바꿨음에도 종료되지 않는다.
         * 14:57:17.258 [     work] flag = false, count = 1200000000, 종료
         */

        // 기대 결과와 실제 결과 출력이 다른 이유는 "메모리 가시성" 문제 때문이다.
        // VolatileFlagMainV1, VolatileFlagMainV2에서 말했듯이, volatile 키워드를 사용하지 않으면 각 스레드는 flag 값을 "메인 메모리"가 아닌 "캐시 메모리"에서 값을 읽고 쓴다.
        // 따라서 main 스레드에서 "flag = false;"를 하더라도 "메인 메모리"가 아닌 "main 스레드의 캐시 메모리"에 값을 쓴다.

        // VolatileFlagMainV1과 달리 VolatileCountMainV1에서는 프로그램이 종료되는데
        // 그 이유는 1억번에 한 번씩 입출력을 하기 때문에 "컨텍스트 스위칭"이 발생하면서 캐시 메모리가 갱신되기 때문이다.
        // 물론 "컨텍스트 스위칭"이 반드시 "캐시 메모리"를 갱신하는 것을 보장하지 않는다.
        // 따라서 volatile 키워드를 사용하여 각 스레드의 "캐시 메모리"가 아닌 "메인 메모리"에 값을 읽고 쓰도록 해야한다.
    }

    static class MyTask implements Runnable {

        boolean flag = true;
        long count;

        @Override
        public void run() {
            while (flag) {
                count++;
                // 1억번에 한 번씩 출력
                if (count % 100_000_000 == 0) {
                    log("flag = " + flag + ", count = " + count + ", in while()"); // 입출력 발생
                }
            }
            log("flag = " + flag + ", count = " + count + ", 종료");
        }
    }
}
