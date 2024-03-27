package org.example;

public interface TransferService {
    void transfer(Long idFrom, Long idTo, Long amount) throws InsufficientFoundsException;

}
