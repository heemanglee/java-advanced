package thread.volatile1;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class VolatileCountMainV2 {

    public static void main(String[] args) {

        MyTask task = new MyTask();
        Thread thread = new Thread(task, "work");
        thread.start();

        sleep(1000);

        task.flag = false;
        log("flag = " + task.flag + ", count = " + task.count + ", in main()");

        /*
         * 15:04:03.696 [     work] flag = true, count = 100000000, in while()
         * 15:04:04.166 [     work] flag = true, count = 200000000, in while()
         * 15:04:04.215 [     main] flag = false, count = 210349552, in main()
         * 15:04:04.215 [     work] flag = false, count = 210349552, 종료
         */

        // volatile 키워드를 사용하였기 때문에 main 스레드와 work 스레드 둘 다 "메인 메모리"에 저장된 flag와 count 변수에 접근한다.
        // 따라서 main 스레드가 변경한 flag 값이 "메인 메모리"에 수정되고, work 스레드가 while문을 돌면서 읽는 flag 값은 "메인 메모리"로부터 읽어오는 값이다.
        // 또한 count 변수도 volatile을 사용하기 때문에 main 스레드와 work 스레드 둘 다 "메인 메모리"에 접근하여 값을 읽고 쓴다.

        // volatile은 "메인 메모리"에 접근하기 때문에 CPU 속도보다 한참 느리다. 캐시 메모리는 CPU와 메인 메모리 간에 속도 차이를 해결하기 위해 사용된다.
        // 이러한 이유로 인해 VolatileCountMainV1에서는 count에 12억이 저장된 반면에, VolatileCountMainV2에는 count에 약 2억이 저장되었다.
        // VolatileCountMainV1는 "캐시 메모리"에 저장된 값을 접근하기 때문이고, VolatileCountMainV2는 "메인 메모리"에 저장된 값에 접근했기 때문이다.
    }

    static class MyTask implements Runnable {

        volatile boolean flag = true;
        volatile long count;

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
