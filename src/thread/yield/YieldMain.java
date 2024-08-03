package thread.yield;

import static util.ThreadUtils.sleep;

public class YieldMain {

    static final int THREAD_CNT = 1000;

    public static void main(String[] args) {
        for (int i = 0; i < THREAD_CNT; i++) {
            Thread thread = new Thread(new MyRunnable());
            thread.start();
        }

        /*
         * 1. Empty -> 0~9까지 멈추지 않고 출력하는 스레드가 많다.
         * Thread-999 - 0
         * Thread-999 - 1
         * Thread-999 - 2
         * Thread-999 - 3
         * Thread-999 - 4
         * Thread-999 - 5
         * Thread-999 - 6
         * Thread-999 - 7
         * Thread-999 - 8
         * Thread-999 - 9
         */

        /*
         * 2. sleep(1) -> 1밀리초 간격으로 RUNNABLE 상태에서 TIMED_WAITING 상태가 되었다가, 다시 RUNNABLE 상태가 된다.
         * TIMED_WAITING 상태일 때 다른 스레드가 CPU 자원을 사용하게 된다.
         * sleep() 시점에 "RUNNABLE" 상태가 아닌 "TIMED_WAITING" 상태가 되는 것에 집중한다.
         * 만약 자신(스레드) 말고 CPU 자원을 사용할 다른 스레드가 없음에도, sleep()을 호출하면 "TIMED_WAITING" 상태가 되어 의미 없는 대기 상태가 된다.
         * CPU 자원을 사용할 다른 스레드가 없다면 자신(스레드)이 멈추지 않고 한 번에 작업을 처리하는 것이 효율적이다.
         * Thread-997 - 9
         * Thread-994 - 9
         * Thread-993 - 9
         * Thread-995 - 9
         * Thread-999 - 9
         * Thread-991 - 9
         * Thread-988 - 9
         * Thread-996 - 9
         * Thread-992 - 9
         */

        /*
         * 자바에서 RUNNABLE 상태 = (1)실행 상태(Running), (2)실행 대기 상태(Ready)
         * 자바는 두 상태를 묶어서 하나의 RUNNABLE 상태로 처리한다. 즉, 두 상태를 구분하지 못한다.
         * (1)실행 상태 : 스레드가 CPU 자원을 사용하고 있는 상태
         * (2)실행 대기 상태 : 스레드를 실행할 수 있으나, 사용할 CPU 자원이 부족하여 대기 중인 상태
         */

        /*
         * 3. yield() -> "RUNNABLE" 상태를 유지하면서 다른 스레드에게 자신이 사용 중인 CPU 자원을 양보한다.
         * sleep()의 경우 "TIMED_WAITING" 상태에서 CPU 자원을 다른 스레드가 사용하게 하지만, yield()는 "RUNNABLE" 상태를 유지하면서 다른 스레드에게 CPU 자원을 양보한다.
         * 위에서 말했듯이, RUNNABLE 상태에는 "실행 상태"와 "실행 대기 상태"가 존재하는데, yield()를 호출하면 "실행 대기 상태"가 된다.
         * sleep()은 CPU 자원을 사용한 스레드가 없음에도 강제로 "TIEMD_WAITING" 상태가 되어야하고, yield()는 CPU 자원을 사용할 다른 스레드가 없다면 자신(스레드)이 계속 사용한다.
         * Thread-850 - 8
         * Thread-850 - 9
         * Thread-816 - 9
         * Thread-977 - 9
         * Thread-989 - 9
         * Thread-994 - 9
         * Thread-820 - 9
         * Thread-976 - 9
         * Thread-967 - 9
         */
    }

    static class MyRunnable implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName() + " - " + i);
//                sleep(1); // 1밀리초 TIMED_WAITING, RUNNABLE -> TIMED_WAITING -> RUNNABLE
//                Thread.yield();
            }
        }
    }
}
