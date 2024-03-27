package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BalanceTransactionsTest {

    @Test
    void deposit() throws InterruptedException {
        BankAccount account = new BankAccount(1L, 1000L);
        Validator validator = new Validator();
        BalanceChanges balanceChanges = new BalanceTransactions(account, validator);

        Thread t1 = new Thread(() -> {
            balanceChanges.deposit(100);
        });
        Thread t2 = new Thread(() -> {
            balanceChanges.deposit(200);
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        assertEquals(1300, account.getBalance());
    }

    @Test
    void rent() throws InterruptedException {
        BankAccount account = new BankAccount(1L, 1000L);
        Validator validator = new Validator();
        BalanceChanges balanceChanges = new BalanceTransactions(account, validator);

        Thread t1 = new Thread(() -> {
            balanceChanges.rent(100);
        });
        Thread t2 = new Thread(() -> {
            balanceChanges.rent(200);
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        assertEquals(700, account.getBalance());
    }
}