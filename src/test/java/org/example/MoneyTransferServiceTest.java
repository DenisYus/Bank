package org.example;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MoneyTransferServiceTest {
    @Test
    public void transfer() throws InterruptedException {
        //given
        BankAccountRepository repository = new BankAccountRepository();
        Validator validation = new Validator();
        BankAccount account1 = new BankAccount(1L, 1000L);
        BankAccount account2 = new BankAccount(2L, 500L);
        repository.addBankAccount(account1);
        repository.addBankAccount(account2);

        TransferService transferService = new MoneyTransferService(repository, new BalanceChangesFactory() {
            @Override
            public BalanceChanges createBalanceChanges(BankAccount account) {
                return new BalanceTransactions(account, validation) ;
            }
        });
        //when
        Thread t1 = new Thread(() -> {
            try {
                transferService.transfer(1L, 2L, 200L);
            } catch (InsufficientFoundsException e) {
                throw new RuntimeException(e);
            }
        });
        Thread t2 = new Thread(() -> {
            try {
                transferService.transfer(2L, 1L, 50L);
            } catch (InsufficientFoundsException e) {
                throw new RuntimeException(e);
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        //then
        assertEquals(850, repository.getBankAccount(1L).getBalance());
        assertEquals(650, repository.getBankAccount(2L).getBalance());
    }



}
