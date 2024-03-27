package org.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        BankAccountRepository repository = new BankAccountRepository();
        Validator validation = new Validator();

        BankAccount account1 = new BankAccount(1L, 1000L);
        BankAccount account2 = new BankAccount(2L, 500L);
        repository.addBankAccount(account1);
        repository.addBankAccount(account2);
        TransferService transferService = new MoneyTransferService(repository, new BalanceChangesFactory() {
            @Override
            public BalanceChanges createBalanceChanges(BankAccount account) {
                return new BalanceTransactions(account, validation);
            }
        });
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            executorService.submit(() -> {
                try {
                    transferService.transfer(1L, 2L, 100L);
                    Thread.sleep(100);
                    transferService.transfer(2L, 1L, 50L);
                } catch (InsufficientFoundsException | InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            });
        }
        executorService.shutdown();
        while (!executorService.isTerminated()){}


        System.out.println("Account 1 balance: " + repository.getBankAccount(1L).getBalance());
        System.out.println("Account 2 balance: " + repository.getBankAccount(2L).getBalance());
    }
}