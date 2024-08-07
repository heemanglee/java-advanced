package thread.sync;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BankAccountV6 implements BankAccount {

    private int balance; // 계좌 잔액, (공유자원)

    private final Lock lock = new ReentrantLock(); // 비공정 모드(Non-fair) : 대기 중인 스레드 중에 아무나 락을 획득할 수 있다.

    public BankAccountV6(int initialBalance) {
        this.balance = initialBalance;
    }

    @Override
    public boolean withdraw(int amount) {
        log("거래 시작: " + getClass().getSimpleName());

        try {
            if (!lock.tryLock(500, TimeUnit.MILLISECONDS)) {
                log("[진입 실패] 0.5초 간 기다렸으나 lock을 획득하지 못하여 작업을 중단합니다.");
                return false;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 임계 영역이 끝나면 반드시 lock을 해제해야 한다.
        // lock을 해지하지 않으면 대기 중인 스레드가 lock을 획득하지 못하고 계속 대기하게 된다.
        try {
            log("[검증 시작] 출금액 : " + amount + ", 현재 잔액 : " + balance);
            if (balance < amount) { // 계좌 잔액 < 출금액
                log("[검증 실패] 출금액 : " + amount + ", 현재 잔액 : " + balance);
                return false;
            }

            log("[검증 완료] 출금액 : " + amount + ", 현재 잔액 : " + balance);
            sleep(1000); // 계좌에서 출금하는데 1초가 걸린다고 가정
            balance -= amount;
            log("[출금 완료] 출금액 : " + amount + ", 현재 잔액 : " + balance);
        } finally { // 반드시 unlock을 해야 한다.
            lock.unlock(); // ReentrantLock 이용하여 lock을 해제한다.
        }

        log("거래 종료");
        return true;
    }

    @Override
    public  int getBalance() {
        lock.lock(); // ReentrantLock 이용하여 lock을 건다.
        try {
            return balance;
        } finally {
            lock.unlock(); // ReentrantLock 이용하여 lock을 해제한다.
        }
    }
}
