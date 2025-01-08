package com.example.OnChainSalary.repository;

import com.example.OnChainSalary.model.SingleTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SingleTransactionRepository extends JpaRepository<SingleTransaction, UUID> {
    List<SingleTransaction> findBySenderId(UUID senderId);
    List<SingleTransaction> findByReceiverId(UUID receiverId);
}
