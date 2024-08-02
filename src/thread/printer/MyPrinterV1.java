package thread.printer;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MyPrinterV1 {

    public static void main(String[] args) {
        Printer printer = new Printer();
        Thread printerThread = new Thread(printer, "printer");
        printerThread.start();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            log("프린터할 문서를 입력하세요. 종료(q): ");
            String userInput = scanner.nextLine();
            if (userInput.equals("q")) {
                printer.work = false;
                break;
            }

            printer.addJob(userInput);
        }

        // 모든 문서를 출력 후 종료
        /*
         * 44:50:58.249 [     main] 프린터할 문서를 입력하세요. 종료(q):
         * a
         * 44:51:00.728 [     main] 프린터할 문서를 입력하세요. 종료(q):
         * 44:51:00.732 [  printer] 출력 시작 : a, 대기 문서 : []
         * b
         * 44:51:00.963 [     main] 프린터할 문서를 입력하세요. 종료(q):
         * c
         * 44:51:01.231 [     main] 프린터할 문서를 입력하세요. 종료(q):
         * 44:51:03.741 [  printer] 출력 완료: a
         * 44:51:03.743 [  printer] 출력 시작 : b, 대기 문서 : [c]
         * 44:51:06.749 [  printer] 출력 완료: b
         * 44:51:06.750 [  printer] 출력 시작 : c, 대기 문서 : []
         * 44:51:09.755 [  printer] 출력 완료: c
         * q
         * 44:51:11.928 [  printer] 프린터 종료
         */

        // 문서 출력 도중에 종료
        /*
         * 44:53:40.454 [     main] 프린터할 문서를 입력하세요. 종료(q):
         * a
         * 44:53:41.021 [     main] 프린터할 문서를 입력하세요. 종료(q):
         * 44:53:41.027 [  printer] 출력 시작 : a, 대기 문서 : []
         * b
         * 44:53:41.298 [     main] 프린터할 문서를 입력하세요. 종료(q):
         * c
         * 44:53:41.544 [     main] 프린터할 문서를 입력하세요. 종료(q):
         * 44:53:44.033 [  printer] 출력 완료: a
         * 44:53:44.035 [  printer] 출력 시작 : b, 대기 문서 : [c]
         * q // 프린터 출력 도중에 강제로 종료하기 위해 "q"를 입력
         * 44:53:47.038 [  printer] 출력 완료: b // 강제 종료가 되지 않고 3초 후에 종료되는 문제 발생
         * 44:53:47.039 [  printer] 프린터 종료
         */
    }

    static class Printer implements Runnable {

        volatile boolean work = true;
        Queue<String> jobQue = new ConcurrentLinkedQueue<>();

        @Override
        public void run() {
            while (work) {
                if (jobQue.isEmpty()) {
                    continue;
                }

                String job = jobQue.poll();
                log("출력 시작 : " + job + ", 대기 문서 : " + jobQue);
                sleep(3000);
                log("출력 완료: " + job);
            }
            log("프린터 종료");
        }

        public void addJob(String job) {
            this.jobQue.offer(job);
        }
    }
}
