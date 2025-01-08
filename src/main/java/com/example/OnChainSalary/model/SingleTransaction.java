package com.example.OnChainSalary.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "single_transactions")
public class SingleTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private BigDecimal totalValueInEth;

    @Column(nullable = false)
    private BigDecimal totalValueInUsd;

    @Column
    private BigDecimal transactionFee;

    @Column(nullable = false, unique = true)
    private String blockchainHash;

    @Column(nullable = false)
    private String contractAddress;

    @Column(nullable = false)
    private UUID senderId;

    @Column(nullable = false)
    private UUID receiverId;

    @Column(nullable = false)
    private String senderEthAddress;

    @Column(nullable = false)
    private String receiverEthAddress;

    // Método para definir a data de criação automaticamente
    @PrePersist
    public void prePersist() {
        if (this.creationDate == null) {
            this.creationDate = LocalDateTime.now();
        }
    }

    // Getters e Setters
    public UUID getSenderId() {
        return senderId;
    }

    public void setSenderId(UUID senderId) {
        this.senderId = senderId;
    }

    public UUID getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(UUID receiverId) {
        this.receiverId = receiverId;
    }

    // Outros getters e setters...

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotalValueInEth() {
        return totalValueInEth;
    }

    public void setTotalValueInEth(BigDecimal totalValueInEth) {
        this.totalValueInEth = totalValueInEth;
    }

    public BigDecimal getTotalValueInUsd() {
        return totalValueInUsd;
    }

    public void setTotalValueInUsd(BigDecimal totalValueInUsd) {
        this.totalValueInUsd = totalValueInUsd;
    }

    public BigDecimal getTransactionFee() {
        return transactionFee;
    }

    public void setTransactionFee(BigDecimal transactionFee) {
        this.transactionFee = transactionFee;
    }

    public String getBlockchainHash() {
        return blockchainHash;
    }

    public void setBlockchainHash(String blockchainHash) {
        this.blockchainHash = blockchainHash;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public String getSenderEthAddress() {
        return senderEthAddress;
    }

    public void setSenderEthAddress(String senderEthAddress) {
        this.senderEthAddress = senderEthAddress;
    }

    public String getReceiverEthAddress() {
        return receiverEthAddress;
    }

    public void setReceiverEthAddress(String receiverEthAddress) {
        this.receiverEthAddress = receiverEthAddress;
    }
}
