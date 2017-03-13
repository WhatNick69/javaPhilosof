package filosof;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

class Officiant {
    private static List<Thread> listOfFilosofes;

    int enterCountOfFilosofes() {
        int countOfFilosofes = 2;
        try {
            System.out.print("Введите число философов: ");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            countOfFilosofes = Integer.parseInt(reader.readLine());
            System.out.println();
            if (countOfFilosofes < 2) {
                countOfFilosofes = 2;
            } else if (countOfFilosofes > 10000) {
                countOfFilosofes = 10000;
            }
        } catch (Exception e) {
            System.out.println("Число философов должно быть числом!");
            enterCountOfFilosofes();
        }
        return countOfFilosofes;
    }

    void sitAllFilosofes(Semaphore semaphore, int countOfFilosofes) {
        List<Thread> listOfThreads = new ArrayList<>();
        CountDownLatch cDL = new CountDownLatch(countOfFilosofes);
        try {
            for (int i = 0;i<countOfFilosofes;i++) {
                listOfThreads.add(new StarvingFilosof(semaphore,cDL));
                listOfThreads.get(i).start();
            }
            cDL.await();
            System.out.println("\r\nВсе философы сели за стол"
                    + "\r\nКоличество ложек: "
                    + StarvingFilosof.getCountOfForks());
            System.out.println("Подготовка к еде\r\n");
        } catch (InterruptedException e) {
            System.out.println("Что-то пошло нет так! Не все философы уселись за стол!");
            sitAllFilosofes(semaphore,countOfFilosofes);
        }
        listOfFilosofes = listOfThreads;
    }

    static boolean toControl(int countOfEatenFood) {
        int minFood = getMinEatenFood();
        boolean flag = false;
        if (StarvingFilosof.getCountOfForks() >= 2 && StarvingFilosof.getCountOfsem() > 0 && !((countOfEatenFood-minFood) > 2)) {
            flag = true;
        }

        return flag;
    }

    private static int getMinEatenFood() {
        int food;
        int minFood = Integer.MAX_VALUE;
        for (int i = 0;i<listOfFilosofes.size();i++) {
            StarvingFilosof sF = (StarvingFilosof) listOfFilosofes.get(i);
            food = sF.getCountOfEatenFood();
            if (food < minFood) {
                minFood = food;
                if ((food-minFood) >= 2) {
                    System.out.println(sF.getFilName() + i + " голоден!");
                }
            }
        }

        return minFood;
    }

    static void toInterruptAllFilosofes() throws InterruptedException {
        Thread.sleep(1000);
        System.out.println();
        for (Thread filosof : listOfFilosofes) {
            filosof.interrupt();
            try {
                filosof.join();
            } catch (Exception e) {
                System.out.println("Поток остановлен некорректно");
            }
        }
    }

    static void callFilosofesCondition() throws InterruptedException {
        System.out.println();
        Thread.sleep(1000);
        for (Thread filosof : listOfFilosofes) {
            StarvingFilosof sF = (StarvingFilosof)filosof;
            sF.getCondition();
        }
    }
}
