package thread.start;

public class HelloThreadMain {

    public static void main(String[] args) {
        Thread thread = Thread.currentThread();
        System.out.println(thread.getName() + ": main thread start()"); // main, 자바가 만드는 기본 스레드

        HelloThread helloThread = new HelloThread();
//        helloThread.run(); // Calls to 'run()' should probably be replaced with 'start()'
        System.out.println(Thread.currentThread().getName() + ": start() 호출 전");
        helloThread.start(); // start()를 호출하면 helloThread를 위한 별도의 스택 영역을 생성하고 run()을 적재한다.
        System.out.println(Thread.currentThread().getName() + ": start() 호출 후");

        System.out.println(Thread.currentThread().getName() + ": main thread end()");

        /**
         * main 스레드와 helloThread가 별도의 스택 영역에서 실행된다.
         * main: main thread start()
         * main: start() 호출 전
         * main: start() 호출 후
         * main: main thread end()
         * Thread-0: run() -> 순서는 바뀔 수 있다.
         */

        // ------------------------------------------------------------------------------

    }
}
