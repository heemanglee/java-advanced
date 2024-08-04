package thread.sync;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class BankAccountV1 implements BankAccount {

    private int balance; // 계좌 잔액, (공유자원)

    public BankAccountV1(int initialBalance) {
        this.balance = initialBalance;
    }

    @Override
    public boolean withdraw(int amount) {
        log("거래 시작: " + getClass().getSimpleName());

        log("[검증 시작] 출금액 : " + amount + ", 현재 잔액 : " + balance);
        if(balance < amount) { // 계좌 잔액 < 출금액
            log("[검증 실패] 출금액 : " + amount + ", 현재 잔액 : " + balance);
            return false;
        }

        log("[검증 완료] 출금액 : " + amount + ", 현재 잔액 : " + balance);
        sleep(1000); // 계좌에서 출금하는데 1초가 걸린다고 가정
        balance -= amount;
        log("[출금 완료] 출금액 : " + amount + ", 현재 잔액 : " + balance);

        log("거래 종료");
        return true;
    }

    @Override
    public int getBalance() {
        return balance;
    }
}
