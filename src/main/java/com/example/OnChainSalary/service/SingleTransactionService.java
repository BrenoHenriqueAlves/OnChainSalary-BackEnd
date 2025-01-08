package com.example.OnChainSalary.service;

import com.example.OnChainSalary.model.SingleTransaction;
import com.example.OnChainSalary.repository.SingleTransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SingleTransactionService {

    private final SingleTransactionRepository transactionRepository;

    public SingleTransactionService(SingleTransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public SingleTransaction addTransaction(SingleTransaction transaction) {
        return transactionRepository.save(transaction);
    }

    public List<SingleTransaction> getTransactionsBySender(UUID senderId) {
        return transactionRepository.findBySenderId(senderId);
    }

    public List<SingleTransaction> getTransactionsByReceiver(UUID receiverId) {
        return transactionRepository.findByReceiverId(receiverId);
    }

    public Optional<SingleTransaction> getTransactionById(UUID id) {
        return transactionRepository.findById(id);
    }

    public void deleteTransaction(UUID id) {
        transactionRepository.deleteById(id);
    }

    // Novo método para atualizar a transação com novo status
    public SingleTransaction updateTransactionStatus(SingleTransaction transaction) {
        return transactionRepository.save(transaction); // Atualiza e salva a transação
    }
}
