package org.example;

public class Validator implements Validation{
    @Override
    public boolean checkBalance(Long balance,Long amount) {
        return balance >= amount;
    }
}
