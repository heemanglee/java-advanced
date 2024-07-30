package thread.start;

public class BadThreadMain {

    public static void main(String[] args) {
        Thread thread = Thread.currentThread();
        System.out.println(thread.getName() + ": main thread start()"); // main, 자바가 만드는 기본 스레드

        HelloThread helloThread = new HelloThread();
        System.out.println(Thread.currentThread().getName() + ": start() 호출 전");
        helloThread.run(); // run()을 호출하면 main 스레드의 스택에 적재된다. 즉, main 스레드가 run()을 실행한다.
        System.out.println(Thread.currentThread().getName() + ": start() 호출 후");

        System.out.println(Thread.currentThread().getName() + ": main thread end()");

        /**
         * main: main thread start()
         * main: start() 호출 전
         * main: run()
         * main: start() 호출 후
         * main: main thread end()
         */

        // ------------------------------------------------------------------------------

    }
}
