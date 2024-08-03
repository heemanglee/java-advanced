package thread.printer;

import static util.MyLogger.log;

import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MyPrinterV4 {

    public static void main(String[] args) {
        Printer printer = new Printer();
        Thread printerThread = new Thread(printer, "printer");
        printerThread.start();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            log("프린터할 문서를 입력하세요. 종료(q): ");
            String userInput = scanner.nextLine();
            if (userInput.equals("q")) {
                printerThread.interrupt();
                break;
            }

            printer.addJob(userInput);
        }
    }

    static class Printer implements Runnable {

        Queue<String> jobQue = new ConcurrentLinkedQueue<>();

        @Override
        public void run() {
            while (!Thread.interrupted()) { // 스레드의 인터럽트 상태 확인
                // 출력할 문서가 없음에도 계속 isEmpty()를 체크하기 때문에, 스레드가 무의미한 작업을 계속 하게 된다.
//                if (jobQue.isEmpty()) {
//                    continue;
//                }

                if(jobQue.isEmpty()) {
                    // 출력할 문서가 없다면 다른 스레드에게 CPU 자원을 양보하여
                    // 현재 스레드가 무의미한 작업을 하는 것보다 다른 스레드가 필요로하는 작업을 처리하도록 CPU 자원을 양보한다.
                    Thread.yield();
                    continue;
                }

                try {
                    String job = jobQue.poll();
                    log("출력 시작 : " + job + ", 대기 문서 : " + jobQue);
                    Thread.sleep(3000);
                    log("출력 완료: " + job);
                } catch (InterruptedException e) {
                    log("인터럽트 발생");
                    break;
                }
            }
            log("프린터 종료");
        }

        public void addJob(String job) {
            this.jobQue.offer(job);
        }
    }
}
