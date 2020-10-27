package com.innoveller.demo.service;

import com.innoveller.demo.dto.BankAccountDto;
import com.innoveller.demo.model.BankAccount;
import com.innoveller.demo.model.Transaction;
import com.innoveller.demo.repository.BankRepository;
import com.innoveller.demo.repository.TransactionReposity;
import org.hibernate.query.criteria.internal.SelectionImplementor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BankServiceImpl implements BankService {
    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private TransactionReposity transactionReposity;

    public void saveTransaction(double amount, String transactionType, BankAccount bank) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date = dateFormat.format(new Date());
            Transaction transaction = new Transaction();
            transaction.setAmount(amount);
            transaction.setTransactionDate(dateFormat.parse(date));
            transaction.setTransactionType(transactionType);
            transaction.setBankAccount(bank);
            transactionReposity.save(transaction);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public BankAccount createAccount(BankAccountDto bank) {
        BankAccount account = new BankAccount();
        try {
            account.setAccountHolder(bank.getAccountHolder());
            account.setAccountNo(bank.getAccountNo());
            account.setAccountType(bank.getAccountType());
            account.setOpenDate(bank.getOpendate());
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.bankRepository.save(account);
        saveTransaction(bank.getBalance(), "DEPOSIT", account);
        return account;
    }

    @Override
    public BankAccount findAccountById(Long id) {
        Optional<BankAccount> optional = bankRepository.findById(id);
        BankAccount account = null;
        if (optional.isPresent()) {
            account = optional.get();
        } else {
            throw new RuntimeException(" Account not found for id :: " + id);
        }
        return account;
    }

    @Override
    public List<BankAccount> getAllBankAccount() {
        List<BankAccount> bankAccountList = bankRepository.findAll();

        if (bankAccountList.size() > 0) {
            return bankAccountList;
        } else {
            return new ArrayList<BankAccount>();
        }
    }

    public double calculatedTotalBalance(Long bank_account_id) {
        double totalAmount = 0.0;
        List<Transaction> transactionList = transactionReposity.find(bank_account_id);
        if (transactionList != null) {
            for (Transaction transaction : transactionList) {
                if (transaction.getTransactionType().equals("DEPOSIT")) {
                    totalAmount += transaction.getAmount();
                } else if (transaction.getTransactionType().equals("WITHDRAW")) {
                    totalAmount -= transaction.getAmount();
                } else {
                    totalAmount -= transaction.getAmount();
                }
            }
        }
        return totalAmount;
    }

    @Override
    public void deposit(BankAccount account, double amount) {
        saveTransaction(amount, "DEPOSIT", account);
    }

    @Override
    public void withdraw(BankAccount account, double amount) {
        double totalBalance = calculatedTotalBalance(account.getId());
        if (totalBalance > amount) {
            saveTransaction(amount, "WITHDRAW", account);
        } else {
            System.out.println("Cannot Withdraw !! You balance have " + totalBalance);
        }
    }

    @Override
    public void transfer(BankAccount fromAccount, BankAccount toAccount, double amount) {
        double totalAmount = calculatedTotalBalance(fromAccount.getId());
        if (totalAmount > amount) {
            saveTransaction(amount, "TRANSFER", fromAccount);
            saveTransaction(amount, "DEPOSIT", toAccount);
        } else {
            System.out.println("Cannot Transfer !! You balance have " + totalAmount);
        }
    }

    @Override
    public List<Transaction> getAccountTransactionList() {
        List<Transaction> transactionList = transactionReposity.findAll();
        if (transactionList.size() > 0) {
            return transactionList;
        } else {
            return new ArrayList<Transaction>();
        }
    }

    @Override
    public List<Transaction> reportForOneDay(Date date) {
        List<Transaction> transactionList = transactionReposity.findByDate(date);
        return transactionList;
    }

    @Override
    public List<Transaction> reportOfDateRange(Date from_date, Date to_date) {
        List<Transaction> transactionList = transactionReposity.findByDateRange(from_date, to_date);
        return transactionList;
    }
}
