package com.example.desafiopicpay.services;

import com.example.desafiopicpay.domain.transaction.Transaction;
import com.example.desafiopicpay.domain.user.User;
import com.example.desafiopicpay.dtos.TransactionDto;
import com.example.desafiopicpay.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository repository;
    @Autowired
    private  UserService userService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private NotificationService notificationService;

    public Transaction createTransaction(TransactionDto transactionDto) throws Exception {
        User sender = userService.findUserById(transactionDto.senderId());
        User receiver = userService.findUserById(transactionDto.receiverId());

        userService.validateTransaction(sender, transactionDto.value());

        boolean isAuthorized = this.authorizeTransaction(sender, transactionDto.value());
        if(!isAuthorized) {
            throw new Exception("Transacão não autorizada");
        }

        Transaction transaction = new Transaction();
        transaction.setAmount(transactionDto.value());
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setTimestamp(LocalDateTime.now());

        sender.setBalance(sender.getBalance().subtract(transactionDto.value()));
        receiver.setBalance(receiver.getBalance().add(transactionDto.value()));

        this.repository.save(transaction);
        this.userService.saveUser(sender);
        this.userService.saveUser(receiver);

        this.notificationService.sendNotification(sender, "Transacao Realizada com sucesso");
        this.notificationService.sendNotification(receiver, "Transacao Recebida com sucesso");

        return transaction;
    }

    public boolean authorizeTransaction(User sender, BigDecimal value) {
        ResponseEntity<Map> authorizationResponse = restTemplate.getForEntity("https://util.devi" +
                ".tools/api/v2/authorize", Map.class);

        if(authorizationResponse.getStatusCode() == HttpStatus.OK) {
            String message = (String) authorizationResponse.getBody().get("status");
            return message.equalsIgnoreCase("success");
        } else return false;
    }
}
