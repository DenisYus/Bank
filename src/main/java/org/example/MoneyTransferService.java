package org.example;

import java.util.concurrent.locks.ReentrantLock;

public class MoneyTransferService implements  TransferService{
    BankAccountRepository repository;
    BalanceChangesFactory balanceChangesFactory;
    //private final Object monitor = new Object();
    private final ReentrantLock lock = new ReentrantLock();
    public MoneyTransferService(BankAccountRepository repository, BalanceChangesFactory balanceChangesFactory) {
        this.repository = repository;
        this.balanceChangesFactory = balanceChangesFactory;
    }

    @Override
    public void transfer(Long idFrom, Long idTo, Long amount) throws InsufficientFoundsException {
        BankAccount fromAccount = repository.getBankAccount(idFrom);
        BankAccount toAccount = repository.getBankAccount(idTo);
        BalanceChanges fromBalanceChanges = balanceChangesFactory.createBalanceChanges(fromAccount);
        BalanceChanges toBalanceChanges = balanceChangesFactory.createBalanceChanges(toAccount);
        lock.lock();
        try {
            fromBalanceChanges.rent(amount);
            toBalanceChanges.deposit(amount);
        } finally {
            lock.unlock();
        }
        //можно просто написать synchronized public void transfer
        // public void transfer(Long idFrom, Long idTo, Long amount) throws InsufficientFoundsException {
        //synchronized (monitor) {
        //    if (!fromBalanceChanges.rent(amount)){
        //        throw new InsufficientFoundsException("На аккаунте недостаточно денег");
        //    }
        //    toBalanceChanges.deposit(amount);
        //}
    }
}
