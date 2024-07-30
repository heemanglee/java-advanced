package thread.start;

public class DaemonThreadMain {

    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + ": main 스레드 run");
        DaemonThread daemonThread = new DaemonThread();
        daemonThread.setDaemon(true); // 사용자 스레드가 아닌 데몬 스레드임을 명시한다.
        daemonThread.start();
        System.out.println(Thread.currentThread().getName() + ": main 스레드 end");

        /**
         * main: main 스레드 run
         * main: main 스레드 end
         * Thread-0: run() start
         */
    }


    static class DaemonThread extends Thread {

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + ": run() start");
            try {
                Thread.sleep(3000); // 3초간 대기
            } catch(InterruptedException ex) {
                throw new RuntimeException();
            }
            System.out.println(Thread.currentThread().getName() + " : run() end");
        }
    }
}
