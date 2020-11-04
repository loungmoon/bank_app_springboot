package com.innoveller.demo.service;

import com.innoveller.demo.dto.BankAccountDto;
import com.innoveller.demo.model.BankAccount;
import com.innoveller.demo.model.Transaction;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Service
public interface BankService {
    BankAccount createAccount(BankAccountDto bank);

    BankAccount findAccountById(Long bank_account_id);

    Transaction deposit(BankAccount account, double amount);

    double calculatedTotalBalance(Long bank_account_id);

    void withdraw(BankAccount account, double amount);

    void transfer(BankAccount fromAccount, BankAccount toAccount, double amount);

    List<Transaction> reportForOneDay(Date date);

    List<Transaction> reportOfDateRange(Date from_date, Date to_date);

    List<Transaction> getAccountTransactionList();

    List<BankAccount> getAllBankAccount();

}
