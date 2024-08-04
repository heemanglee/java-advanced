package thread.sync;

public class WithdrawTask implements Runnable{

    private BankAccount bankAccount; // 출금할 계좌
    private int amount; // 출금할 금액

    public WithdrawTask(BankAccount bankAccount, int amount) {
        this.bankAccount = bankAccount;
        this.amount = amount;
    }

    @Override
    public void run() {
        bankAccount.withdraw(amount); // 계좌에서 amount 만큼 출금한다.
    }
}
