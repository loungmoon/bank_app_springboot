package com.innoveller.demo.web;
import com.innoveller.demo.dto.DTO;
import com.innoveller.demo.model.BankAccount;
import com.innoveller.demo.model.Transaction;
import com.innoveller.demo.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class HomeController {
    @Autowired
    BankService service;

    @GetMapping("/balance")
    public double index(@RequestParam Long id) {
        double balance = service.calculatedTotalBalance(id);
        return balance;
    }

    @PostMapping("/deposit")
    public DTO getTransaction(@RequestBody DTO transaction) {
        System.out.println(transaction);
        BankAccount bankAccount = service.findAccountById(transaction.getBankAccId());
        Transaction transaction1 = service.deposit(bankAccount, transaction.getAmount());
        if (transaction1 != null) {
            transaction.setStatus("Success");
        } else {
            transaction.setStatus("Fail");
        }return transaction;
    }
}