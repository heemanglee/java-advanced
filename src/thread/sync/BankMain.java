package thread.sync;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class BankMain {

    public static void main(String[] args) throws InterruptedException {
        BankAccountV1 bankAccountV1 = new BankAccountV1(1000); // 계좌 초기 잔액은 1000원이다.

        Thread t1 = new Thread(new WithdrawTask(bankAccountV1, 800), "t1");
        Thread t2 = new Thread(new WithdrawTask(bankAccountV1, 800), "t2");
        t1.start();
        t2.start();

        sleep(500);
        log("t1 state: " + t1.getState());
        log("t2 state: " + t2.getState());

        t1.join();
        t2.join();
        log("최종 잔액: " + bankAccountV1.getBalance());

        /*
         * 13:37:46.704 [       t2] 거래 시작: BankAccountV1
         * 13:37:46.704 [       t1] 거래 시작: BankAccountV1
         * 13:37:46.710 [       t1] [검증 시작] 출금액 : 800, 현재 잔액 : 1000
         * 13:37:46.711 [       t1] [검증 완료] 출금액 : 800, 현재 잔액 : 1000
         * 13:37:46.712 [       t2] [검증 시작] 출금액 : 800, 현재 잔액 : 1000
         * 13:37:46.712 [       t2] [검증 완료] 출금액 : 800, 현재 잔액 : 1000
         * 13:37:47.181 [     main] t1 state: TIMED_WAITING
         * 13:37:47.181 [     main] t2 state: TIMED_WAITING
         * 13:37:47.716 [       t1] [출금 완료] 출금액 : 800, 현재 잔액 : 200
         * 13:37:47.717 [       t1] 거래 종료
         * 13:37:47.719 [       t2] [출금 완료] 출금액 : 800, 현재 잔액 : -600
         * 13:37:47.720 [       t2] 거래 종료
         * 13:37:47.725 [     main] 최종 잔액: -600
         */

        // t1 스레드와 t2 스레드는 동일한 BankAccountV1 인스턴스를 사용한다. -> BankAccountV1은 "공유변수"
        // t2 스레드가 먼저 시작되었고, 계좌에 남은 잔액을 먼저 확인한 스레드는 t1이다.
        // t1 스레드가 출금하기 전에 t2 스레드가 계좌의 잔액을 확인했으므로 검증에 성공한다.
        // 두 스레드 모두 검증에 성공한 상태이기 때문에 출금을 시도한다.
        // t1 스레드가 먼저 출금하여 (1000 - 800)을 하여 잔액이 200원이 남은 상태가 되었다.
        // 이어서 t2 스레드가 출금을 완료하였다. -> 여기서 문제는 t2 스레드가 출금 후 계좌에 남은 잔액이 "-600"인 것이다.
        // 분명 검증 로직에 "현재 잔액 < 출금할 잔액"이면 출금을 못하게 막았으나 정상적으로 출금 처리가 되었다.
        // 이러한 이유가 발생한 이유는 t1 스레드가 출금하기 이전에 t2 스레드가 계좌에 남은 잔액을 읽었기 때문이다.
        // t2 스레드가 계죄의 잔액을 읽었을 당시에는 "현재 잔액 > 출금할 잔액" 이였으므로 문제가 발상하지 않았다.
    }

}
