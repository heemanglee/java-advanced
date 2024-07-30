package thread.control;

import static util.MyLogger.log;

import thread.start.HelloRunnable;

public class ThreadInfoMain {

    public static void main(String[] args) {

        // main 스레드
        Thread mainThread = Thread.currentThread();
        log("mainThread Object = " + mainThread); // mainThread Object = Thread[#1,main,5,main], [id, name, priority, group]
        log("mainThread Id = " + mainThread.threadId()); // mainThread Id = 1
        log("mainThread Name = " + mainThread.getName()); // mainThread Name = main
        log("mainThread Priority = " + mainThread.getPriority()); // mainThread Priority = 5, 기본순위 5
        log("mainThread ThreadGroup = " + mainThread.getThreadGroup()); // mainThread ThreadGroup = java.lang.ThreadGroup[name=main,maxpri=10]
        log("mainThread ThreadState= " + mainThread.getState()); // mainThread ThreadState= RUNNABLE, 스레드가 실행 중인 상태

        log("\n-----------------------------------------------\n");

        Thread helloThread = new Thread(new HelloRunnable(), "helloThread");
        log("helloThread Object = " + helloThread); // helloThread Object = Thread[#21,helloThread,5,main]
        log("helloThread Id = " + helloThread.threadId()); // helloThread Id = 21
        log("helloThread Name = " + helloThread.getName()); // helloThread Name = helloThread
        log("helloThread Priority = " + helloThread.getPriority()); // helloThread Priority = 5
        // helloThread는 main 스레드에 의해 생성되었으므로 같은 스레드 그룹에 속한다.
        // helloThread의 부모 스레드는 main 스레드이다.
        log("helloThread ThreadGroup = " + helloThread.getThreadGroup()); // helloThread ThreadGroup = java.lang.ThreadGroup[name=main,maxpri=10]
        log("helloThread ThreadState= " + helloThread.getState()); // helloThread ThreadState= NEW, 스레드를 생성하고 실행하지 않은 상태
    }
}
