package org.example;

import java.util.concurrent.locks.ReentrantLock;

public class BalanceTransactions implements BalanceChanges {
    private final BankAccount bankAccount;
    private final Validation validation;
    private final ReentrantLock lock = new ReentrantLock();

    public BalanceTransactions(BankAccount bankAccount, Validation validation) {
        this.validation = validation;
        this.bankAccount = bankAccount;
    }
    @Override
    public void deposit(long amount) {
        lock.lock();
        try {
            bankAccount.setBalance(bankAccount.getBalance() + amount);
        } finally {
            lock.unlock();
        }
    }
    //public synchronized void deposit(long amount) {
    //    bankAccount.setBalance(bankAccount.getBalance() + amount);
    //}
    @Override
    public boolean rent(long amount) {
        lock.lock();
        try {
            if (validation.checkBalance(bankAccount.getBalance(), amount)){
                bankAccount.setBalance(bankAccount.getBalance() - amount);
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }
    //public synchronized boolean rent(long amount) {
    //        if (validation.checkBalance(bankAccount.getBalance(), amount)){
    //            bankAccount.setBalance(bankAccount.getBalance() - amount);
    //            return true;
    //        }
    //        return false;
    //}
}
