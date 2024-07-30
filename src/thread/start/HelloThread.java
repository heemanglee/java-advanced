package thread.start;

public class HelloThread extends Thread{

    @Override
    public void run() {
        // Thread.currentThread() : 실행 중인 스레드
        System.out.println(Thread.currentThread().getName() + ": run()");
    }
}
