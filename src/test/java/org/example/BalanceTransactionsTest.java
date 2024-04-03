package org.example;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class BalanceTransactionsTest {
    @Test
    void deposit() throws InterruptedException {
        //given
    BankAccount account = new BankAccount(1L, 1000L);
    Validator validator = new Validator();
    BalanceChanges balanceChanges = new BalanceTransactions(account, validator);
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
    //when
        executor.schedule(() -> {
        balanceChanges.deposit(100);
    }, 100, TimeUnit.MILLISECONDS);

        executor.schedule(() -> {
        balanceChanges.deposit(200);
    }, 200, TimeUnit.MILLISECONDS);

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
    //then
    assertEquals(1300, account.getBalance());
}

    @Test
    void rent() throws InterruptedException {
        //given
        BankAccount account = new BankAccount(1L, 1000L);
        Validator validator = new Validator();
        BalanceChanges balanceChanges = new BalanceTransactions(account, validator);
        ExecutorService executor = Executors.newCachedThreadPool();
        //when
        executor.submit(() -> {
            balanceChanges.rent(100);
        });
        executor.submit(() -> {
            balanceChanges.rent(200);
        });

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        //then
        assertEquals(700, account.getBalance());
    }
}