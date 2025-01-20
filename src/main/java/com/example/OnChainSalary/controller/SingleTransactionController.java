package com.example.OnChainSalary.controller;

import com.example.OnChainSalary.model.SingleTransaction;
import com.example.OnChainSalary.service.SingleTransactionService;
import com.example.OnChainSalary.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions/single")
public class SingleTransactionController {

    private final SingleTransactionService transactionService;
    private final UserService userService;

    public SingleTransactionController(SingleTransactionService transactionService, UserService userService) {
        this.transactionService = transactionService;
        this.userService = userService;
    }

    // Método para obter o ID do usuário logado
    private UUID getLoggedUserId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found")).getId();
    }

    // Endpoint: Transações onde o usuário logado é o sender
    @GetMapping
    public ResponseEntity<List<SingleTransaction>> getTransactionsWhereUserIsSender() {
        UUID loggedUserId = getLoggedUserId();
        List<SingleTransaction> transactions = transactionService.getTransactionsBySender(loggedUserId);
        return ResponseEntity.ok(transactions);
    }

    // Novo endpoint: Transações onde o usuário logado é o receiver
    @GetMapping("/received")
    public ResponseEntity<List<SingleTransaction>> getTransactionsWhereUserIsReceiver() {
        UUID loggedUserId = getLoggedUserId();
        List<SingleTransaction> transactions = transactionService.getTransactionsByReceiver(loggedUserId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SingleTransaction> getTransactionById(@PathVariable UUID id) {
        Optional<SingleTransaction> transaction = transactionService.getTransactionById(id);
        return transaction.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Método para criar a transação onde o senderId é o usuário logado
    @PostMapping
    public ResponseEntity<SingleTransaction> createTransaction(@RequestBody SingleTransaction transaction) {
        // Atribuir o senderId automaticamente com o usuário logado
        UUID senderId = getLoggedUserId();
        transaction.setSenderId(senderId); // Definindo o senderId do usuário logado

        // Adicionar a transação
        SingleTransaction createdTransaction = transactionService.addTransaction(transaction);
        return ResponseEntity.ok(createdTransaction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable UUID id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint para alterar apenas o status da transação
    @PatchMapping("/{id}/status")
    public ResponseEntity<SingleTransaction> updateTransactionStatus(
            @PathVariable UUID id,
            @RequestBody StatusUpdateRequest statusUpdateRequest) {  // Alterei para usar StatusUpdateRequest

        // Verificar se a transação existe
        Optional<SingleTransaction> transactionOpt = transactionService.getTransactionById(id);

        if (transactionOpt.isPresent()) {
            SingleTransaction transaction = transactionOpt.get();

            // Verifique se o usuário logado é o sender ou receiver (dependendo das permissões)
            UUID loggedUserId = getLoggedUserId();
            if (transaction.getSenderId().equals(loggedUserId) || transaction.getReceiverId().equals(loggedUserId)) {
                // Atualizar o status da transação
                transaction.setStatus(statusUpdateRequest.getNewStatus());  // Alteração aqui
                SingleTransaction updatedTransaction = transactionService.addTransaction(transaction); // Atualiza a transação
                return ResponseEntity.ok(updatedTransaction); // Retorna a transação atualizada
            } else {
                return ResponseEntity.status(403).body(null); // Se o usuário não tiver permissão
            }
        }
        return ResponseEntity.notFound().build(); // Transação não encontrada
    }

    @PatchMapping("/{id}/update-fee")
    public ResponseEntity<?> updateTransactionFee(
            @PathVariable UUID id,
            @RequestBody Map<String, Object> requestBody) {
        Optional<SingleTransaction> transactionOptional = transactionService.getTransactionById(id);

        if (transactionOptional.isEmpty()) {
            return ResponseEntity.status(404).body("Transaction not found.");
        }

        SingleTransaction transaction = transactionOptional.get();

        // Verifica se a chave "transactionFee" existe no JSON
        if (!requestBody.containsKey("transactionFee")) {
            return ResponseEntity.status(400).body("Transaction fee is required.");
        }

        // Atualiza o transactionFee e o status
        try {
            BigDecimal transactionFee = new BigDecimal(requestBody.get("transactionFee").toString());
            transaction.setTransactionFee(transactionFee);
            transaction.setStatus("Success");
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("Invalid transaction fee format.");
        }

        SingleTransaction updatedTransaction = transactionService.addTransaction(transaction);
        return ResponseEntity.ok(updatedTransaction);
    }




}
class StatusUpdateRequest {
    private String newStatus;

    // Getter e Setter
    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }
}