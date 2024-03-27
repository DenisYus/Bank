package org.example;

import java.util.HashMap;

public class BankAccountRepository {
    private final HashMap<Long, BankAccount> accounts = new HashMap<>();
    public void addBankAccount(BankAccount account) {
        accounts.put(account.getId(), account);
    }
    public BankAccount getBankAccount(Long id) {
        return accounts.get(id);
    }
}
