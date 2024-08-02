package thread.printer;

import static util.MyLogger.log;

import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MyPrinterV3 {

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
                if (jobQue.isEmpty()) {
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
