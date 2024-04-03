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
        // given
        BankAccountRepository repository = new BankAccountRepository();
        Validator validation = new Validator();
        BankAccount account1 = new BankAccount(1L, 1000L);
        BankAccount account2 = new BankAccount(2L, 500L);
        repository.addBankAccount(account1);
        repository.addBankAccount(account2);

        TransferService transferService = new MoneyTransferService(repository, account -> new BalanceTransactions(account, validation));

        // when
        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.submit(() -> {
            try {
                transferService.transfer(1L, 2L, 200L);
            } catch (InsufficientFoundsException e) {
                throw new RuntimeException(e);
            }
        });
        executor.submit(() -> {
            try {
                transferService.transfer(2L, 1L, 50L);
            } catch (InsufficientFoundsException e) {
                throw new RuntimeException(e);
            }
        });

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        // then
        assertEquals(850, repository.getBankAccount(1L).getBalance());
        assertEquals(650, repository.getBankAccount(2L).getBalance());
    }


}
