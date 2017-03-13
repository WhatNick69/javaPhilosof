package filosof;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

/*
 * Created by WhatNick on 27.08.2016.
 */

class StarvingFilosof extends Thread {
    private volatile static Semaphore semaphore;
    private volatile static int countOfForks = 0;
    private CountDownLatch cDL;
    private String name = "";
    private int countOfEatenFood = 0;

    StarvingFilosof(Semaphore semaphore, CountDownLatch cDL) {
        StarvingFilosof.semaphore = semaphore;
        countOfForks++;
        this.cDL = cDL;
        this.name = "Философ №" + countOfForks;
    }

    @Override
    public void run() {
        Random rnd = new Random();
        try {
            Thread.sleep(rnd.nextInt(1000));
            System.out.println(name + " сел за стол");
            cDL.countDown();
            cDL.await();

            Thread.sleep(1000);
            while (!Thread.currentThread().isInterrupted()) {
                if (Officiant.toControl(getCountOfEatenFood())) {
                    semaphore.acquire();
                    System.out.println(name + " начал кушать");
                    takeFork();
                    takeFork();
                    Thread.sleep(rnd.nextInt(1000)+500);
                    setCountOfEatenFood();
                    System.out.println(this.name + " поел. Теперь он думает.");
                    pickFork();
                    pickFork();
                    semaphore.release();
                }
            }
        } catch (InterruptedException e) {
            System.out.println(name + " закончил застолье");
        }
    }

    static int getCountOfForks() {
        return countOfForks;
    }

    private void takeFork() {
        countOfForks--;
    }

    private void pickFork() {
        countOfForks++;
    }

    String getFilName() {
        return name;
    }

    private void setCountOfEatenFood() {
        this.countOfEatenFood++;
    }

    int getCountOfEatenFood() {
        return this.countOfEatenFood;
    }

    void getCondition() {
        if (countOfEatenFood == 0) {
            System.out.println(name + " еще ни разу не поел!");
        } else {
            System.out.println(name + " поел " + countOfEatenFood + " раз");
        }

    }

    static int getCountOfsem() {
        return semaphore.availablePermits();
    }
}
