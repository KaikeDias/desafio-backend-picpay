package com.example.desafiopicpay.controllers;

import com.example.desafiopicpay.domain.transaction.Transaction;
import com.example.desafiopicpay.dtos.TransactionDto;
import com.example.desafiopicpay.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody TransactionDto transactionDto) throws Exception {
        Transaction transaction = this.transactionService.createTransaction(transactionDto);

        return ResponseEntity.ok(transaction);
    }
}
