package thread.control;

import static util.MyLogger.log;

public class ThreadStateMain {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new MyRunnable(), "myThread");
        log("myThread.state1 = " + thread.getState()); // NEW, 스레드가 생성된 상태이다.
        log("myThread.start");
        thread.start();
        Thread.sleep(1000);
        log("myThread.state3 = " + thread.getState()); // TIMED_WAITING, 특정 시간 동안 다른 스레드의 작업이 완료되기를 대기하는 상태이다.
        Thread.sleep(4000);
        log("myThread.state5 = " + thread.getState()); // TERMINATED, 스레드가 모든 작업을 마치고 종료된 상태이다.
        log("mainThread.state6 = " + Thread.currentThread().getState()); // RUNNABLE, main 스레드는 아직 종료되지 않았다.
        log("end");
    }

    static class MyRunnable implements Runnable {

        @Override
        public void run() {
            try {
                log("start");
                log("myThread.state2 = " + Thread.currentThread().getState()); // RUNNABLE, 스레드가 실행된 상태
                log("sleep() start");
                Thread.sleep(3000);
                log("sleep() end");
                log("myThread.state4 = " + Thread.currentThread().getState());
                log("end");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
