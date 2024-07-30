package thread.control;

/**
 * Checked Exception 규칙
 * 부모 메서드가 체크 예외를 던지지 않는 경우, 재정의된 자식 메서드도 체크 예외를 던질 수 없다.
 * 자식 메서드는 부모 메서드가 던지는 체크 예외를 포함한 하위 타입만 던질 수 있다.
 */

public class CheckedExceptionMain {

    public static void main(String[] args) throws Exception {
        throw new Exception(); // 체크 예외를 던질 수 있댜.
    }

    static class CheckedRunnable implements Runnable {

        @Override
        public void run() {
            // 상위 인터페이스인 Runnable의 run()에서 Checked Exception을 던지지 않으므로 이를 구현하는 클래스에서도 던질 수 없다.
            // throw new Exception();
        }
    }
}
