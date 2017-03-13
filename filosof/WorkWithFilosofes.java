package filosof;

import java.util.concurrent.Semaphore;

public class WorkWithFilosofes {
    private static Officiant officiant = new Officiant();

    public static void main(String[] args) throws InterruptedException {
        int countOfFilosofes = officiant.enterCountOfFilosofes();

        Semaphore semaphore = new Semaphore((countOfFilosofes/2),true);
        officiant.sitAllFilosofes(semaphore,countOfFilosofes);

        Officiant.toInterruptAllFilosofes();
        Officiant.callFilosofesCondition();
    }
}
